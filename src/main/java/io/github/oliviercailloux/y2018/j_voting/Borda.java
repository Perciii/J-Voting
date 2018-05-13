
package io.github.oliviercailloux.y2018.j_voting;


import java.util.*;

import org.slf4j.*;



public class Borda implements SocialWelfareFunction{

	private static Logger log = LoggerFactory.getLogger(Borda.class.getName());	
	public Map<Alternative,Integer> finalScores;
	
	/***
	 * returns a StrictPreference with the alternatives sorted
	 */
	@Override
	public StrictPreference getSocietyStrictPreference(StrictProfile sProfile){
		log.debug("getSocietyStrictPreference\n");
		Objects.requireNonNull(sProfile);
		log.debug("parameter SProfile : {}\n", sProfile.toSOC());
		
		finalScores = getScores(sProfile);
		finalScores = descendingOrder(finalScores);
		
		log.debug("return AScores : {}\n", finalScores.toString());
		
		Iterable<Alternative> alternatives = finalScores.keySet();
		List<Alternative> al = new ArrayList<>();
		for(Alternative a : alternatives) {
			al.add(a);
		}
		StrictPreference pref = new StrictPreference(al);
		
		log.debug("return AScores : {}\n", pref.toString());
		return pref;
	}


	/***
	 * assigns a score to each alternative of a StrictPreference
	 * @param SPref
	 */

	public Map<Alternative, Integer> getScores(StrictPreference sPref){
		log.debug("getScorePref\n");
		Objects.requireNonNull(sPref);
		log.debug("parameter SPref : {}\n", sPref.toString());
		Map<Alternative,Integer> unsortedScores = new HashMap<>();
		int i;
		List<Alternative> Alternatives = sPref.getPreferences();
		for(i=0;i<Alternatives.size();i++){
			unsortedScores.put(Alternatives.get(i),Integer.valueOf(Alternatives.size()-i));
		}
		log.debug("return score : {}\n", unsortedScores.toString());
		return unsortedScores;
	}


	/***
	 * 	assigns a score to each alternative of a StrictProfile
	 * @param SProfile
	 * @return
	 */
	public Map<Alternative, Integer> getScores(StrictProfile sProfile){
		log.debug("getScoreProf\n");
		Objects.requireNonNull(sProfile);
		log.debug("parameter SProfile : {}\n", sProfile.toSOC());
		
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
				while(iteratorA.hasNext()){
					currentAlternative = iteratorA.next();
					score = unsortedScores.get(currentAlternative) + tempScores.get(currentAlternative);
					unsortedScores.remove(currentAlternative);
					unsortedScores.put(currentAlternative, score);
					
				}
			}

			else{
				unsortedScores = tempScores;
			}
		}
		log.debug("return unsortedScores : {}\n", unsortedScores.toString());
		return unsortedScores;
	}

	/***
	 * Sorts by descending order
	 */
	public Map<Alternative, Integer> descendingOrder(Map<Alternative, Integer> unsortedScores){
		log.debug("descendingOrder\n");
		
		Map<Alternative,Integer> tempScores = unsortedScores;
		Alternative alternativeMax;
		
		Map<Alternative,Integer> finalScores = new HashMap<>();
		
		for(int i=0 ; i<unsortedScores.size();i++){
			alternativeMax = getMax(tempScores);
			finalScores.put(alternativeMax,tempScores.get(alternativeMax));
			tempScores.remove(alternativeMax);
		}
		
		
		log.debug("return sortedScores : {}\n", finalScores.toString());
		return finalScores;
	}

	/***
	 * get the alternative of the score max
	 * @param AScore
	 * @return
	 */
	public Alternative getMax(Map<Alternative, Integer> scores){
		log.debug("getMax\n");
		
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
		
		log.debug("Max : {} \n", alternativeMax);
		return alternativeMax;
		
	}


	/***
	 * create a HashMap of Alternatives sorted by descending order of score
	 * @param SProfile
	 * @return
	 */
	public Map<Alternative, Integer> getSortedScores(StrictProfile sProfile){
		log.debug("getSortedScores\n");
		Objects.requireNonNull(sProfile);
		log.debug("parameter sProfile : {}\n", sProfile.toSOC());
		
		finalScores = getScores(sProfile);
		finalScores = descendingOrder(finalScores);
		log.debug("return AScores : {}\n", finalScores.toString());
		return finalScores;
	}

	

	
	/***
	 * returns true if the maps have the same alternatives with the same scores
	 * @param map1
	 * @param map2
	 * @return
	 */
	
	public boolean equals(Map<Alternative,Integer> map1, Map<Alternative,Integer> map2){
		log.debug("equalsMaps\n");
		Iterable<Alternative> alternativesList = map1.keySet();
		Iterator<Alternative> iteratorA = alternativesList.iterator();
		Alternative currentAlternative;
		
		while(iteratorA.hasNext()){
			currentAlternative=iteratorA.next();
			if(!map2.containsKey(currentAlternative)){
				log.debug("not same alternatives -> false\n");
				return false;
			}
			
			if(!map2.get(currentAlternative).equals(map1.get(currentAlternative))){
				log.debug("not same scores -> false\n");
				return false;		
			}
			map2.remove(currentAlternative);		
	
		}
		if(map2.size()!=0){
			log.debug("not same alternatives -> false\n");
			return false;
		}
		log.debug("returns true\n");
		return true;
		
	}
	
	
	
	

}

