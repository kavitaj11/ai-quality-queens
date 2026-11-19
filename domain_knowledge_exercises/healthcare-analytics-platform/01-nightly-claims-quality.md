# Exercise 1: Nightly claims quality
Recommended time: 90 minutes · Total points: 100 · Passing score: 75 · Distinction: 85+ 
## Scenario 
You are joining a QA team supporting a healthcare analytics web application. Every night, claims files from 
client organizations land in an S3 bucket. An EventBridge schedule starts an AWS Glue ETL job that cleans 
and transforms the data and loads it into Redshift. A Lambda function sends a notification when the load 
completes. Users view the results in a web application and in QuickSight dashboards. A new feature uses 
AWS Bedrock to generate a plain-language summary of a provider's claims trends. 
Today, regression is mostly manual, there is no regular performance testing, and data issues are usually 
found by clients. 
Your assignment is to: 
- Design tests for the data pipeline 
- Automate a UI scenario in Selenium with Java 
- Design and analyze a JMeter load test 
- Validate data with SQL 
- Troubleshoot a pipeline failure using AWS services 
- Propose tests for the AI feature 
- Explain the CI/CD and release readiness approach 
## Part 1: Data Pipeline Test Design 
Question 1: List the test scenarios for the nightly claims pipeline. 
Reference coverage 
Ingestion (S3) 
- File arrives with the expected naming convention and partition (for example /client/yyyy/mm/dd/) 
- Empty file, duplicate file, late file, wrong format, wrong delimiter or encoding 
- Header-only file and file with extra or missing columns 
Trigger (EventBridge and Glue) 
- Job starts on schedule; does not start twice 
- Job handles no new files gracefully 
- Re-running the job for the same date does not duplicate data 
Transformation (Glue) 
- Code mappings applied correctly (for example diagnosis and procedure codes) 
- Dates standardized; amounts keep correct precision 
- Invalid records rejected to an error location with a reason 
- Schema change in the source detected, not silently ignored 
Load (Redshift) 
- Source and target row counts reconcile (loaded plus rejected equals received) 
- No duplicates on business keys 
- Totals reconcile, for example SUM of paid amounts per client per day 
- No orphan records against member and provider dimensions 
Notification (Lambda) 
- Success and failure notifications sent with correct counts 
- Failure is visible in CloudWatch with enough detail to debug 
Reporting (web app and QuickSight) 
- Dashboard KPIs match SQL on Redshift 
- Users see only their own organization's data 
- "Data as of" date is correct 
Privacy and non-functional 
- No PHI in logs or notifications 
- Job completes within its time window at peak volume 
## Part 2: Selenium with Java 
Question 2: Automate this scenario. 
A user logs in, opens the Claims Summary report, filters by provider "P1001" and date range January 2026, 
and the test verifies that the Total Paid value matches the expected value from the database. 
Sample Answer 
```java
public class ClaimsSummaryPage { 
private final WebDriver driver; 
private final WebDriverWait wait; 
private final By providerFilter = By.cssSelector("[data-testid='provider-filter']"); 
private final By fromDate = By.cssSelector("[data-testid='from-date']"); 
private final By toDate = By.cssSelector("[data-testid='to-date']"); 
private final By applyButton = By.cssSelector("[data-testid='apply-filters']"); 
private final By loadingSpinner = By.cssSelector(".loading-spinner"); 
private final By totalPaid = By.cssSelector("[data-testid='total-paid']"); 
public ClaimsSummaryPage(WebDriver driver) { 
this.driver = driver; 
this.wait = new WebDriverWait(driver, Duration.ofSeconds(20)); 
} 
public ClaimsSummaryPage filterBy(String providerId, String from, String to) { 
type(providerFilter, providerId); 
type(fromDate, from); 
        type(toDate, to); 
        wait.until(ExpectedConditions.elementToBeClickable(applyButton)).click(); 
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner)); 
        return this; 
    } 
  
    public BigDecimal getTotalPaid() { 
        String text = 
wait.until(ExpectedConditions.visibilityOfElementLocated(totalPaid)).getText(); 
        return new BigDecimal(text.replaceAll("[$,]", "")); 
    } 
  
    private void type(By locator, String value) { 
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator)); 
        el.clear(); 
        el.sendKeys(value); 
    } 
} 
  
public class ClaimsSummaryTest extends BaseTest { 
  
    @Test(groups = {"regression", "reports"}) 
    public void totalPaidMatchesWarehouse() { 
        ClaimsSummaryPage page = new LoginPage(driver()) 
            .loginAs(TestUsers.analyst()) 
            .openClaimsSummary() 
            .filterBy("P1001", "01/01/2026", "01/31/2026"); 
  
        BigDecimal expected = claimsRepository.totalPaid("P1001", 
            LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31)); 
  
        assertEquals(page.getTotalPaid(), expected, 
            "Total Paid on report does not match Redshift for P1001, Jan 2026"); 
    } 
} 
```

