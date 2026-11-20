# Visual comparisons

[07 · Module topics](../../modules/07-playwright.md) · [Curriculum index](../../README.md)

**Category** · SCALE & STRUCTURE

toHaveScreenshot() with maxDiffPixelRatio gives you baseline-diff visual testing out of the box. Know when it helps (design systems, canvases) and when it's noise.

## Worked reference

````text
# pytest-playwright + pixelmatch-style compare
expect(page.get_by_test_id("pricing-card"))\
    .to_have_screenshot("pricing-card.png",
        max_diff_pixel_ratio=0.01,
        mask=[page.get_by_test_id("live-price")])

# Update baselines:  pytest --update-snapshots
````

## Exercise

Add a visual assertion for a stable component; break it intentionally and read the diff artifact.
