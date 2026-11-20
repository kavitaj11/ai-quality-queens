# async / await & asyncio

[03 · Module topics](../../modules/03-language-deep-dives.md) · [Curriculum index](../../README.md)

**Category** · PYTHON DEEP DIVE

async def defines a coroutine; await suspends it until an awaitable resolves, freeing the loop to run others. It's concurrency, not parallelism — perfect for I/O-bound test work (many HTTP calls at once). asyncio.gather runs awaitables together; Playwright's async API is built on this.

## Worked reference

````text
import asyncio, httpx

# 'async def' makes a coroutine; 'await' suspends it
async def check(client, url):
    r = await client.get(url)          # yields control while waiting
    return r.status_code

async def main():
    urls = [f"https://api.shop.dev/h/{i}" for i in range(20)]
    async with httpx.AsyncClient() as client:
        # gather runs all 20 CONCURRENTLY, not one-by-one
        codes = await asyncio.gather(
            *(check(client, u) for u in urls))
    assert all(c == 200 for c in codes)

asyncio.run(main())

# Playwright async API uses the same model:
#   async with async_playwright() as p:
#       browser = await p.chromium.launch()
#       page = await browser.new_page()
#       await page.goto("https://shop.dev")
````

## Exercise

Fire 20 health-check requests concurrently with asyncio.gather and assert all return 200.
