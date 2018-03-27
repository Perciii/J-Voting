package io.github.oliviercailloux.y2018.j_voting;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Collection;
/**
 * This class is immutable
 * Contains a list of Alternatives sorted by preferences 
 * Two alternatives can't be equally ranked
 * You can't store the same alternative several times in the list
 * Every alternative is an integer and corresponds to a voting choice
 */
public class StrictPreference {
	
	private Collection<Alternative> preferences;
	
	
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
	public StrictPreference(Collection<Alternative> preferences) {
		this.preferences = Objects.requireNonNull(preferences);
	}
	
	
	/**
	 * @return the list of strict preferences
	 */
	public Collection<Alternative> getPreferences() {
		return preferences;
	}
}
