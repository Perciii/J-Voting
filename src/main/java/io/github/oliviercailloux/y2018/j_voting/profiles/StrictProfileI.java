package io.github.oliviercailloux.y2018.j_voting.profiles;

import io.github.oliviercailloux.y2018.j_voting.StrictPreference;
import io.github.oliviercailloux.y2018.j_voting.Voter;

/**
 *A StrictProfileI represents an incomplete StrictProfile. The preferences are strict. The preferences are not necessarily about the same alternatives.
 *
 */
public interface StrictProfileI extends ProfileI{

	/**
	 * 
	 * @param v a voter not <code>null</code>
	 * @return the StrictPreference of the voter v in the profile.
	 */
	@Override
	public StrictPreference getPreference(Voter v);
	
	@Override
	public default boolean isStrict() {
		return true;
	}
}
