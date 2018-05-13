package io.github.oliviercailloux.y2018.j_voting;

public interface SocialWelfareFunction {
	
	/**
	 * 
	 * @param profile not null
	 * @return a StrictPreference with the society's preference from the profile. This StrictPreference cannot be empty.
	 */
	 public StrictPreference getSocietyStrictPreference(StrictProfile profile);
}
