# TLS & certificates

[05 · Module topics](../../modules/05-networking.md) · [Curriculum index](../../README.md)

**Category** · NETWORK FUNDAMENTALS

HTTPS = HTTP inside a TLS tunnel. The handshake authenticates the server (certificate chain up to a trusted CA) and agrees on encryption keys. Testers hit TLS whenever a proxy needs its CA installed to decrypt traffic, a staging env uses self-signed certs, or a cert silently expires and every test goes red at once.

## Worked reference

````text
TLS — the tunnel around HTTP

THE HANDSHAKE (simplified, TLS 1.3)
  client ──hello (supported ciphers)──▶ server
  client ◀──certificate + key share──── server
  client verifies cert CHAIN:
    server cert → intermediate CA → root CA
    (root must be in the client's TRUST STORE)
  keys agreed → everything after is encrypted

WHAT THE CERT PROVES
  the server controls this DOMAIN (SAN list) ·
  vouched for by a CA your machine already trusts ·
  within validity dates

TESTER ENCOUNTERS
  INTERCEPTING PROXIES (Charles/mitmproxy) decrypt by
    re-signing traffic with THEIR CA → you must install
    that CA on the device; certificate PINNING in
    mobile apps blocks this by design
  SELF-SIGNED staging certs → tools need an escape:
    curl -k · verify=False (requests) ·
    ignoreHTTPSErrors (Playwright) — NEVER in prod tests
  EXPIRY — certs die silently and everything reds at
    once; monitor days-to-expiry as a synthetic check

READING TLS ERRORS
  UNKNOWN_CA / self-signed   trust store missing the CA
  HOSTNAME MISMATCH          cert's SAN ≠ requested host
                             (classic: IP vs domain)
  EXPIRED                    check server date too
  HANDSHAKE_FAILURE          protocol/cipher mismatch
                             (ancient client or server)
````

## Exercise

Inspect your staging cert with `openssl s_client -connect host:443`: who issued it, when does it expire, does the chain validate? Then add a cert-expiry check to a monitoring suite.
