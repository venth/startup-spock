package org.venth.startup.spock.simple

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
        def answer = trainer.giveMeHintFor(ChiTrainer.KNOWN_PROBLEM);

        then:
        ChiTrainer.PROBLEM_ANSWER == answer
    }

    def "trainer provides default answer for all unknown problems"() {
        def unknownProblem = "Unknown problem"

        when:
        def answer = trainer.giveMeHintFor(unknownProblem);

        then: "default answer is expected"
        ChiTrainer.DEFAULT_ANSWER == answer
    }
}
