package org.hamcrest.text.pattern.internal.ast;

import org.hamcrest.text.pattern.PatternComponent;
import org.hamcrest.text.pattern.internal.naming.GroupNamespace;

public class CharacterNotInRange implements PatternComponent {
    private final String range;

    public CharacterNotInRange(final String range) {
        this.range = range;
    }

    @Override
    public void buildRegex(final StringBuilder builder, final GroupNamespace groups) {
        builder.append("[^").append(range).append("]");
    }
}
