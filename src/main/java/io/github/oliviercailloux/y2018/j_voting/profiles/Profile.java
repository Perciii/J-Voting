package io.github.oliviercailloux.y2018.j_voting.profiles;

/**
 * 
 * A Profile represents a complete profile. The preferences can be strict or
 * not. The preferences are all about the same alternatives exactly.
 *
 */
public interface Profile extends ProfileI {

	@Override
	public default boolean isComplete() {
		return true;
	}
}
