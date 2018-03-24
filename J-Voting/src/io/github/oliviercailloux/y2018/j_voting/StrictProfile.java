package io.github.oliviercailloux.y2018.j_voting;

import java.util.*;

/**
 * 
 * This class maps the voters to all their strict preferences (StrictPreference). Each voter can have several preferences : for different sets of alternatives.
 *
 */
public class StrictProfile {
	private Map<Voter,StrictPreference> association;
	
	public StrictProfile(){
		association=new HashMap<>();
	}
	
	/**
	 * Adds a StrictPreference to a voter. If the voter is already in the map, it adds the StrictPreference. If the voter isn't in the map, it adds a new voter and associates the StrictPreference.
	 * @param voter
	 * @param preference
	 */
	public void addProfile(Voter voter,StrictPreference preference) {
		association.put(voter, preference);
	}
	
	/**
	 * Returns the StrictPreference associated to a Voter. If the voter is not found, returns null
	 * @param voter
	*/
	public StrictPreference getPreferences(Voter voter){
		if(association.containsKey(voter)) {
			return association.get(voter);
		}
		else {
			return null;
		}
	}
}
