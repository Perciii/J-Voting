package io.github.oliviercailloux.y2018.j_voting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * This class is immutable Contains a list of Alternatives sorted by preferences
 * Two alternatives can't be equally ranked You can't store the same alternative
 * several times in the list Every alternative is an integer and corresponds to
 * a voting choice
 */
public class StrictPreference extends Preference {

	private static final Logger LOGGER = LoggerFactory.getLogger(StrictPreference.class.getName());

	/**
	 * @param preferences
	 *            a list of alternatives.
	 */
	public StrictPreference(List<Alternative> preferences) {
		super(listAlternativeToListSetAlternative(preferences));
		LOGGER.debug("StrictPreference constructor");
	}

	@Override
	public String toString() {
		LOGGER.debug("toString : ");
		String s = "";
		for (Set<Alternative> set : preference) {
			for (Alternative a : set) {
				s += a.toString() + ",";
			}
		}
		return s.substring(0, s.length() - 1);
	}

	/**
	 * 
	 * @return a list of the alternatives by order of preference
	 */
	public List<Alternative> getAlternatives() {
		LOGGER.debug("getAlternatives :");
		return listSetAlternativeToList(preference);
	}

	/**
	 * @param list
	 *            a list of alternatives not <code> null </code>
	 * @return a list of set of alternatives. each set is composed of one
	 *         alternative
	 */
	public static List<Set<Alternative>> listAlternativeToListSetAlternative(List<Alternative> list) {
		LOGGER.debug("listAlternativeToListSetAlternative :");
		Preconditions.checkNotNull(list);
		LOGGER.debug("parameter list : {}", list);
		List<Set<Alternative>> set = new ArrayList<>();
		for (Alternative a : list) {
			Set<Alternative> alterset = new HashSet<>();
			alterset.add(a);
			set.add(alterset);
		}
		LOGGER.debug("new list of set : {}", set);
		return set;
	}

	/**
	 * @param sets
	 *            not <code>null</code>
	 * @return a list of alternatives from a list of sets of alternatives.
	 */
	public static List<Alternative> listSetAlternativeToList(List<Set<Alternative>> sets) {
		LOGGER.debug("listSetAlternativeToList :");
		Preconditions.checkNotNull(sets);
		LOGGER.debug("parameter sets :{}", sets);
		List<Alternative> alts = new ArrayList<>();
		for (Set<Alternative> s : sets) {
			for (Alternative a : s) {
				alts.add(a);
			}
		}
		LOGGER.debug("list : {}", alts);
		return alts;
	}

	/**
	 * 
	 * @param position
	 *            not <code>null</code>
	 * @return the alternative at the position given in the strict preference
	 */
	@Override
	public Alternative getAlternative(Integer position) {
		LOGGER.debug("getAlternative");
		Preconditions.checkNotNull(position);
		LOGGER.debug("position : {}", position);
		if (position >= preference.size()) {
			throw new IndexOutOfBoundsException("This position doesn't exist in the Preference");
		}

		return preference.get(position).iterator().next();
	}
}
