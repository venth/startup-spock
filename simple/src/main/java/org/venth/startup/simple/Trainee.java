package org.venth.startup.simple;

/**
 * @author Artur Krysiak (last modified by $Author$).
 * @version $Revision$ $Date$
 */
public class Trainee {
    private Phone phone;

    public void notify(ExercisePhase exercisePhase) {
        if (phone == null) {
            throw new RefuseException("I have no phone. Please give me one");
        }

        if (!phone.isEnabled()) {
            throw new RefuseException("My phone is switched off. Please switch the phone on");
        }
        phone.sayTo("I started " + exercisePhase.name());

        if (ExercisePhase.STOPPED == exercisePhase) {
            phone.makePhoneEnabled(false);
        }
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public String getUsedPhoneModel() {
        if (phone == null) {
            throw new LackOfPhoneException();
        }
        return phone.getModel();
    }
}
