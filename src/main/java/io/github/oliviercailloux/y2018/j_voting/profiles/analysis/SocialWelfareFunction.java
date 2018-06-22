package io.github.oliviercailloux.y2018.j_voting.profiles.analysis;

import io.github.oliviercailloux.y2018.j_voting.Preference;
import io.github.oliviercailloux.y2018.j_voting.profiles.ImmutableProfileI;

public interface SocialWelfareFunction {

	/**
	 * 
	 * @param profile
	 *            not <code>null</code>
	 * @return a Preference with the society's preference from the profile. This
	 *         Preference cannot be empty.
	 */
	public Preference getSocietyPreference(ImmutableProfileI profile);
}
