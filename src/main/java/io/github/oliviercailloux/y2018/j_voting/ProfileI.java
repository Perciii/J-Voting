package io.github.oliviercailloux.y2018.j_voting;

/**
 * A ProfileI represents an incomplete profile. The preferences can be strict or not. The preferences are not necessarily about the same alternatives.
 *
 */
public interface ProfileI {

	/**
	 * 
	 * @param v a voter not <code>null</code>
	 * @return the preference of the voter v in the profile.
	 */
	public Preference getPreference(Voter v);
}
