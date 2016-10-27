package org.hamcrest.text.pattern.internal.naming;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class PathTest {
    @Test
    @SuppressWarnings("static-method")
    public void testCanBeComparedForEquality() {
        assertTrue(new Path("a", "b").equals(new Path("a", "b")));
        assertFalse(new Path("a", "b").equals(new Path("a")));
        assertFalse(new Path("a", "b").equals(new Path("a", "b", "c")));
        assertFalse(new Path("a", "b").equals(new Path("x", "y")));
        assertFalse(new Path("a", "b").equals(null));

        assertTrue(new Path().equals(new Path()));

        assertTrue(new Path("a", "b").equals(new Path("x", "a", "b").tail()));
    }

    @Test
    @SuppressWarnings("static-method")
    public void testReturnsLengthAndComponents() {
        final Path abc = new Path("a", "b", "c");
        assertEquals(3, abc.size());
        assertEquals("a", abc.component(0));
        assertEquals("b", abc.component(1));
        assertEquals("c", abc.component(2));

        final Path xy = new Path("x", "y");
        assertEquals(2, xy.size());
        assertEquals("x", xy.component(0));
        assertEquals("y", xy.component(1));

        assertEquals(0, new Path().size());
    }

    @Test
    @SuppressWarnings("static-method")
    public void testReturnsHead() {
        assertEquals("a", new Path("a", "b", "c", "d").head());
        assertEquals("x", new Path("x", "y", "z").head());
    }

    @Test
    @SuppressWarnings("static-method")
    public void testReturnsTail() {
        assertEquals(new Path("b", "c", "d"), new Path("a", "b", "c", "d").tail());
        assertEquals(new Path("y", "z"), new Path("x", "y", "z").tail());
        assertEquals(new Path(), new Path("a").tail());
    }

    @Test
    @SuppressWarnings("static-method")
    public void testParsesPathFromDottedNotation() {
        assertEquals(new Path("a"), Path.parse("a"));
        assertEquals(new Path("a", "b", "c"), Path.parse("a.b.c"));
    }

    @Test
    @SuppressWarnings("static-method")
    public void testReturnsDottedPathNotationFromToString() {
        assertEquals("a", new Path("a").toString());
        assertEquals("a.b.c", new Path("a", "b", "c").toString());
    }
}
