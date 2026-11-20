# async / await & error handling

[03 · Module topics](../../modules/03-language-deep-dives.md) · [Curriculum index](../../README.md)

**Category** · JAVASCRIPT DEEP DIVE

async/await is promise syntax that reads like synchronous code. await unwraps a promise; errors become normal exceptions you catch with try/catch. The traps: forgetting await (a floating promise — the #1 flake source), and awaiting in a loop when the calls could run in parallel.

## Worked reference

````text
// await unwraps a promise; try/catch handles rejection
async function login(page, user) {
  try {
    await page.fill('#email', user.email);   // await EVERY action
    await page.click('#submit');
    await page.waitForURL('**/dashboard');
  } catch (err) {
    await page.screenshot({ path: 'login-fail.png' });
    throw err;
  }
}

// TRAP 1: forgotten await = floating promise = flake
// page.click('#submit');   // test races ahead ❌

// TRAP 2: serial when it could be parallel
// SLOW — each waits for the last:
for (const id of ids) await getUser(id);

// FAST — all in flight at once:
const users = await Promise.all(ids.map(getUser));
````

## Exercise

Take a serial await-in-loop that fetches 10 pages and rewrite it to run concurrently with Promise.all. Compare timing.
