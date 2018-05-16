package io.github.oliviercailloux.y2018.j_voting;

import java.util.*;

import org.slf4j.*;
import com.google.common.base.Preconditions;

/**
 * 
 * Immutable class. A Preference represents a list of alternatives, sorted by order of preference. 
 * Two alternatives can be equally ranked.
 * There cannot be twice the same alternative.
 *
 */
public class Preference {
	private static Logger LOGGER = LoggerFactory.getLogger(Preference.class.getName());
	protected List<Set<Alternative>> preferences;
	
	/**
	 * @param preferences <code>not null</code>
	 */
	public Preference(List<Set<Alternative>> preferences) {
		LOGGER.debug("Preference constructor\n");
		Preconditions.checkNotNull(preferences);
		LOGGER.debug("parameter : {}\n",preferences);
		if(toAlternativeSet(preferences).size() != size(preferences)) {
			LOGGER.debug("alternative several times in the preference\n");
			throw new IllegalArgumentException("A preference cannot contain several times the same alternative.");
		}
		this.preferences = preferences; 
	}
	
	/**
	 * @return the preference of alternatives
	 */
	public List<Set<Alternative>> getPreferencesNonStrict(){
		LOGGER.debug("getPreferencesNonStrict:\n");
		return preferences;
	}
	
	/**
	 * @return the string representing a preference.
	 */
	@Override
	public String toString() {
		LOGGER.debug("toString:\n");
		String s = "";
		for(Set<Alternative> set : preferences) {
			s += "{";
			for(Alternative alter : set) {
				s += alter.getId() + ",";
			}
			s = s.substring(0,s.length()-1) + "},";
		}
		s = s.substring(0,s.length()-1);
		LOGGER.debug("preference string : {}\n",s);
		return s;
	}
	
	/**
	 * @return the size of the Preference, i.e. the number of alternatives in the Preference
	 */
	public int size() {
		LOGGER.debug("size:\n");
		int size = 0;
		for(Set<Alternative> set : preferences) {
			size += set.size();
		}
		LOGGER.debug("size = {}\n",size);
		return size;
	}
	
	/**
	 * @param p <code>not null</code>
	 * @return whether the calling preference is equal to the preference as a parameter.
	 */
	@Override
	public boolean equals(Object pref) {
		LOGGER.debug("equals:\n");
		Preconditions.checkNotNull(pref);
		if(! (pref instanceof Preference)) {
			LOGGER.debug("not a preference\n");
			return false;
		}
		Preference p = (Preference) pref;
		LOGGER.debug("parameter preference : {}\n",p);
		if(this.size() == p.size() && preferences.size() == p.getPreferencesNonStrict().size()) { //same number of alternatives and same number of sets
			for(int i=0;i<this.preferences.size();i++) {
				if(!alternativeSetEqual(preferences.get(i),p.getPreferencesNonStrict().get(i))) {
					LOGGER.debug("return false\n");
					return false;
				}
			}
			LOGGER.debug("return true\n");
			return true;
		}
		LOGGER.debug("return false\n");
		return false;
		
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(preferences);
	}
	
	/**
	 * @param alter <code>not null</code>
	 * @return whether the preference contains the alternative given as parameter
	 */
	public boolean contains(Alternative alter) {
		LOGGER.debug("contains:\n");
		Preconditions.checkNotNull(alter);
		LOGGER.debug("parameter alternative : {}\n",alter);
		return(alternativeSetContains(toAlternativeSet(preferences),alter));
	}
	
	/**
	 * @param p <code>not null</code>
	 * @return whether the preferences are about the same alternatives exactly (not necessarily in the same order).
	 */
	public boolean hasSameAlternatives(Preference p) {
		LOGGER.debug("hasSameAlternatives:\n");
		Preconditions.checkNotNull(p);
		LOGGER.debug("parameter preference : {}\n",p);
		return(this.isIncludedIn(p) && p.isIncludedIn(this));
	}
	
	/**
	 * @param p <code>not null</code>
	 * @return whether the parameter preference contains all the alternatives in the calling preference
	 */
	public boolean isIncludedIn(Preference p) {
		LOGGER.debug("isIncludedIn:\n");
		Preconditions.checkNotNull(p);
		LOGGER.debug("parameter preference : {}\n",p);
		for(Alternative alter : toAlternativeSet(preferences)) {
			if(!p.contains(alter)) {
				LOGGER.debug("return false\n");
				return false;
			}
		}
		LOGGER.debug("return true\n");
		return true;
	}
	
	/**
	 * @return a set of alternatives containing all the alternatives of the list of set of alternative given. If an alternative appears several times in the list of sets, it appears only once in the new set.
	 */
	public static Set<Alternative> toAlternativeSet(List<Set<Alternative>> preferences){
		LOGGER.debug("toAlternativeSet:\n");
		Preconditions.checkNotNull(preferences);
		Set<Alternative> set = new HashSet<>();
		for(Set<Alternative> sets : preferences) {
			for(Alternative alter : sets) {
				if(!alternativeSetContains(set,alter)) {
					set.add(alter);
				}
			}
		}
		LOGGER.debug("set : {}\n",set);
		return set;
	}
	
	/**
	 * 
	 * @param set1 <code>not null</code>
	 * @param set2 <code>not null</code>
	 * @return true if the two sets contain the same alternatives
	 */
	public static boolean alternativeSetEqual(Set<Alternative> set1, Set<Alternative> set2) {
		LOGGER.debug("alternativeSetEquals:\n");
		Preconditions.checkNotNull(set1);
		Preconditions.checkNotNull(set2);
		LOGGER.debug("parameter set1 : {}\nset2 : {}\n",set1,set2);
		if(set1.size() == set2.size()) {
			for(Alternative alter : set1) {
				if(!alternativeSetContains(set2,alter)) {
					LOGGER.debug("return false (same size and some different alternatives");
					return false;
				}
			}
			LOGGER.debug("return true");
			return true;
		}
		LOGGER.debug("return false (not the same size)");
		return false;
	}
	
	/**
	 * @param set <code>not null</code>
	 * @param alter <code>not null</code>
	 * @return whether the set contains the alternative
	 */
	public static boolean alternativeSetContains(Set<Alternative> set,Alternative alter) {
		LOGGER.debug("alternativeSetContains:\n");
		Preconditions.checkNotNull(set);
		Preconditions.checkNotNull(alter);
		LOGGER.debug("parameter set : {}\n,alternative : {}\n",set,alter);
		for(Alternative a : set) {
			if(a.equals(alter)) {
				LOGGER.debug("return true\n");
				return true;
			}
		}
		LOGGER.debug("return false\n");
		return false;
	}
	
	/**
	 * 
	 * @param list not <code> null </code>
	 * @return the size of a list of alternative sets
	 */
	public static int size(List<Set<Alternative>> list) {
		LOGGER.debug("list set alternative size:\n");
		Preconditions.checkNotNull(list);
		int size = 0;
		for(Set<Alternative> set : list) {
			size += set.size();
		}
		LOGGER.debug("size = {}\n",size);
		return size;
	}

}
