# 4. Your First Test Plan

Open `jmeter/shop_load_test.jmx` in the JMeter GUI and follow along. This is the tree:

```
Shop API - Load Test                      (Test Plan)
├── Load settings (override with -J)      User Defined Variables: host, port, think time
├── HTTP Request Defaults                 server, port, protocol, timeouts for every request
├── JSON headers                          HTTP Header Manager: Content-Type, Accept
├── Users from CSV                        CSV Data Set Config: username, password, searchTerm
└── Shoppers                              Thread Group: users, ramp-up, duration from -J properties
    ├── Login once per user               Once Only Controller
    │   └── 01 Login                      POST /api/login
    │       ├── Extract token             JSON Extractor  → ${token}
    │       └── Status is 200             Response Assertion
    ├── Bearer token (correlated)         HTTP Header Manager: Authorization: Bearer ${token}
    ├── 02 Search products                GET /api/products?q=${searchTerm}
    │   ├── Think time before search      Uniform Random Timer 500–1500 ms
    │   ├── Extract a random productId    JSON Extractor (match no. 0 = random) → ${productId}
    │   └── Body has items                Response Assertion
    ├── 03 View product                   GET /api/products/${productId}
    │   ├── Think time before viewing
    │   └── Status is 200
    └── 30% of iterations buy             Throughput Controller (percent executions = 30)
        ├── 04 Add to cart                POST /api/cart → JSON Extractor → ${cartId}
        │   └── Think time before adding to cart
        └── 05 Checkout                   POST /api/orders {"cartId": "${cartId}"}
            └── Order confirmed           Response Assertion: body contains "status": "CONFIRMED"
```

## The building blocks

| Element type | What it does | In this plan |
|---|---|---|
| **Thread Group** | Defines users (threads), ramp-up, and how long or how many loops | Shoppers |
| **Sampler** | Sends a request and records the result | The five HTTP Requests |
| **Config element** | Shared settings and data | HTTP Request Defaults, Header Managers, CSV Data Set |
| **Logic controller** | Decides which samplers run and how often | Once Only, Throughput Controller |
| **Timer** | Adds a pause (think time) before a sampler | Uniform Random Timers |
| **Pre/Post-processor** | Runs before/after a sampler, e.g. extract values | JSON Extractors |
| **Assertion** | Marks a sample as failed if the response is wrong | Status and body checks |
| **Listener** | Shows or saves results | None in the plan: results go to a file (Chapter 6) |

## Scope: the rule beginners trip over

An element applies to **everything at its level and below**:

- A **config element** or **assertion** placed under the Thread Group applies to every sampler in it.
- A **timer** placed under the Thread Group runs **before every sampler** in it. Two timers there means two pauses before every request.
- An element placed **as a child of a sampler** applies only to that sampler.

The first draft of this very plan had two timers at Thread Group level. That would have doubled the think time before every request, including login and checkout, and quietly halved the load. That's why each timer now sits under the sampler it belongs to. When your measured throughput doesn't match Little's Law, check timer scope first.

## Making the plan configurable

Hard-coding "50 users" in the plan means editing it for every run. Instead the plan reads **JMeter properties** with the `__P` function, with defaults:

```
Number of threads:  ${__P(users,20)}
Ramp-up period:     ${__P(rampup,20)}
Duration:           ${__P(duration,60)}
Server port:        ${__P(port,8099)}
```

On the command line you override them with `-J`:

```bash
jmeter -n -t shop_load_test.jmx -Jusers=100 -Jrampup=60 -Jduration=300
```

One plan file, any load level. That is also what makes the agent in Chapter 10 possible.

## Assertions: make failures visible

Without assertions, JMeter only marks a sample failed on HTTP error codes or connection problems. A server that returns `200 OK` with an error page counts as a success, often a very fast one, which makes results look better than reality.

Each request in this plan checks something specific: login returns 200, search returns an `items` field, checkout returns `"status": "CONFIRMED"`. Keep assertions light (status codes, a short substring) because heavy ones, such as large regular expressions or XPath on big responses, use load-generator CPU.

## Build it yourself (exercise)

Recreate the plan from scratch in the GUI with only login and search first:

1. Add a Thread Group with 1 user and 1 loop.
2. Add HTTP Request Defaults (`127.0.0.1`, port `8099`).
3. Add an HTTP Header Manager with `Content-Type: application/json`.
4. Add the login request with body `{"username": "alice", "password": "Passw0rd!"}`.
5. Add **View Results Tree** temporarily, run it, and look at the response.
6. Add the search request without a token, run it, and look at the `401`. You have just discovered why Chapter 5 exists.

Next: [5. Correlation and parameterization](05-correlation-and-parameterization.md)
