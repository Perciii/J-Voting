
package io.github.oliviercailloux.y2018.j_voting;


import java.util.*;

import org.slf4j.*;

import com.google.common.base.Preconditions;
import com.google.common.collect.*;

public class Borda implements SocialWelfareFunction{

	private static Logger LOGGER = LoggerFactory.getLogger(Borda.class.getName());	
	private HashMultiset<Alternative> scores;
	
	/**
	 * @param sProfile a StrictProfile <code>not null</code>
	 * @return a StrictPreference with the alternatives sorted
	 */
	@Override
	public StrictPreference getSocietyStrictPreference(StrictProfile sProfile){
		LOGGER.debug("getSocietyStrictPreference");
		Preconditions.checkNotNull(sProfile);
		LOGGER.debug("parameter SProfile : {}", sProfile.toSOC());
		
		getScores(sProfile);
		
		LOGGER.debug("return AScores : {}", scores);
		
		Alternative alMax;
		int size = scores.elementSet().size();
		List<Alternative> al = new ArrayList<>();
		HashMultiset<Alternative> tempscores = scores;
		
		for(int i=0 ; i<size ; i++){
			alMax = getMax(tempscores);
			al.add(alMax);
			tempscores.remove(alMax,tempscores.count(alMax));
		}
		
		StrictPreference pref = new StrictPreference(al);
		
		LOGGER.debug("return AScores : {}", pref);
		return pref;
	}


	/**
	 * assigns a score to each alternative of a StrictPreference
	 * @param sPref a StrictPreference <code>not null</code>
	 * @return a multiset for the alternaitves in this StrictPreference
	 */
	public void getScores(StrictPreference sPref){
		LOGGER.debug("getScorePref");
		Preconditions.checkNotNull(sPref);
		LOGGER.debug("parameter SPref : {}", sPref.toString());
		int size = sPref.getPreferences().size();
		for(Alternative a : sPref.getPreferences()){
			scores.add(a,size - sPref.getAlternativeRank(a));
		}
		LOGGER.debug("return score : {}", scores);
	}


	/**
	 * 
	 * @param sProfile a StrictProfile <code>not null</code>
	 * @return unsortedScores a HashMultiset for the alternatives of the profile
	 */
	public void getScores(StrictProfile sProfile){
		LOGGER.debug("getScoreProf");
		Preconditions.checkNotNull(sProfile);
		LOGGER.debug("parameter SProfile : {}", sProfile.toSOC());
		
		boolean notfirst = false;
		
		Iterable<Voter> allVoters  = sProfile.getAllVoters();
		List<Alternative> alternatives;
		int size = 0;
		
		for(Voter v : allVoters){
			alternatives = sProfile.getPreference(v).getPreferences();
			
			for(Alternative a : alternatives){
				size = alternatives.size();
				scores.add(a,size - (sProfile.getPreference(v)).getAlternativeRank(a));
			}
			
		}
		
		LOGGER.debug("return scores : {}", scores);
	}


	/**
	 * @param scores a multiset <code>not null</code>
	 * @return the alternative with the maximum score in the multiset
	 */
	public Alternative getMax(HashMultiset<Alternative> tempscores){
		LOGGER.debug("getMax");
		Preconditions.checkNotNull(tempscores);
		
		Iterable<Alternative> alternativesList = tempscores.elementSet();
		Alternative alternativeMax = new Alternative(0); 
		
		boolean first = true;
		
		for(Alternative a : alternativesList){
			if (first){
				alternativeMax = a;
				first = false;
			}
			else{
				if (tempscores.count(a)>tempscores.count(alternativeMax)){
					alternativeMax = a ;
				}
			}
		}
		
		
		LOGGER.debug("Max : {} ", alternativeMax);
		return alternativeMax;
		
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

