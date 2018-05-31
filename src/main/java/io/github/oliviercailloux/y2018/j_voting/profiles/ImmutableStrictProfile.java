package io.github.oliviercailloux.y2018.j_voting.profiles;

import org.slf4j.*;
import com.google.common.base.Preconditions;
import io.github.oliviercailloux.y2018.j_voting.*;
import java.io.*;
import java.util.*;

/**
 * This class is immutable.
 * Represents a Strict Complete Profile.
 */
public class ImmutableStrictProfile extends ImmutableStrictProfileI implements StrictProfile{

	private static final Logger LOGGER = LoggerFactory.getLogger(ImmutableStrictProfile.class.getName());
	
	public ImmutableStrictProfile(Map<Voter,StrictPreference> map) {
		super(map);
	}

	@Override
	public int getNbAlternatives() {
		LOGGER.debug("getNbAlternatives:");
		return getAlternatives().size();
	}

	@Override
	public Set<Alternative> getAlternatives() {
		LOGGER.debug("getAlternatives:");
		Preference p = votes.values().iterator().next();
		return Preference.toAlternativeSet(p.getPreferencesNonStrict());
	}

	@Override
	public void writeToSOC(OutputStream output) throws IOException {
		LOGGER.debug("writeToSOC:");
		Preconditions.checkNotNull(output);
		try(Writer writer = new BufferedWriter(new OutputStreamWriter(output))){
			String soc = "";
			soc += getNbAlternatives() + "\n";
			for(Alternative alter : getAlternatives()) {
				soc += alter.getId() + "\n";
			}
			soc += getNbVoters() + ","+getSumVoteCount() + "," + getNbUniquePreferences() + "\n";
			for(Preference pref : this.getUniquePreferences()) {
				soc += getNbVoterByPreference(pref);
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
