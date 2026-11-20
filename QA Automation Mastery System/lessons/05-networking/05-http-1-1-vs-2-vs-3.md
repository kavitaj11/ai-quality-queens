# HTTP/1.1 vs 2 vs 3

[05 · Module topics](../../modules/05-networking.md) · [Curriculum index](../../README.md)

**Category** · HTTP IN DEPTH

Same semantics, different wire. HTTP/1.1 opens ~6 parallel TCP connections and suffers head-of-line blocking; HTTP/2 multiplexes many streams over one connection with header compression; HTTP/3 rides QUIC (UDP) to survive lossy networks and roaming. Perf test results shift dramatically between versions — know which one your load tool and your production edge actually speak.

## Worked reference

````text
HTTP VERSIONS — same semantics, different wire

  HTTP/1.1 (1997)      HTTP/2 (2015)       HTTP/3 (2022)
  text protocol        binary frames       binary on QUIC
  1 req per conn       MULTIPLEXED         multiplexed,
  at a time            streams, one        0-RTT resume,
  (browsers open ~6)   TCP connection      survives IP
  head-of-line         header compression  change (mobile)
  blocking at HTTP     (HPACK)             UDP not TCP —
  level                HoL moves to TCP    no TCP HoL

  WATERFALL SHAPE (DevTools)
  1.1: staircases of 6 · 2/3: a dense parallel burst

WHY QA CARES
  • load tests lie across versions: 1000 virtual users
    on HTTP/1.1 ≈ 6000 connections; on HTTP/2 ≈ 1000.
    Match your tool's version to production's edge
    (k6/JMeter: check http2 support + flags)
  • 'slow only on bad wifi' bugs: HTTP/2 over lossy
    links suffers TCP HoL — HTTP/3 exists for this
  • proxies/corp middleboxes may silently downgrade
    to 1.1 — the protocol column explains odd perf
  • server push is dead; preload/early hints replaced it

CHECK WHAT'S ACTUALLY SPOKEN
  DevTools network tab → Protocol column (h2, h3)
  curl -sI --http2 https://app | head
  your API client's version ≠ the browser's version
````

## Exercise

Load the same page forced to HTTP/1.1 and HTTP/2 (DevTools protocol column + a local server); compare waterfall shapes and explain the difference.
