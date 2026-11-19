import unittest
from pathlib import Path
from unittest.mock import Mock, patch

from cloud_smoke import ObjectMetadata, differences, main, read_gcs, read_s3


FIXTURE = Path(__file__).resolve().parents[1] / "fixtures" / "hello.txt"


class StorageContractTests(unittest.TestCase):
    def test_s3_reads_metadata_and_checks_bucket_owner(self):
        client = Mock()
        client.head_object.return_value = {"ContentLength": 8, "ContentType": "text/plain"}
        actual = read_s3(client, "aws-qa-bucket", "qa/hello.txt", "123456789012")
        client.head_object.assert_called_once_with(
            Bucket="aws-qa-bucket", Key="qa/hello.txt", ExpectedBucketOwner="123456789012"
        )
        self.assertEqual(differences(actual, ObjectMetadata(FIXTURE.stat().st_size, "text/plain")), [])

    def test_gcs_reads_metadata_of_the_requested_object(self):
        client = Mock()
        bucket = client.bucket.return_value
        blob = bucket.blob.return_value
        blob.size = FIXTURE.stat().st_size
        blob.content_type = "text/plain"
        actual = read_gcs(client, "gcp-qa-bucket", "qa/hello.txt")
        client.bucket.assert_called_once_with("gcp-qa-bucket")
        bucket.blob.assert_called_once_with("qa/hello.txt")
        blob.reload.assert_called_once_with(timeout=10, retry=None)
        self.assertEqual(actual, ObjectMetadata(8, "text/plain"))

    @patch("cloud_smoke.read_live", return_value=ObjectMetadata(3, "application/json"))
    def test_cli_fails_on_metadata_mismatch(self, read_live):
        with patch("sys.stderr") as stderr:
            code = main(["gcp", "--bucket", "gcp-qa-bucket", "--gcp-project", "qa-project"])
        self.assertEqual(code, 1)
        output = "".join(str(call.args[0]) for call in stderr.write.call_args_list)
        self.assertIn("size expected 8 bytes", output)
        self.assertIn("content type expected", output)

    @patch("cloud_smoke.read_live", side_effect=TimeoutError("fixture timeout"))
    def test_cli_distinguishes_provider_error(self, read_live):
        with patch("sys.stderr"):
            code = main(["gcp", "--bucket", "gcp-qa-bucket", "--gcp-project", "qa-project"])
        self.assertEqual(code, 2)


if __name__ == "__main__":
    unittest.main()
