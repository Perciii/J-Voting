package io.github.oliviercailloux.y2018.j_voting.profiles;

import io.github.oliviercailloux.y2018.j_voting.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
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
	
	@Override
	public List<String> getIthAlternativesStrings(int i){
		LOGGER.debug("getIthAlternativesStrings");
		if(i > getMaxSizeOfPreference()) {
			throw new IllegalArgumentException("The given index is out of bound.");
		}
		List<String> list = new ArrayList<>();
		for(Voter v : getAllVoters()) {
			StrictPreference p = getPreference(v);
			LOGGER.debug("the voter {} votes for the preference {}",v,p);
			if(i >= p.size()) {
				list.add("");
				LOGGER.debug("the preference is smaller than the given index");
			}
			else {
				list.add(p.getAlternative(i).toString());
				LOGGER.debug("the ith alternative is {}",p.getAlternative(i));
			}
		}
		return list;
	}
	
	@Override
	public void writeToSOI(OutputStream output) throws IOException {
		LOGGER.debug("writeToSOC :");
		Preconditions.checkNotNull(output);
		try(Writer writer = new BufferedWriter(new OutputStreamWriter(output))){
			String soc = "";
			soc += getNbAlternatives() + "\n";
			for(Alternative alter : getAlternatives()) {
				soc += alter.getId() + "\n";
			}
			soc += getNbVoters() + "," + getSumVoteCount() + "," + getNbUniquePreferences() + "\n";
			for(Preference pref : this.getUniquePreferences()) {
				soc += getNbVoterForPreference(pref);
				
				for(Alternative a : Preference.toAlternativeSet(pref.getPreferencesNonStrict())) {
					soc = soc + "," + a;
				}
				soc = soc + "\n";
			}
			writer.append(soc);
			writer.close();
		}
	}
}
