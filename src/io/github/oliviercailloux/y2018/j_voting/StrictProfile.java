package io.github.oliviercailloux.y2018.j_voting;

import java.util.*;

/**
 * 
 * This class maps the voters to all their strict preferences (StrictPreference). Each voter can have several preferences : for different sets of alternatives.
 *
 */
public class StrictProfile {
	private Map<Voter,List<StrictPreference>> association;
	private Voter VoterId;
	private List<StrictPreference> pref;
	
	public StrictProfile(){
		association=new HashMap<>();
	}
	
	/**
	 * Adds a StrictPreference to a voter. If the voter is already in the map, it adds the StrictPreference. If the voter isn't in the map, it adds a new voter and associates the StrictPreference.
	 * @param voter
	 * @param preference
	 */
	public void addProfile(Voter voter,StrictPreference preference) {
		if(association.containsKey(voter)) {
			List<StrictPreference> preferences=association.get(voter);
			preferences.add(preference);
			pref=preferences;
		}
		else {
			List<StrictPreference> preferences=new ArrayList<>();
			preferences.add(preference);
			association.put(voter, preferences);
			pref=preferences;
		}
		VoterId=voter;
	}
	
	public Voter getkey(){
		return VoterId;
	}
	
	public List<StrictPreference> getPreferences(){
		return pref;
	}
}
