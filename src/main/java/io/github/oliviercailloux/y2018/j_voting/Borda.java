package io.github.oliviercailloux.y2018.j_voting;


import java.util.*;
import org.slf4j.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.*;

public class Borda implements SocialWelfareFunction{

	private static Logger LOGGER = LoggerFactory.getLogger(Borda.class.getName());	
	private HashMultiset<Alternative> scores;
	
	/**
	 * @param profile a ProfileI <code>not null</code>
	 * @return a Preference with the alternatives sorted
	 */
	@Override
	public Preference getSocietyStrictPreference(ProfileI profile){
		LOGGER.debug("getSocietyStrictPreference");
		Preconditions.checkNotNull(profile);
		LOGGER.debug("parameter SProfile : {}", profile);
		
		getScores(profile);
		
		LOGGER.debug("return AScores : {}", scores);
		
		//int size = scores.elementSet().size();
		List<Set<Alternative>> al = new ArrayList<>();
		Set<Alternative> s = new HashSet<>();
		HashMultiset<Alternative> tempscores = scores;
		while(!tempscores.isEmpty()) {
			s = getMax(tempscores);
			al.add(s);
			for(Alternative a : s) {
				tempscores.remove(a,tempscores.count(a));
			}
		}
		/*
		Alternative alMax;
		for(int i=0 ; i<size ; i++){
			alMax = getMax(tempscores);
			al.add(alMax);
			tempscores.remove(alMax,tempscores.count(alMax));
		}*/
		
		Preference pref = new Preference(al);
		
		LOGGER.debug("return AScores : {}", pref);
		return pref;
	}


	/**
	 * assigns a score to each alternative of a StrictPreference
	 * @param sPref a StrictPreference <code>not null</code>
	 * @return a multiset for the alternaitves in this StrictPreference
	 */
	public void getScores(Preference pref){
		LOGGER.debug("getScorePref");
		Preconditions.checkNotNull(pref);
		LOGGER.debug("parameter SPref : {}", pref);
		int size = pref.size();
		for(Alternative a : Preference.toAlternativeSet(pref.getPreferencesNonStrict())){
			scores.add(a,size - pref.getAlternativeRank(a));
		}
		LOGGER.debug("return score : {}", scores);
	}


	/**
	 * 
	 * @param profile a ProfileI <code>not null</code>
	 * @return unsortedScores a HashMultiset for the alternatives of the profile
	 */
	public void getScores(ProfileI profile){
		LOGGER.debug("getScoreProf");
		Preconditions.checkNotNull(profile);
		LOGGER.debug("parameter SProfile : {}", profile);
		Iterable<Voter> allVoters  = profile.getAllVoters();
		Set<Alternative> alternatives;
		int size = 0;
		
		for(Voter v : allVoters){
			alternatives = Preference.toAlternativeSet(profile.getPreference(v).getPreferencesNonStrict());
			
			for(Alternative a : alternatives){
				size = alternatives.size();
				scores.add(a,size - (profile.getPreference(v)).getAlternativeRank(a));
			}
			
		}
		
		LOGGER.debug("return scores : {}", scores);
	}


	/**
	 * @param scores a multiset <code>not null</code>
	 * @return the alternative with the maximum score in the multiset
	 */
	public Set<Alternative> getMax(HashMultiset<Alternative> tempscores){
		LOGGER.debug("getMax");
		Preconditions.checkNotNull(tempscores);
		Set<Alternative> set = new HashSet<>();
		Iterable<Alternative> alternativesList = tempscores.elementSet();
		Alternative alternativeMax = new Alternative(0); 
		
		boolean first = true;
		
		for(Alternative a : alternativesList){
			if (first){
				alternativeMax = a;
				set.add(a);
				first = false;
			}
			else{
				if (tempscores.count(a)>tempscores.count(alternativeMax)){
					alternativeMax = a ;
					set = new HashSet<>();
					set.add(a);					
				}
				if (tempscores.count(a) == tempscores.count(alternativeMax)){
					set.add(a);					
				}
			}
		}
		
		
		LOGGER.debug("Max : {} ", set);
		return set;
		
	}
	
	
	public Borda(HashMultiset<Alternative> tempscores) {
		LOGGER.debug("Borda");
		scores = tempscores;
	}

	public Borda() {
		LOGGER.debug("emptyBorda");
		scores = HashMultiset.create();
	}
	
	@Override
	public int hashCode() {
		LOGGER.debug("hashCode");
		return Objects.hash(scores);
		}
	
	/**
	 * @param mset1 <code>not null</code> 
	 * @param mset2<code>not null</code>
	 * @return true if the multisets have the same alternatives with the same scores
	 */
	
	
	@Override
	public boolean equals(Object o1){
		LOGGER.debug("equals");
		Preconditions.checkNotNull(o1);
		
		if (!(o1 instanceof Borda)){
			LOGGER.debug("returns false");
			return false;
		}
		
		Borda borda = (Borda) o1;
		HashMultiset<Alternative> mset1 = borda.scores;
		HashMultiset<Alternative> mset2 = this.scores;
		
		Iterable<Alternative> alternativesList = mset1.elementSet();
		
		for(Alternative a : alternativesList){	
			if(!mset2.contains(a)){
				LOGGER.debug("not same alternatives -> false");
				return false;
			}
			
			if(!(mset2.count(a)==mset1.count(a))){
				LOGGER.debug("not same scores -> false");
				return false;		
			}
			mset2.remove(a, mset2.count(a));		
	
		}
		if(mset2.size()!=0){
			LOGGER.debug("not same alternatives -> false");
			return false;
		}
		LOGGER.debug("returns true");
		return true;
		
	}
	
	

}

