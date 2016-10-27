package org.hamcrest.text.pattern.internal.naming;

import java.util.HashMap;
import java.util.Map;

public class GroupNamespace {
    private final GroupNamespace parent;
    private final Map<String, GroupNamespace> bindings = new HashMap<>();
    private final IndexSequence nextGroupIndex;
    private final int groupIndex;

    private GroupNamespace(final GroupNamespace parent, final IndexSequence nextGroupIndex) {
        this.parent = parent;
        this.nextGroupIndex = nextGroupIndex;
        groupIndex = nextGroupIndex.next();
    }

    public GroupNamespace() {
        this(null, new IndexSequence());
    }

    public int toIndex() {
        return groupIndex;
    }

    public GroupNamespace create(final String name) {
        if (bindings.containsKey(name)) {
            throw new IllegalArgumentException("duplicate name '" + name + "'");
        }

        final GroupNamespace child = new GroupNamespace(this, nextGroupIndex);
        bindings.put(name, child);
        return child;
    }

    public int resolve(final String pathAsString) {
        return resolve(Path.parse(pathAsString));
    }

    public int resolve(final Path path) {
        return environmentContaining(path.head()).resolveInternally(path.tail());
    }

    public int resolveInternally(final Path path) {
        if (path.size() == 0) {
            return groupIndex;
        } else if (bindings.containsKey(path.head())) {
            return bindings.get(path.head()).resolveInternally(path.tail());
        } else {
            throw new IllegalArgumentException("name '" + path.head() + "' not bound");
        }
    }

    private GroupNamespace environmentContaining(final String name) {
        if (bindings.containsKey(name)) {
            return bindings.get(name);
        } else if (parent != null) {
            return parent.environmentContaining(name);
        } else {
            throw new IllegalArgumentException("name '" + name + "' not bound");
        }
    }
}
