# Cloud Testing with AWS and Google Cloud

A beginner lab for QA engineers. Use the same smoke-test contract on Amazon S3 and Google Cloud Storage: a known object exists, its size matches the fixture, and its content type is correct.

## 1. Start offline

With Python 3.10 or newer, enter this lab from the repository root. Run the remaining commands from this directory:

```sh
cd practice-qa-projects/cloud-testing-aws-gcp
python3 -m unittest discover -s tests -v
```

These tests verify the AWS and GCP response adapters, the fixture, and how the command reports failures. They do not call a provider or require cloud SDKs or credentials. Passing them gives you confidence in the test code. The optional smoke test below checks the actual cloud deployment.

## 2. Learn what to test in the cloud

| Test layer | Typical question | Example |
|---|---|---|
| Offline contract | Does test logic interpret provider responses correctly? | Compare Amazon S3 and GCS metadata with the same expected values |
| Deployment smoke | Can the application identity read the expected fixture? | Read metadata from a named QA bucket |
| Integration | Does the full workflow cross service boundaries correctly? | Upload through the application, then verify the processed object or event |
| Operational readiness | Do timeouts, retries, and monitoring behave as expected? | Simulate a dependency error in staging and verify the alert |
| Performance | Does this workload meet the latency and throughput targets? | Run a separate load test against a designated QA environment |

This lab implements the first two layers. The others are practice designs you can adapt to your team’s architecture.

## 3. Run an optional cloud smoke test

Use an existing QA bucket. Place the contents of `fixtures/hello.txt` at object key `qa/hello.txt` with content type `text/plain`. The fixture is eight bytes including a final LF newline. Use the application's test identity or a dedicated QA identity with permission to read the named fixture.

Install only the dependencies needed for the live exercises:

```sh
python3 -m venv .venv
source .venv/bin/activate
python -m pip install -r requirements.txt
```

On Windows PowerShell, use `py -m venv .venv` and `.\.venv\Scripts\Activate.ps1`. Replace all example resource and account values below with your QA values.

### AWS S3

Use an existing AWS CLI profile. If it uses IAM Identity Center, sign in with `aws sso login --profile qa`. Boto3 uses the profile selected by `AWS_PROFILE`. The identity needs `s3:GetObject` for the fixture. The request also verifies the expected AWS bucket owner account ID. See [Boto3 credentials](https://docs.aws.amazon.com/boto3/latest/guide/credentials.html) and [HeadObject](https://docs.aws.amazon.com/boto3/latest/reference/services/s3/client/head_object.html).

```sh
AWS_PROFILE=qa python cloud_smoke.py aws --bucket your-aws-qa-bucket --aws-account-id 123456789012
```

On PowerShell, set `$env:AWS_PROFILE = "qa"` before running the Python command.

### Google Cloud Storage

Use existing [Application Default Credentials](https://docs.cloud.google.com/docs/authentication/application-default-credentials). For local development with an authorized account, run `gcloud auth application-default login`. The identity needs `storage.objects.get` for the fixture. The project flag configures the client. The bucket name selects the storage resource, so verify the bucket in your approved QA inventory. See [Cloud Storage authentication](https://docs.cloud.google.com/storage/docs/authentication).

```sh
python cloud_smoke.py gcp --bucket your-gcp-qa-bucket --gcp-project your-qa-project
```

Both commands fetch only metadata. They do not download object contents or modify cloud resources. Both accept `--key`, `--expected-bytes`, and `--expected-content-type` for a different fixture.

## 4. Read the result

| Exit code | Meaning | First check |
|---|---|---|
| `0` | The metadata matched | Record the environment and run timestamp |
| `1` | The size or content type differs | Verify the uploaded fixture and its configured content type |
| `2` | Setup, credential, or provider request error | Check the identity, bucket, object key, and error type |

An S3 `403` can mean access was denied or that the fixture was missing while the caller lacked permission to list the bucket. Verify the fixture through the QA owner or approved inventory. The same smoke failure should never be presented as proof of a specific cause until there is evidence. See [S3 HeadObject error behavior](https://docs.aws.amazon.com/boto3/latest/reference/services/s3/client/head_object.html).

## 5. Practice

1. Run the offline suite and trace how the two provider-specific responses become one `ObjectMetadata` contract.
2. If you have an authorized QA bucket, run the live check with the correct metadata. Repeat with `--expected-bytes 9` and explain why the second exit code differs.
3. Design an integration scenario for an upload pipeline. Specify the fixture path, expected event, bounded wait, and test-data cleanup.
4. Decide which checks run for every pull request and which require a deployed QA environment. For live CI checks, use an approved short-lived workload identity and report setup failures separately from contract failures.
