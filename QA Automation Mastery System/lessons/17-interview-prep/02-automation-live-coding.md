# Automation live-coding

[17 · Module topics](../../modules/17-interview-prep.md) · [Curriculum index](../../README.md)

**Category** · SDET CODING DRILLS

‘Automate this page in front of me’: narrate locator choices, waits, and assertions as you type. Interviewers grade your decision commentary as much as the code.

## Worked reference

````text
# Narrate as you write:
# "Role-based locator — survives CSS refactors"
page.get_by_role("button", name="Checkout").click()
# "Web-first assertion — no explicit wait needed"
expect(page.get_by_test_id("order-confirm"))\
    .to_contain_text("Thank you")
````

## Exercise

Record yourself automating saucedemo.com checkout in 15 minutes while explaining every choice aloud.
