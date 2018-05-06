package io.github.oliviercailloux.y2018.j_voting;

import java.util.*;

import org.junit.Test;

public class BordaTest {

	
	@Test
	public void testgetSocietyStrictPreference(){
		StrictProfile sProfile = new StrictProfile();
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Voter v1 = new Voter(1);
		Voter v2 = new Voter(2);
		List<Alternative> alternatives = new ArrayList<Alternative>();
		alternatives.add(a1);
		alternatives.add(a2);
		StrictPreference pref1 = new StrictPreference(alternatives);
		alternatives.add(a3);
		StrictPreference pref2 = new StrictPreference(alternatives);
		sProfile.addProfile(v1, pref1);
		sProfile.addProfile(v2, pref2);
		System.out.println("profile : "+sProfile);	
		StrictPreference sPref = new Borda().getSocietyStrictPreference(sProfile);
		System.out.println("preference : "+sPref);	
		
	}
	
	
	@Test
	public void testgetScoresPref() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Voter v1 = new Voter(1);
		Voter v2 = new Voter(2);
		List<Alternative> alternatives = new ArrayList<Alternative>();
		alternatives.add(a1);
		alternatives.add(a2);
		alternatives.add(a3);
		StrictPreference sPref = new StrictPreference(alternatives);
		List<AlternativeScore> scores = Borda.getScore(sPref);
		System.out.println("preferences : "+sPref+'\n'+'\n'+"Scores : " + scores);	
	}
	
	@Test
	public void testgetScoresProfile() {
		StrictProfile p = new StrictProfile();
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Voter v1 = new Voter(1);
		Voter v2 = new Voter(2);
		List<Alternative> alternatives = new ArrayList<Alternative>();
		alternatives.add(a1);
		alternatives.add(a2);
		StrictPreference pref1 = new StrictPreference(alternatives);
		alternatives.add(a3);
		StrictPreference pref2 = new StrictPreference(alternatives);
		p.addProfile(v1, pref1);
		p.addProfile(v2, pref2);
		List<AlternativeScore> scores = Borda.getScore(p);
		System.out.println("profile : "+p+'\n'+'\n'+"Scores : " + scores);	
	}
	
	
	@Test
	public void testdescendingOrder(){
		List<AlternativeScore> listScores = new ArrayList<>();
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		listScores.add(new AlternativeScore(a1,2));
		listScores.add(new AlternativeScore(a2,0));
		listScores.add(new AlternativeScore(a3,6));
		System.out.println("before : "+listScores);	
		List<AlternativeScore> listScoresSorted = Borda.descendingOrder(listScores);
		System.out.println("after : "+listScoresSorted);	
	}
	
	
	@Test
	public void testgetMax(){
		List<AlternativeScore> listScores = new ArrayList<>();
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		listScores.add(new AlternativeScore(a1,2));
		listScores.add(new AlternativeScore(a2,0));
		listScores.add(new AlternativeScore(a3,6));
		System.out.println("list : "+listScores + '\n'+'\n'+"Max : "+Borda.getMax(listScores));	
	}
	
	
	
	
	@Test
	public void testgetSortedScores(){
		StrictProfile p = new StrictProfile();
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Voter v1 = new Voter(1);
		Voter v2 = new Voter(2);
		List<Alternative> alternatives = new ArrayList<Alternative>();
		alternatives.add(a1);
		alternatives.add(a2);
		StrictPreference pref1 = new StrictPreference(alternatives);
		alternatives.add(a3);
		StrictPreference pref2 = new StrictPreference(alternatives);
		p.addProfile(v1, pref1);
		p.addProfile(v2, pref2);
		System.out.println("before : "+p);	
		List<AlternativeScore> listScoresSorted = Borda.getSortedScores(p);
		System.out.println("after : "+listScoresSorted);	
	}
	
	
	
	
}
