package org.hamcrest.text.pattern.internal.ast;

import org.hamcrest.text.pattern.PatternComponent;
import org.hamcrest.text.pattern.internal.naming.GroupNamespace;

public class Exactly implements PatternComponent {
    private final int requiredNumber;

    private final PatternComponent repeatedPattern;

    public Exactly(final int requiredNumber, final PatternComponent repeatedPattern) {
        this.requiredNumber = requiredNumber;
        this.repeatedPattern = repeatedPattern;
    }

    @Override
    public void buildRegex(final StringBuilder builder, final GroupNamespace groups) {
        repeatedPattern.buildRegex(builder, groups);
        builder.append("{");
        builder.append(requiredNumber);
        builder.append("}");

    }
}
