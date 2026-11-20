# Locators, filters & chaining

[07 · Module topics](../../modules/07-playwright.md) · [Curriculum index](../../README.md)

**Category** · PLAYWRIGHT CORE

User-facing locators first: getByRole, getByLabel, getByText. Chain and filter to disambiguate instead of writing long CSS paths.

## Worked reference

````text
# Semantic first
page.get_by_role("button", name="Add to cart")
page.get_by_label("Promo code")

# Chain + filter for precision
page.get_by_role("listitem")\
    .filter(has_text="Nimbus 2000")\
    .get_by_role("button", name="Remove")
````

## Exercise

Rewrite 5 CSS-based locators from your Selenium suite as role/label locators.
