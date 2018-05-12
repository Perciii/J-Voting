package io.github.oliviercailloux.y2018.j_voting;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class StrictPreference2 extends Preference{

	static Logger LOGGER = LoggerFactory.getLogger(StrictPreference2.class.getName());
	
	/**
	 * 
	 * @param preferences a list of alternatives.
	 */
	public StrictPreference2(List<Alternative> preferences) {
		//Preconditions.checkNotNull(preferences);
		super(listAlternativeToListSetAlternative(preferences));
		LOGGER.debug("StrictPreference2 constructor\n");
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
			Set<Alternative> alterset = new HashSet<Alternative>();
			alterset.add(list.get(i));
			set.add(alterset);
		}
		LOGGER.debug("new list of set : {}\n",set);
		return set;
	}
	
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
}
