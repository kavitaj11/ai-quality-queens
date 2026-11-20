# Playwright with Java and APIRequestContext

**Hands-on project** · [Playwright with Java and APIRequestContext lab](../../../../practice-qa-projects/playwright-java/README.md)

**QA Automation Mastery** · [09 · API Testing](../../../../QA%20Automation%20Mastery%20System/modules/09-api-testing.md)

Does an API result agree with the browser and stored record?

## API checks and browser checks observe different layers

An API request context sends HTTP requests directly. It can create a fixture, retrieve a service representation or exercise an endpoint independently of the browser UI. A browser check observes what the user can see or do. Connecting those observations requires the same record identity and an intentional authentication context. Playwright supports both a request context associated with a browser context and an independently created request context. Decide whether the test requires shared cookies or a separate client.

Separate transport, contract and business assertions. A successful HTTP status establishes how the request was handled. Schema validation checks the structure and types of the response. A business assertion checks whether the returned value is correct for the starting state and action. Carry a generated identifier across layers to prevent an unrelated successful transaction from satisfying the test.

## What to test

Create test data through the API and carry its identifier into the browser and JDBC check. A valid JSON schema establishes structure. A separate assertion must establish the expected status for this record.

## Practice assignment

Follow the setup in the linked lab. Change the expected business status while keeping the schema valid. Identify the first assertion that detects the mismatch and preserve its diagnostic output.

[Tools and technologies study guide](README.md) · [All practice QA projects](../../../../practice-qa-projects/)
