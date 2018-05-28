package io.github.oliviercailloux.y2018.j_voting.profiles.analysis;

import org.slf4j.*;
import com.google.common.base.Preconditions;

import io.github.oliviercailloux.y2018.j_voting.Preference;
import io.github.oliviercailloux.y2018.j_voting.Voter;
import io.github.oliviercailloux.y2018.j_voting.profiles.ProfileI;

import java.util.*;

/**
 * 
 * This class provides a result for an election that is necessarily the preference of the dictator (a Voter).
 *
 */
public class Dictator implements SocialWelfareFunction{

	private Voter Dictator;
	private static Logger LOGGER = LoggerFactory.getLogger(Borda.class.getName());	

	public Dictator (Voter v){
		LOGGER.debug("Dictator");
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

	
	public Voter getDictator(){
		LOGGER.debug("getDictator");
		return Dictator;
	}

	@Override
	public int hashCode() {
		LOGGER.debug("hashCode");
		return Objects.hash(Dictator);
	}

	@Override
	public boolean equals(Object o1){
		LOGGER.debug("equals");
		Preconditions.checkNotNull(o1);
		if (!(o1 instanceof Dictator)){
			LOGGER.debug("returns false");
			return false;
		}
		Dictator d1 = (Dictator) o1;
		if ((d1.getDictator()).equals(this.Dictator)){
			LOGGER.debug("returns true");
			return true;
		}
		LOGGER.debug("returns false");
		return false;
	}
	
	
	/***
	 * 
	 * @param o1 : a voter
	 * @return true if o1 is the dictator
	 */
	
	public boolean equalsVoter(Object o1){
		LOGGER.debug("equalsVoter");
		Preconditions.checkNotNull(o1);
		if (!(o1 instanceof Voter)){
			LOGGER.debug("returns false");
			return false;
		}
		Voter v1 = (Voter) o1;
		if (v1.equals(this.Dictator)){
			LOGGER.debug("returns true");
			return true;
		}
		LOGGER.debug("returns false");
		return false;
	}
	
}
