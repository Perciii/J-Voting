package io.github.oliviercailloux.y2018.j_voting;

import org.slf4j.*;
import com.google.common.base.Preconditions;
import java.util.*;

/**
 * 
 * This class is a builder for profiles.
 *
 */
public class ProfileBuilder {

	private static Logger LOGGER = LoggerFactory.getLogger(ProfileBuilder.class.getName());
	protected Map<Voter,Preference> votes;
	
	public ProfileBuilder() {
		votes = new HashMap<Voter,Preference>();
	}
	
	public ProfileBuilder(ProfileI prof) {
		Preconditions.checkNotNull(prof);
		votes = prof.getProfile();
	}
	
	public void addProfile(Voter v, Preference pref) {
		Preconditions.checkNotNull(v);
		Preconditions.checkNotNull(pref);
		votes.put(v,pref);
	}
	
	public ProfileI createProfileI() {
		return new ImmutableProfileI(votes);
	}
	
	public Profile createProfile() {
		if(!createProfileI().isComplete()) {
			throw new IllegalArgumentException("The built profile is not complete.");
		}
		return new ImmutableProfile(votes);
	}
	
	public StrictProfileI createStrictProfileI() {
		if(!createProfileI().isStrict()) {
			throw new IllegalArgumentException("The built profile is not strict.");
		}
		return new ImmutableStrictProfileI(ImmutableStrictProfile.mapNonStrictToStrict(votes));
	}
	
	public StrictProfile createStrictProfile() {
		if(!createProfileI().isComplete()) {
			throw new IllegalArgumentException("The built profile is not complete.");
		}
		if(!createProfileI().isStrict()) {
			throw new IllegalArgumentException("The built profile is not strict.");
		}
		return new ImmutableStrictProfile(ImmutableStrictProfile.mapNonStrictToStrict(votes));
	}
}
