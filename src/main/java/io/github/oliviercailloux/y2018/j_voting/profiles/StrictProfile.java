package io.github.oliviercailloux.y2018.j_voting.profiles;

import io.github.oliviercailloux.y2018.j_voting.StrictPreference;
import io.github.oliviercailloux.y2018.j_voting.Voter;

import java.io.*;

/**
 *A StrictProfile represents a complete StrictProfile. The preferences are strict. The preferences are about the same alternatives exactly.
 *
 */
public interface StrictProfile extends StrictProfileI,Profile{

	/**
	 * 
	 * @param v a voter not <code>null</code>
	 * @return the StrictPreference of the voter v in the profile.
	 */
	@Override
	public StrictPreference getPreference(Voter v);
	
	/**
	 * writes the strict and complete profile into a new file with the SOC format.
	 */
	public void writeToSOC(OutputStream output) throws IOException;
}
