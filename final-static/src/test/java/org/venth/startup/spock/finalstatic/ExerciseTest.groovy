package org.venth.startup.spock.finalstatic

import org.junit.Rule
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.rule.PowerMockRule
import org.venth.startup.spock.simple.Check
import org.venth.startup.spock.simple.ComplainingTrainer
import org.venth.startup.spock.simple.Exercise
import org.venth.startup.spock.simple.RefuseException
import spock.lang.Specification

/**
 * Exercise represents a little legacy code with
 * uses Helper classes to delegate common parts of a code,
 * that are used by different business classes
 *
 * @author Artur Krysiak
 *
 */
@PrepareForTest([ ComplainingTrainer ])
class ExerciseTest extends Specification {

    //necessary to get powermock's flavor
    @Rule public PowerMockRule powerMockRule = new PowerMockRule()

    def Exercise exercise

    def "phase cannot change because of Trainer complains"() {
        given: "not fulfilled condition"
        def List<Check> notFulfilledConditions = [ PowerMockito.mock(Check) ]
        PowerMockito.mockStatic(ComplainingTrainer)
        //noinspection GroovyAssignabilityCheck
        PowerMockito.doThrow(new RefuseException()).when(ComplainingTrainer, "checkPreparation", notFulfilledConditions)

        and: "exercise contains not fulfilled condition"
        exercise = new Exercise(conditions: notFulfilledConditions)

        when: "exercise phase is changed"
        exercise.changePhase()

        then: "trainer is complaining"
        thrown RefuseException
    }

    void setup() {
        exercise = new Exercise()
    }
}
