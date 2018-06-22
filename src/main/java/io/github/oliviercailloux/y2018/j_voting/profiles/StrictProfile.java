package io.github.oliviercailloux.y2018.j_voting.profiles;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import io.github.oliviercailloux.y2018.j_voting.Alternative;

/**
 * A StrictProfile represents a complete StrictProfile. The preferences are
 * strict. The preferences are about the same alternatives exactly.
 *
 */
public interface StrictProfile extends StrictProfileI, Profile {

	/**
	 * @param i
	 *            not <code>null</code> the ith alternative to get from Voters in
	 *            the profile
	 * @return a List of Alternatives
	 */
	public List<Alternative> getIthAlternatives(int i);

	/**
	 * @param i
	 *            not <code>null</code> the ith alternative to get from Voters with
	 *            different Preferences in the profile
	 * @return a List of Alternatives
	 */
	public List<Alternative> getIthAlternativesOfUniquePreferences(int i);

	/**
	 * writes the strict and complete profile into a new file with the SOC format.
	 */
	public void writeToSOC(OutputStream output) throws IOException;
}
