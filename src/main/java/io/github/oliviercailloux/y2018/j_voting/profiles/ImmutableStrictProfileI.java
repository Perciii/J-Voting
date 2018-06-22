package io.github.oliviercailloux.y2018.j_voting.profiles;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import io.github.oliviercailloux.y2018.j_voting.Alternative;
import io.github.oliviercailloux.y2018.j_voting.Preference;
import io.github.oliviercailloux.y2018.j_voting.StrictPreference;
import io.github.oliviercailloux.y2018.j_voting.Voter;

/**
 * This class is immutable. Represents a Strict Incomplete Profile.
 */
public class ImmutableStrictProfileI extends ImmutableProfileI implements StrictProfileI {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImmutableStrictProfileI.class.getName());

	public ImmutableStrictProfileI(Map<Voter, ? extends Preference> map) {
		super(checkStrictMap(map));
	}

	@Override
	public StrictPreference getPreference(Voter v) {
		LOGGER.debug("getPreference:");
		Preconditions.checkNotNull(v);
		if (!votes.containsKey(v)) {
			throw new NoSuchElementException("Voter " + v + "is not in the map !");
		}
		return votes.get(v).toStrictPreference();
	}

	@Override
	public List<String> getIthAlternativesAsStrings(int i) {
		LOGGER.debug("getIthAlternativesStrings");
		if (i > getMaxSizeOfPreference()) {
			throw new IndexOutOfBoundsException("The given index is out of bound.");
		}
		List<String> list = new ArrayList<>();
		for (Voter v : getAllVoters()) {
			StrictPreference p = getPreference(v);
			LOGGER.debug("the voter {} votes for the preference {}", v, p);
			if (i >= p.size()) {
				list.add("");
				LOGGER.debug("the preference is smaller than the given index");
			} else {
				list.add(p.getAlternative(i).toString());
				LOGGER.debug("the ith alternative is {}", p.getAlternative(i));
			}
		}
		return list;
	}

	@Override
	public List<String> getIthAlternativesOfUniquePrefAsString(int i) {
		LOGGER.debug("getIthAlternativesOfUniquePrefAsString");
		Preconditions.checkNotNull(i);
		List<String> list = new ArrayList<>();
		for (Preference p : getUniquePreferences()) {
			String alter = "";
			if (i < p.size()) {
				alter = p.getAlternative(i).toString();
				LOGGER.debug("the ith alternative is {}", p.getAlternative(i));
			}
			list.add(alter);
		}
		return list;
	}

	@Override
	public void writeToSOI(OutputStream output) throws IOException {
		LOGGER.debug("writeToSOI :");
		Preconditions.checkNotNull(output);
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(output))) {
			String soi = "";
			soi += getNbAlternatives() + "\n";
			for (Alternative alter : getAlternatives()) {
				soi += alter.getId() + "\n";
			}
			soi += getNbVoters() + "," + getSumVoteCount() + "," + getNbUniquePreferences() + "\n";
			for (Preference pref : this.getUniquePreferences()) {
				soi += getNbVoterForPreference(pref);

				for (Alternative a : Preference.toAlternativeSet(pref.getPreferencesNonStrict())) {
					soi = soi + "," + a;
				}
				soi = soi + "\n";
			}
			writer.append(soi);
			writer.close();
		}
	}
}
