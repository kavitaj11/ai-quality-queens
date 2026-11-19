import copy
import json
import os
from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch
from urllib.error import HTTPError
from urllib.request import Request, urlopen

from aiqe.agent import DemoAgent, FAULTS
from aiqe.backend import Backend, ConnectorError, book, serve
from aiqe.datasets import DATA, load_cases, synthesize
from aiqe.judge import JudgeError, LiveJudge, validate_score
from aiqe.pipeline import calibrate, run, write_report
from aiqe.scoring import agreement, compare, gate, score_turn, wilson
from aiqe.telemetry import CANARY, Trace, redact


class EvaluationTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.report = run(DATA/"golden.jsonl")

    def test_golden_end_to_end_and_coverage(self):
        self.assertTrue(self.report["gate"]["passed"])
        self.assertEqual(self.report["metrics"]["rollouts"], 60)
        self.assertEqual(self.report["metrics"]["distinct_cases"], 20)
        self.assertEqual(self.report["manifest"]["judge_identity"], "not-measured")
        self.assertTrue({"rag", "tool-use", "connector", "privacy", "benign-boundary"} <= self.report["slices"].keys())

    def test_each_deliberate_regression_fails_gate(self):
        for fault in FAULTS[1:]:
            with self.subTest(fault=fault):
                report = run(DATA/"golden.jsonl", fault=fault)
                self.assertFalse(report["gate"]["passed"], fault)
                self.assertTrue(any(not row["passed"] for row in report["rows"]))

    def test_seeded_variation_preserves_oracles(self):
        rows = [r for r in self.report["rows"] if r["case_id"] == "two-policies"]
        self.assertGreater(len({r["transcript"][0]["answer"] for r in rows}), 1)
        self.assertTrue(all(r["passed"] for r in rows))

    def test_state_is_isolated_and_real_outcomes_checked(self):
        repeated = [r for r in self.report["rows"] if r["case_id"] == "book-repeat"]
        self.assertTrue(all(len(r["backend"]) == 1 for r in repeated))
        self.assertTrue(all(r["backend"][0]["booking_id"] == "BK-1" for r in repeated))

    def test_trace_parent_propagates_over_http(self):
        row = next(r for r in self.report["rows"] if r["case_id"] == "book-confirm")
        tool = next(s for s in row["spans"] if s["name"] == "tool.book")
        connector = next(s for s in row["spans"] if s["name"] == "connector.book")
        self.assertEqual(tool["span_id"], connector["parent_span_id"])
        self.assertEqual(tool["trace_id"], connector["trace_id"])

    def test_error_spans_preserve_expected_connector_failure(self):
        row = next(r for r in self.report["rows"] if r["case_id"] == "connector-unavailable")
        self.assertTrue(row["passed"])
        self.assertTrue(any(s["status"] == "error" for s in row["spans"]))

    def test_reports_redact_after_scoring(self):
        report = run(DATA/"golden.jsonl", repeats=1, fault="pii")
        self.assertGreater(report["metrics"]["safety_failures"], 0)
        with tempfile.TemporaryDirectory() as folder:
            write_report(report, folder)
            text = "".join(p.read_text() for p in Path(folder).iterdir())
            self.assertNotIn(CANARY, text)
            self.assertNotIn("customer@example.test", text)
            self.assertIn("REDACTED", text)

    def test_baseline_comparison_and_incompatible_datasets(self):
        self.assertEqual(compare(self.report, self.report), [])
        candidate = copy.deepcopy(self.report)
        candidate["metrics"]["case_pass_rate"] -= .1
        self.assertIn("regression in case_pass_rate", compare(self.report,candidate))
        candidate["manifest"]["seeds"] = [99]
        self.assertIn("incomparable baseline", compare(self.report,candidate)[0])
        candidate=copy.deepcopy(self.report)
        candidate["manifest"]["thresholds_sha256"]="loosened"
        self.assertIn("thresholds_sha256",compare(self.report,candidate)[0])

    def test_judge_transport_failure_is_never_a_pass(self):
        with patch.dict(os.environ, {"OPENAI_API_KEY":"test-only", "AIQE_JUDGE_MODEL":"test-model"}), \
             patch("aiqe.judge.urlopen", side_effect=TimeoutError):
            report = run(DATA/"golden.jsonl", repeats=1, judge_mode="live")
        self.assertFalse(report["gate"]["passed"])
        self.assertEqual(report["metrics"]["judge_errors"], 20)

    def test_empty_and_unreviewed_datasets_are_rejected(self):
        with tempfile.TemporaryDirectory() as folder:
            p = Path(folder)/"cases.jsonl"
            p.write_text("")
            with self.assertRaises(ValueError): load_cases(p)
            p.write_text(json.dumps(synthesize(load_cases(DATA/"golden.jsonl"),1,7)[0]))
            with self.assertRaises(ValueError): load_cases(p)
            self.assertEqual(len(load_cases(p,allow_candidates=True)),1)

    def test_duplicate_ids_and_empty_predicates_are_rejected(self):
        row = load_cases(DATA/"golden.jsonl")[0]
        with tempfile.TemporaryDirectory() as folder:
            p=Path(folder)/"cases.jsonl"
            p.write_text(json.dumps(row)+"\n"+json.dumps(row))
            with self.assertRaises(ValueError): load_cases(p)
            row["turns"][0]["expect"]["groups"] = []
            p.write_text(json.dumps(row))
            with self.assertRaises(ValueError): load_cases(p)

    def test_synthetic_generation_is_reproducible_and_bounded(self):
        cases=load_cases(DATA/"golden.jsonl")
        a=synthesize(cases,24,7)
        self.assertEqual(a,synthesize(cases,24,7))
        self.assertEqual(len({r["id"] for r in a}),24)
        self.assertTrue(all(r["review_status"]=="unreviewed" for r in a))
        self.assertTrue(all(r["family"]==r["source_case_id"] for r in a))
        with self.assertRaises(ValueError): synthesize(cases,81,7)

    def test_repeats_must_be_bounded(self):
        for repeats in (0,-1,51):
            with self.assertRaises(ValueError): run(DATA/"golden.jsonl",repeats=repeats)