Follow-Up Questions 
- Why BigDecimal instead of double? (precision for money) 
- Why wait for the spinner to disappear? (the old total may still be on screen) 
- Where do the credentials come from? (configuration or secrets, never the code) 
- How does driver() work in parallel runs? (ThreadLocal WebDriver in BaseTest) 
- What if the test data changes daily? (query the expected value at run time, or use a fixed, seeded 
data set) 
## Part 3: JMeter Performance Testing 
Question 3A: Design a load test for the Claims Summary report API. 
Target: 300 concurrent users at peak, 95th percentile under 3 seconds, error rate under 1 percent. 
Strong Answer: Test Plan Structure 
Test Plan: Claims Summary Load Test 
├── User Defined Variables (host, users, rampup, duration) 
├── CSV Data Set Config (users.csv: username, password, providerId) 
├── HTTP Cookie Manager / HTTP Header Manager 
├── setUp Thread Group (optional: seed or verify test data) 
└── Thread Group (${__P(users,300)} users, ramp-up 600s, duration 3600s) 
├── Transaction: Login 
│   └── POST /api/auth/login  → JSON Extractor: token 
├── Transaction: Open Dashboard (weight 50%) 
├── Transaction: Claims Summary with filters (weight 35%) 
│   └── Response Assertion: status 200, body contains "totalPaid" 
├── Transaction: Export Report (weight 15%) 
└── Uniform Random Timer (think time 3–8 s) 
Design notes: the workload mix comes from real usage data, each user logs 
in with different data, the token is correlated, assertions stop errors being counted as fast successes, and 
the test runs in non-GUI mode with listeners disabled. 
Question 3B: Analyze these results. 
| Users | Avg (ms) | p95 (ms) | Requests/s | Errors (%) |
|---:|---:|---:|---:|---:|
|50|420|780|48|0.0|
|100|460|850|95|0.0|
|200|900|2400|160|0.2|
|300|3800|9500|165|4.8|

Reference interpretation: Up to 100 users the system scales well: throughput doubles and response time is stable. Between 200 
and 300 users, throughput flattens at about 165 requests per second while the 95th percentile jumps to 
9.5 seconds and errors reach 4.8 percent. That's a classic bottleneck: requests are queuing for a limited 
resource. 
The test fails the target (95th percentile under 3 seconds and errors under 1 percent at 300 users). Next 
I'd check which transaction is slowest, then look at server and database metrics during the run: CPU, 
connection pool usage, slow-query logs, Redshift query queues and CloudWatch metrics. I'd also 
confirm the load generator wasn't maxed out. Then I'd report it with graphs and a clear statement: the 
system currently supports about 200 users within target, not 300." 
## Part 4: SQL and Data Validation 
Tables 
stg_claims (raw data loaded from S3): claim_id, member_id, provider_id, service_date, paid_amount, 
load_date 
fact_claims (warehouse): claim_key, claim_id, member_key, provider_key, service_date, paid_amount, 
load_date 
dim_member: member_key, member_id, plan_code, is_current 
dim_provider: provider_key, provider_id, provider_name 
claims_rejects: claim_id, load_date, reject_reason 
Question 4A: Reconcile row counts for a load date. 

```sql
SELECT 
  (SELECT COUNT(*) FROM stg_claims  WHERE load_date = '2026-11-03') AS source_count, 
  (SELECT COUNT(*) FROM fact_claims WHERE load_date = '2026-11-03') AS target_count, 
  (SELECT COUNT(*) FROM claims_rejects WHERE load_date = '2026-11-03') AS rejected_count; -- Expected: source_count = target_count + rejected_count 
```

Question 4B: Find claims in staging that never reached the warehouse. 

```sql
SELECT s.claim_id 
FROM stg_claims s 
LEFT JOIN fact_claims f ON f.claim_id = s.claim_id 
LEFT JOIN claims_rejects r ON r.claim_id = s.claim_id 
WHERE s.load_date = '2026-11-03' 
  AND f.claim_id IS NULL 
  AND r.claim_id IS NULL; 
```

Question 4C: Reconcile paid amounts by provider. 

```sql
SELECT s.provider_id, s.src_total, t.tgt_total 
FROM (SELECT provider_id, SUM(paid_amount) AS src_total 
      FROM stg_claims WHERE load_date = '2026-11-03' GROUP BY provider_id) s 
JOIN (SELECT p.provider_id, SUM(f.paid_amount) AS tgt_total 
      FROM fact_claims f JOIN dim_provider p ON p.provider_key = f.provider_key 
      WHERE f.load_date = '2026-11-03' GROUP BY p.provider_id) t 
  ON t.provider_id = s.provider_id 
WHERE s.src_total <> t.tgt_total; 
```

Question 4D: Find orphan facts and check the member dimension. -- Facts with no matching member 

