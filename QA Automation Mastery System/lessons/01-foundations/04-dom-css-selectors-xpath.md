# DOM, CSS selectors & XPath

[01 · Module topics](../../modules/01-foundations.md) · [Curriculum index](../../README.md)

**Category** · WEB & HTTP ESSENTIALS

Every UI tool ultimately queries the DOM. Master CSS selectors first (faster, cleaner, the default everywhere), and keep XPath for the two things CSS can't do: match by visible text, and traverse to ancestors/preceding siblings. Priority order for stable tests: test id > id > semantic CSS > XPath by structure — never brittle nth-child chains or absolute XPath.

## Worked reference

````text
THE DOM, CSS SELECTORS & XPATH

Every UI tool ultimately finds an element in the DOM tree. Two
query languages do it — learn CSS first, keep XPath for its two
superpowers.

┌─ CSS SELECTORS  (fast · clean · the default everywhere) ───┐
  BASIC        #id   .class   tag   *
  ATTRIBUTE    [name='email']        exact
               [href^='https']       ^=  starts-with
               [src$='.png']         $=  ends-with
               [class*='col-']       *=  contains
               [data-x~='active']    ~=  word in a list
  COMBINATORS  a b   descendant   ·  a > b  direct child
               a + b adjacent sib ·  a ~ b  general sibling
  PSEUDO       :first-child :last-child :nth-child(2n)
               :not([disabled]) :is(...) :has(> img)

┌─ XPATH  (use for the 2 things CSS CAN'T do) ───────────────┐
  1. MATCH BY TEXT   //button[text()='Save']
                     //a[contains(., 'Sign')]
  2. TRAVERSE UP/SIDE //td[.='Total']/ancestor::table
                     //label[.='Email']/following-sibling::input
  Also: //div[@class='card'][2]   (attribute + 1-based index)

┌─ FINDING ELEMENTS FROM JAVASCRIPT (DevTools console) ──────┐
  document.getElementById('email')          fastest, id only
  document.getElementsByClassName('btn')     live collection
  document.getElementsByName('email')
  document.querySelector('.btn.primary')     first CSS match
  document.querySelectorAll('form input')    all matches
  $$("[data-testid='x']")  $x("//button")    console helpers

LOCATOR PRIORITY (for STABLE tests)
  test id  >  id  >  semantic CSS  >  XPath by structure
  ✗ avoid  brittle nth-child chains & absolute XPath —
           they break on the smallest layout change.
  Brittle locators are the #1 cause of flaky suites.

  → The full per-language locator code (Selenium By strategies,
    dynamic/partial attrs, relative locators, hidden elements,
    shadow DOM, custom strategies) lives in the Selenium phase
    under "Locator strategies".
````

## Exercise

On any site, write one selector for each CSS family (id, class, attribute, combinator, pseudo-class) and two XPath expressions (one by text, one by axis). Verify each matches exactly one element in DevTools.
