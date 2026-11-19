# 01. Retryable UI assertions with Cypress

**Intermediate** · [Guide and prerequisites](../README.md) · [Worked example](../examples/cypress/cypress/e2e/shipping.cy.js)

This JavaScript spec generates a named test for each subtotal. Each test opens the calculator and clicks once. Cypress retries the status query and its assertion as the DOM updates. Keeping the click outside the assertion avoids submitting the same action again during verification.

## Run the example

From the guide directory, after completing the prerequisites in the guide:

```sh
cd examples/cypress
npm install
npm test
```

## Interview question

**Why does reading text once create a flaky check?**

A snapshot taken just after the click can contain Calculating… . A linked query and should assertion can observe the later update. Use the assertion log to distinguish a missing element from a wrong final message.

## Practice

Run both cases against the local demo. Change only the expected free-shipping text. Observe that the paid case passes and the failing case retries the assertion until timeout.
