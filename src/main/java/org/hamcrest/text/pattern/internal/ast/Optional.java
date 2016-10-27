package org.hamcrest.text.pattern.internal.ast;

import org.hamcrest.text.pattern.PatternComponent;

public class Optional extends PatternModifier {
    public Optional(final PatternComponent pattern) {
        super(pattern);
    }

    @Override
    protected void appendModifier(final StringBuilder builder) {
        builder.append("?");
    }
}
