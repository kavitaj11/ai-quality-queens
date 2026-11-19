# Test Automation in JavaScript

## Browser testing with Cypress

Use [01. retryable UI assertions with Cypress](tutorials/01-retryable-ui-assertions-with-cypress.md) to check a shipping calculator at the free-shipping boundary. The [JavaScript spec](examples/cypress/cypress/e2e/shipping.cy.js) submits each subtotal once and waits for the displayed quote to match the expected result.

### Run the lab

Use Node.js 20+, npm, and Python 3. Open two terminals in this guide directory. In the first, serve the [shipping calculator](../test-automation-in-java/examples/web/index.html) shared with the Java browser labs:

```sh
python3 -m http.server 8000 --bind 127.0.0.1 --directory ../test-automation-in-java/examples/web
```

Open <http://127.0.0.1:8000> to confirm the calculator loads. In the second terminal, install the pinned Cypress dependency and run the spec in Electron:

```sh
cd examples/cypress
npm install
npm test
```

Expect two passing cases. A subtotal of 4,999 cents incurs a 499-cent fee, and 5,000 cents qualifies for free shipping.
