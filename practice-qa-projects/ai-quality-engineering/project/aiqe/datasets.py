import copy
import hashlib
import json
from pathlib import Path
import random

DATA = Path(__file__).resolve().parent.parent / "data"


def load_cases(path, allow_candidates=False):
    cases, ids = [], set()
    for number, line in enumerate(Path(path).read_text(encoding="utf-8").splitlines(), 1):
        if not line.strip():
            continue
        row = json.loads(line)
        if not isinstance(row, dict):
            raise ValueError(f"line {number}: object required")
        required = {"id", "family", "category", "split", "turns", "expected_bookings", "attack"}
        if not required <= row.keys():
            raise ValueError(f"line {number}: missing fields")
        if not all(isinstance(row[k], str) and row[k] for k in ("id", "family", "category")):
            raise ValueError(f"line {number}: invalid identifiers")
        if row["id"] in ids:
            raise ValueError("duplicate case id")
        ids.add(row["id"])
        if row["split"] not in ({"golden", "candidate"} if allow_candidates else {"golden"}):
            raise ValueError("unreviewed candidates require --allow-candidates; never silently promote them")
        if type(row["attack"]) is not bool or not isinstance(row["turns"], list) or not row["turns"]:
            raise ValueError("attack must be boolean and turns must be nonempty")
        if not isinstance(row["expected_bookings"], list) or any(s not in ("tuesday", "thursday") for s in row["expected_bookings"]):
            raise ValueError("invalid expected bookings")
        for turn in row["turns"]:
            if not isinstance(turn.get("user"), str) or not turn["user"].strip():
                raise ValueError("turn requires user message")
            bookings = turn.get("expected_bookings_after")
            if not isinstance(bookings, list) or any(s not in ("tuesday", "thursday") for s in bookings):
                raise ValueError("each turn needs an independent backend-state expectation")
            expected = turn.get("expect", {})
            if not isinstance(expected, dict) or set(expected) != {"groups", "citations", "refused"}:
                raise ValueError("turn requires complete expectation")
            if type(expected["refused"]) is not bool:
                raise ValueError("refused must be boolean")
            if not isinstance(expected["groups"], list) or not expected["groups"]:
                raise ValueError("each turn needs at least one independent content predicate")
            for group in expected["groups"]:
                if not isinstance(group, list) or not group or not all(isinstance(s, str) and s for s in group):
                    raise ValueError("predicate groups must contain nonempty strings")
            if not isinstance(expected["citations"], list) or not all(isinstance(s, str) for s in expected["citations"]):
                raise ValueError("invalid citations")
        cases.append(row)
    if not cases:
        raise ValueError("empty evaluation dataset")
    return cases


def synthesize(cases, count, seed):
    """Seeded template expansion, not LLM generation or automatic golden labels."""
    if count < 1:
        raise ValueError("count must be positive")
    personas = {"new-user": "I am new to this service. ", "busy-parent": "I am short on time. ",
                "traveler": "I am traveling. ", "accessibility": "Please use short, clear sentences. "}
    pool = [(case, persona, prefix) for case in cases for persona, prefix in personas.items()]
    if count > len(pool):
        raise ValueError(f"at most {len(pool)} unique candidates available")
    random.Random(seed).shuffle(pool)
    result = []
    for base, persona, prefix in pool[:count]:
        row = copy.deepcopy(base)
        row["turns"][0]["user"] = prefix + row["turns"][0]["user"]
        scenario = {"turns": row["turns"], "context_poisoned": row.get("poisoned_context", False),
                    "connector_fault": row.get("connector_fault", "none")}
        fingerprint = hashlib.sha256(json.dumps(scenario, sort_keys=True).encode()).hexdigest()[:12]
        row.update(id=f"candidate-{fingerprint}", split="candidate", persona=persona,
                   review_status="unreviewed", source_case_id=base["id"], generator_version="templates-v1",
                   generator_seed=seed)
        result.append(row)
    return result

