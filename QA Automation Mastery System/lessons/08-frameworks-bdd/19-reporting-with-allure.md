# Reporting with Allure

[08 · Module topics](../../modules/08-frameworks-bdd.md) · [Curriculum index](../../README.md)

**Category** · BDD WITH CUCUMBER

Rich, step-level HTML reports with history, attachments, and flaky tracking. Works across pytest, TestNG, Cucumber, and Playwright — one reporting language for all your stacks.

## Worked reference

````text
import allure

@allure.title("Checkout with saved card")
@allure.severity(allure.severity_level.CRITICAL)
def test_checkout(page):
    with allure.step("Add product to cart"):
        add_to_cart(page, "A-100")
    with allure.step("Pay with saved card"):
        pay(page)
    allure.attach(page.screenshot(),
                  attachment_type=allure.attachment_type.PNG)
````

## Exercise

Add allure-pytest, attach a screenshot on failure, and publish the report from a CI artifact.
