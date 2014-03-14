package org.venth.startup.spock.simple;

/**
 * @author Artur Krysiak
 *
 */
public class LackOfPhoneException extends RuntimeException {

    public LackOfPhoneException() {
        super("I don't have any phone. Please give me one");

    }
}
