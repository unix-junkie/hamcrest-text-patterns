package org.hamcrest.text.pattern.internal.ast;

import org.hamcrest.text.pattern.PatternComponent;
import org.hamcrest.text.pattern.internal.naming.GroupNamespace;

public class Sequence implements PatternComponent {
    private final PatternComponent[] elements;

    public Sequence(final PatternComponent[] alternatives) {
        elements = alternatives.clone();
    }

    @Override
    public void buildRegex(final StringBuilder builder, final GroupNamespace groups) {
        for (final PatternComponent element : elements) {
            element.buildRegex(builder, groups);
        }
    }
}
