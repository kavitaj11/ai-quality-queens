# DNS: how names find servers

[05 · Module topics](../../modules/05-networking.md) · [Curriculum index](../../README.md)

**Category** · NETWORK FUNDAMENTALS

Before any connection, the hostname resolves: browser cache → OS cache → /etc/hosts → resolver → authoritative server. TTLs control how long answers stick. Test engineers meet DNS constantly: routing a test run at a staging IP with a hosts entry, waiting out TTLs after an environment cutover, and diagnosing 'works on my machine' that's really split DNS or a stale cache.

## Worked reference

````text
DNS RESOLUTION — the chain before every request

  browser cache → OS cache → /etc/hosts → configured
  resolver → root → TLD (.com) → authoritative server
                                  │
            answer + TTL ◀────────┘
  TTL = seconds every cache may keep the answer

RECORDS TESTERS MEET
  A / AAAA   name → IPv4 / IPv6
  CNAME      alias → another name (chains resolve)
  the rest (MX, TXT, SRV) — recognize, rarely test

THE TESTER'S TOOLBOX
  /etc/hosts (or Windows equivalent)
    app.shop.dev  10.1.4.22   ← route THIS machine's
    traffic at staging without touching real DNS
  dig app.shop.dev            what does the resolver say?
  dig @8.8.8.8 app.shop.dev   bypass local caches

CLASSIC DNS-SHAPED BUGS
  • env cutover 'didn't work' → old TTL still cached;
    check the TTL before declaring the migration done
  • works on VPN, fails off it → split DNS (internal
    zone answers differently inside the network)
  • intermittent slow first-request → resolution delay,
    not app latency: measure DNS time separately (HAR
    files break it out — see DevTools lesson)
  • container can't reach 'db' → DNS is per-network
    inside Docker; the name only exists on that network
````

## Exercise

Point your app's domain at a different environment using only your hosts file; verify with `dig` (or nslookup) which answer the OS is actually using.
