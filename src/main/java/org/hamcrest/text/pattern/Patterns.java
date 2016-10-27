package org.hamcrest.text.pattern;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.text.pattern.internal.ast.AnyCharacter;
import org.hamcrest.text.pattern.internal.ast.CaptureGroup;
import org.hamcrest.text.pattern.internal.ast.CharacterInRange;
import org.hamcrest.text.pattern.internal.ast.CharacterInUnicodeCategory;
import org.hamcrest.text.pattern.internal.ast.CharacterNotInRange;
import org.hamcrest.text.pattern.internal.ast.CharacterNotInUnicodeCategory;
import org.hamcrest.text.pattern.internal.ast.Choice;
import org.hamcrest.text.pattern.internal.ast.Exactly;
import org.hamcrest.text.pattern.internal.ast.Flags;
import org.hamcrest.text.pattern.internal.ast.GroupReference;
import org.hamcrest.text.pattern.internal.ast.ListOf;
import org.hamcrest.text.pattern.internal.ast.Literal;
import org.hamcrest.text.pattern.internal.ast.OneOrMore;
import org.hamcrest.text.pattern.internal.ast.Optional;
import org.hamcrest.text.pattern.internal.ast.Sequence;
import org.hamcrest.text.pattern.internal.ast.ZeroOrMore;

public abstract class Patterns {
    public static PatternComponent text(final String text) {
        return new Literal(text);
    }

    public static PatternComponent anyCharacter() {
        return AnyCharacter.INSTANCE;
    }

    public static PatternComponent anyCharacterIn(final String range) {
        return new CharacterInRange(range);
    }

    public static PatternComponent anyCharacterNotIn(final String range) {
        return new CharacterNotInRange(range);
    }

    public static PatternComponent anyCharacterInCategory(final String category) {
        return new CharacterInUnicodeCategory(category);
    }

    public static PatternComponent anyCharacterNotInCategory(final String category) {
        return new CharacterNotInUnicodeCategory(category);
    }

    public static PatternComponent either(final Object... alternatives) {
        return new Choice(toPatterns(alternatives));
    }

    public static PatternComponent sequence(final Object... elements) {
        return new Sequence(toPatterns(elements));
    }

    public static PatternComponent optional(final Object o) {
        return new Optional(toPattern(o));
    }

    public static PatternComponent zeroOrMore(final Object o) {
        return new ZeroOrMore(toPattern(o));
    }

    public static PatternComponent oneOrMore(final Object o) {
        return new OneOrMore(toPattern(o));
    }

    public static PatternComponent capture(final String name, final PatternComponent pattern) {
        return new CaptureGroup(name, pattern);
    }

    public static PatternComponent valueOf(final String name) {
        return new GroupReference(name);
    }

    public static PatternComponent exactly(final int requiredCount, final Object o) {
        return new Exactly(requiredCount, toPattern(o));
    }

    public static PatternComponent from(final int minimumCount, final int maximumCount, final Object o) {
        return new FromTo(minimumCount, maximumCount, toPattern(o));
    }

    public static SeparablePatternComponent listOf(final Object element) {
        return new ListOf(toPattern(element), toPattern(","));
    }

    public static PatternComponent whitespace() {
        return zeroOrMore(anyCharacterInCategory("Space"));
    }

    public static PatternComponent separatedBy(final Object separator, final Object... elements) {
        final List<Object> separated = new ArrayList<>(elements.length*2 - 1);

        separated.add(elements[0]);
        for (int i = 1; i < elements.length; i++) {
            separated.add(separator);
            separated.add(elements[i]);
        }

        return sequence(separated.toArray());
    }

    public static PatternComponent caseInsensitive(final Object o) {
        return new Flags("i", toPattern(o));
    }

    public static PatternComponent caseSensitive(final Object o) {
        return new Flags("-i", toPattern(o));
    }

    public static PatternComponent[] toPatterns(final Object... args) {
        final PatternComponent[] patterns = new PatternComponent[args.length];
        for (int i = 0; i < patterns.length; i++) {
            patterns[i] = toPattern(args[i]);
        }
        return patterns;
    }

    public static PatternComponent toPattern(final Object object) {
        if (object instanceof PatternComponent) {
            return (PatternComponent) object;
        }
        return text(object.toString());
    }
}
