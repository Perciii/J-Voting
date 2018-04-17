package io.github.oliviercailloux.y2018.j_voting;

import java.util.*;
import org.slf4j.*;
import java.lang.Iterable;

/**
 * This class is immutable
 * Contains a list of Alternatives sorted by preferences 
 * Two alternatives can't be equally ranked
 * You can't store the same alternative several times in the list 
 * Every alternative is an integer and corresponds to a voting choice
 */
public class StrictPreference {
	static Logger Log = LoggerFactory.getLogger(ReadProfile.class.getName());
	private List<Alternative> listOfPreferences = new ArrayList<Alternative>();
	
	/**
	 * Creates a set of strict preferences with the set given as a parameter
	 * @param preferences a set of alternatives
	 */
	public StrictPreference(Iterable<Alternative> preferences) {
		Log.info("StrictPreference constructor\n");
		Objects.requireNonNull(preferences); 
		Log.debug("parameter : preferences = {}\n",preferences);
		listOfPreferences = new ArrayList<Alternative>();
		Iterator<Alternative> iterator = preferences.iterator();
		while (iterator.hasNext()) {
			listOfPreferences.add(iterator.next());
		}
		Log.debug("List of preferences : {}\n",listOfPreferences);
	}
	
	/**
	 * @return the list of strict preferences
	 */
	public List<Alternative> getPreferences() {
		return listOfPreferences;
	}
	
	public String toString(){
		return listOfPreferences.toString();
	}
	
	public int size() {
		return listOfPreferences.size();
	}
	
	/**
	 * 
	 * @param pref
	 * @return whether both Strict preferences are about the same alternatives exactly in the same order.
	 */
	public boolean equals(StrictPreference pref) {
		Log.info("equals\n");
		Objects.requireNonNull(pref);
		Log.debug("calling preferences : {}, parameter preferences : {}\n",listOfPreferences,pref.getPreferences());
		return listOfPreferences.equals(pref.getPreferences());
	}
	
	/**
	 * 
	 * @param pref
	 * @return true if all the alternatives in the calling Strict Preference are in the Strict Preference given as a parameter.
	 */
	public boolean isIncludedIn(StrictPreference pref) {
		Log.info("isIncludedIn\n");
		Objects.requireNonNull(pref);
		Log.debug("calling preferences : {}, parameter preferences : {}\n",listOfPreferences,pref.getPreferences());
		return (pref.getPreferences()).containsAll(listOfPreferences);
	}
	
	/**
	 * 
	 * @param pref
	 * @return whether both Strict preferences are about the same alternatives exactly (in the same order or not).
	 */
	public boolean hasSameAlternatives(StrictPreference pref) {
		Log.info("hasSameAlternatives\n");
		Objects.requireNonNull(pref);
		Log.debug("calling preferences : {}, parameter preferences : {}\n",listOfPreferences,pref.getPreferences());
		return (this.isIncludedIn(pref) && pref.isIncludedIn(this));
	}

}
