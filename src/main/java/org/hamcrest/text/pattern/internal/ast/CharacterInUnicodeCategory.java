package org.hamcrest.text.pattern.internal.ast;

import org.hamcrest.text.pattern.PatternComponent;
import org.hamcrest.text.pattern.internal.naming.GroupNamespace;

public class CharacterInUnicodeCategory implements PatternComponent {
    private final String categoryName;

    public CharacterInUnicodeCategory(final String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public void buildRegex(final StringBuilder builder, final GroupNamespace groups) {
        builder.append("\\p{Is").append(categoryName).append("}");
    }
}
