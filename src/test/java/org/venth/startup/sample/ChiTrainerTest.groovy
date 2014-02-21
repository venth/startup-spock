package org.venth.startup.sample

import spock.lang.Specification

/**
 * @author Artur Krysiak (last modified by $Author$).
 * @version $Revision$ $Date$
 */
class ChiTrainerTest extends Specification {

    ChiTrainer trainer

    void setup() {
        trainer = new ChiTrainer();
    }

    def "the trainer answer for a Hint is fill your self with chi and defend the problem"() {

        when:
        def answer = trainer.giveMeHintFor("a problem");

        then:
        "fill yourself with chi and defend a problem" == answer
    }
}
