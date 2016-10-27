package org.hamcrest.text.pattern.internal.ast;

import org.hamcrest.text.pattern.PatternComponent;
import org.hamcrest.text.pattern.internal.naming.GroupNamespace;

public class CaptureGroup implements PatternComponent {
    private final String name;
    private final PatternComponent pattern;

    public CaptureGroup(final String name, final PatternComponent pattern) {
        this.name = name;
        this.pattern = pattern;
    }

    @Override
    public void buildRegex(final StringBuilder builder, final GroupNamespace groups) {
        final GroupNamespace subgroups = groups.create(name);
        builder.append("(");
        pattern.buildRegex(builder, subgroups);
        builder.append(")");
    }
}