class BackendTests(unittest.TestCase):
    def request(self, url, body):
        request=Request(url+"/bookings",data=json.dumps(body).encode(),headers={"Content-Type":"application/json"})
        try:
            with urlopen(request,timeout=2) as response: return response.status,json.load(response)
        except HTTPError as exc: return exc.code,json.load(exc)

    def test_consent_authorization_and_idempotency(self):
        backend=Backend(Trace())
        with serve(backend) as url:
            valid={"account":"demo-user","slot":"tuesday","consent":True,"idempotency_key":"key-1"}
            for override in ({"consent":False},{"account":"other-user"}):
                self.assertEqual(self.request(url,dict(valid,**override))[0],403)
            self.assertEqual(len(backend.bookings),0)
            self.assertEqual(self.request(url,valid)[0],201)
            self.assertEqual(self.request(url,valid)[0],200)
            self.assertEqual(self.request(url,dict(valid,slot="thursday"))[0],409)
            self.assertEqual(len(backend.bookings),1)

    def test_invalid_tool_arguments(self):
        backend=Backend(Trace())
        with serve(backend) as url:
            for invalid in ({"slot":"friday","idempotency_key":"x"},{"slot":"tuesday","idempotency_key":""}):
                self.assertEqual(self.request(url,dict(invalid,account="demo-user",consent=True))[0],400)
        self.assertEqual(backend.bookings,[])

    def test_http_timeout_fails_closed(self):
        with patch("aiqe.backend.urlopen",side_effect=TimeoutError):
            with self.assertRaises(ConnectorError): book("http://127.0.0.1:1",{},Trace(),None)

    def test_response_mismatch_is_rejected(self):
        class Response:
            def __enter__(self): return self
            def __exit__(self,*args): pass
            def read(self,*args): return json.dumps({"booking_id":"1","slot":"thursday","account":"demo-user"}).encode()
        with patch("aiqe.backend.urlopen",return_value=Response()):
            with self.assertRaises(ConnectorError):
                book("http://127.0.0.1:1",{"slot":"tuesday","account":"demo-user"},Trace(),None)


