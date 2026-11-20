# Playwright–.NET framework architecture

[13 · Module topics](../../modules/13-test-automation-system-design.md) · [Curriculum index](../../README.md)

**Category** · FRAMEWORK BLUEPRINTS

For C# shops: Microsoft.Playwright with NUnit (or MSTest/xUnit) via Microsoft.Playwright.NUnit base classes — PageTest gives you a fresh page per test. Architecture mirrors the TS version with .NET idioms: appsettings.json + IConfiguration for env config, DI via constructor injection or base-class composition, async/await throughout, and dotnet test as the CI entrypoint alongside the rest of the .NET solution.

## Worked reference

````text
PLAYWRIGHT–.NET — the C#-shop blueprint

  TestProject/
  ├─ TestProject.csproj      Microsoft.Playwright.NUnit
  ├─ appsettings.json        env config via
  │  appsettings.Staging.json IConfiguration —
  │                          same pattern as the app
  ├─ Fixtures/               base classes extending
  │    AuthenticatedPageTest  PageTest: storage state,
  │                          per-test page isolation
  ├─ Pages/                  page objects, async all
  ├─ Api/                    HttpClient seeding helpers
  └─ Tests/                  [Test] methods, [Category]
                             for smoke/regression

  WHAT PageTest GIVES YOU (per test, automatic)
  browser (shared) → context (fresh) → Page property —
  the isolation model TS gets from fixtures, via
  inheritance instead

  .NET-SPECIFIC WIRING
  config     IConfiguration + env override:
             ASPNETCORE_ENVIRONMENT=Staging
  parallel   NUnit [Parallelizable(ParallelScope.Self)]
             + workers via .runsettings
  CI         dotnet test --logger junit — plugs into
             the same pipeline as unit tests
  install    playwright.ps1 install (after build)

  WHY .NET TEAMS PICK IT
  one language across app + tests · shares models/
  DTOs with the product (compile-time API drift
  detection!) · same solution, same code review

  TRADE-OFFS VS TS
  runner features arrive TS-first (UI mode, some
  trace tooling) · smaller community · NUnit base-
  class inheritance is stiffer than fixture
  composition — keep hierarchies shallow
````

## Exercise

Your org's product and unit tests are C#/.NET. Argue Playwright-.NET vs a separate TS test repo: build integration, skill reuse, and where each wins.
