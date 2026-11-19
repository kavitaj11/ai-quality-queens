import sqlite3, tempfile, unittest
from pathlib import Path
from lab import ROOT, load, check

class PipelineTests(unittest.TestCase):
    def setUp(self):
        self.db=sqlite3.connect(':memory:')
        self.addCleanup(self.db.close)
        load(self.db,'etl')

    def test_equivalence_and_replay(self):
        for mode in ('etl','elt','elt'):
            load(self.db,mode)
            self.assertEqual(self.db.execute('SELECT count(*),sum(cents) FROM fact').fetchone(),(3,2900))
            self.assertEqual(check(self.db),[])

    def test_offsetting_errors(self):
        self.db.execute("UPDATE fact SET cents=cents+CASE order_id WHEN 'A' THEN 100 ELSE -100 END WHERE order_id IN ('A','B')")
        self.assertEqual(set(check(self.db)),{('missing or changed fact','A'),('missing or changed fact','B')})

    def test_missing_and_extra(self):
        self.db.execute("UPDATE fact SET order_id='X' WHERE order_id='C'")
        self.assertEqual(set(check(self.db)),{('missing or changed fact','C'),('unexpected fact','X')})

    def test_daily_total(self):
        self.db.execute("UPDATE fact SET cents=0 WHERE order_id='A'")
        self.assertIn(('daily count or amount mismatch','2026-01-01'),check(self.db))

    def test_bad_batches_preserve_previous_snapshot(self):
        source=(ROOT/'raw/orders.csv').read_text()
        with tempfile.TemporaryDirectory() as tmp:
            path=Path(tmp)/'orders.csv'
            for mode in ('etl','elt'):
                for bad,reason in [(source+'A,C1,2026-01-01,1500\n','duplicate source key'),(source.replace('C2','X'),'unknown customer'),(source.replace('1500','N/A'),'invalid key'),(source.replace('order_id','id'),'schema drift')]:
                    with self.subTest(mode=mode,reason=reason):
                        path.write_text(bad)
                        with self.assertRaisesRegex(ValueError,reason):
                            load(self.db,mode,path)
                        self.assertEqual(check(self.db),[])
                        self.assertEqual(self.db.execute('SELECT sum(cents) FROM fact').fetchone()[0],2900)
