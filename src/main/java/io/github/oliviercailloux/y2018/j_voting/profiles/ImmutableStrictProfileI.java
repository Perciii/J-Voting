package io.github.oliviercailloux.y2018.j_voting.profiles;

import io.github.oliviercailloux.y2018.j_voting.*;
import java.util.*;
import org.slf4j.*;
import com.google.common.base.Preconditions;

public class ImmutableStrictProfileI extends ImmutableProfileI implements StrictProfileI{

	private static Logger LOGGER = LoggerFactory.getLogger(ImmutableStrictProfileI.class.getName());
	
	
	public ImmutableStrictProfileI(Map<Voter, StrictPreference> map) {
		super(mapStrictToNonStrict(map));
	}
	
	/**
	 * 
	 * @param map not <code> null</code>
	 * @return a mapping of the voters and their strictPreference as a Preference.
	 */
	public static Map<Voter,Preference> mapStrictToNonStrict(Map<Voter,StrictPreference> map){
		LOGGER.debug("mapStrictToNonStrict:");
		Preconditions.checkNotNull(map);
		Map<Voter,Preference> newmap = new HashMap<>();
		Set<Map.Entry<Voter,StrictPreference>> mapping = map.entrySet();
		for(Map.Entry<Voter,StrictPreference> vote : mapping) {
			newmap.put(vote.getKey(),vote.getValue());
		}
		return newmap;
	}
	
	/**
	 * 
	 * @param map not <code> null</code>
	 * @return a mapping of the voters and their Preference as a StrictPreference if possible.
	 */
	public static Map<Voter,StrictPreference> mapNonStrictToStrict(Map<Voter,Preference> map){
		LOGGER.debug("mapNonStrictToStrict:");
		Preconditions.checkNotNull(map);
		Map<Voter,StrictPreference> newmap = new HashMap<>();
		Set<Map.Entry<Voter,Preference>> mapping = map.entrySet();
		for(Map.Entry<Voter,Preference> vote : mapping) {
			if(!vote.getValue().isStrict()) {
				throw new IllegalArgumentException("The preferences are not all strict.");
			}
			newmap.put(vote.getKey(),new StrictPreference(StrictPreference.listSetAlternativeToList(vote.getValue().getPreferencesNonStrict())));
		}
		return newmap;
	}
	
	@Override
	public StrictPreference getPreference(Voter v) {
		LOGGER.debug("getPreference:");
		Preconditions.checkNotNull(v);
		boolean voterInMap = false;
		List<Preference> prefs = new ArrayList<>();
		Set<Map.Entry<Voter,Preference>> mapping = votes.entrySet();
		for(Map.Entry<Voter,Preference> vote : mapping) {
			if(vote.getKey().equals(v)) {
				LOGGER.debug("return {}", vote.getValue());
				prefs.add(vote.getValue());
				voterInMap = true;
			}
		}
		if(!voterInMap || prefs.size() == 0) {
			throw new NoSuchElementException("Voter " + v + "is not in the map !");
		}
		return new StrictPreference(StrictPreference.listSetAlternativeToList(prefs.get(0).getPreferencesNonStrict()));
	}
}
