# Security testing basics

[11 · Module topics](../../modules/11-non-functional-testing.md) · [Curriculum index](../../README.md)

**Category** · SECURITY TESTING

Review the controls around identity, permissions and sensitive data. Start with a role matrix listing each protected action, the roles allowed to perform it, and the expected response when access is denied. Verify that audit records capture the decision without storing secrets.

## Turn a permission rule into test cases

Authentication establishes the identity making a request. Authorization decides whether that identity may perform an action on a particular record. For this exercise, assume an order owner can read their order, an assigned support agent can read it, and only an assigned supervisor can change its delivery window. Write the expected outcomes before running the checks.

## Worked example

Use a synthetic order with delivery window A. Each row starts from the same saved state. This example API returns 403 for an authenticated caller whose action is forbidden.

````text
Actor                 Action           Response   Expected outcome
Order owner           Read order       200        Own order is returned
Assigned agent        Read order       200        Approved support fields only
Assigned agent        Set window B     403        Window remains A
Assigned supervisor   Set window B     200        Window becomes B
Signed-out visitor    Read order       401        No order details returned
````

## What counts as evidence?

For the denied update, check the response, then read the saved order through an approved test fixture and compare the window with its starting value. Match the request ID to the audit event. Under this example policy, the event must identify the actor, action and decision without recording a session token or the customer's address. A 403 paired with a saved change is a failed test.

## Practice and self-check

Add a second synthetic customer and specify the result when they request the first customer's order. Write down the API's agreed response for this case, which fields may appear, and how you will verify that the record stayed unchanged. Then explain which bug the assigned-supervisor success case would catch if every update were accidentally denied.
