package org.venth.startup.simple;

/**
 * @author Artur Krysiak (last modified by $Author$).
 * @version $Revision$ $Date$
 */
public class LackOfPhoneException extends RuntimeException {

    public LackOfPhoneException() {
        super("I don't have any phone. Please give me one");

    }
}
