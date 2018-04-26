
package io.github.oliviercailloux.y2018.j_voting;


import java.util.*;


public abstract class Bordas implements SocialWelfareFunction{
	
	
	/***
	 * assigns a score to each alternative of a StrictPreference
	 * @param SPref
	 */
	public List<AlternativeScore> getScore(StrictPreference SPref){
		log.debug("getScorePref\n");
		Objects.requireNonNull(SPref);
		log.debug("parameter SPref : {}\n", SPref.toString());
		int i;
		List<AlternativeScore> scores = new ArrayList<>();
		List<Alternative> Alternatives = SPref.getPreferences();
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
	public List<AlternativeScore> getScore(StrictProfile SProfile){
		log.debug("getScoreProf\n");
		Objects.requireNonNull(SProfile);
		log.debug("parameter SProfile : {}\n", SProfile.toSOC());
		int i,j;
		boolean notfirst = false;
		Iterable<Voter> AllVoters  = SProfile.getAllVoters();
		Iterator<Voter> iterator = AllVoters.iterator();
		Voter currentVoter;
		List<AlternativeScore> FinalScores = new ArrayList<>();
		List<AlternativeScore> TempScores ;
		List<AlternativeScore> PrefScores ;
		while(iterator.hasNext()){
			currentVoter = iterator.next();
			PrefScores = getScore((SProfile.getPreference(currentVoter)));

			if (notfirst){
				TempScores = new ArrayList<>();
				for(i=1;i<=FinalScores.size();i++){
					for(j=1;j<=PrefScores.size();j++){
						if(PrefScores.get(i).getA()==FinalScores.get(i).getA()){
							TempScores.add(new AlternativeScore(PrefScores.get(i).getA(),PrefScores.get(i).getS()+FinalScores.get(i).getS()));
						}
					}
				}
				FinalScores = TempScores;
			}
			
			else{
				FinalScores = PrefScores;
			}
		}
		log.debug("return FinalScores : {}\n", FinalScores.toString());
		return FinalScores;
	}
	
	/***
	 * Sorts by descending order
	 */
	public List<AlternativeScore> descendingOrder(List<AlternativeScore> AScores){
		log.debug("descendingOrder\n");
		Objects.requireNonNull(AScores);
		log.debug("parameter AScores : {}\n", AScores.toString());
		int i, index = 0;
		List<AlternativeScore> sortedScores = new ArrayList<>();
		for(i=1;i<=AScores.size();i++){
			index = getMax(AScores);
			sortedScores.add(AScores.get(index));
			AScores.remove(index);
		}
		log.debug("return sortedScores : {}\n", sortedScores.toString());
		return sortedScores;
	}
	
	/***
	 * get the index of the score max
	 * @param AScore
	 * @return
	 */
	public int getMax(List<AlternativeScore> AScores){
		log.debug("getMax\n");
		Objects.requireNonNull(AScores);
		log.debug("parameter AScores : {}\n", AScores.toString());
		int index = 0, i = 1, scoreMax = 0;
		for(i=1;i<=AScores.size();i++){
			if(AScores.get(i).getS()>scoreMax){
				index = i;
				scoreMax  =AScores.get(i).getS();
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
	public List<AlternativeScore> getSortedScores(StrictProfile SProfile){
		log.debug("getSortedScores\n");
		Objects.requireNonNull(SProfile);
		og.debug("parameter SProfile : {}\n", SProfile.toSOC());
		List<AlternativeScore> AScores = getScore(SProfile);
		AScores = descendingOrder(AScores);
		log.debug("return AScores : {}\n", AScores.toString());
		return AScores;
	}
	
	
}
