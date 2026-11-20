# Test code: structure & assertions

[02 · Module topics](../../modules/02-test-harness-setup.md) · [Curriculum index](../../README.md)

**Category** · LANGUAGE ESSENTIALS

How a test file is organized in each stack: pytest classes and parametrize, Playwright's test/expect, TestNG classes and data providers. Same login test, four dialects — switch tabs to compare.

## Worked reference

````text
import pytest
from pages.login_page import LoginPage

class TestLogin:
    def test_valid_login(self, page, user):
        LoginPage(page).login(user.email, "s3cret")
        assert page.title() == "Dashboard"

    @pytest.mark.parametrize("pwd", ["", "short", "x" * 129])
    def test_rejects_bad_password(self, page, user, pwd):
        LoginPage(page).login(user.email, pwd)
        assert "Invalid credentials" in \
            page.text_content(".alert")
````

## Exercise

Write the same 3-case login test in your primary language, then port it to one other stack and note what changed.
