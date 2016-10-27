/*  Copyright (c) 2000-2006 hamcrest.org
 */
package org.hamcrest;

import junit.framework.TestCase;

public abstract class AbstractMatcherTest extends TestCase {

    /**
     * Create an instance of the Matcher so some generic safety-net tests can be
     * run on it.
     */
    protected abstract Matcher<?> createMatcher();

    protected static final Object ARGUMENT_IGNORED = new Object();
    protected static final Object ANY_NON_NULL_ARGUMENT = new Object();

    public static <T> void assertMatches(final String message, final Matcher<T> c, final T arg) {
        assertTrue(message, c.matches(arg));
    }

    public static <T> void assertDoesNotMatch(final String message, final Matcher<T> c, final T arg) {
        assertFalse(message, c.matches(arg));
    }

    public static void assertDescription(final String expected, final Matcher<?> matcher) {
        final Description description = new StringDescription();
        description.appendDescriptionOf(matcher);
        assertEquals("Expected description", expected, description.toString());
    }

    public void testIsNullSafe() {
        // should not throw a NullPointerException
        createMatcher().matches(null);
    }

    public void testCopesWithUnknownTypes() {
        // should not throw ClassCastException
        createMatcher().matches(new UnknownType());
    }

    public static class UnknownType {
    }

}
