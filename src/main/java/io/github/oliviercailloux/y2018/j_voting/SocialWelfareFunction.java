package io.github.oliviercailloux.y2018.j_voting;

public interface SocialWelfareFunction {
	
	/**
	 * 
	 * @param profile
	 * @return a StrictPreference with the society's preference from a profile
	 */
	 StrictPreference getSocietyStrictPreference(StrictProfile profile);
}
