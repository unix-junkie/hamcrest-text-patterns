package org.hamcrest.text.pattern.internal.ast;

import org.hamcrest.text.pattern.PatternComponent;

public class OneOrMore extends PatternModifier {
    public OneOrMore(final PatternComponent pattern) {
        super(pattern);
    }

    @Override
    protected void appendModifier(final StringBuilder builder) {
        builder.append("+");
    }
}
