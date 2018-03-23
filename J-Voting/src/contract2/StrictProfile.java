package contract2;

import java.util.*;

import contract1.*;

/**
 * 
 * This class maps the voters to all their strict preferences (StrictPreference). Each voter can have several preferences : for different sets of alternatives.
 *
 */
public class StrictProfile {
	private Map<Voter,List<StrictPreference>> association;
	
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
		}
		else {
			List<StrictPreference> preferences=new ArrayList<>();
			preferences.add(preference);
			association.put(voter, preferences);
		}
	}
	
	/**
	 * Adds a list of StrictPreference to a voter. If the voter is already in the map, it adds the list of StrictPreference. If the voter isn't in the map, it adds a new voter and associates the list of StrictPreference.
	 * @param voter
	 * @param preferencesToAdd
	 */
	public void addProfile(Voter voter,List<StrictPreference> preferencesToAdd) {
		if(association.containsKey(voter)) {
			List<StrictPreference> preferences=association.get(voter);
			preferences.addAll(preferencesToAdd);
		}
		else {
			association.put(voter, preferencesToAdd);
		}
	}
}
