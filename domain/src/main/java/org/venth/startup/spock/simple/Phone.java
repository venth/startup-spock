package org.venth.startup.spock.simple;

/**
 * @author Artur Krysiak (last modified by $Author$).
 * @version $Revision$ $Date$
 */
public class Phone {
    private String model;
    private boolean enabled;

    public void sayTo(String comment) {

    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void makePhoneEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
