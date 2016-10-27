package org.hamcrest.text.pattern.internal.ast;

import org.hamcrest.text.pattern.PatternComponent;
import org.hamcrest.text.pattern.internal.naming.GroupNamespace;

public abstract class PatternModifier implements PatternComponent {
    private final PatternComponent pattern;

    public PatternModifier(final PatternComponent pattern) {
        this.pattern = pattern;
    }

    @Override
    public void buildRegex(final StringBuilder builder, final GroupNamespace groups) {
        builder.append("(?:");
        pattern.buildRegex(builder, groups);
        builder.append(")");
        appendModifier(builder);
    }

    protected abstract void appendModifier(StringBuilder builder);
}
