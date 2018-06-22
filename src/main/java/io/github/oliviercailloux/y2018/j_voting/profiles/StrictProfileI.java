package io.github.oliviercailloux.y2018.j_voting.profiles;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import io.github.oliviercailloux.y2018.j_voting.StrictPreference;
import io.github.oliviercailloux.y2018.j_voting.Voter;

/**
 * A StrictProfileI represents an incomplete StrictProfile. The preferences are
 * strict. The preferences are not necessarily about the same alternatives.
 *
 */
public interface StrictProfileI extends ProfileI {

	/**
	 * 
	 * @param v
	 *            a voter not <code>null</code>
	 * @return the StrictPreference of the voter v in the profile.
	 */
	@Override
	public StrictPreference getPreference(Voter v);

	@Override
	public default boolean isStrict() {
		return true;
	}

	/**
	 * 
	 * @param i
	 *            the index of the alternatives to get
	 * @return a list of strings containing the ith alternatives in the profile. If
	 *         the preference doesn't have an ith alternative, it adds an empty
	 *         string to the list.
	 */
	public List<String> getIthAlternativesAsStrings(int i);

	/**
	 * 
	 * @param i
	 * @return a list of strings of the ith alternatives of the unique preferences.
	 *         If the preference doesn't have an ith alternative, it adds an empty
	 *         string to the list.
	 */
	public List<String> getIthAlternativesOfUniquePrefAsString(int i);

	/**
	 * 
	 * @param output
	 *            the outputstream to write the soi profile
	 */
	public void writeToSOI(OutputStream output) throws IOException;

}
