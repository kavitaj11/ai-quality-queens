"""Check metadata of one existing fixture in Amazon S3 or Google Cloud Storage."""

import argparse
import sys
from dataclasses import dataclass


@dataclass(frozen=True)
class ObjectMetadata:
    size: int
    content_type: str


def read_s3(client, bucket, key, account_id):
    response = client.head_object(
        Bucket=bucket, Key=key, ExpectedBucketOwner=account_id
    )
    return ObjectMetadata(response["ContentLength"], response.get("ContentType", ""))


def read_gcs(client, bucket, key):
    blob = client.bucket(bucket).blob(key)
    blob.reload(timeout=10, retry=None)
    return ObjectMetadata(blob.size, blob.content_type or "")


def differences(actual, expected):
    findings = []
    if actual.size != expected.size:
        findings.append(f"size expected {expected.size} bytes, got {actual.size}")
    if actual.content_type != expected.content_type:
        findings.append(
            f"content type expected {expected.content_type!r}, got {actual.content_type!r}"
        )
    return findings


def read_live(args):
    if args.provider == "aws":
        import boto3
        from botocore.config import Config

        client = boto3.client(
            "s3",
            config=Config(
                connect_timeout=5,
                read_timeout=10,
                retries={"total_max_attempts": 1},
            ),
        )
        return read_s3(client, args.bucket, args.key, args.aws_account_id)

    from google.cloud import storage

    return read_gcs(storage.Client(project=args.gcp_project), args.bucket, args.key)


def main(argv=None):
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("provider", choices=("aws", "gcp"))
    parser.add_argument("--bucket", required=True)
    parser.add_argument("--key", default="qa/hello.txt")
    parser.add_argument("--aws-account-id", help="Expected 12-digit bucket owner")
    parser.add_argument("--gcp-project", help="Project ID for the GCS client")
    parser.add_argument("--expected-bytes", type=int, default=8)
    parser.add_argument("--expected-content-type", default="text/plain")
    args = parser.parse_args(argv)

    if args.expected_bytes < 0:
        parser.error("--expected-bytes must be nonnegative")
    if args.provider == "aws":
        value = args.aws_account_id or ""
        if len(value) != 12 or not value.isascii() or not value.isdigit():
            parser.error("aws requires --aws-account-id with 12 digits")
    elif not args.gcp_project:
        parser.error("gcp requires --gcp-project")

    try:
        actual = read_live(args)
    except ImportError:
        print("ERROR: install the SDKs with python -m pip install -r requirements.txt", file=sys.stderr)
        return 2
    except Exception as error:
        # SDK exceptions can include endpoint or credential details. Print only the type.
        detail = type(error).__name__
        response = getattr(error, "response", None)
        if isinstance(response, dict):
            detail += f" ({response.get('Error', {}).get('Code', 'unknown')})"
        print(f"ERROR: {args.provider} metadata request failed: {detail}", file=sys.stderr)
        return 2

    expected = ObjectMetadata(args.expected_bytes, args.expected_content_type)
    findings = differences(actual, expected)
    if findings:
        print("FAIL: " + "; ".join(findings), file=sys.stderr)
        return 1
    print(f"PASS: {args.provider} fixture metadata matches")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
