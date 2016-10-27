package org.hamcrest.text.pattern;

import java.util.regex.MatchResult;

import org.hamcrest.text.pattern.internal.naming.GroupNamespace;

public class Parse {
    private final GroupNamespace groups;
    private final MatchResult result;

    public Parse(final GroupNamespace groups, final MatchResult result) {
        this.groups = groups;
        this.result = result;
    }

    public String get(final String name) {
        final int groupIndex = groups.resolve(name);
        return result.group(groupIndex);
    }
}
