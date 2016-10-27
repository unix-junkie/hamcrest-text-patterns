package org.hamcrest.text.pattern;

public class PatternMatchException extends Exception {
    private static final long serialVersionUID = 1L;

    public PatternMatchException(final String message) {
        super(message);
    }

    public PatternMatchException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
