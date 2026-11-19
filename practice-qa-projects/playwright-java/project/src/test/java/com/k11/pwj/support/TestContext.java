package com.k11.pwj.support;

import com.k11.pwj.db.ShopDb;
import com.k11.pwj.models.Order;
import com.k11.pwj.services.ShopApi;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Page;

/**
 * Per-scenario shared state for Cucumber step definitions.
 *
 * cucumber-picocontainer creates ONE TestContext per scenario and injects the same instance
 * into every step-definition class and hook that asks for it in its constructor. That is how
 * LoginSteps, ShoppingSteps, OrderApiSteps and DatabaseSteps share the page, the API clients
 * and values such as the last order, without static fields (which would break parallel runs).
 */
public class TestContext {

    private ShopSession session;
    private ShopDb db;

    // Values passed from one step to the next within a scenario
    public String username;
    public ShopApi userApi;          // the logged-in user's standalone API client
    public Order lastOrder;
    public String lastOrderId;
    public APIResponse lastResponse;

    void start(String scenarioName) {
        session = new ShopSession(scenarioName);
    }

    byte[] finish(boolean failed) {
        return session == null ? null : session.close(failed);
    }

    public ShopSession session() {
        return session;
    }

    public Page page() {
        return session.page();
    }

    /** API calls that share the browser's cookies. */
    public ShopApi ui() {
        return session.ui();
    }

    public ShopDb db() {
        if (db == null) {
            db = new ShopDb();
        }
        return db;
    }
}
