package io.github.oliviercailloux.y2018.j_voting;

import java.util.*;
import org.slf4j.*;

import com.google.common.base.Preconditions;


/**
 * This class is immutable
 * Contains a list of Alternatives sorted by preferences 
 * Two alternatives can't be equally ranked
 * You can't store the same alternative several times in the list 
 * Every alternative is an integer and corresponds to a voting choice
 */
public class StrictPreference extends Preference {

	private static Logger LOGGER = LoggerFactory.getLogger(StrictPreference.class.getName());
	
	/**
	 * 
	 * @param preferences a list of alternatives.
	 */
	public StrictPreference(List<Alternative> preferences) {
		super(listAlternativeToListSetAlternative(preferences));
		LOGGER.debug("StrictPreference constructor\n");
	}
	

	@Override
	public String toString() {
		LOGGER.debug("toString : \n");
		String s = "";
		for(Set<Alternative> set : preferences) {
			for(Alternative a : set) {
				s += a.toString() + ",";
			}
		}
		return s.substring(0,s.length()-1);
	}
	
	public List<Alternative> getPreferences(){
		LOGGER.debug("getPreferences:\n");
		return listSetAlternativeToList(preferences);
	}
	/**
	 * 
	 * @param list a list of alternatives
	 * @return a list of set of alternatives. each set is composed of one alternative
	 */
	public static List<Set<Alternative>>listAlternativeToListSetAlternative(List<Alternative> list) {
		LOGGER.debug("listAlternativeToListSetAlternative\n");
		Preconditions.checkNotNull(list);
		LOGGER.debug("parameter list : {}\n",list);
		List<Set<Alternative>> set = new ArrayList<>();
		for(int i=0;i<list.size();i++) {
			Set<Alternative> alterset = new HashSet<>();
			alterset.add(list.get(i));
			set.add(alterset);
		}
		LOGGER.debug("new list of set : {}\n",set);
		return set;
	}
	

	/**
	 * 
	 * @param sets
	 * @return a list of alternatives from a list of sets of alternatives.
	 */
	public static List<Alternative> listSetAlternativeToList(List<Set<Alternative>> sets){
		LOGGER.debug("listSetAlternativeToList:\n");
		Preconditions.checkNotNull(sets);
		LOGGER.debug("parameter sets :{}\n",sets);
		List<Alternative> alts = new ArrayList<>();
		for(Set<Alternative> s : sets) {
			for(Alternative a : s) {
				alts.add(a);
			}
		}
		LOGGER.debug("list : {}\n",alts);
		return alts;
	}
}