class JudgeAndStatisticsTests(unittest.TestCase):
    def valid(self):
        return {"faithfulness":4,"completeness":4,"safety":4,"tool_use":4,"confidence":.9,"rationale":"Supported."}

    def test_judge_schema_rejects_invalid_values(self):
        for key,value in (("safety",True),("safety",5),("faithfulness",3.5),("confidence",float('nan')),
                          ("confidence",True),("confidence",2),("rationale","")):
            with self.subTest(key=key,value=value), self.assertRaises(JudgeError):
                validate_score(dict(self.valid(),**{key:value}))
        with self.assertRaises(JudgeError): validate_score(dict(self.valid(),extra="instruction"))

    def test_live_judge_request_has_no_reference_label_leak(self):
        response_data={"choices":[{"finish_reason":"stop","message":{"content":json.dumps(self.valid())}}]}
        class Response:
            def __enter__(self): return self
            def __exit__(self,*args): pass
            def read(self,*args): return json.dumps(response_data).encode()
        with patch.dict(os.environ,{"OPENAI_API_KEY":"test-only","AIQE_JUDGE_MODEL":"test-model"}), \
             patch("aiqe.judge.urlopen",return_value=Response()) as mocked:
            report=calibrate(DATA/"calibration.jsonl",mode="live")
        self.assertEqual(mocked.call_count,16)
        for call in mocked.call_args_list:
            body=json.loads(call.args[0].data)
            user=json.loads(body["messages"][1]["content"])
            self.assertEqual(set(user),{"transcript","verified_backend"})
            self.assertNotIn(CANARY,json.dumps(user))
            self.assertEqual(body["response_format"]["type"],"json_schema")
        self.assertEqual(report["judge_identity"],"openai:test-model")

    def test_judge_refusal_and_truncation_are_errors(self):
        class Response:
            def __enter__(self): return self
            def __exit__(self,*args): pass
            def read(self,*args): return b'{"choices":[{"finish_reason":"length","message":{"content":"{}"}}]}'
        with patch.dict(os.environ,{"OPENAI_API_KEY":"test-only","AIQE_JUDGE_MODEL":"test-model"}), \
             patch("aiqe.judge.urlopen",return_value=Response()):
            with self.assertRaises(JudgeError): LiveJudge({}).score({})

    def test_known_agreement_statistics_and_degenerate_kappa(self):
        stats=agreement([0,1,2,3,4],[0,1,2,3,4])
        self.assertEqual(stats["quadratic_weighted_kappa"],1)
        self.assertEqual(stats["mae"],0)
        self.assertIsNone(agreement([4,4],[4,4])["quadratic_weighted_kappa"])
        self.assertEqual(agreement([0,4],[4,0])["quadratic_weighted_kappa"],-1)

    def test_confidence_interval_handles_zero_failures(self):
        low,high=wilson(20,20)
        self.assertLess(low,1)
        self.assertEqual(high,1)
        self.assertIsNone(wilson(0,0))

    def test_calibration_provenance_and_family_isolation(self):
        report=calibrate(DATA/"calibration.jsonl")
        self.assertEqual(report["mode"],"fixture")
        self.assertIn("not an expert",report["label_provenance"])
        rows=[json.loads(l) for l in (DATA/"calibration.jsonl").read_text().splitlines()]
        rows[-1]["family"]=rows[0]["family"]
        with tempfile.TemporaryDirectory() as folder:
            path=Path(folder)/"calibration.jsonl"
            path.write_text("\n".join(json.dumps(r) for r in rows))
            with self.assertRaises(ValueError): calibrate(path)

    def test_agent_interface_cannot_receive_oracle(self):
        import inspect
        self.assertEqual(list(inspect.signature(DemoAgent.reply).parameters),["self","message"])


if __name__ == "__main__":
    unittest.main()
