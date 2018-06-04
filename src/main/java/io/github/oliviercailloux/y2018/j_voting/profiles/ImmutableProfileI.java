package io.github.oliviercailloux.y2018.j_voting.profiles;

import io.github.oliviercailloux.y2018.j_voting.*;
import java.util.*;
import org.slf4j.*;
import com.google.common.base.Preconditions;

/**
 * This class is immutable.
 * Represents an Incomplete Profile.
 */
public class ImmutableProfileI implements ProfileI{

	private static final Logger LOGGER = LoggerFactory.getLogger(ImmutableProfileI.class.getName());
	protected Map<Voter,Preference> votes;
	
	public ImmutableProfileI(Map<Voter,Preference> votes) {
		LOGGER.debug("constructor:");
		Preconditions.checkNotNull(votes);
		this.votes = votes;
	}
	
	@Override
	public Preference getPreference(Voter v) {
		LOGGER.debug("getPreference:");
		Preconditions.checkNotNull(v);
		LOGGER.debug("parameter voter : {}",v);
		Set<Map.Entry<Voter,Preference>> mapping = votes.entrySet();
		for(Map.Entry<Voter,Preference> vote : mapping) {
			if(vote.getKey().equals(v)) {
				LOGGER.debug("return {}", vote.getValue());
				return vote.getValue();
			}
		}
		throw new NoSuchElementException("Voter " + v + "is not in the map !");
	}
	
	@Override
	public int getMaxSizeOfPreference() {
		int maxSize = 0;
		Set<Map.Entry<Voter,Preference>> mapping = votes.entrySet();
		for(Map.Entry<Voter,Preference> vote : mapping) {
			if(vote.getValue().size() > maxSize)
				maxSize = vote.getValue().size();
		}
		return maxSize;
	}

	@Override
	public Map<Voter, Preference> getProfile() {
		LOGGER.debug("getProfile:");
		return votes;
	}

	@Override
	public boolean contains(Voter v) {
		LOGGER.debug("contains:");
		Preconditions.checkNotNull(v);
		LOGGER.debug("parameter voter : {}",v);
		for(Voter voter : getAllVoters()) {
			if(v.equals(voter)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public NavigableSet<Voter> getAllVoters() {
		LOGGER.debug("getAllVoters:");
		NavigableSet<Voter> keys = new TreeSet<>();
		for(Voter v : votes.keySet()) {
			keys.add(v);
		}
		LOGGER.debug("all voter : {}",keys);
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
		Set<Preference> unique = new HashSet<>();
		for(Preference pref : votes.values()) {
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
		for(Preference p : votes.values()) {
			if(!p.hasSameAlternatives(pref)) {
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
		for(Preference p : votes.values()) {
			if(!p.isStrict()) {
				LOGGER.debug("non strict");
				return false;
			}
		}
		LOGGER.debug("strict");
		return true;
	}

	@Override
	public int getNbVoterByPreference(Preference p) {
		LOGGER.debug("getnbVoterByPreference:");
		Preconditions.checkNotNull(p);
		LOGGER.debug("parameter preference: {}",p);
		int nb = 0;
		for(Preference p1 : votes.values()) {
			if(p.equals(p1)) {
				nb++;
			}
		}
		LOGGER.debug("result: {}",nb);
		return nb;
	}
	
	@Override
	public boolean equals(Object o) {
		LOGGER.debug("equals:");
		Preconditions.checkNotNull(o);
		if(!(o instanceof ProfileI)) {
			return false;
		}
		ProfileI prof = (ImmutableProfileI) o;
		SortedSet<Voter> set1 = prof.getAllVoters();
		SortedSet<Voter> set2 = getAllVoters();
		if(set1.size() != set2.size()) {
			LOGGER.debug("false : not as many voters.");
			return false;
		}
		for(Voter v : set1) {
			if(!contains(v)) {
				LOGGER.debug("false : at least a voter not in both profiles.");
				return false;
			}
			if(!prof.getPreference(v).equals(getPreference(v))) {
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
		if(isComplete()) {
			if(isStrict()) {
				LOGGER.debug("strict complete profile");
				ImmutableStrictProfile isp = (ImmutableStrictProfile) this;
				return isp;
			}
			LOGGER.debug("non strict complete profile");
			ImmutableProfile ip = (ImmutableProfile) this;
			return ip;
		}
		if(isStrict()) {
			LOGGER.debug("strict incomplete profile");
			ImmutableStrictProfileI ispi = (ImmutableStrictProfileI) this;
			return ispi;
		}
		LOGGER.debug("non strict incomplete profile");
		return this;
	}

}
