# Waits: implicit vs explicit

[06 · Module topics](../../modules/06-selenium.md) · [Curriculum index](../../README.md)

**Category** · WEBDRIVER CORE

Never mix them — an implicit wait plus an explicit wait produces unpredictable, compounding timeouts. Set implicit to 0 and use explicit waits everywhere. An explicit wait polls until an ExpectedCondition (EC) is true or it times out. This lists the full EC toolbox; sleep() in a test is a code smell you'll be asked about in interviews.

## Worked reference

````text
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By

# Implicit wait: set ONCE, and prefer 0 when using explicit
driver.implicitly_wait(0)

wait = WebDriverWait(driver, 10, poll_frequency=0.25,
    ignored_exceptions=[StaleElementReferenceException])
L = (By.ID, "checkout")

# ── PRESENCE / VISIBILITY ──
wait.until(EC.presence_of_element_located(L))       # in DOM
wait.until(EC.visibility_of_element_located(L))     # in DOM + shown
wait.until(EC.visibility_of_all_elements_located(L))
wait.until(EC.invisibility_of_element_located(L))   # gone/hidden

# ── CLICKABILITY / STATE ──
wait.until(EC.element_to_be_clickable(L))           # visible + enabled
wait.until(EC.element_located_to_be_selected(L))
wait.until(EC.element_selection_state_to_be(L, True))

# ── TEXT / ATTRIBUTE / VALUE ──
wait.until(EC.text_to_be_present_in_element(L, "Paid"))
wait.until(EC.text_to_be_present_in_element_value(L, "USD"))
wait.until(EC.text_to_be_present_in_element_attribute(
    L, "class", "active"))

# ── TITLE / URL ──
wait.until(EC.title_is("Dashboard"))
wait.until(EC.title_contains("Dash"))
wait.until(EC.url_contains("/orders"))
wait.until(EC.url_to_be("https://shop.dev/done"))
wait.until(EC.url_matches(r"/orders/\d+"))

# ── FRAMES / WINDOWS / ALERTS ──
wait.until(EC.frame_to_be_available_and_switch_to_it("editor"))
wait.until(EC.number_of_windows_to_be(2))
wait.until(EC.new_window_is_opened(driver.window_handles))
wait.until(EC.alert_is_present())

# ── STALENESS (element re-rendered) ──
old = driver.find_element(*L)
wait.until(EC.staleness_of(old))

# ── COUNT / SELECTION ──
wait.until(EC.presence_of_all_elements_located((By.CSS_SELECTOR, ".row")))
# Combine: any / all of several conditions
wait.until(EC.any_of(EC.title_is("A"), EC.title_is("B")))
wait.until(EC.all_of(EC.visibility_of_element_located(L),
                     EC.element_to_be_clickable(L)))
````

## Exercise

Replace every sleep() in a suite with the most specific ExpectedCondition for each case (visible / clickable / text present / staleness).
