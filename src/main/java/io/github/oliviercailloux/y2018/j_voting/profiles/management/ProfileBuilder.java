package io.github.oliviercailloux.y2018.j_voting.profiles.management;

import org.slf4j.*;
import com.google.common.base.Preconditions;
import io.github.oliviercailloux.y2018.j_voting.*;
import io.github.oliviercailloux.y2018.j_voting.profiles.*;
import java.util.*;

/**
 * 
 * This class is a builder for profiles.
 *
 */
public class ProfileBuilder {

	private static Logger LOGGER = LoggerFactory.getLogger(ProfileBuilder.class.getName());
	protected Map<Voter,Preference> votes;
	protected int nextVoterId = 1;
	
	public ProfileBuilder() {
		LOGGER.debug("constructor empty:");
		votes = new HashMap<>();
	}
	
	/**
	 * 
	 * @param prof not <code> null </code>
	 * 
	 * initiates a ProfileBuilder from a Profile.
	 */
	public ProfileBuilder(ProfileI prof) {
		LOGGER.debug("constructor ProfileI:");
		Preconditions.checkNotNull(prof);
		LOGGER.debug("parameter prof : {}",prof);
		votes = prof.getProfile();
	}
	
	/**
	 * 
	 * @param v not <code> null </code>
	 * @param pref not <code> null </code>
	 * 
	 * adds the preference pref for the voter v in the map.
	 */
	public void addProfile(Voter v, Preference pref) {
		LOGGER.debug("addProfile:");
		Preconditions.checkNotNull(v);
		Preconditions.checkNotNull(pref);
		LOGGER.debug("parameters: voter {} pref {}",v,pref);
		votes.put(v,pref);
	}
	
	/**
	 * 
	 * @return a ProfileI created from the builder.
	 */
	public ProfileI createProfileI() {
		LOGGER.debug("createProfileI:");
		return new ImmutableProfileI(votes);
	}
	
	/**
	 * 
	 * @return a Profile created from the builder if it is complete, otherwise throws an exception.
	 */
	public Profile createProfile() {
		LOGGER.debug("createProfile:");
		if(!createProfileI().isComplete()) {
			throw new IllegalArgumentException("The built profile is not complete.");
		}
		return new ImmutableProfile(votes);
	}
	
	/**
	 * 
	 * @return a StrictProfileI created from the builder if it is strict, otherwise throws an exception.
	 */
	public StrictProfileI createStrictProfileI() {
		LOGGER.debug("createStrictProfileI:");
		if(!createProfileI().isStrict()) {
			throw new IllegalArgumentException("The built profile is not strict.");
		}
		return new ImmutableStrictProfileI(ImmutableStrictProfileI.mapNonStrictToStrict(votes));
	}
	
	/**
	 * 
	 * @return a StrictProfile created from the builder if it is strict and complete, otherwise throws an exception.
	 */
	public StrictProfile createStrictProfile() {
		if(!createProfileI().isComplete()) {
			throw new IllegalArgumentException("The built profile is not complete.");
		}
		if(!createProfileI().isStrict()) {
			throw new IllegalArgumentException("The built profile is not strict.");
		}
		return new ImmutableStrictProfile(ImmutableStrictProfileI.mapNonStrictToStrict(votes));
	}
}
