package io.github.oliviercailloux.y2018.j_voting.profiles;

import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;

import io.github.oliviercailloux.y2018.j_voting.Alternative;
import io.github.oliviercailloux.y2018.j_voting.Preference;
import io.github.oliviercailloux.y2018.j_voting.Voter;

/**
 * A ProfileI represents an incomplete profile. The preferences can be strict or
 * not. The preferences are not necessarily about the same alternatives.
 *
 */
public interface ProfileI {

	/**
	 * 
	 * @param v
	 *            a voter not <code>null</code>
	 * @return the preference of the voter v in the profile.
	 */
	public Preference getPreference(Voter v);

	/**
	 * @return the maximum size of a Preference in an incomplete Profile
	 */
	public int getMaxSizeOfPreference();

	/**
	 * 
	 * @return the profile as a map mapping the voters to their preference.
	 */
	public Map<Voter, ? extends Preference> getProfile();

	/**
	 * 
	 * @param v
	 *            a voter not <code>null</code>
	 * @return true if the profile contains the voter
	 */
	// public boolean contains(Voter v);

	/**
	 * 
	 * @return a sorted set of all the voters in the profile. The voters are ordered
	 *         by id.
	 */
	public NavigableSet<Voter> getAllVoters();

	/**
	 * 
	 * @return the number of voters in the profile
	 */
	public int getNbVoters();

	/**
	 * 
	 * @return the sum of the counted votes
	 */
	public int getSumVoteCount();

	/**
	 * 
	 * @return a set of all the different preferences in the profile.
	 */
	public Set<Preference> getUniquePreferences();

	/**
	 * 
	 * @return the number of different preferences in the profile.
	 */
	public int getNbUniquePreferences();

	/**
	 * 
	 * @return true if the profile is complete (all the preferences are about the
	 *         same alternatives exactly).
	 */
	public boolean isComplete();

	/**
	 * 
	 * @return true if the profile is strict (the preferences don't have several
	 *         alternatives that have the same rank).
	 */
	public boolean isStrict();

	/**
	 * 
	 * @param p
	 *            a Preference not <code >null </code>
	 * @return the number of voters that voted for p.
	 */
	public int getNbVoterForPreference(Preference p);

	/**
	 * 
	 * @param o
	 *            an object not <code>null</code>
	 * @return true if both objects implement ProfileI, contain all the same voters
	 *         and each voter has the same preference in the calling profile and in
	 *         the profile given as parameter.
	 */
	@Override
	public boolean equals(Object o);

	/**
	 * 
	 * @return the stricter profile possible
	 */
	public ProfileI restrictProfile();

	/**
	 * 
	 * @return the number of alternatives in the profile
	 */
	public int getNbAlternatives();

	/**
	 * 
	 * @return a set of all the alternatives in the profile
	 */
	public Set<Alternative> getAlternatives();

	/**
	 * 
	 * @return the format of the Profile when restricted
	 */
	public String getFormat();
}
