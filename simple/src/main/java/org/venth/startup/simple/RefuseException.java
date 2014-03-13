package org.venth.startup.simple;

/**
 * @author Artur Krysiak (last modified by $Author$).
 * @version $Revision$ $Date$
 */
public class RefuseException extends RuntimeException {
    public RefuseException(String comment) {
        super(comment);
    }
}
