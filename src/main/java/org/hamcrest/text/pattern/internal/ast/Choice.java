package org.hamcrest.text.pattern.internal.ast;

import org.hamcrest.text.pattern.PatternComponent;
import org.hamcrest.text.pattern.internal.naming.GroupNamespace;

public class Choice implements PatternComponent {
    private final PatternComponent[] alternatives;

    public Choice(final PatternComponent[] alternatives) {
        this.alternatives = alternatives.clone();
    }

    @Override
    public void buildRegex(final StringBuilder builder, final GroupNamespace groups) {
        builder.append("(?:");
        boolean needsSeparator = false;
        for (final PatternComponent alternative : alternatives) {
            if (needsSeparator) {
                builder.append("|");
            } else {
                needsSeparator = true;
            }

            alternative.buildRegex(builder, groups);
        }
        builder.append(")");
    }
}
