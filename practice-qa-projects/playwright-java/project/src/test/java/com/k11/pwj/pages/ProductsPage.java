package com.k11.pwj.pages;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public class ProductsPage {

    private final Page page;
    private final Locator searchInput;
    private final Locator searchButton;
    private final Locator spinner;
    private final Locator results;
    private final Locator cards;
    private final Locator names;
    private final Locator welcome;

    public ProductsPage(Page page) {
        this.page = page;
        this.searchInput = page.getByTestId("search-input");
        this.searchButton = page.getByTestId("search-button");
        this.spinner = page.getByTestId("spinner");
        this.results = page.getByTestId("results");
        this.cards = page.getByTestId("product-card");
        this.names = page.getByTestId("product-name");
        this.welcome = page.getByTestId("welcome");
    }

    public ProductsPage open() {
        page.navigate("/products");
        return waitUntilLoaded();
    }

    /**
     * Results come from a slow API call. The results container is empty until loading
     * finishes, then holds cards, a "no results" message or an error. The assertions retry,
     * so there is no sleep and no manual polling.
     */
    public ProductsPage waitUntilLoaded() {
        assertThat(spinner).isHidden();
        assertThat(results).not().isEmpty();
        return this;
    }

    public ProductsPage search(String term) {
        searchInput.fill(term);
        searchButton.click();
        // The OLD page already has results, so first wait for the NEW page's URL.
        String encoded = URLEncoder.encode(term, StandardCharsets.UTF_8);
        assertThat(page).hasURL(Pattern.compile("\\?q=" + escapeForJsRegex(encoded) + "$"));
        return waitUntilLoaded();
    }

    /**
     * Playwright for Java evaluates a Pattern with the browser's JavaScript regex engine, so
     * Java-only syntax such as Pattern.quote()'s \Q...\E does not work. Escape characters instead.
     */
    private static String escapeForJsRegex(String text) {
        return text.replaceAll("[.*+?^${}()|\\[\\]\\\\]", "\\\\$0");
    }

    public Locator productNames() {
        return names;
    }

    public Locator cards() {
        return cards;
    }

    public Locator welcome() {
        return welcome;
    }

    public List<String> productNameList() {
        return names.allInnerTexts();
    }

    public ProductPage openProduct(String name) {
        cards.filter(new Locator.FilterOptions().setHasText(name))
                .getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("View"))
                .click();
        assertThat(page).hasURL(Pattern.compile("/products/\\d+$"));
        return new ProductPage(page);
    }
}
