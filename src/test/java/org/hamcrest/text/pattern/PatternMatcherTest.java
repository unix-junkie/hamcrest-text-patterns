package org.hamcrest.text.pattern;

import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.text.pattern.PatternMatcher.matchesPattern;
import static org.hamcrest.text.pattern.Patterns.anyCharacter;
import static org.hamcrest.text.pattern.Patterns.anyCharacterIn;
import static org.hamcrest.text.pattern.Patterns.anyCharacterInCategory;
import static org.hamcrest.text.pattern.Patterns.anyCharacterNotIn;
import static org.hamcrest.text.pattern.Patterns.anyCharacterNotInCategory;
import static org.hamcrest.text.pattern.Patterns.capture;
import static org.hamcrest.text.pattern.Patterns.caseInsensitive;
import static org.hamcrest.text.pattern.Patterns.caseSensitive;
import static org.hamcrest.text.pattern.Patterns.either;
import static org.hamcrest.text.pattern.Patterns.exactly;
import static org.hamcrest.text.pattern.Patterns.from;
import static org.hamcrest.text.pattern.Patterns.listOf;
import static org.hamcrest.text.pattern.Patterns.oneOrMore;
import static org.hamcrest.text.pattern.Patterns.optional;
import static org.hamcrest.text.pattern.Patterns.separatedBy;
import static org.hamcrest.text.pattern.Patterns.sequence;
import static org.hamcrest.text.pattern.Patterns.text;
import static org.hamcrest.text.pattern.Patterns.valueOf;
import static org.hamcrest.text.pattern.Patterns.whitespace;
import static org.hamcrest.text.pattern.Patterns.zeroOrMore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class PatternMatcherTest {
    @Test
    @SuppressWarnings("static-method")
    public void matchesPlainText() {
        final PatternMatcher matcher = new PatternMatcher(text("text"));

        assertThat("text", matchesPattern(matcher));
        assertThat("xxxtextxxx", not(matchesPattern(matcher)));
        assertThat("tex", not(matchesPattern(matcher)));
        assertThat("blah", not(matchesPattern(matcher)));
    }

    @Test
    @SuppressWarnings("static-method")
    public void matchesPlainTextContainingSpecialRegexCharacters() {
        assertThat("*star*", matchesPattern(
                new PatternMatcher(text("*star*"))));

        assertThat("-", matchesPattern(
                new PatternMatcher(text("-"))));
    }

    @Test
    @SuppressWarnings("static-method")
    public void matchesSequenceOfText() {
        final PatternMatcher matcher = new PatternMatcher(sequence("hello", " ", "world"));

        assertThat("hello world", matchesPattern(matcher));
    }

    @Test
    @SuppressWarnings("static-method")
    public void matchesAlternatives() {
        final PatternMatcher matcher = new PatternMatcher(either(text("hello"), text("world")));

        assertThat("hello", matchesPattern(matcher));
        assertThat("world", matchesPattern(matcher));
        assertThat("hello world", not(matchesPattern(matcher)));
    }

    @Test
    @SuppressWarnings("static-method")
    public void matchesOptionalPattern() {
        final PatternMatcher matcher = new PatternMatcher(sequence(text("hello"), optional(text(" world"))));

        assertThat("hello", matchesPattern(matcher));
        assertThat("hello world", matchesPattern(matcher));
        assertThat(" world", not(matchesPattern(matcher)));
    }

    @Test
    @SuppressWarnings("static-method")
    public void matchesRepetitionZeroOrMoreTimes() {
        final PatternMatcher matcher = new PatternMatcher(zeroOrMore(text("x")));

        assertThat("", matchesPattern(matcher));
        assertThat("x", matchesPattern(matcher));
        assertThat("xxx", matchesPattern(matcher));
        assertThat(" xx", not(matchesPattern(matcher)));
        assertThat("x x", not(matchesPattern(matcher)));
        assertThat("xx ", not(matchesPattern(matcher)));
    }

    @Test
    @SuppressWarnings("static-method")
    public void matchesRepetitionOneOrMoreTimes() {
        final PatternMatcher matcher = new PatternMatcher(oneOrMore(text("x")));

        assertThat("", not(matchesPattern(matcher)));
        assertThat("x", matchesPattern(matcher));
        assertThat("xxx", matchesPattern(matcher));
        assertThat(" xx", not(matchesPattern(matcher)));
        assertThat("x x", not(matchesPattern(matcher)));
        assertThat("xx ", not(matchesPattern(matcher)));
    }

    @Test
    @SuppressWarnings("static-method")
    public void testCanMatchAnyCharacter() {
        final PatternMatcher matcher = new PatternMatcher(sequence(text("x"), anyCharacter(), text("y")));

        assertThat("x.y", matchesPattern(matcher));
        assertThat("xzy", matchesPattern(matcher));
        assertThat("xy", not(matchesPattern(matcher)));
    }

    @Test
    @SuppressWarnings("static-method")
    public void testCapturesMatchedGroups() throws Exception {
        final PatternMatcher matcher = new PatternMatcher(sequence(capture("xs", oneOrMore(text("x"))), capture("ys", oneOrMore(text("y")))));

        Parse parse;

        parse = matcher.parse("xxxyyy");
        assertEquals("xxx", parse.get("xs"));
        assertEquals("yyy", parse.get("ys"));

        parse = matcher.parse("xxyyyyyy");
        assertEquals("xx", parse.get("xs"));
        assertEquals("yyyyyy", parse.get("ys"));
    }

    @Test
    @SuppressWarnings("static-method")
    public void testFailsIfDoesNotMatchParseInput() {
        final PatternMatcher matcher = new PatternMatcher(text("expected input"));

        try {
            matcher.parse("input that doesn't match");
            fail("should have thrown ParseException");
        } catch (@SuppressWarnings("unused") final PatternMatchException ex) {
            // expected
        }
    }

    @Test
    @SuppressWarnings("static-method")
    public void testCanReferToContentOfMatchedGroups() {
        final PatternMatcher matcher = new PatternMatcher(sequence(capture("first", oneOrMore(text("x"))), text("-"), valueOf("first")));

        assertThat("x-x", matchesPattern(matcher));
        assertThat("xx-xx", matchesPattern(matcher));

        assertThat("x-xx", not(matchesPattern(matcher)));
        assertThat("xx-x", not(matchesPattern(matcher)));
    }

    PatternMatcher scopedMatcher = new PatternMatcher(sequence(capture("xs", oneOrMore(text("x"))), capture("inside", sequence(capture("xs", oneOrMore(text("X"))), valueOf("xs"))), valueOf("xs")));

    @Test
    public void testReferencesToGroupsAreLexicallyScoped() {
        assertThat("xxXXXXxx", matchesPattern(scopedMatcher));
        assertThat("xXXx", matchesPattern(scopedMatcher));
        assertThat("xXxx", not(matchesPattern(scopedMatcher)));
        assertThat("xXXX", not(matchesPattern(scopedMatcher)));
    }

    @Test
    public void testNamesInInnerScopesCanBeQueriedUsingDottedPathNotation() throws Exception {
        final Parse parse = scopedMatcher.parse("xxXXXXXXxx");
        assertEquals("xx", parse.get("xs"));
        assertEquals("XXX", parse.get("inside.xs"));
    }

    @Test
    @SuppressWarnings("static-method")
    public void testCanReferToValuesOfGroupsInInnerScopesUsingDottedPathNotation() {
        final PatternMatcher matcher = new PatternMatcher(sequence(capture("xs", oneOrMore(text("x"))), capture("inside", sequence(capture("xs", oneOrMore(text("X"))), valueOf("xs"))), valueOf("xs"), valueOf("inside.xs")));

        assertThat("xXXXXxXX", matchesPattern(matcher));
        assertThat("xxXXxxX", matchesPattern(matcher));
    }

    @Test
    @SuppressWarnings("static-method")
    public void testCanDefinePatternsInTermsOfExistingPatterns() {
        final PatternMatcher emailAddressMatcher = new PatternMatcher(sequence(capture("user", oneOrMore(anyCharacter())), "@", capture("host", oneOrMore(anyCharacter()))));

        final PatternMatcher mailToURLMatcher = new PatternMatcher(sequence(capture("scheme", text("mailto")), ":", capture("email", emailAddressMatcher)));

        assertThat("mailto:npryce@users.sf.net", matchesPattern(mailToURLMatcher));
    }

    @Test
    @SuppressWarnings("static-method")
    public void matchesCharacterInList() {
        final PatternMatcher matcher = new PatternMatcher(anyCharacterIn("0123456789"));

        for (int i = 0; i < 9; i++) {
            final String input = String.valueOf(i);

            assertThat(input, matchesPattern(matcher));
        }
        assertThat("X", not(matchesPattern(matcher)));
    }

    @Test
    @SuppressWarnings("static-method")
    public void matchesCharacterRange() {
        final PatternMatcher matcher = new PatternMatcher(anyCharacterIn("0-9"));

        for (int i = 0; i < 9; i++) {
            final String input = String.valueOf(i);

            assertThat(input, matchesPattern(matcher));
        }
        assertThat("X", not(matchesPattern(matcher)));
    }

    @Test
    @SuppressWarnings("static-method")
    public void matchesCharacterNotInRange() {
        final PatternMatcher matcher = new PatternMatcher(anyCharacterNotIn("0-9"));

        for (int i = 0; i < 9; i++) {
            final String input = String.valueOf(i);

            assertThat(input, not(matchesPattern(matcher)));
        }
        assertThat("X", matchesPattern(matcher));
    }

    @Test
    @SuppressWarnings("static-method")
    public void matchesCharactersInUnicodeCategories() {
        final PatternMatcher matcher = new PatternMatcher(anyCharacterInCategory("Lu"));

        assertThat("A", matchesPattern(matcher));
        assertThat("a", not(matchesPattern(matcher)));
        assertThat("B", matchesPattern(matcher));
        assertThat("b", not(matchesPattern(matcher)));
    }

    @Test
    @SuppressWarnings("static-method")
    public void matchesCharactersNotInUnicodeCategories() {
        final PatternMatcher matcher = new PatternMatcher(anyCharacterNotInCategory("Lu"));

        assertThat("A", not(matchesPattern(matcher)));
        assertThat("a", matchesPattern(matcher));
        assertThat("B", not(matchesPattern(matcher)));
        assertThat("b", matchesPattern(matcher));
    }

    @Test
    @SuppressWarnings("static-method")
    public void matchesExactlyTheSpecifiedNumberOfRepetitions() {
        final PatternMatcher matcher = new PatternMatcher(exactly(3, "x"));

        assertThat("xx", not(matchesPattern(matcher)));
        assertThat("xxx", matchesPattern(matcher));
        assertThat("xxxx", not(matchesPattern(matcher)));
    }

    @Test
    @SuppressWarnings("static-method")
    public void matchesARangeOfAllowableRepetitions() {
        final PatternMatcher matcher = new PatternMatcher(from(3, 5, "x"));

        assertThat("xx", not(matchesPattern(matcher)));
        assertThat("xxx", matchesPattern(matcher));
        assertThat("xxxx", matchesPattern(matcher));
        assertThat("xxxxx", matchesPattern(matcher));
        assertThat("xxxxxx", not(matchesPattern(matcher)));
    }

    @Test
    @SuppressWarnings("static-method")
    public void matchesAListOfMatchedThings() {
        final PatternMatcher matcher = new PatternMatcher(listOf("x"));

        assertThat("", matchesPattern(matcher));
        assertThat("x", matchesPattern(matcher));
        assertThat("x,x", matchesPattern(matcher));
        assertThat("x,x,x,x,x", matchesPattern(matcher));

        assertThat(",", not(matchesPattern(matcher)));
        assertThat("x,x,x,", not(matchesPattern(matcher)));
    }

    @Test
    @SuppressWarnings("static-method")
    public void matchesAListWithSpecificSeparator() {
        final PatternMatcher matcher = new PatternMatcher(listOf("x").separatedBy(":"));

        assertThat("", matchesPattern(matcher));
        assertThat("x", matchesPattern(matcher));
        assertThat("x:x", matchesPattern(matcher));
        assertThat("x:x:x:x:x", matchesPattern(matcher));

        assertThat("x,x,x", not(matchesPattern(matcher)));
    }

    @Test
    @SuppressWarnings("static-method")
    public void matchesWhitespace() {
        final PatternMatcher matcher = new PatternMatcher(sequence("a", whitespace(), "z"));

        assertThat("az", matchesPattern(matcher));
        assertThat("a z", matchesPattern(matcher));
        assertThat("a \t z", matchesPattern(matcher));
    }

    @Test
    @SuppressWarnings("static-method")
    public void matchesSequenceSeparatedByPattern() {
        final PatternMatcher matcher = new PatternMatcher(
                separatedBy(",", "a", "b", "c"));

        assertThat("a,b,c", matchesPattern(matcher));
    }

    @Test
    @SuppressWarnings("static-method")
    public void canControlCaseSensitivity() {
        final PatternMatcher matcher = new PatternMatcher(
            sequence("a",caseInsensitive(sequence("b",caseSensitive("c"))))
        );

        assertThat("abc", matchesPattern(matcher));
        assertThat("aBc", matchesPattern(matcher));
        assertThat("aBC", not(matchesPattern(matcher)));
    }
}
