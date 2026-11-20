# AWS and Google Cloud storage testing

**Hands-on project** · [AWS and Google Cloud storage testing lab](../../../../practice-qa-projects/cloud-testing-aws-gcp/README.md)

**QA Automation Mastery** · [10 · CI/CD & Infrastructure](../../../../QA%20Automation%20Mastery%20System/modules/10-ci-cd-infrastructure.md)

Do provider adapters preserve the same storage contract?

## Storage contracts and provider adapters

Object storage addresses content through a bucket and an object key. Alongside the bytes, an application may depend on metadata such as content type or a business identifier. Define the application's contract in terms of operations and observable outcomes: write an object, read its bytes and metadata, or handle a missing key. This supplies a common test oracle across providers.

An adapter translates that contract into provider-specific operations and maps the result back to the application's model. Offline tests can exercise that mapping with controlled fixtures, including missing metadata and empty content. A deployed identity's permissions, a bucket policy and live service configuration require evidence from the corresponding environment. Keep those observations separate when stating what a passing test establishes.

## What to test

Use the offline adapters to check object keys, content and required metadata with identical fixtures. Compare the normalized contract across providers. Keep provider-specific behavior visible in separate assertions.

## Practice assignment

Follow the setup in the linked lab. Run the offline suite and identify one claim it supports and one requiring a live environment, such as the deployed identity's access to a specific bucket.

[Tools and technologies study guide](README.md) · [All practice QA projects](../../../../practice-qa-projects/)
