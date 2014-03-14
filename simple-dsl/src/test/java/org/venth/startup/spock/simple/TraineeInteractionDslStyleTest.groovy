package org.venth.startup.spock.simple

import spock.lang.Specification

/**
 * The goal is to show different kind of interactions
 * and its implementation with spock
 *
 * There are three entities
 * Trainee, Phone and Exercise. An exercise has following phases:
 * <pre>
 * - STOPPED,
 * - WARM_UP,
 * - EXERCISE,
 * - COOL_DOWN
 * </pre>
 *
 * The observer's pattern is used to notify a trainee about exercise phase
 * so a trainee observes an exercise
 *
 * A trainee contains a phone
 *
 * When an exercise changes a phase, a trainee says to a phone about his doing
 *
 * @author Artur Krysiak (last modified by $Author$).
 * @version $Revision$ $Date$
 */
@SuppressWarnings("GroovyAssignabilityCheck")
class TraineeInteractionDslStyleTest extends Specification {

    def Trainee trainee
    def Phone phone

    def "trainee say that started a warm-up"() {
        given:
        trainee = traineeBuilder(
                spec: this,
                phone: { builder -> builder.phoneBuilder(switchedOn: true) }
        ).build()

        when: "exercise notifies trainee that warm-up has started"
        trainee.notify ExercisePhase.WARM_UP

        then: "trainee said that started a warm-up"
        1 * phone.sayTo({String traineeComment ->
            "I started WARM_UP" == traineeComment
        })
    }

    def "trainee informs via phone about all started exercise phases"() {
        given: "trainee holds a phone"
        phone = Mock()
        trainee.phone = phone
        phone.enabled >> true

        when: "exercise notifies trainee that the exercise phase has changed"
        trainee.notify exercisePhase

        then: "trainee said that started a warm-up"
        1 * phone.sayTo({String traineeComment ->
            expectedComment == traineeComment
        })

        where:
        exercisePhase           | expectedComment
        ExercisePhase.WARM_UP   | "I started WARM_UP"
        ExercisePhase.STOPPED   | "I started STOPPED"
        ExercisePhase.EXERCISE  | "I started EXERCISE"
        ExercisePhase.COOL_DOWN | "I started COOL_DOWN"
    }

    def "trainee without phone refuses making any exercises"() {
        given: "trainee has no phone"
        trainee.phone = null

        when: "exercise notifies trainee that warm-up has started"
        trainee.notify exercisePhase

        then: "trainee refuses the training"
        thrown RefuseException

        where: "all exercise phases"
        exercisePhase << ExercisePhase.values()
    }

    def "trainee keeps the received exercise's phase sequence"() {
        given: "trainee holds a phone"
        phone = Mock()
        trainee.phone = phone
        phone.enabled >> true

        when: "exercise notifies about warm-up and cool down in a sequence"
        trainee.notify ExercisePhase.WARM_UP
        trainee.notify ExercisePhase.COOL_DOWN

        then: "trainee said that started a warm-up"
        1 * phone.sayTo({String traineeComment ->
            "I started WARM_UP" == traineeComment
        })

        then: "trainee said that started a cool down"
        1 * phone.sayTo({String traineeComment ->
            "I started COOL_DOWN" == traineeComment
        })
    }

    def "trainee answer that he has no phone, if he doesn't have one and someone ask about it"() {
        given: "trainee has no phone"
        trainee.phone = null

        when: "ask about phone model"
        trainee.usedPhoneModel

        then: "I use no phone"
        def e = thrown LackOfPhoneException
        e.message == "I don't have any phone. Please give me one"
    }

    def "trainee gives currently used phone model"() {
        given: "a super duper phone"
        phone = Mock()
        def expectedPhoneModel = "Super Duper"
        phone.model >> expectedPhoneModel

        and: "trainee uses that phone"
        trainee.phone = phone

        when: "is asked about phone model"
        def usedPhoneModel = trainee.usedPhoneModel

        then: "the used model is ${expectedPhoneModel}"
        expectedPhoneModel == usedPhoneModel
    }

    def "trainee refuses to train if a phone is turned off"() {
        given: "a turned off phone"
        phone = Mock()
        phone.isEnabled() >> false

        and: "trainee uses that phone"
        trainee.phone = phone

        when: "trainee is notified about exercise phase change"
        trainee.notify ExercisePhase.WARM_UP

        then: "trainee refuses to train due to switched off phone"
        def e = thrown RefuseException
        e.message == "My phone is switched off. Please switch the phone on"
    }

    def "when a trainee receives information about stopping exercise, he switches off his phone"() {
        given: "a turned on phone"
        phone = Mock()
        phone.isEnabled() >> true

        and: "trainee uses that phone"
        trainee.phone = phone

        when: "trainee is notified about exercise end"
        trainee.notify ExercisePhase.STOPPED

        then: "the phone is switched off"
        1 * phone.makePhoneEnabled(false)
    }

    void setup() {
        trainee = new Trainee()
    }

    TraineeBuilder traineeBuilder(arguments) {
        new TraineeBuilder(arguments)
    }


}

class PhoneBuilder {
    Specification spec

    def switchedOn

    def Phone build() {
        spec.Mock()
    }
}

class TraineeBuilder {
    def Specification spec
    def Closure phone

    PhoneBuilder phoneBuilder(arguments) {
        def args = [spec: spec]
        args.putAll arguments
        new PhoneBuilder(args)
    }


    def Trainee build() {
        new Trainee(phone: phone(this).build())
    }
}