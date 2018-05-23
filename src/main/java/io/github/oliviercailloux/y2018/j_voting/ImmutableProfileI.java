package io.github.oliviercailloux.y2018.j_voting;

import java.util.*;
import org.slf4j.*;
import com.google.common.base.Preconditions;

public class ImmutableProfileI implements ProfileI{

	private static Logger LOGGER = LoggerFactory.getLogger(ImmutableProfileI.class.getName());
	private Map<Voter,Preference> votes;
	
	public ImmutableProfileI(Map<Voter,Preference> votes) {
		LOGGER.debug("constructor:");
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
	public SortedSet<Voter> getAllVoters() {
		SortedSet<Voter> keys = new TreeSet<>();
		for(Voter v : votes.keySet()) {
			keys.add(v);
		}
		return keys;
	}

	@Override
	public int getNbVoters() {
		return getAllVoters().size();
	}

	@Override
	public int getSumVoteCount() {
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

}
