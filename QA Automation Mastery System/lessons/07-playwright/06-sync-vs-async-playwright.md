# Sync vs async Playwright

[07 · Module topics](../../modules/07-playwright.md) · [Curriculum index](../../README.md)

**Category** · PLAYWRIGHT CORE

Playwright is asynchronous at heart — every browser action is a promise/awaitable because it waits on a real browser over a socket. In JS/TS and Java that's the only mode: you await every call. Python is special: it ships BOTH a sync API (blocking, reads top-to-bottom — best for pytest) and an async API (asyncio, for concurrency or async apps). The rule everywhere: forgetting to await is the #1 source of race-condition flakes.

## Worked reference

````text
# ── SYNC API — blocking, top-to-bottom (use with pytest) ──
from playwright.sync_api import sync_playwright

with sync_playwright() as p:
    browser = p.chromium.launch()
    page = browser.new_page()
    page.goto("https://shop.dev")      # blocks until done
    page.get_by_role("button").click() # no await needed
    browser.close()

# ── ASYNC API — asyncio; await every call ──
import asyncio
from playwright.async_api import async_playwright

async def main():
    async with async_playwright() as p:
        browser = await p.chromium.launch()
        page = await browser.new_page()
        await page.goto("https://shop.dev")
        await page.get_by_role("button").click()
        await browser.close()

# ── WHY ASYNC: drive many pages CONCURRENTLY ──
async def check(browser, url):
    page = await browser.new_page()
    await page.goto(url)
    return await page.title()

async def parallel():
    async with async_playwright() as p:
        b = await p.chromium.launch()
        titles = await asyncio.gather(         # all at once
            check(b, "https://shop.dev/1"),
            check(b, "https://shop.dev/2"),
            check(b, "https://shop.dev/3"))
        await b.close()

asyncio.run(main())
# Rule of thumb: pytest suite -> SYNC. Async app / concurrency -> ASYNC.
````

## Exercise

Write the same login flow twice in Python — sync_playwright and async_playwright — then run two pages concurrently with asyncio.gather in the async version.
