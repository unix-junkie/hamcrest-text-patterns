package org.hamcrest.text.pattern.internal.ast;

import org.hamcrest.text.pattern.PatternComponent;
import org.hamcrest.text.pattern.internal.naming.GroupNamespace;

public class AnyCharacter implements PatternComponent {
    public static final AnyCharacter INSTANCE = new AnyCharacter();

    private AnyCharacter() {
    }

    @Override
    public void buildRegex(final StringBuilder builder, final GroupNamespace groups) {
        builder.append(".");
    }
}
