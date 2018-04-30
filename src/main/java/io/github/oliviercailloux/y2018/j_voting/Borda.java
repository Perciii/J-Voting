
package io.github.oliviercailloux.y2018.j_voting;


import java.util.*;

import org.slf4j.*;



public abstract class Borda implements SocialWelfareFunction{

	static Logger log = LoggerFactory.getLogger(Borda.class.getName());	


	
	/***
	 * returns a StrictPreference with the alternatives sorted
	 */
	@Override
	public StrictPreference getSocietyStrictPreference(StrictProfile sProfile){
		log.debug("getSocietyStrictPreference\n");
		Objects.requireNonNull(sProfile);
		log.debug("parameter SProfile : {}\n", sProfile.toSOC());
		List<AlternativeScore> aScores = getScore(sProfile);
		aScores = descendingOrder(aScores);
		log.debug("return AScores : {}\n", aScores.toString());
		List<Alternative> alternatives = new ArrayList<>();
		for(int i=0; i<aScores.size();i++){
			alternatives.add((aScores.get(0)).getA());
		}
		StrictPreference sPref = new StrictPreference(alternatives);
		return sPref;
	}


	/***
	 * assigns a score to each alternative of a StrictPreference
	 * @param SPref
	 */

	public List<AlternativeScore> getScore(StrictPreference sPref){
		log.debug("getScorePref\n");
		Objects.requireNonNull(sPref);
		log.debug("parameter SPref : {}\n", sPref.toString());
		int i;
		List<AlternativeScore> scores = new ArrayList<>();
		List<Alternative> Alternatives = sPref.getPreferences();
		for(i=1;i<=Alternatives.size();i++){
			scores.add(new AlternativeScore(Alternatives.get(i),Integer.valueOf(Alternatives.size()-i)));
		}
		log.debug("return score : {}\n", scores.toString());
		return scores ;
	}


	/***
	 * 	assigns a score to each alternative of a StrictProfile
	 * @param SProfile
	 * @return
	 */
	public List<AlternativeScore> getScore(StrictProfile sProfile){
		log.debug("getScoreProf\n");
		Objects.requireNonNull(sProfile);
		log.debug("parameter SProfile : {}\n", sProfile.toSOC());
		int i,j;
		boolean notfirst = false;
		Iterable<Voter> allVoters  = sProfile.getAllVoters();
		Iterator<Voter> iterator = allVoters.iterator();
		Voter currentVoter;
		List<AlternativeScore> finalScores = new ArrayList<>();
		List<AlternativeScore> tempScores ;
		List<AlternativeScore> prefScores ;
		while(iterator.hasNext()){
			currentVoter = iterator.next();
			prefScores = getScore((sProfile.getPreference(currentVoter)));

			if (notfirst){
				tempScores = new ArrayList<>();
				for(i=1;i<=finalScores.size();i++){
					for(j=1;j<=prefScores.size();j++){
						if(prefScores.get(i).getA()==finalScores.get(i).getA()){
							tempScores.add(new AlternativeScore(prefScores.get(i).getA(),prefScores.get(i).getS()+finalScores.get(i).getS()));
						}
					}
				}
				finalScores = tempScores;
			}

			else{
				finalScores = prefScores;
			}
		}
		log.debug("return FinalScores : {}\n", finalScores.toString());
		return finalScores;
	}

	/***
	 * Sorts by descending order
	 */
	public List<AlternativeScore> descendingOrder(List<AlternativeScore> aScores){
		log.debug("descendingOrder\n");
		Objects.requireNonNull(aScores);
		log.debug("parameter aScores : {}\n", aScores.toString());
		int i, index = 0;
		List<AlternativeScore> sortedScores = new ArrayList<>();
		for(i=1;i<=aScores.size();i++){
			index = getMax(aScores);
			sortedScores.add(aScores.get(index));
			aScores.remove(index);
		}
		log.debug("return sortedScores : {}\n", sortedScores.toString());
		return sortedScores;
	}

	/***
	 * get the index of the score max
	 * @param AScore
	 * @return
	 */
	public int getMax(List<AlternativeScore> aScores){
		log.debug("getMax\n");
		Objects.requireNonNull(aScores);
		log.debug("parameter AScores : {}\n", aScores.toString());
		int index = 0, i = 1, scoreMax = 0;
		for(i=1;i<=aScores.size();i++){
			if(aScores.get(i).getS()>scoreMax){
				index = i;
				scoreMax  = aScores.get(i).getS();
			}
		}
		log.debug("return index : {}\n", index);
		return index;
	}

	/***
	 * create a list of AlternativeScore sorted by descending order
	 * @param SProfile
	 * @return
	 */
	public List<AlternativeScore> getSortedScores(StrictProfile sProfile){
		log.debug("getSortedScores\n");
		Objects.requireNonNull(sProfile);
		log.debug("parameter sProfile : {}\n", sProfile.toSOC());
		List<AlternativeScore> aScores = getScore(sProfile);
		aScores = descendingOrder(aScores);
		log.debug("return AScores : {}\n", aScores.toString());
		return aScores;
	}


}
