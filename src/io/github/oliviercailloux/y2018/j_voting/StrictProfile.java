package io.github.oliviercailloux.y2018.j_voting;
import java.util.*;

/**
 * 
 * This class maps the voters to their strict preference.
 *
 */
public class StrictProfile {
	private Map<Voter,StrictPreference> association;
	
	public StrictProfile(){
		association=new HashMap<>();
	}
	
	/**
	 * Adds a StrictPreference to a voter. If the voter is already in the map, the StrictPreference in the map is replaced by the StrictPreference given as an argument. If the voter is not in the map, it adds the voter and his StrictPreference.
	 * @param voter
	 * @param preference
	 */
	public void addProfile(Voter voter,StrictPreference preference) {
		if(voter==null) {
			throw new IllegalArgumentException("The voter cannot be null.");
		}
		if(preference==null) {
			throw new IllegalArgumentException("The preference cannot be null.");
		}
		association.put(voter, preference);
	}
	
	/**
	 * 
	 * @param voter
	 * @return the StrictPreference associated to the voter given as an argument. If the voter is not in the map, it throws a NoSuchElementException.
	 */
	public StrictPreference getPreferences(Voter voter){
		if(voter==null) {
			throw new IllegalArgumentException("The voter cannot be null.");
		}
		if(association.containsKey(voter)) {
			return association.get(voter);
		}
		throw new NoSuchElementException("This voter isn't in the map.");
	}
}
