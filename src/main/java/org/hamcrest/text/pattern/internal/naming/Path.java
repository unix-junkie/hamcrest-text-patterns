package org.hamcrest.text.pattern.internal.naming;

import java.util.Arrays;

public class Path {
    private final String[] components;
    private final int offset;

    public Path(final String... components) {
        this.components = components;
        offset = 0;
    }

    private Path(final String[] components, final int offset) {
        this.components = components;
        this.offset = offset;
    }

    public int size() {
        return components.length - offset;
    }

    public String component(final int i) {
        return components[offset + i];
    }

    public String head() {
        return component(0);
    }

    public Path tail() {
        return new Path(components, offset + 1);
    }

    @Override
    public int hashCode() {
        return offset ^ Arrays.hashCode(components);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final Path other = (Path) obj;

        if (size() != other.size()) {
            return false;
        }

        for (int i = 0; i < size(); i++) {
            if (!component(i).equals(other.component(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        for (final String component : components) {
            if (builder.length() > 0) {
                builder.append(".");
            }
            builder.append(component);
        }

        return builder.toString();
    }

    public static Path parse(final String pathAsString) {
        return new Path(pathAsString.split("\\."));
    }
}
