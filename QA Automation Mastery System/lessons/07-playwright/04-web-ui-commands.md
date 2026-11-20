# Web UI commands

[07 · Module topics](../../modules/07-playwright.md) · [Curriculum index](../../README.md)

**Category** · PLAYWRIGHT CORE

The everyday verbs of a Playwright UI test: navigation, the input actions (click/fill/type/press/check/select), reading state (text, attribute, count, visibility), keyboard and mouse, files and dialogs, and frames. All of them auto-wait for the element to be actionable — no manual waits. This is the reference you'll reach for most.

## Worked reference

````text
# ── NAVIGATION ──
page.goto("https://shop.dev/login")
page.reload(); page.go_back(); page.go_forward()

# ── INPUT ACTIONS (all auto-wait for actionability) ──
page.get_by_label("Email").fill("qa@test.dev")   # clear + set
page.get_by_label("Bio").type("hello", delay=50) # keystroke-by-key
page.get_by_role("button", name="Save").click()
page.get_by_role("checkbox").check()             # / uncheck()
page.get_by_label("Country").select_option("US") # <select>
page.get_by_role("link", name="Docs").hover()
page.get_by_test_id("row").dblclick()
page.get_by_role("textbox").press("Enter")       # key press
page.get_by_role("textbox").clear()

# ── READING STATE ──
page.get_by_role("heading").text_content()
page.get_by_test_id("total").inner_text()
page.get_by_role("textbox").input_value()
page.get_by_test_id("badge").get_attribute("data-state")
page.get_by_role("listitem").count()
page.get_by_role("alert").is_visible()           # is_enabled/checked

# ── ASSERTIONS (web-first, auto-retry) ──
expect(page).to_have_title("Dashboard")
expect(page).to_have_url("**/dashboard")
expect(page.get_by_role("alert")).to_have_text("Saved")

# ── FILES / DIALOGS / FRAMES ──
page.get_by_label("Avatar").set_input_files("a.png")
page.on("dialog", lambda d: d.accept())          # alert/confirm
frame = page.frame_locator("iframe#editor")
frame.get_by_role("textbox").fill("in frame")
page.screenshot(path="page.png", full_page=True)
````

## Exercise

Automate a full form: navigate, fill fields, pick a dropdown, check a box, upload a file, submit, and read back the confirmation text.
