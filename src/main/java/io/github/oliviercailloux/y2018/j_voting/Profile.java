package io.github.oliviercailloux.y2018.j_voting;

import java.util.*;

/**
 * 
 * A Profile represents a complete profile. The preferences can be strict or not. The preferences are all about the same alternatives exactly.
 *
 */
public interface Profile extends ProfileI{

	/**
	 * 
	 * @return the number of alternatives of each preference. As the profile is complete, each voter voted for the same number of alternatives.
	 */
	public int getNbAlternatives();
	
	/**
	 * 
	 * @return a set of all the alternatives in the profile.
	 */
	public Set<Alternative> getAlternatives();
	
	@Override
	public default boolean isComplete() {
		return true;
	}
}
