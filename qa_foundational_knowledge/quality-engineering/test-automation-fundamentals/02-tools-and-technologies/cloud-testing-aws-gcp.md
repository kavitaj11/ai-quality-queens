# AWS and Google Cloud storage testing

Do provider adapters preserve the same storage contract?

[Hands-on lab](../../../../practice-qa-projects/cloud-testing-aws-gcp/README.md) · [10 · CI/CD & Infrastructure](../../../../QA%20Automation%20Mastery%20System/modules/10-ci-cd-infrastructure.md)

## What to test

Use the offline adapters to check object keys, content and required metadata with identical fixtures. Compare the normalized contract across providers. Keep provider-specific behavior visible in separate assertions.

## Practice assignment

Follow the setup in the linked lab. Run the offline suite and identify one claim it supports and one requiring a live environment, such as the deployed identity's access to a specific bucket.

[Tools and technologies study guide](README.md) · [All practice QA projects](../../../../practice-qa-projects/)
