package org.hamcrest.text.pattern.internal.ast;

import java.util.regex.Pattern;

import org.hamcrest.text.pattern.PatternComponent;
import org.hamcrest.text.pattern.internal.naming.GroupNamespace;

public class Literal implements PatternComponent {
    private final String literal;

    public Literal(final String literal) {
        this.literal = literal;
    }

    @Override
    public void buildRegex(final StringBuilder builder, final GroupNamespace groups) {
        builder.append(Pattern.quote(literal));
    }
}
