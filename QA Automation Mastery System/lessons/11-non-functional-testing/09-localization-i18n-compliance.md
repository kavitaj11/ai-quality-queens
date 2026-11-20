# Localization, i18n & compliance

[11 · Module topics](../../modules/11-non-functional-testing.md) · [Curriculum index](../../README.md)

**Category** · OTHER NON-FUNCTIONAL TYPES

Internationalization (i18n) is building for many locales; localization (l10n) is verifying each one. Test translated strings (no truncation/overflow), formats (date, number, currency), RTL layouts (Arabic/Hebrew), character sets/encoding, and legal/compliance needs (GDPR consent, cookie banners, data residency). Pseudo-localization catches layout bugs before real translations arrive.

## Worked reference

````text
LOCALIZATION (l10n), i18n & COMPLIANCE

INTERNATIONALIZATION (i18n) — build for any locale
  externalize strings · locale-aware date/number/currency ·
  Unicode/UTF-8 everywhere · no concatenated sentences

LOCALIZATION (l10n) TESTING — verify each locale
  ✓ translations present, correct, no truncation/overflow
  ✓ date/number/currency/timezone formats per locale
  ✓ RTL layouts mirror correctly (Arabic, Hebrew)
  ✓ character sets, sorting/collation, pluralization
  ✓ no hardcoded / untranslated strings
  TRICK: pseudo-localization ([!!! Ëxpänded !!!]) surfaces
         layout + hardcoded-string bugs before translators finish

COMPLIANCE / REGULATORY (often non-functional gates)
  GDPR/CCPA consent + data rights · cookie banners ·
  accessibility law (see a11y) · data residency ·
  industry: HIPAA (health), PCI-DSS (payments), SOC 2
````

## Exercise

Pseudo-localize the UI (expand strings + accents), then check for truncation, hardcoded strings, and broken RTL mirroring on one screen.
