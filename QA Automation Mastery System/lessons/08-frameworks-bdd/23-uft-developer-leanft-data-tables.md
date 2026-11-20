# UFT Developer (LeanFT) & data tables

[08 · Module topics](../../modules/08-frameworks-bdd.md) · [Curriculum index](../../README.md)

**Category** · UFT / UFT DEVELOPER

UFT Developer (formerly LeanFT) is the modern, IDE-based sibling: shippable as libraries for Java/C#/JS so you write UFT automation in real code inside Visual Studio/Eclipse/IntelliJ with Git, JUnit/TestNG/NUnit, and CI. It brings UFT's robust object identification to a developer workflow. Classic UFT One adds the DataTable (Excel-like) for data-driven runs.

## Worked reference

````text
// ── UFT Developer (LeanFT) — Java, runs under TestNG ──
import com.hp.lft.sdk.*;
import com.hp.lft.sdk.web.*;

@Test
public void login() throws GeneralLeanFtException {
    Browser browser = BrowserFactory.launch(BrowserType.CHROME);
    browser.navigate("https://shop.dev/login");

    browser.describe(EditField.class,
        new EditFieldDescription.Builder()
            .id("email").build()).setValue("qa@test.dev");
    browser.describe(Button.class,
        new ButtonDescription.Builder()
            .id("submit").build()).click();

    Assert.assertTrue(browser.describe(WebElement.class,
        new WebElementDescription.Builder()
            .className("welcome").build()).exists());
}
````

## Exercise

Write a UFT Developer (Java) test that drives the same login via its object model and asserts the result with TestNG; then parameterize a UFT One test from its DataTable.
