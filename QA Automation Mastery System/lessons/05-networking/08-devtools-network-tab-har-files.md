# DevTools network tab & HAR files

[05 · Module topics](../../modules/05-networking.md) · [Curriculum index](../../README.md)

**Category** · TOOLING & DEBUGGING

The network tab is the tester's oscilloscope: every request's timing broken into DNS, connect, TLS, TTFB, and download — plus initiator chains (what triggered this call?) and the protocol column. HAR files freeze all of it into a shareable artifact: attach one to a bug and the 'cannot reproduce' argument ends.

## Worked reference

````text
DEVTOOLS NETWORK TAB — reading a request's life

  TIMING BREAKDOWN (click any request → Timing)
  queued → DNS → connect → TLS → SENT → TTFB → download
           │       │        │            │        │
         resolver  TCP      handshake    SERVER   payload
         slow?     refused? cert issue?  THINK    size/
                                         TIME     bandwidth

  TTFB dominates   → backend/db problem
  download dominates → payload too big (check
                       compression: content-encoding)
  DNS/connect/TLS  → infra, not the app

  COLUMNS THAT ANSWER QUESTIONS
  Initiator   what code/redirect caused this call?
  Protocol    h1/h2/h3 (see HTTP versions)
  Size        transferred vs resource = compression
  Waterfall   the shape tells the story

  FILTERS: XHR/fetch only · status:>=400 ·
  'Preserve log' across redirects · 'Disable cache'
  for honest first-load numbers

HAR FILES — the reproducibility artifact
  network tab → export HAR = every request, header,
  timing, and body* as JSON
  • attach to bug reports (ends 'works for me')
  • diff a good session vs a bad one
  • mine in code: har['log']['entries'] → timings
  • Playwright records them: record_har_path
  *SCRUB before sharing: HARs contain cookies,
   tokens, and PII — sanitize or use redacted export
````

## Exercise

Record a HAR of a slow page load, find the request with the worst TTFB, name which timing segment dominates, and state — with evidence — whether it's frontend, network, or backend.
