package org.venth.startup.spock.simple;

/**
 * @author Artur Krysiak
 *
 */
public class ChiTrainer {

	static final String DEFAULT_ANSWER = "Patience, patience. Young apprentice.";
	static final String PROBLEM_ANSWER = "fill yourself with chi and defend a problem";
	static final String KNOWN_PROBLEM = "a problem";

	public String giveMeHintFor(String problem) {
		String answer = DEFAULT_ANSWER;

		if (KNOWN_PROBLEM.equals(problem)) {
			answer = PROBLEM_ANSWER;
		}
		return answer;
	}
}
