package org.venth.startup.spock.simple;

import java.util.List;

/**
 * @author Artur Krysiak
 *
 */
public class Exercise {
    List<Check> conditions;

    public void changePhase() {
        ComplainingTrainer.checkPreparation(conditions);
    }
}
