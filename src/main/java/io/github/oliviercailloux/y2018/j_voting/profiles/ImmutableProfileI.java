package io.github.oliviercailloux.y2018.j_voting.profiles;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import io.github.oliviercailloux.y2018.j_voting.Alternative;
import io.github.oliviercailloux.y2018.j_voting.Preference;
import io.github.oliviercailloux.y2018.j_voting.Voter;
import io.github.oliviercailloux.y2018.j_voting.profiles.management.ProfileBuilder;

/**
 * This class is immutable. Represents an Incomplete Profile.
 */
public class ImmutableProfileI implements ProfileI {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImmutableProfileI.class.getName());
	protected Map<Voter, ? extends Preference> votes;

	public ImmutableProfileI(Map<Voter, ? extends Preference> votes) {
		LOGGER.debug("constructor:");
		Preconditions.checkNotNull(votes);
		this.votes = votes;
	}

	@Override
	public Preference getPreference(Voter v) {
		LOGGER.debug("getPreference:");
		Preconditions.checkNotNull(v);
		LOGGER.debug("parameter voter : {}", v);
		if (votes.containsKey(v)) {
			return votes.get(v);
		}
		throw new NoSuchElementException("Voter " + v + "is not in the map !");
	}

	@Override
	public int getMaxSizeOfPreference() {
		LOGGER.debug("getMaxSizeOfPreference");
		int maxSize = 0;
		Collection<? extends Preference> pref = votes.values();
		for (Preference p : pref) {
			if (maxSize < p.size()) {
				maxSize = p.size();
			}
		}
		LOGGER.debug("biggest Preference has size : {}", maxSize);
		return maxSize;
	}

	@Override
	public Map<Voter, ? extends Preference> getProfile() {
		LOGGER.debug("getProfile:");
		return votes;
	}

	// @Override
	/*
	 * public boolean contains(Voter v) { LOGGER.debug("contains:");
	 * Preconditions.checkNotNull(v); LOGGER.debug("parameter voter : {}",v);
	 * for(Voter voter : getAllVoters()) { if(v.equals(voter)) { return true; } }
	 * return false; }
	 */

	@Override
	public NavigableSet<Voter> getAllVoters() {
		LOGGER.debug("getAllVoters:");
		NavigableSet<Voter> keys = new TreeSet<>();
		for (Voter v : votes.keySet()) {
			keys.add(v);
		}
		LOGGER.debug("all voter : {}", keys);
		return keys;
	}

	@Override
	public int getNbVoters() {
		LOGGER.debug("getNbVoters:");
		return getAllVoters().size();
	}

	@Override
	public int getSumVoteCount() {
		LOGGER.debug("getSumCount:");
		return getAllVoters().size();
	}

	@Override
	public Set<Preference> getUniquePreferences() {
		LOGGER.debug("getUniquePreferences");
		Set<Preference> unique = new LinkedHashSet<>();
		for (Preference pref : votes.values()) {
			LOGGER.debug("next preference : {}", pref);
			unique.add(pref);
		}
		return unique;
	}

	@Override
	public int getNbUniquePreferences() {
		LOGGER.debug("getNbUniquePreferences:");
		return getUniquePreferences().size();
	}

	@Override
	public boolean isComplete() {
		LOGGER.debug("isComplete");
		Preference pref = votes.values().iterator().next();
		LOGGER.debug("first preferences :{}", pref);
		for (Preference p : votes.values()) {
			if (!p.hasSameAlternatives(pref)) {
				LOGGER.debug("Profile incomplete.");
				return false;
			}
		}
		LOGGER.debug("Profile is complete.");
		return true;
	}

	@Override
	public boolean isStrict() {
		LOGGER.debug("isStrict:");
		for (Preference p : votes.values()) {
			if (!p.isStrict()) {
				LOGGER.debug("non strict");
				return false;
			}
		}
		LOGGER.debug("strict");
		return true;
	}

	@Override
	public int getNbVoterForPreference(Preference p) {
		LOGGER.debug("getnbVoterByPreference:");
		Preconditions.checkNotNull(p);
		LOGGER.debug("parameter preference: {}", p);
		int nb = 0;
		for (Preference p1 : votes.values()) {
			if (p.equals(p1)) {
				nb++;
			}
		}
		LOGGER.debug("result: {}", nb);
		return nb;
	}

	@Override
	public boolean equals(Object o) {
		LOGGER.debug("equals:");
		Preconditions.checkNotNull(o);
		if (!(o instanceof ProfileI)) {
			return false;
		}
		ProfileI prof = (ImmutableProfileI) o;
		SortedSet<Voter> set1 = prof.getAllVoters();
		SortedSet<Voter> set2 = getAllVoters();
		if (set1.size() != set2.size()) {
			LOGGER.debug("false : not as many voters.");
			return false;
		}
		for (Voter v : set1) {
			if (!votes.containsKey(v)) {
				LOGGER.debug("false : at least a voter not in both profiles.");
				return false;
			}
			if (!prof.getPreference(v).equals(getPreference(v))) {
				LOGGER.debug("false : voter did not vote for the same preference.");
				return false;
			}
		}
		LOGGER.debug("true");
		return true;
	}

	@Override
	public int hashCode() {
		LOGGER.debug("hasCode:");
		return Objects.hash(votes);
	}

	@Override
	public ProfileI restrictProfile() {
		LOGGER.debug("StricterProfile : ");
		ProfileBuilder profileBuilder = new ProfileBuilder(this);
		if (isComplete()) {
			if (isStrict()) {
				LOGGER.debug("strict complete profile");
				return profileBuilder.createStrictProfile();
			}
			LOGGER.debug("non strict complete profile");
			return profileBuilder.createProfile();
		}
		if (isStrict()) {
			LOGGER.debug("strict incomplete profile");
			return profileBuilder.createStrictProfileI();
		}
		LOGGER.debug("non strict incomplete profile");
		return this;
	}

	/**
	 * 
	 * @param map
	 *            not <code> null </code>
	 * @return the map if and only if it represents a complete profile. If it is
	 *         incomplete, it throws an IllegalArgumentException.
	 */
	public static Map<Voter, ? extends Preference> checkCompleteMap(Map<Voter, ? extends Preference> map) {
		LOGGER.debug("checkCompleteMap:");
		Preconditions.checkNotNull(map);
		if (!new ImmutableProfileI(map).isComplete()) {
			throw new IllegalArgumentException("map is incomplete");
		}
		return map;
	}

	/**
	 * 
	 * @param map
	 *            not <code> null </code>
	 * @return the map if and only if it represents a strict profile. If it is not
	 *         strict, it throws an IllegalArgumentException.
	 */
	public static Map<Voter, ? extends Preference> checkStrictMap(Map<Voter, ? extends Preference> map) {
		LOGGER.debug("checkstrictMap:");
		Preconditions.checkNotNull(map);
		if (!new ImmutableProfileI(map).isStrict()) {
			throw new IllegalArgumentException("map is not strict");
		}
		return map;
	}

	@Override
	public int getNbAlternatives() {
		LOGGER.debug("getNbAlternatives");
		return getAlternatives().size();
	}

	@Override
	public Set<Alternative> getAlternatives() {
		LOGGER.debug("getAlternatives");
		Set<Alternative> set = new HashSet<>();
		for (Preference pref : getUniquePreferences()) {
			for (Alternative a : Preference.toAlternativeSet(pref.getPreferencesNonStrict())) {
				set.add(a);
			}
		}
		return set;
	}

	@Override
	public String getFormat() {
		LOGGER.debug("getFormat : ");
		if (isComplete()) {
			if (isStrict()) {
				LOGGER.debug("strict complete profile");
				return "soc";
			}
			LOGGER.debug("non strict complete profile");
			return "toc";
		}
		if (isStrict()) {
			LOGGER.debug("strict incomplete profile");
			return "soi";
		}
		LOGGER.debug("non strict incomplete profile");
		return "toi";
	}
}
