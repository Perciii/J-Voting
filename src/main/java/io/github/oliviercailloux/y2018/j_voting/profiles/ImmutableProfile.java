package io.github.oliviercailloux.y2018.j_voting.profiles;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.oliviercailloux.y2018.j_voting.Alternative;
import io.github.oliviercailloux.y2018.j_voting.Preference;
import io.github.oliviercailloux.y2018.j_voting.Voter;

/**
 * This class is immutable. Represents a Complete Profile.
 */
public class ImmutableProfile extends ImmutableProfileI implements Profile {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImmutableProfile.class.getName());

	public ImmutableProfile(Map<Voter, ? extends Preference> votes) {
		super(checkCompleteMap(votes));
	}

	@Override
	public Set<Alternative> getAlternatives() {
		LOGGER.debug("getAlternatives:");
		Preference p = votes.values().iterator().next();
		return Preference.toAlternativeSet(p.getPreferencesNonStrict());
	}
}
