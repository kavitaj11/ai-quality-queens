# 2. Setup: JMeter and the Demo App

## What you need

| Tool | Version | Check |
|---|---|---|
| Java (JDK or JRE) | 17 or later | `java -version` |
| Apache JMeter | 5.6.3 | see below |
| Python | 3.10 or later | `python --version` |

## Install JMeter (and verify the download)

1. Download `apache-jmeter-5.6.3.zip` (about 90 MB) and its `.sha512` file from the official Apache site: <https://jmeter.apache.org/download_jmeter.cgi>.
2. Verify the checksum before unzipping. A tester who checks downloads is a tester who checks everything:

```bash
# macOS / Linux / Git Bash
sha512sum apache-jmeter-5.6.3.zip
cat apache-jmeter-5.6.3.zip.sha512          # the two hashes must match
```
```powershell
# Windows PowerShell
(Get-FileHash apache-jmeter-5.6.3.zip -Algorithm SHA512).Hash.ToLower()
Get-Content apache-jmeter-5.6.3.zip.sha512
```

3. Unzip it anywhere (no installer) and set `JMETER_HOME` to that folder:

```bash
export JMETER_HOME=$HOME/tools/apache-jmeter-5.6.3
```
```powershell
$env:JMETER_HOME = "C:\tools\apache-jmeter-5.6.3"
```

## Start the demo app

```bash
python demo-app/shop_api.py
# Shop API on http://127.0.0.1:8099  (DB pool size = 4, backlog = 5)
```

Check it from a second terminal:

```bash
curl http://127.0.0.1:8099/health      # {"status": "UP"}
curl http://127.0.0.1:8099/metrics     # pool size, waiting requests, average pool wait
```

> **Port already in use?** Port 8080 is used by many other tools, which is why this tutorial uses 8099. If 8099 is taken too, start the app with `--port 8199` and pass `--port 8199` to the scripts. `run_test.py` checks `/health` before every run, so you get a clear message instead of a test full of 404 errors from the wrong server. (That exact mistake happened while this tutorial was being written.)

## Open the test plan in the JMeter GUI

```bash
$JMETER_HOME/bin/jmeter.sh -t jmeter/shop_load_test.jmx       # macOS / Linux
%JMETER_HOME%\bin\jmeter.bat -t jmeter\shop_load_test.jmx      # Windows
```

Use the GUI to **build and debug** a plan with 1–2 users. Never use it to run a real load test: the GUI itself uses CPU and memory and distorts results. Real runs use non-GUI mode (Chapter 6).

## Your first smoke run

```bash
python scripts/run_test.py --users 10 --rampup 10 --duration 30 --name smoke --p95-ms 800 --max-error-pct 1
```

You should see `SLO: PASSED` and a link to an HTML dashboard. If you see errors, read Chapter 5's debugging section before continuing.

Next: [3. Workload modeling](03-workload-modeling.md)
