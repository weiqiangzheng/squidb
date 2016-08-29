/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache 2.0 License.
 * See the accompanying LICENSE file for terms.
 */
package com.yahoo.squidb.sql;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Default implementation of {@link ArgumentResolver} that unwraps AtomicReferences, AtomicBooleans, and ThreadLocals
 */
public class DefaultArgumentResolver implements ArgumentResolver {

    @Override
    public Object resolveArgument(Object arg) {
        boolean resolved = false;
        while (!resolved) {
            if (arg instanceof AtomicReference) {
                arg = ((AtomicReference<?>) arg).get();
            } else if (arg instanceof AtomicBoolean) { // Not a subclass of Number so we need to unwrap it
                arg = ((AtomicBoolean) arg).get() ? 1 : 0;
                resolved = true;
            } else if (arg instanceof ThreadLocal) {
                arg = ((ThreadLocal<?>) arg).get();
            } else {
                resolved = true;
            }
        }
        return arg;
    }
}
