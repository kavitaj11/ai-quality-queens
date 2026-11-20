# WebSockets, SSE & real-time

[05 · Module topics](../../modules/05-networking.md) · [Curriculum index](../../README.md)

**Category** · HTTP IN DEPTH

Chat, dashboards, notifications, and agent UIs don't fit request/response. WebSockets upgrade an HTTP connection into a persistent two-way channel; Server-Sent Events stream one-way over plain HTTP; polling is the fallback. Testing real-time means asserting on messages over time, reconnect behavior, and what happens when the socket dies mid-flow.

## Worked reference

````text
REAL-TIME TRANSPORT — beyond request/response

  POLLING        ask every N sec — simple, laggy, chatty
  SSE            one HTTP response that never ends;
                 server → client only; auto-reconnect
                 built in; it's how LLM tokens stream
  WEBSOCKET      HTTP Upgrade → persistent 2-way socket
                 (ws:// wss://) — chat, games, live collab

  WS HANDSHAKE
  client ──GET + Upgrade: websocket──▶ server
  client ◀──101 Switching Protocols──  server
  then frames flow both ways until close

WHAT TO TEST (the request/response habits break)
  • lifecycle: connect → auth (token in handshake or
    first frame?) → subscribe → receive → close
  • ORDER + timing of messages, not just content
  • reconnect: kill the socket — does the client
    resume? are missed messages replayed or LOST?
  • idle: LBs kill quiet sockets (~60s) — is there a
    heartbeat/ping? (the classic 'works locally,
    drops in staging')
  • fallback when WS is blocked by proxy/firewall

TOOLING
  DevTools network → WS tab: live frame inspector
  Playwright: page.on('websocket', ws → frame events)
  wscat / websocat — curl for sockets
  load: k6 has WS support; connection count is the
  bottleneck, not requests/sec
````

## Exercise

For a live-updating feature you know, write the test plan: connection lifecycle, message assertions, reconnect-with-missed-messages, and the fallback when WS is blocked.
