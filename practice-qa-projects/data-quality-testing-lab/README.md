# SQL and Data Pipeline Testing Lab
Practice SQL validation and compare ETL with ELT using a local CSV landing zone and SQLite warehouse. Requires Python 3.9+; no cloud account or packages required.

    python -m unittest -v
    python lab.py etl --db etl.db
    python lab.py elt --db elt.db

Use new database files: each successful load replaces the four lab tables. The raw CSV represents one complete snapshot, not an incremental feed. This local simulation does not exercise a managed lake or warehouse service.

## Walkthrough
1. Inspect raw/orders.csv. The grain is one row per order. Verify column names, required keys, ISO dates and nonnegative integer cents at ingestion. Three orders should total 2,900 cents.
2. Compare the two loads in lab.py. ETL converts values before inserting them. ELT lands text in raw_orders first, validates it and casts cents inside SQL. Failed loads roll back; rerunning the same snapshot should produce the same facts.
3. Open checks.sql. The LEFT JOIN finds unmatched customers. GROUP BY and HAVING expose duplicate business keys. Two directional joins detect missing, unexpected or changed facts. Signed counts and amounts reconcile daily aggregates without dropping dates that occur on only one side.
4. Read test_offsetting_errors: shifting 100 cents between two orders on the same day preserves the daily total. Run the test and inspect why comparing individual order amounts still catches both errors.
5. Extend the lab: add a second landing partition and a batch identifier. Load a late correction, replay the batch, and assert that only the affected order and daily total change. Decide how to distinguish a correction from a duplicate before implementing the merge.
