
package io.github.oliviercailloux.y2018.j_voting;


import java.util.*;

import org.slf4j.*;



public class Borda implements SocialWelfareFunction{

	private static Logger LOGGER = LoggerFactory.getLogger(Borda.class.getName());	
	public Map<Alternative,Integer> finalScores;
	
	/***
	 * returns a StrictPreference with the alternatives sorted
	 */
	@Override
	public StrictPreference getSocietyStrictPreference(StrictProfile sProfile){
		LOGGER.debug("getSocietyStrictPreference\n");
		Objects.requireNonNull(sProfile);
		LOGGER.debug("parameter SProfile : {}\n", sProfile.toSOC());
		
		finalScores = getScores(sProfile);
		finalScores = descendingOrder(finalScores);
		
		LOGGER.debug("return AScores : {}\n", finalScores.toString());
		
		Iterable<Alternative> alternatives = finalScores.keySet();
		List<Alternative> al = new ArrayList<>();
		for(Alternative a : alternatives) {
			al.add(a);
		}
		StrictPreference pref = new StrictPreference(al);
		
		LOGGER.debug("return AScores : {}\n", pref.toString());
		return pref;
	}


	/***
	 * assigns a score to each alternative of a StrictPreference
	 * @param SPref
	 */

	public Map<Alternative, Integer> getScores(StrictPreference sPref){
		LOGGER.debug("getScorePref\n");
		Objects.requireNonNull(sPref);
		LOGGER.debug("parameter SPref : {}\n", sPref.toString());
		Map<Alternative,Integer> unsortedScores = new HashMap<>();
		int i;
		List<Alternative> Alternatives = sPref.getPreferences();
		for(i=0;i<Alternatives.size();i++){
			unsortedScores.put(Alternatives.get(i),Integer.valueOf(Alternatives.size()-(i+1)));
		}
		LOGGER.debug("return score : {}\n", unsortedScores.toString());
		return unsortedScores;
	}


	/***
	 * 	assigns a score to each alternative of a StrictProfile
	 * @param SProfile
	 * @return
	 */
	public Map<Alternative, Integer> getScores(StrictProfile sProfile){
		LOGGER.debug("getScoreProf\n");
		Objects.requireNonNull(sProfile);
		LOGGER.debug("parameter SProfile : {}\n", sProfile.toSOC());
		
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
		LOGGER.debug("return unsortedScores : {}\n", unsortedScores.toString());
		return unsortedScores;
	}

	/***
	 * Sorts by descending order
	 */
	public Map<Alternative, Integer> descendingOrder(Map<Alternative, Integer> unsortedScores){
		LOGGER.debug("descendingOrder\n");
		
		Map<Alternative,Integer> tempScores = unsortedScores;
		Alternative alternativeMax;
		int size = unsortedScores.size();
		
		Map<Alternative,Integer> finalScoresToSort = new HashMap<>();
		
		for(int i=0 ; i<size;i++){
			alternativeMax = getMax(tempScores);
			finalScoresToSort.put(alternativeMax, tempScores.get(alternativeMax));
			tempScores.remove(alternativeMax);
		}
		
		
		LOGGER.debug("return sortedScores : {}\n", finalScoresToSort.toString());
		return finalScoresToSort;
	}

	/***
	 * get the alternative of the score max
	 * @param AScore
	 * @return
	 */
	public Alternative getMax(Map<Alternative, Integer> scores){
		LOGGER.debug("getMax\n");
		
		Iterable<Alternative> alternativesList = scores.keySet();
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
				if (scores.get(currentAlternative)>scores.get(alternativeMax)){
					alternativeMax = currentAlternative ;
				}
			}
		}
		
		LOGGER.debug("Max : {} \n", alternativeMax);
		return alternativeMax;
		
	}


	/***
	 * create a HashMap of Alternatives sorted by descending order of score
	 * @param SProfile
	 * @return
	 */
	public Map<Alternative, Integer> getSortedScores(StrictProfile sProfile){
		LOGGER.debug("getSortedScores\n");
		Objects.requireNonNull(sProfile);
		LOGGER.debug("parameter sProfile : {}\n", sProfile.toSOC());
		
		finalScores = getScores(sProfile);
		finalScores = descendingOrder(finalScores);
		LOGGER.debug("return AScores : {}\n", finalScores.toString());
		return finalScores;
	}

	

	
	/***
	 * returns true if the maps have the same alternatives with the same scores
	 * @param map1
	 * @param map2
	 * @return
	 */
	
	public boolean equals(Map<Alternative,Integer> map1, Map<Alternative,Integer> map2){
		LOGGER.debug("equalsMaps\n");
		Iterable<Alternative> alternativesList = map1.keySet();
		Iterator<Alternative> iteratorA = alternativesList.iterator();
		Alternative currentAlternative;
		
		while(iteratorA.hasNext()){
			currentAlternative=iteratorA.next();
			if(!map2.containsKey(currentAlternative)){
				LOGGER.debug("not same alternatives -> false\n");
				return false;
			}
			
			if(!map2.get(currentAlternative).equals(map1.get(currentAlternative))){
				LOGGER.debug("not same scores -> false\n");
				return false;		
			}
			map2.remove(currentAlternative);		
	
		}
		if(map2.size()!=0){
			LOGGER.debug("not same alternatives -> false\n");
			return false;
		}
		LOGGER.debug("returns true\n");
		return true;
		
	}
	
	
	
	

}

