# TCP/IP & the layered model

[05 · Module topics](../../modules/05-networking.md) · [Curriculum index](../../README.md)

**Category** · NETWORK FUNDAMENTALS

Every request your test makes rides a stack: application (HTTP) over transport (TCP/UDP, ports) over network (IP) over link. Knowing the layers tells you WHERE a failure lives — 'connection refused' is transport (nothing listening on the port), 'no route to host' is network, a 500 is application. Sockets, ports, and the TCP handshake are the vocabulary of every timeout you'll ever debug.

## Worked reference

````text
THE STACK — where your test's request actually travels

  ┌─ APPLICATION  HTTP · WebSocket · gRPC · DNS ─────┐
  │   what your assertions see: methods, headers,   │
  │   status codes, payloads                        │
  ├─ TRANSPORT    TCP (reliable) · UDP (fast) ──────┤
  │   PORTS: 443 https · 80 http · 5432 postgres    │
  │   'connection refused' lives HERE               │
  ├─ NETWORK      IP — addressing + routing ────────┤
  │   'no route to host' · unreachable subnets      │
  ├─ LINK         ethernet / wifi ──────────────────┤
  └──────────────────────────────────────────────────┘

TCP: THE 3-WAY HANDSHAKE (before any HTTP happens)
  client ──SYN──▶ server
  client ◀─SYN-ACK─ server
  client ──ACK──▶ server     then bytes flow, in order

  a SOCKET = (client ip:port ⇄ server ip:port)

FAILURE → LAYER → MEANING
  connection refused    transport   nothing on that port
                                    (app down, wrong port)
  connection timeout    network     firewall drops, or
                                    host unreachable
  connection reset      transport   peer killed the socket
                                    (crash, LB idle kill)
  404 / 500             application server is fine — the
                                    APP has the problem

WHY QA CARES
  reading failures by layer turns 'the test is flaky'
  into 'staging LB kills idle sockets at 60s' — a
  ticket someone can actually fix.
````

## Exercise

Run `curl -v https://your-staging-app` and label every line of output with its layer: DNS, TCP connect, TLS, HTTP request, HTTP response.
