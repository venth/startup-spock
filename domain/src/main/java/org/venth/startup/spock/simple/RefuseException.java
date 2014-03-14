package org.venth.startup.spock.simple;

/**
 * @author Artur Krysiak
 *
 */
public class RefuseException extends RuntimeException {
    public RefuseException(String comment) {
        super(comment);
    }
}
