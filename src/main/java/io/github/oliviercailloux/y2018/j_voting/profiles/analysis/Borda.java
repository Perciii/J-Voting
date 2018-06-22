package io.github.oliviercailloux.y2018.j_voting.profiles.analysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import io.github.oliviercailloux.y2018.j_voting.Alternative;
import io.github.oliviercailloux.y2018.j_voting.Preference;
import io.github.oliviercailloux.y2018.j_voting.Voter;
import io.github.oliviercailloux.y2018.j_voting.profiles.ImmutableProfileI;
import io.github.oliviercailloux.y2018.j_voting.profiles.ProfileI;

public class Borda implements SocialWelfareFunction {

	private static final Logger LOGGER = LoggerFactory.getLogger(Borda.class.getName());
	private Multiset<Alternative> scores;

	public Borda(Multiset<Alternative> tempscores) {
		LOGGER.debug("Borda");
		scores = tempscores;
	}

	public Borda() {
		LOGGER.debug("emptyBorda");
		scores = HashMultiset.create();
	}

	/**
	 * 
	 * @return the multiset of alternatives of the object Borda.
	 */
	public Multiset<Alternative> getMultiSet() {
		LOGGER.debug("getMultiSet:");
		return scores;
	}

	/**
	 * @param profile
	 *            a ProfileI <code>not null</code>
	 * @return a Preference with the alternatives sorted
	 */
	@Override
	public Preference getSocietyPreference(ImmutableProfileI profile) {
		LOGGER.debug("getSocietyStrictPreference");
		Preconditions.checkNotNull(profile);
		LOGGER.debug("parameter SProfile : {}", profile);
		setScores(profile);
		LOGGER.debug("return AScores : {}", scores);
		List<Set<Alternative>> al = new ArrayList<>();
		Set<Alternative> s = new HashSet<>();
		Multiset<Alternative> tempscores = scores;
		while (!tempscores.isEmpty()) {
			s = getMax(tempscores);
			al.add(s);
			for (Alternative a : s) {
				tempscores.remove(a, tempscores.count(a));
			}
		}
		Preference pref = new Preference(al);
		LOGGER.debug("return AScores : {}", pref);
		return pref;
	}

	/**
	 * assigns a score to each alternative of a StrictPreference
	 * 
	 * @param sPref
	 *            a StrictPreference <code>not null</code> adds the scores of the
	 *            preference to the multiset for the alternatives in this
	 *            StrictPreference
	 */
	public void setScores(Preference pref) {
		LOGGER.debug("getScorePref");
		Preconditions.checkNotNull(pref);
		LOGGER.debug("parameter SPref : {}", pref);
		int size = pref.getPreferencesNonStrict().size();
		for (Alternative a : Preference.toAlternativeSet(pref.getPreferencesNonStrict())) {
			scores.add(a, size - pref.getAlternativeRank(a) + 1);
		}
		LOGGER.debug("return score : {}", scores);
	}

	/**
	 * 
	 * @param profile
	 *            a ProfileI <code>not null</code> sets the scores in the multiset
	 *            for the profile (if an alternative has a score of 3, it ill appear
	 *            three times in the multiset)
	 */
	public void setScores(ProfileI profile) {
		LOGGER.debug("getScoreProf");
		Preconditions.checkNotNull(profile);
		LOGGER.debug("parameter SProfile : {}", profile);
		Iterable<Voter> allVoters = profile.getAllVoters();
		for (Voter v : allVoters) {
			setScores(profile.getPreference(v));
		}
		LOGGER.debug("return scores : {}", scores);
	}

	/**
	 * @param tempscores
	 *            a multiset <code>not null</code>
	 * @return the alternatives with the maximum score in the multiset
	 */
	public Set<Alternative> getMax(Multiset<Alternative> tempscores) {
		LOGGER.debug("getMax");
		Preconditions.checkNotNull(tempscores);
		Set<Alternative> set = new HashSet<>();
		Iterable<Alternative> alternativesList = tempscores.elementSet();
		Alternative alternativeMax = new Alternative(0);

		boolean first = true;

		for (Alternative a : alternativesList) {
			if (first) {
				alternativeMax = a;
				set.add(a);
				first = false;
			} else {
				if (tempscores.count(a) > tempscores.count(alternativeMax)) {
					alternativeMax = a;
					set = new HashSet<>();
					set.add(a);
				}
				if (tempscores.count(a) == tempscores.count(alternativeMax)) {
					set.add(a);
				}
			}
		}
		LOGGER.debug("Max : {} ", set);
		return set;
	}

	@Override
	public int hashCode() {
		LOGGER.debug("hashCode");
		return Objects.hash(scores);
	}

	/**
	 * @param mset1
	 *            <code>not null</code>
	 * @param mset2<code>not null</code>
	 * @return true if the multisets have the same alternatives with the same scores
	 */
	@Override
	public boolean equals(Object o1) {
		LOGGER.debug("equals");
		Preconditions.checkNotNull(o1);
		if (!(o1 instanceof Borda)) {
			LOGGER.debug("returns false");
			return false;
		}
		Borda borda = (Borda) o1;
		Multiset<Alternative> mset1 = borda.scores;
		Multiset<Alternative> mset2 = this.scores;
		Iterable<Alternative> alternativesList = mset1.elementSet();

		for (Alternative a : alternativesList) {
			if (!mset2.contains(a)) {
				LOGGER.debug("not same alternatives -> false");
				return false;
			}

			if (!(mset2.count(a) == mset1.count(a))) {
				LOGGER.debug("not same scores -> false");
				return false;
			}
			mset2.remove(a, mset2.count(a));

		}
		if (mset2.size() != 0) {
			LOGGER.debug("not same alternatives -> false");
			return false;
		}
		LOGGER.debug("returns true");
		return true;
	}
}
