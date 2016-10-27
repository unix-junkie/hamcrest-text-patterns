package org.hamcrest.text.pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.text.pattern.internal.naming.GroupNamespace;

public class PatternMatcher extends TypeSafeMatcher<String> implements PatternComponent {
    private final GroupNamespace groups = new GroupNamespace();
    private final PatternComponent root;
    private final Pattern pattern;

    public PatternMatcher(final PatternComponent root) {
        this.root = root;
        pattern = compile(root, groups);
    }

    @Factory
    public static PatternMatcher matchesPattern(final PatternComponent pattern) {
        return new PatternMatcher(pattern);
    }

    @Factory
    public static PatternMatcher matchesPattern(final PatternMatcher pattern) {
        return pattern;
    }

    @Override
    public String toString() {
        return pattern.toString();
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("a string matching ");
        description.appendValue(toString());
    }

    @Override
    public boolean matchesSafely(final String s) {
        return pattern.matcher(s).matches();
    }

    public Parse parse(final String input) throws PatternMatchException {
        final Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            return new Parse(groups, matcher);
        }
        throw new PatternMatchException("did not match input: " + input);
    }

    @Override
    public void buildRegex(final StringBuilder builder, final GroupNamespace groups) {
        root.buildRegex(builder, groups);
    }

    private static Pattern compile(final PatternComponent root, final GroupNamespace groups) {
        final StringBuilder builder = new StringBuilder();
        root.buildRegex(builder, groups);
        return Pattern.compile(builder.toString());
    }
}
