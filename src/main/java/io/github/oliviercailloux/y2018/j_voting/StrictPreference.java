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
	static Logger log = LoggerFactory.getLogger(ReadProfile.class.getName());
	private List<Alternative> listOfPreferences = new ArrayList<Alternative>();
	
	/**
	 * Creates a set of strict preferences with the set given as a parameter
	 * @param preferences a set of alternatives
	 */
	public StrictPreference(Iterable<Alternative> preferences) {
		log.debug("StrictPreference constructor\n");
		Objects.requireNonNull(preferences); 
		log.debug("parameter : preferences = {}\n",preferences);
		listOfPreferences = new ArrayList<Alternative>();
		Iterator<Alternative> iterator = preferences.iterator();
		while (iterator.hasNext()) {
			listOfPreferences.add(iterator.next());
		}
		log.debug("List of preferences : {}\n",listOfPreferences);
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
		log.debug("equals\n");
		Objects.requireNonNull(pref);
		log.debug("calling preferences : {}, parameter preferences : {}\n",listOfPreferences,pref.getPreferences());
		List<Alternative> list2= pref.getPreferences();
		if(list2.size() != listOfPreferences.size()) {
			return false;
		}
		else {
			for(int i=0;i<list2.size();i++) {
				if(!list2.get(i).equals(listOfPreferences.get(i))) {
					return false;
				}
			}
			return true;
		}
	}
	
	/**
	 * 
	 * @param pref
	 * @return true if all the alternatives in the calling Strict Preference are in the Strict Preference given as a parameter.
	 */
	public boolean isIncludedIn(StrictPreference pref) {
		log.debug("isIncludedIn\n");
		Objects.requireNonNull(pref);
		log.debug("calling preferences : {}, parameter preferences : {}\n",listOfPreferences,pref.getPreferences());
		for(Alternative a : listOfPreferences) {
			if(!pref.contains(a)) {
				log.debug("return false");
				return false;
			}
		}
		log.debug("return true");
		return true;
	}
	
	/**
	 * 
	 * @param pref
	 * @return whether both Strict preferences are about the same alternatives exactly (in the same order or not).
	 */
	public boolean hasSameAlternatives(StrictPreference pref) {
		log.debug("hasSameAlternatives\n");
		Objects.requireNonNull(pref);
		log.debug("calling preferences : {}, parameter preferences : {}\n",listOfPreferences,pref.getPreferences());
		return (this.isIncludedIn(pref) && pref.isIncludedIn(this));
	}
	
	/**
	 * 
	 * @param alter
	 * @return true if the Strict preference contains the alternative alter.
	 */
	public boolean contains(Alternative alter) {
		log.debug("contains :\n");
		Objects.requireNonNull(alter);
		log.debug("parameter :alter = {}\n",alter.getId());
		for(Alternative a : listOfPreferences) {
			if(a.equals(alter)) {
				log.debug("return true");
				return true;
			}
		}
		log.debug("return false");
		return false;
	}

}