```sql
SELECT f.claim_id 
FROM fact_claims f 
LEFT JOIN dim_member m ON m.member_key = f.member_key 
WHERE m.member_key IS NULL; 
  -- More than one current row per member (SCD Type 2 defect) 
SELECT member_id, COUNT(*) 
FROM dim_member 
WHERE is_current = TRUE 
GROUP BY member_id 
HAVING COUNT(*) > 1; 
```

Follow-Up Question
Why is a matching row count not enough? 
"Counts can match while the data is wrong: values truncated, codes mapped incorrectly, amounts 
rounded, or one duplicate canceling out one missing record. I also reconcile totals, check business keys, 
and sample records field by field." 
## Part 5: Debugging Exercise 
Question 5: Find the problems in this test. 
```java
@Test
public void test1() throws Exception { 
WebDriver driver = new ChromeDriver(); 
driver.get("https://claims.example.test/login"); 
driver.findElement(By.xpath("/html/body/div[2]/div/form/input[1]")).sendKeys("demo_analyst"); 
driver.findElement(By.xpath("/html/body/div[2]/div/form/input[2]")).sendKeys("EXAMPLE_ONLY"); 
driver.findElement(By.xpath("/html/body/div[2]/div/form/button")).click(); 
Thread.sleep(8000); 
driver.findElement(By.id("claimsReport")).click(); 
Thread.sleep(5000); 
String total = driver.findElement(By.id("totalPaid")).getText(); 
System.out.println(total); 
} 
```

Expected Findings 
- No assertion: the test can never fail on a wrong value 
- Absolute XPaths break with any layout change 
- Hard-coded URL, user and password; credentials in source code 
- Thread.sleep instead of explicit waits: slow and still flaky 
- Driver created inside the test: no reuse, no parallel support, no configuration 
- Driver never quit: browser processes leak 
- No page objects: locators and actions mixed into the test 
- Meaningless test name 
- System.out instead of logging or reporting 
- Expected value not defined; no database or data validation 
- throws Exception hides useful failure detail 
- No test data strategy; depends on whatever data exists 
## Part 6: AWS Troubleshooting Scenario 
Question 6: Clients report that yesterday's claims are missing from the dashboard. How do you 
investigate? 
Strong Answer 
"I'd follow the data path from the end back to the start, checking each hop: 
- QuickSight: did the dataset refresh? What is the last refresh time and status? 
- Redshift: is the data there? Query by load_date. Check STL_LOAD_ERRORS for failed loads. 
- Glue: did the job run? Check job run history for status, duration and error message, and the job's 
CloudWatch logs. 
- EventBridge: did the schedule rule fire? Check the rule's metrics for invocations and failed 
invocations. 
- S3: did the files arrive, in the right path and format, before the job started? 
- Lambda: was a notification sent? What did it report? 
Once I find the failing hop I collect evidence (timestamps, run IDs, log lines), raise a defect with the root 
cause, and suggest a test or alarm that would catch it next time, for example a CloudWatch alarm on 
Glue job failure and a row-count check before the dashboard refreshes." 
## Part 7: AI Feature Testing (Bedrock) 
Question 7: How would you test the AI-generated claims trend summary? 
Strong Answer 
Accuracy and grounding 
- Build a golden set of providers with known trends and check the summary states them correctly 
- Every number in the summary must match the warehouse data 
- The summary must not invent trends, causes or clinical claims 
Safety and privacy 
- No member names or PHI in summaries meant for aggregate views 
- Prompt injection through data fields (for example a provider name containing instructions) 
- Users cannot get summaries for organizations they can't access 
Consistency and fairness 
- Similar data produces similar summaries 
- Tone and content are consistent across provider types and regions 
Robustness 
- Empty data, one claim, extreme values, missing fields 
- Bedrock timeout or throttling: the app shows a clear fallback, not an error page 
Regression and non-functional 
- Re-run the evaluation set whenever the prompt, model version or data changes 
- Latency under load (JMeter) and token cost per summary 
- Human review sample for quality, with results tracked over time 
## Part 8: CI/CD Pipeline 
Question 8: Describe where each type of test runs. 
Stage 
Tests 
Pull request 
Unit tests, API smoke, short Selenium smoke, static checks 
Deploy to QA 
Health checks, smoke, critical regression, 10-minute JMeter check 
Nightly 
Full Selenium and API regression, data pipeline validation, SQL reconciliation 
Release candidate 
Full regression, full JMeter load and soak, AI evaluation set, security checks 
Stage 
Tests 
Production 
Non-destructive smoke, synthetic monitoring, CloudWatch alarms 
### Release decision

Publish JUnit and JMeter evidence for the same build and dataset. Block release when the 300-user target fails, claims do not reconcile, or a tenant can see another tenant’s data. Identify the owner and the retest needed to clear each failure.

**SQL scope:** The examples assume an isolated single-client batch. When claim IDs repeat across clients or batches, join on tenant, batch and claim key. Reconcile accepted amounts separately from rejects and compare both directions so a provider missing from either side cannot disappear in an inner join.
