package io.github.oliviercailloux.y2018.j_voting;

import org.slf4j.*;
import com.google.common.base.Preconditions;

public class Dictator implements SocialWelfareFunction{
	
	private Voter Dictator;
	private static Logger LOGGER = LoggerFactory.getLogger(Borda.class.getName());	
	
	public Dictator (Voter v){
		Preconditions.checkNotNull(v);
		Dictator = v;
	}
	
	/***
	 * 
	 * @param profile
	 * @return the dictator's preference
	 */
	@Override
	public Preference getSocietyPreference(ProfileI profile) {
		LOGGER.debug("getSocietyStrictPreference");
		Preconditions.checkNotNull(profile);
		Preconditions.checkArgument(profile.contains(Dictator));
		LOGGER.debug("parameter profile : {}", profile);
		LOGGER.debug("Dictator : {}", Dictator);
		LOGGER.debug("return preference : {}", profile.getPreference(Dictator));
		return profile.getPreference(Dictator);
	}
}
