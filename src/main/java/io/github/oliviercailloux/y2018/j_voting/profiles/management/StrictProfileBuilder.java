package io.github.oliviercailloux.y2018.j_voting.profiles.management;

import io.github.oliviercailloux.y2018.j_voting.*;
import io.github.oliviercailloux.y2018.j_voting.profiles.*;
import java.util.HashMap;

import org.slf4j.*;
import com.google.common.base.Preconditions;

/**
 * 
 * This class builds a strict profile.
 *
 */
public class StrictProfileBuilder extends ProfileBuilder{
	private static final Logger LOGGER = LoggerFactory.getLogger(StrictProfileBuilder.class.getName());
	
	public StrictProfileBuilder() {
		LOGGER.debug("constructor empty:");
		votes = new HashMap<>();
	}
	
	/**
	 * 
	 * @param prof a StrictProfileI not <code> null </code>
	 * 
	 * initiates a ProfileBuilder from a StrictProfile.
	 * 	 */
	public StrictProfileBuilder(StrictProfileI prof) {
		LOGGER.debug("constructor ProfileI:");
		Preconditions.checkNotNull(prof);
		LOGGER.debug("parameter prof : {}",prof);
		votes = castMapExtendsToRegularVoterPref(prof.getProfile());
	}	
	
	/**
	 * 
	 * @param v not <code> null </code>
	 * @param pref not <code> null </code>
	 * 
	 * adds the preference pref for the voter v in the map. If the preference isn't strict, it throws an IllegalArgumentException.
	 */
	@Override
	public void addVote(Voter v, Preference pref) {
		LOGGER.debug("addProfile:");
		Preconditions.checkNotNull(v);
		Preconditions.checkNotNull(pref);
		LOGGER.debug("parameters: voter {} pref {}",v,pref);
		if(!pref.isStrict()) {
			throw new IllegalArgumentException("The preference must be strict.");
		}
		votes.put(v,pref);
	}
}
