
package io.github.oliviercailloux.y2018.j_voting;


import java.util.*;

import org.slf4j.*;

import com.google.common.base.Preconditions;



public class Borda implements SocialWelfareFunction{

	private static Logger LOGGER = LoggerFactory.getLogger(Borda.class.getName());	
	private Map<Alternative,Integer> scores;
	
	/**
	 * @param sProfile a StrictProfile <code>not null</code>
	 * @return a StrictPreference with the alternatives sorted
	 */
	@Override
	public StrictPreference getSocietyStrictPreference(StrictProfile sProfile){
		LOGGER.debug("getSocietyStrictPreference");
		Preconditions.checkNotNull(sProfile);
		LOGGER.debug("parameter SProfile : {}", sProfile.toSOC());
		
		getSortedScores(sProfile);
		
		LOGGER.debug("return AScores : {}", scores);
		
		Iterable<Alternative> alternatives = scores.keySet();
		List<Alternative> al = new ArrayList<>();
		for(Alternative a : alternatives) {
			al.add(a);
		}
		StrictPreference pref = new StrictPreference(al);
		
		LOGGER.debug("return AScores : {}", pref);
		return pref;
	}


	/**
	 * assigns a score to each alternative of a StrictPreference
	 * @param sPref a StrictPreference <code>not null</code>
	 * @return a mapping of each alternative to its score in this StrictPreference
	 */
	public Map<Alternative, Integer> getScores(StrictPreference sPref){
		LOGGER.debug("getScorePref");
		Preconditions.checkNotNull(sPref);
		LOGGER.debug("parameter SPref : {}", sPref.toString());
		Map<Alternative,Integer> unsortedScores = new HashMap<>();
		int i=1;
		List<Alternative> alternatives = sPref.getPreferences();
		for(Alternative a : alternatives){
			i++;
			unsortedScores.put(a,Integer.valueOf(alternatives.size()-i));
		}
		LOGGER.debug("return score : {}", unsortedScores);
		return unsortedScores;
	}


	/**
	 * 	assigns a score to each alternative of a StrictProfile
	 * @param sProfile a StrictProfile <code>not null</code>
	 * @return unsortedScores a Map giving a score to each alternative of the profile given in parameter
	 */
	public Map<Alternative, Integer> getScores(StrictProfile sProfile){
		LOGGER.debug("getScoreProf");
		Preconditions.checkNotNull(sProfile);
		LOGGER.debug("parameter SProfile : {}", sProfile.toSOC());
		
		boolean notfirst = false;
		
		Iterable<Voter> allVoters  = sProfile.getAllVoters();
		Iterator<Voter> iteratorV = allVoters.iterator();
		Voter currentVoter;
		
		Map<Alternative,Integer> tempScores = new HashMap<>();
		Map<Alternative,Integer> unsortedScores = new HashMap<>();
		
		Iterable<Alternative> alternativesList = sProfile.getAlternativesComplete();
		Iterator<Alternative> iteratorA = alternativesList.iterator();
		Alternative currentAlternative;
		
		Integer score = 0;
		
		while(iteratorV.hasNext()){
			currentVoter = iteratorV.next();
			tempScores = getScores((sProfile.getPreference(currentVoter)));
			
			if (notfirst){
				iteratorA = alternativesList.iterator();
				while(iteratorA.hasNext()){
					currentAlternative = iteratorA.next();
					score = unsortedScores.get(currentAlternative) + tempScores.get(currentAlternative);
					unsortedScores.remove(currentAlternative);
					unsortedScores.put(currentAlternative, score);
					
				}
			}

			else{
				unsortedScores = tempScores;
				notfirst=true;
			}
		}
		LOGGER.debug("return unsortedScores : {}", unsortedScores);
		return unsortedScores;
	}

	/**
	 * @param unsortedScores a map <code>not null</code> of scores
	 * @return map of scores sorted by descending order
	 */
	public void descendingOrder(Map<Alternative, Integer> unsortedScores){
		LOGGER.debug("descendingOrder");
		Preconditions.checkNotNull(unsortedScores);
		
		Map<Alternative,Integer> tempScores = unsortedScores;
		Alternative alternativeMax;
		int size = unsortedScores.size();
		
		for(int i=0 ; i<size ; i++){
			alternativeMax = getMax(tempScores);
			scores.put(alternativeMax, tempScores.get(alternativeMax));
			tempScores.remove(alternativeMax);
		}
		
		LOGGER.debug("return sortedScores : {}\n", scores);
	}

	/**
	 * @param scores a map <code>not null</code>
	 * @return the alternative with the maximum score in the map
	 */
	public Alternative getMax(Map<Alternative, Integer> tempscores){
		LOGGER.debug("getMax");
		Preconditions.checkNotNull(tempscores);
		
		Iterable<Alternative> alternativesList = tempscores.keySet();
		Iterator<Alternative> iteratorA = alternativesList.iterator();
		Alternative currentAlternative;
		
		Alternative alternativeMax = new Alternative(0); 
		boolean first = true;
		
		while(iteratorA.hasNext()){
			currentAlternative = iteratorA.next();
			if (first){
				alternativeMax = currentAlternative;
				first = false;
			}
			else{
				if (tempscores.get(currentAlternative)>tempscores.get(alternativeMax)){
					alternativeMax = currentAlternative ;
				}
			}
		}
		
		LOGGER.debug("Max : {} ", alternativeMax);
		return alternativeMax;
		
	}


	/**
	 * create a HashMap of Alternatives sorted by descending order of score
	 * @param sProfile a StrictProfile<code>not null</code>
	 * @return sortedScores a sorted map by descending order of the scores of the profile given in parameter
	 */
	public void getSortedScores(StrictProfile sProfile){
		LOGGER.debug("getSortedScores");
		Preconditions.checkNotNull(sProfile);
		LOGGER.debug("parameter sProfile : {}", sProfile.toSOC());
		
		scores = getScores(sProfile);
		descendingOrder(scores);
		LOGGER.debug("return AScores : {}", scores);
	}

	
	
	/**
	 *
	 * @param scores
	 */
	public Borda(Map<Alternative, Integer> tempscores) {
		LOGGER.debug("Borda");
		this.scores = tempscores;
	}

	public Borda() {
		LOGGER.debug("emptyBorda");
		this.scores = new HashMap<>();
	}
	
	@Override
		public int hashCode() {
		LOGGER.debug("hashCode");
			return Objects.hash(scores);
		}
	
	/**
	 * @param map1 <code>not null</code> 
	 * @param map2<code>not null</code>
	 * @return true if the maps have the same alternatives with the same scores
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
		Map<Alternative,Integer> map1 = borda.scores;
		Map<Alternative,Integer> map2 = this.scores;
		
		Iterable<Alternative> alternativesList = map1.keySet();
		Iterator<Alternative> iteratorA = alternativesList.iterator();
		Alternative currentAlternative;
		
		while(iteratorA.hasNext()){
			currentAlternative=iteratorA.next();
			if(!map2.containsKey(currentAlternative)){
				LOGGER.debug("not same alternatives -> false");
				return false;
			}
			
			if(!map2.get(currentAlternative).equals(map1.get(currentAlternative))){
				LOGGER.debug("not same scores -> false");
				return false;		
			}
			map2.remove(currentAlternative);		
	
		}
		if(map2.size()!=0){
			LOGGER.debug("not same alternatives -> false");
			return false;
		}
		LOGGER.debug("returns true");
		return true;
		
	}
	
	

}

