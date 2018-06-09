package io.github.oliviercailloux.y2018.j_voting.profiles;

import io.github.oliviercailloux.y2018.j_voting.*;
import java.util.*;
import org.slf4j.*;
import com.google.common.base.Preconditions;

/**
 * This class is immutable.
 * Represents a Strict Incomplete Profile.
 */
public class ImmutableStrictProfileI extends ImmutableProfileI implements StrictProfileI{

	private static final Logger LOGGER = LoggerFactory.getLogger(ImmutableStrictProfileI.class.getName());
	
	
	public ImmutableStrictProfileI(Map<Voter,? extends Preference> map) {
		super(checkStrictMap(map));
	}
	
	@Override
	public StrictPreference getPreference(Voter v) {
		LOGGER.debug("getPreference:");
		Preconditions.checkNotNull(v);
		if(!votes.containsKey(v)) {
			throw new NoSuchElementException("Voter " + v + "is not in the map !");
		}
		return votes.get(v).toStrictPreference();
	}
}
