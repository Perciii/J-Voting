package io.github.oliviercailloux.y2018.j_voting.profiles;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import io.github.oliviercailloux.y2018.j_voting.Alternative;
import io.github.oliviercailloux.y2018.j_voting.Preference;
import io.github.oliviercailloux.y2018.j_voting.Voter;

/**
 * This class is immutable. Represents a Strict Complete Profile.
 */
public class ImmutableStrictProfile extends ImmutableStrictProfileI implements StrictProfile {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImmutableStrictProfile.class.getName());

	public ImmutableStrictProfile(Map<Voter, ? extends Preference> map) {
		super(checkCompleteMap(map));
	}

	@Override
	public int getNbAlternatives() {
		LOGGER.debug("getNbAlternatives:");
		return getAlternatives().size();
	}

	@Override
	public Set<Alternative> getAlternatives() {
		LOGGER.debug("getAlternatives :");
		Preference p = votes.values().iterator().next();
		return Preference.toAlternativeSet(p.getPreferencesNonStrict());
	}

	/**
	 * Get a List of each ith Alternative of each Voter in the profile
	 * 
	 * @param i
	 *            not <code> null</code> the rank of the Alternatives to get
	 * @return a List of Alternatives
	 */
	@Override
	public List<Alternative> getIthAlternatives(int i) {
		LOGGER.debug("getIthAlternatives :");
		Preconditions.checkNotNull(i);
		NavigableSet<Voter> voters = getAllVoters();
		List<Alternative> listIthAlternatives = new ArrayList<>();
		for (Voter v : voters) {
			listIthAlternatives.add(getPreference(v).getAlternative(i));
		}
		return listIthAlternatives;
	}

	/**
	 * Get a List of each ith Alternative of each unique Preference in the profile
	 * 
	 * @param i
	 *            not <code> null</code> the rank of the Alternatives to get
	 * @return a List of Alternatives
	 */
	@Override
	public List<Alternative> getIthAlternativesOfUniquePreferences(int i) {
		LOGGER.debug("getIthAlternativesOfUniquePreferences :");
		Preconditions.checkNotNull(i);
		List<Alternative> listIthAlternatives = new ArrayList<>();

		for (Preference p : getUniquePreferences()) {
			listIthAlternatives.add(p.getAlternative(i));
		}
		return listIthAlternatives;
	}

	@Override
	public void writeToSOC(OutputStream output) throws IOException {
		LOGGER.debug("writeToSOC :");
		Preconditions.checkNotNull(output);
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(output))) {
			String soc = "";
			soc += getNbAlternatives() + "\n";
			for (Alternative alter : getAlternatives()) {
				soc += alter.getId() + "\n";
			}
			soc += getNbVoters() + "," + getSumVoteCount() + "," + getNbUniquePreferences() + "\n";
			for (Preference pref : this.getUniquePreferences()) {
				soc += getNbVoterForPreference(pref);

				for (Alternative a : Preference.toAlternativeSet(pref.getPreferencesNonStrict())) {
					soc = soc + "," + a;
				}
				soc = soc + "\n";
			}
			writer.append(soc);
			writer.close();
		}
	}

}
