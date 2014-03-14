package org.venth.startup.spock.simple

import org.mockito.Mockito
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
 * @author Artur Krysiak
 *
 */
@SuppressWarnings("GroovyAssignabilityCheck")
class TraineeInteractionDslStyleTest extends Specification {

    def Trainee trainee

    def "trainee say that started a warm-up"() {
        given:
        trainee = traineeBuilder()
                .phone(switchedOn: true)
                .build()

        when: "exercise notifies trainee that warm-up has started"
        trainee.notify ExercisePhase.WARM_UP

        then:
        //the condition is because spock needs a comparison
        assertThat(trainee).hasSaid "I started WARM_UP"
    }

    def "trainee informs via phone about all started exercise phases"() {
        given:
        trainee = traineeBuilder()
                .phone(switchedOn: true)
                .build()

        when:
        trainee.notify exercisePhase

        then:
        //the condition is because spock needs a comparison
        assertThat(trainee).hasSaid(expectedComment)

        where:
        exercisePhase           | expectedComment
        ExercisePhase.WARM_UP   | "I started WARM_UP"
        ExercisePhase.STOPPED   | "I started STOPPED"
        ExercisePhase.EXERCISE  | "I started EXERCISE"
        ExercisePhase.COOL_DOWN | "I started COOL_DOWN"
    }

    def "trainee without phone refuses making any exercises"() {
        given: "trainee has no phone"
        trainee = traineeBuilder()
                .withoutPhone()
                .build()

        when:
        trainee.notify exercisePhase

        then:
        def e = thrown Exception
        //the condition is because spock needs a comparison
        assertThat(e).hasRefusedTraining()

        where: "all exercise phases"
        exercisePhase << ExercisePhase.values()
    }

    def "trainee keeps the received exercise's phase sequence"() {
        given:
        trainee = traineeBuilder()
                .phone(switchedOn: true)
                .build()

        when:
        trainee.notify ExercisePhase.WARM_UP
        trainee.notify ExercisePhase.COOL_DOWN

        then:
        //the condition is because spock needs a comparison
        assertThat(trainee).hasSaid "I started WARM_UP"

        then: "trainee said that started a cool down"
        //the condition is because spock needs a comparison
        assertThat(trainee).hasSaid "I started COOL_DOWN"
    }

    def "trainee answer that he has no phone, if he doesn't have one and someone ask about it"() {
        given:
        trainee = traineeBuilder()
                .withoutPhone()
                .build()

        when:
        trainee.usedPhoneModel

        then:
        def e = thrown Exception
        assertThat(e).hasInformAboutMissingPhone()
    }

    def "trainee gives currently used phone model"() {
        given:
        def expectedPhoneModel = "Super Duper"
        trainee = traineeBuilder()
                .phone(model: expectedPhoneModel)
                .build()

        when:
        def usedPhoneModel = trainee.usedPhoneModel

        then:
        assertThat(usedPhoneModel).usesPhoneModel(expectedPhoneModel)
    }

    def "trainee refuses to train if a phone is turned off"() {
        given:
        trainee = traineeBuilder()
                .phone(switchedOn: false)
                .build()

        when:
        trainee.notify ExercisePhase.WARM_UP

        then:
        def e = thrown Exception
        assertThat(e).hasRefusedTraining().saying "My phone is switched off. Please switch the phone on"
    }

    def "when a trainee receives information about stopping exercise, he switches off his phone"() {
        given:
        trainee = traineeBuilder()
                .phone(switchedOn: true)
                .build()

        when:
        trainee.notify ExercisePhase.STOPPED

        then:
        assertThat(trainee).hasSwitchedOffPhone()
    }

    TraineeBuilder traineeBuilder() {
        new TraineeBuilder()
    }

    TraineeAssertion assertThat(Trainee trainee) {
        new TraineeAssertion(trainee: trainee)
    }

    PhoneAssertion assertThat(String phoneModel) {
        new PhoneAssertion(phoneModel: phoneModel)
    }

    ExerciseExceptionAssertion assertThat(Throwable exception) {
        new ExerciseExceptionAssertion(exception: exception)
    }

}

class PhoneBuilder {
    def boolean switchedOn
    def String model

    def Phone build() {
        def phone = Mockito.mock(Phone)
        Mockito.when(phone.enabled).thenReturn switchedOn
        Mockito.when(phone.model).thenReturn model

        phone
    }
}

class TraineeBuilder {
    def phone

    def TraineeBuilder phone(arguments) {
        phone = arguments

        this
    }

    def Trainee build() {
        //noinspection GroovyAssignabilityCheck
        new Trainee(phone: phone == null ? null : new PhoneBuilder(phone).build())
    }

    def TraineeBuilder withoutPhone() {
        phone = null

        this
    }
}

@SuppressWarnings("GroovyAccessibility")
class TraineeAssertion {

    def Trainee trainee

    def TraineeAssertion hasSaid(String message) {
        //noinspection GroovyAccessibility
        Mockito.verify(trainee.phone).sayTo(message)

        this
    }

    def hasSwitchedOffPhone() {
        Mockito.verify(trainee.phone).makePhoneEnabled false

        this
    }
}

class PhoneAssertion {
    def phoneModel

    def usesPhoneModel(CharSequence expectedPhoneModel) {
        phoneModel == expectedPhoneModel

        this
    }
}

class ExerciseExceptionAssertion {
    def Throwable exception

    def ExerciseExceptionAssertion hasRefusedTraining() {
        assert exception instanceof RefuseException

        this
    }

    def ExerciseExceptionAssertion hasInformAboutMissingPhone() {
        assert exception instanceof LackOfPhoneException
        assert exception.message == "I don't have any phone. Please give me one"

        this
    }

    def saying(String comment) {
        assert exception.message == comment

        this
    }
}
