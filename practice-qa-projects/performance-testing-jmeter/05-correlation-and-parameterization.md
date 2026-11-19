# 5. Correlation and Parameterization

## Two ideas, often confused

- **Parameterization** = feeding **different input data** into requests, so 100 virtual users are not all "alice searching for mug". Source: CSV files, random functions, counters.
- **Correlation** = **capturing a dynamic value from one response** and sending it in a later request: session tokens, IDs the server just created, CSRF tokens. Without it the script works once, during recording, and fails ever after.

## Parameterization in this plan

`jmeter/data/users.csv`:

```
username,password,searchTerm
alice,Passw0rd!,mug
bob,Passw0rd!,lamp
...
```

The **CSV Data Set Config** leaves *Variable Names* empty, so JMeter uses the header line as names: `${username}`, `${password}`, `${searchTerm}`. With *Sharing mode: All threads* and *Recycle on EOF: true*, each thread takes the next line and wraps around at the end.

Two more parameterization tricks used in the plan:

- `${__Random(1,3)}` picks a random quantity for add-to-cart.
- `${__P(users,20)}` reads a property with a default (Chapter 4).

## Correlation in this plan: a chain of three

```
POST /api/login           → {"token": "9f2c..."}            extract $.token        → ${token}
GET  /api/products?q=mug  → {"items": [{"id": 1001}, ...]}  extract $.items[*].id  → ${productId}  (random match)
GET  /api/products/${productId}
POST /api/cart  {"productId": ${productId}}  → {"cartId": "CART-5a1e..."}  extract $.cartId → ${cartId}
POST /api/orders {"cartId": "${cartId}"}     → {"status": "CONFIRMED"}
```

And the token is sent on every later request by a **Header Manager**: `Authorization: Bearer ${token}`.

### The JSON Extractor settings that matter

| Field | Value here | Why |
|---|---|---|
| Names of created variables | `token` | The variable you'll use as `${token}` |
| JSON Path expressions | `$.token` | Where the value is in the response |
| Match No. | `1` for token, `0` for productId | `1` = first match, `0` = **random** match, `-1` = all (as `name_1`, `name_2` …) |
| Default Values | `TOKEN_NOT_FOUND` | Makes a failed extraction **obvious** in later requests |

Always set a meaningful default. An empty default produces requests like `/api/products/`, which are hard to diagnose. `PRODUCT_NOT_FOUND` in a URL tells you immediately which extraction failed.

Other extractors: **Regular Expression Extractor** (any text, HTML), **Boundary Extractor** (text between two strings: fast and simple), **XPath / XPath2 Extractor** (XML).

## How to find what needs correlating

1. Record or write the flow and run it with **1 user** in the GUI with **View Results Tree**.
2. For each request, look for values that came from a previous response: tokens, IDs, hidden form fields, `Location` headers.
3. Run the flow **twice**. Any value that changes between runs and is sent back to the server must be correlated.
4. In View Results Tree, use the **JSON Path Tester** to try expressions against a real response.
5. Add a **Debug Sampler** temporarily to print all variables after each step.

## Debugging a broken chain: a real example

During the 200-user step test in Chapter 7, the results file contained these errors:

```
Non HTTP response code: ...HttpHostConnectException   9   ← the first failure
401 Unauthorized                                        48
400 Bad Request                                          9
404 Not Found                                            4
```

The first rows of the results file for the failing virtual user tell the whole story:

```
01 Login             Connection refused        ← login never reached the server
02 Search products   401 Unauthorized          ← no token: ${token} = TOKEN_NOT_FOUND
03 View product      401  /api/products/PRODUCT_NOT_FOUND   ← search failed, so no productId
```

One refused connection at login caused every later error for that user, because the **Once Only Controller** never retries the login. If you count errors by type, 401 looks like the biggest problem. It isn't. **Always find the first error in a chain**; the rest are usually consequences.

Ways to make a script more robust to this:

- Put an **If Controller** around the rest of the iteration: run it only when `"${token}" != "TOKEN_NOT_FOUND"`, and otherwise log in again.
- Set the Thread Group's *Action to be taken after a Sampler error* to **Start Next Thread Loop**, so a broken iteration is abandoned instead of sending garbage requests.
- Never "fix" it by ignoring errors: the refused connection is a real finding (Chapter 7).

Next: [6. Running tests and analyzing results](06-running-and-analyzing-results.md)
