package io.github.oliviercailloux.y2018.j_voting;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * 
 * Immutable class. A Preference represents a list of alternatives, sorted by
 * order of preference. Two alternatives can be equally ranked. There cannot be
 * twice the same alternative.
 *
 */
public class Preference {
	private static final Logger LOGGER = LoggerFactory.getLogger(Preference.class.getName());
	protected List<Set<Alternative>> preference;

	/**
	 * @param preferences
	 *            <code>not null</code> a list of sets of alternatives. In a set,
	 *            the alternatives are equally ranked. The sets are sorted by
	 *            preference in the list. If an alternative is present several
	 *            times, an IllegalArgumentException is thrown.
	 */
	public Preference(List<Set<Alternative>> preference) {
		LOGGER.debug("Preference constructor");
		Preconditions.checkNotNull(preference);
		LOGGER.debug("parameter : {}", preference);
		if (toAlternativeSet(preference).size() != size(preference)) {
			LOGGER.debug("alternative several times in the preference");
			throw new IllegalArgumentException("A preference cannot contain several times the same alternative.");
		}
		this.preference = preference;
	}

	/**
	 * 
	 * @param position
	 *            not <code>null</code>
	 * @return the alternative at the position given in the strict preference
	 */
	public Alternative getAlternative(Integer position) throws IndexOutOfBoundsException {
		LOGGER.debug("getAlternative");
		Preconditions.checkNotNull(position);
		if (position >= preference.size()) {
			throw new IndexOutOfBoundsException("This position doesn't exist in the Preference");
		}

		return preference.get(position).iterator().next();
	}

	/**
	 * @return the preference of alternatives
	 */
	public List<Set<Alternative>> getPreferencesNonStrict() {
		LOGGER.debug("getPreferencesNonStrict :");
		return preference;
	}

	/**
	 * @return the string representing a preference.
	 */
	@Override
	public String toString() {
		LOGGER.debug("toString:");
		String s = "";
		for (Set<Alternative> set : preference) {
			s += "{";
			for (Alternative alter : set) {
				s += alter.getId() + ",";
			}
			s = s.substring(0, s.length() - 1) + "},";
		}
		s = s.substring(0, s.length() - 1);
		LOGGER.debug("preference string : {}", s);
		return s;
	}

	/**
	 * @return the size of the Preference, i.e. the number of alternatives in the
	 *         Preference
	 */
	public int size() {
		LOGGER.debug("size :");
		return size(preference);
	}

	/**
	 * @param p
	 *            <code>not null</code>
	 * @return whether the calling preference is equal to the preference as a
	 *         parameter.
	 */
	@Override
	public boolean equals(Object pref) {
		LOGGER.debug("equals:");
		Preconditions.checkNotNull(pref);
		if (!(pref instanceof Preference)) {
			LOGGER.debug("not a preference");
			return false;
		}
		Preference p = (Preference) pref;
		LOGGER.debug("parameter preference : {}", p);
		if (this.size() == p.size() && preference.size() == p.getPreferencesNonStrict().size()) { // same number of
																									// alternatives and
																									// same number of
																									// sets
			for (int i = 0; i < this.preference.size(); i++) {
				if (!preference.get(i).equals(p.getPreferencesNonStrict().get(i))) {
					LOGGER.debug("return false");
					return false;
				}
			}
			LOGGER.debug("return true");
			return true;
		}
		LOGGER.debug("return false");
		return false;

	}

	@Override
	public int hashCode() {
		return Objects.hash(preference);
	}

	/**
	 * @param alter
	 *            <code>not null</code>
	 * @return whether the preference contains the alternative given as parameter
	 */
	public boolean contains(Alternative alter) {
		LOGGER.debug("contains:");
		Preconditions.checkNotNull(alter);
		LOGGER.debug("parameter alternative : {}", alter);
		return (toAlternativeSet(preference).contains(alter));
	}

	/**
	 * @param p
	 *            <code>not null</code>
	 * @return whether the preferences are about the same alternatives exactly (not
	 *         necessarily in the same order).
	 */
	public boolean hasSameAlternatives(Preference p) {
		LOGGER.debug("hasSameAlternatives:");
		Preconditions.checkNotNull(p);
		LOGGER.debug("parameter preference : {}", p);
		return (this.isIncludedIn(p) && p.isIncludedIn(this));
	}

	/**
	 * @param p
	 *            <code>not null</code>
	 * @return whether the parameter preference contains all the alternatives in the
	 *         calling preference
	 */
	public boolean isIncludedIn(Preference p) {
		LOGGER.debug("isIncludedIn:");
		Preconditions.checkNotNull(p);
		LOGGER.debug("parameter preference : {}", p);
		for (Alternative alter : toAlternativeSet(preference)) {
			if (!p.contains(alter)) {
				LOGGER.debug("return false");
				return false;
			}
		}
		LOGGER.debug("return true");
		return true;
	}

	/**
	 * 
	 * @param alter
	 *            not <code>null</code>. If the alternative is not in the
	 *            preference, it throws an IllegalArgumentException.
	 * @return the rank of the alternative given in the Preference.
	 */
	public int getAlternativeRank(Alternative alter) {
		LOGGER.debug("getAlternativeRank:");
		Preconditions.checkNotNull(alter);
		if (!this.contains(alter)) {
			throw new IllegalArgumentException("Alternative not in the set");
		}
		int rank = 1;
		for (Set<Alternative> set : preference) {
			if (set.contains(alter)) {
				LOGGER.debug("alternative rank : {}", rank);
				break;
			}
			rank++;
		}
		return rank;
	}

	/**
	 * 
	 * @param preferences
	 *            not <code> null </code> a list of sets of alternatives
	 * @return a set of alternatives containing all the alternatives of the list of
	 *         set of alternative given. If an alternative appears several times in
	 *         the list of sets, it appears only once in the new set.
	 */
	public static Set<Alternative> toAlternativeSet(List<Set<Alternative>> preference) {
		LOGGER.debug("toAlternativeSet:");
		Preconditions.checkNotNull(preference);
		Set<Alternative> set = new LinkedHashSet<>();
		for (Set<Alternative> sets : preference) {
			for (Alternative alter : sets) {
				if (!set.contains(alter)) {
					set.add(alter);
				}
			}
		}
		LOGGER.debug("set : {}", set);

		return set;
	}

	/**
	 * 
	 * @param list
	 *            not <code> null </code>
	 * @return the size of a list of alternative sets
	 */
	public static int size(List<Set<Alternative>> list) {
		LOGGER.debug("list set alternative size:");
		Preconditions.checkNotNull(list);
		int size = 0;
		for (Set<Alternative> set : list) {
			size += set.size();
		}
		LOGGER.debug("size = {}", size);
		return size;
	}

	/**
	 * 
	 * @return true if the Preference is Strict (without several alternatives having
	 *         the same rank)
	 */
	public boolean isStrict() {
		LOGGER.debug("isStrict:");
		if (preference.size() == size(preference)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @return the StrictPreference built from the preference if the preference is
	 *         strict. If the preference is not strict it throws an
	 *         IllegalArgumentException.
	 */
	public StrictPreference toStrictPreference() {
		LOGGER.debug("toStrictPreference");
		if (!isStrict()) {
			throw new IllegalArgumentException("the preference is not strict.");
		}
		List<Alternative> list = new ArrayList<>();
		for (Set<Alternative> set : preference) {
			for (Alternative a : set) {
				list.add(a);
			}
		}
		LOGGER.debug("list : {}", list);
		return new StrictPreference(list);
	}
}
