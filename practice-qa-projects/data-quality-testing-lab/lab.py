import csv
import sqlite3
from datetime import date
from pathlib import Path

ROOT = Path(__file__).resolve().parent

def load(db, mode, source=ROOT/'raw/orders.csv'):
    """Replace one complete snapshot atomically. Raw source is read only."""
    if mode not in ('etl', 'elt'):
        raise ValueError('choose etl or elt')
    with source.open(newline='') as stream:
        reader = csv.reader(stream)
        if next(reader, []) != ['order_id','customer_id','order_date','cents']:
            raise ValueError('raw schema drift')
        rows = list(reader)
    def typed(row):
        key, customer, day, money = row
        if not key.strip() or not customer.strip() or not money.isascii() or not money.isdigit():
            raise ValueError('invalid key or cents')
        if date.fromisoformat(day).isoformat() != day:
            raise ValueError('expected YYYY-MM-DD')
        return key, customer, day, int(money)
    with db:
        db.execute('BEGIN')
        for table in ('raw_orders','staging','customer','fact'):
            db.execute('DROP TABLE IF EXISTS '+table)
        db.execute('CREATE TABLE customer(customer_id TEXT PRIMARY KEY)')
        db.executemany('INSERT INTO customer VALUES (?)', [('C1',),('C2',)])
        schema='(order_id TEXT,customer_id TEXT,order_date TEXT,cents INTEGER)'
        db.execute('CREATE TABLE staging'+schema)
        db.execute('CREATE TABLE fact(order_id TEXT PRIMARY KEY,customer_id TEXT,order_date TEXT,cents INTEGER)')
        if mode=='elt':
            db.execute('CREATE TABLE raw_orders(k TEXT,c TEXT,d TEXT,m TEXT)')
            db.executemany('INSERT INTO raw_orders VALUES (?,?,?,?)',rows)
            # Validate before CAST: SQLite otherwise converts malformed text to zero.
            for row in db.execute('SELECT * FROM raw_orders'):
                typed(row)
            db.execute('INSERT INTO staging SELECT k,c,d,CAST(m AS INTEGER) FROM raw_orders')
        else:
            db.executemany('INSERT INTO staging VALUES (?,?,?,?)',map(typed,rows))
        db.execute('INSERT OR IGNORE INTO fact SELECT * FROM staging')
        issues=check(db)
        if issues:
            raise ValueError(issues)

def check(db):
    return db.execute((ROOT/'checks.sql').read_text()).fetchall()

if __name__=='__main__':
    import argparse
    parser=argparse.ArgumentParser()
    parser.add_argument('mode',choices=['etl','elt'])
    parser.add_argument('--db',default='warehouse.db')
    args=parser.parse_args()
    db=sqlite3.connect(args.db)
    load(db,args.mode)
    print('PASS: orders, customers and daily totals reconcile')
