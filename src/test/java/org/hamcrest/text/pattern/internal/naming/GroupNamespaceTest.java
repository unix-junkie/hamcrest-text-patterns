package org.hamcrest.text.pattern.internal.naming;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class GroupNamespaceTest {
    GroupNamespace environment = new GroupNamespace();

    @Test
    public void testBindsIndicesToNamesAllocatingIndicesFromOne() {
        environment.create("a");
        environment.create("b");

        assertEquals(1, environment.resolve("a"));
        assertEquals(2, environment.resolve("b"));
    }

    @Test
    public void testThrowsIllegalArgumentExceptionIfNameNotBound() {
        try {
            environment.resolve("not bound");
            fail("expected IllegalArgumentException");
        } catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testThrowsIllegalArgumentExceptionIfDuplicateNameBound() {
        environment.create("a");
        try {
            environment.create("a");
            fail("expected IllegalArgumentException");
        } catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLooksForNameInParentEnvironmentIfNotFoundLocally() {
        environment.create("a");
        final GroupNamespace b = environment.create("b");

        assertEquals(environment.resolve("a"), b.resolve("a"));
    }

    @Test
    public void testAllocatesIndicesForAndResolvesPathsWithinChildEnvironments() {
        final GroupNamespace a = environment.create("a");
        a.create("x");
        a.create("y");
        final GroupNamespace b = environment.create("b");
        b.create("x");
        b.create("y");

        assertEquals(2, environment.resolve(new Path("a", "x")));
        assertEquals(3, environment.resolve(new Path("a", "y")));
        assertEquals(5, environment.resolve(new Path("b", "x")));
        assertEquals(6, environment.resolve(new Path("b", "y")));

        assertEquals(environment.resolve(new Path("b", "y")), a.resolve(new Path("b", "y")));
    }

    @Test
    public void testThrowsIllegalArgumentExceptionIfMiddleOfPathNotBound() {
        environment.create("a");

        try {
            environment.resolve(new Path("a", "b", "c"));
            fail("should have thrown IllegalArgumentException");
        } catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
            // expected
        }
    }
}
