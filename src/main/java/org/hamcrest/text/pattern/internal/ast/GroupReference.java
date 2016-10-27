package org.hamcrest.text.pattern.internal.ast;

import org.hamcrest.text.pattern.PatternComponent;
import org.hamcrest.text.pattern.internal.naming.GroupNamespace;

public class GroupReference implements PatternComponent {
    private final String name;

    public GroupReference(final String name) {
        this.name = name;
    }

    @Override
    public void buildRegex(final StringBuilder builder, final GroupNamespace groups) {
        builder.append("\\").append(groups.resolve(name));
    }
}
