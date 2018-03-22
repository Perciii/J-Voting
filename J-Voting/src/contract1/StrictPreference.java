package contract1;

import java.util.LinkedHashSet;
/**
 * This class is immutable
 * Contains a list of Alternatives sorted by preferences 
 * Two alternatives can't be equally ranked
 * You can't store the same alternative several times in the list
 * Every alternative is an integer and corresponds to a voting choice
 */
public class StrictPreference {
	
	private LinkedHashSet<Alternative> preferences = new LinkedHashSet<>();
	
	
	/**
	 * Default constructor, initiates an empty LinkedHashSet
	 */
	public StrictPreference() {
		this.preferences = new LinkedHashSet<>();
	}
	
	/**
	 * Creates a set of strict preferences with the set given as a parameter
	 * @param preferences a set of alternatives
	 */
	public StrictPreference(LinkedHashSet<Alternative> preferences) {
		//TODO: check how to manage null value
		this.preferences = preferences;
	}
	
	
	/**
	 * @return the list of strict preferences
	 */
	public LinkedHashSet<Alternative> getPreferences() {
		return preferences;
	}
}
