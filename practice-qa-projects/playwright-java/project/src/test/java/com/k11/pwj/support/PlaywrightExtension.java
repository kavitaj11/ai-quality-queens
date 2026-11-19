package com.k11.pwj.support;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

/**
 * JUnit 5 extension: gives every test method its own ShopSession as a parameter and closes it
 * afterwards, keeping the trace and screenshot only when the test failed.
 *
 *   @ExtendWith(PlaywrightExtension.class)
 *   class MyTest { @Test void works(ShopSession session) { ... } }
 */
public class PlaywrightExtension implements ParameterResolver, AfterEachCallback {

    private static final ExtensionContext.Namespace NS = ExtensionContext.Namespace.create(PlaywrightExtension.class);

    @Override
    public boolean supportsParameter(ParameterContext parameter, ExtensionContext context) {
        return parameter.getParameter().getType() == ShopSession.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameter, ExtensionContext context) {
        return context.getStore(NS).getOrComputeIfAbsent("session",
                k -> new ShopSession(context.getRequiredTestClass().getSimpleName() + "-" + context.getDisplayName()),
                ShopSession.class);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        ShopSession session = context.getStore(NS).remove("session", ShopSession.class);
        if (session != null) {
            session.close(context.getExecutionException().isPresent());
        }
    }
}
