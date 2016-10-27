package org.hamcrest.text.pattern;

import org.hamcrest.text.pattern.internal.ast.CaptureGroup;
import org.hamcrest.text.pattern.internal.ast.Literal;
import org.hamcrest.text.pattern.internal.naming.GroupNamespace;

public class Field implements PatternComponent {
    private final String name;
    private final PatternComponent sequence;

    public Field(final String name, final PatternComponent pattern) {
        this.name = name;
        // TODO: clean this up and make the delimiter changeable
        final Object[] args = new Object[] { "\"", new CaptureGroup(name, pattern), new Literal("\"") };
        sequence = Patterns.sequence(args);
    }

    @Override
    public void buildRegex(final StringBuilder builder, final GroupNamespace groups) {
        sequence.buildRegex(builder, groups);
    }

    public String getName() {
        return name;
    }

}
