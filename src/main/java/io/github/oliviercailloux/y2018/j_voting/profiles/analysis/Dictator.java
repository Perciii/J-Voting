package io.github.oliviercailloux.y2018.j_voting.profiles.analysis;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import io.github.oliviercailloux.y2018.j_voting.Preference;
import io.github.oliviercailloux.y2018.j_voting.Voter;
import io.github.oliviercailloux.y2018.j_voting.profiles.ImmutableProfileI;

/**
 * This class is immutable. This class provides a result for an election that is
 * necessarily the preference of the dictator (a Voter).
 */
public class Dictator implements SocialWelfareFunction {

	private Voter dictator;
	private static final Logger LOGGER = LoggerFactory.getLogger(Borda.class.getName());

	public Dictator(Voter v) {
		LOGGER.debug("Dictator");
		Preconditions.checkNotNull(v);
		dictator = v;
	}

	/**
	 * 
	 * @param profile
	 * @return the dictator's preference
	 */
	@Override
	public Preference getSocietyPreference(ImmutableProfileI profile) {
		LOGGER.debug("getSocietyStrictPreference");
		Preconditions.checkNotNull(profile);
		Preconditions.checkArgument(profile.getProfile().containsKey(dictator));
		LOGGER.debug("parameter profile : {}", profile);
		LOGGER.debug("Dictator : {}", dictator);
		LOGGER.debug("return preference : {}", profile.getPreference(dictator));
		return profile.getPreference(dictator);
	}

	public Voter getDictator() {
		LOGGER.debug("getDictator");
		return dictator;
	}

	@Override
	public int hashCode() {
		LOGGER.debug("hashCode");
		return Objects.hash(dictator);
	}

	/**
	 * @param o1
	 *            not <code>null</code>
	 * @return true if both Dictators have the same voter as dictator.
	 */
	@Override
	public boolean equals(Object o1) {
		LOGGER.debug("equals");
		Preconditions.checkNotNull(o1);
		if (!(o1 instanceof Dictator)) {
			LOGGER.debug("returns false");
			return false;
		}
		Dictator d1 = (Dictator) o1;
		return (d1.getDictator()).equals(this.dictator);
	}
}
