package org.hamcrest.text.pattern;

import org.hamcrest.text.pattern.internal.naming.GroupNamespace;

public class FromTo implements PatternComponent {
    private final int minimumNumber;
    private final int maximumNumber;
    private final PatternComponent repeatedPattern;

    public FromTo(final int minimumNumber, final int maximumNumber, final PatternComponent repeatedPattern) {
        this.minimumNumber = minimumNumber;
        this.maximumNumber = maximumNumber;
        this.repeatedPattern = repeatedPattern;
    }

    @Override
    public void buildRegex(final StringBuilder builder, final GroupNamespace groups) {
        repeatedPattern.buildRegex(builder, groups);
        builder.append("{");
        builder.append(minimumNumber);
        builder.append(",");
        builder.append(maximumNumber);
        builder.append("}");

    }
}
