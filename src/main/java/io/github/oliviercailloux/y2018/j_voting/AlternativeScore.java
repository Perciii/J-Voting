package io.github.oliviercailloux.y2018.j_voting;

import java.util.Objects;

public class AlternativeScore {

	private int score;
	private Alternative alt;
	static Logger log = LoggerFactory.getLogger(AlternativeScore.class.getName());	
	
	public AlternativeScore(Alternative a, int s) {
		log.debug("AlternativeScore\n");
		Objects.requireNonNull(s);
		log.debug("parameter Alternative : {}\n", a.toString());
		log.debug("parameter Score : {}\n", Integer.toString(score));
		alt = a;
		score = s;
	}

	public Alternative getA() {
		log.debug("getA\n");
		return alt;
	}

	public int getS() {
		log.debug("getS\n");
		return score;
	}

	public String toString() {
		return ("A : " + alt.toString() + " S : " + Integer.toString(score) + '\n');

	}

}
