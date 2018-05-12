package io.github.oliviercailloux.y2018.j_voting;

import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;


public class BordaTest {

	
	@Test
	public void testgetSocietyStrictPreference(){
		StrictProfile profile = new StrictProfile();
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Voter v1 = new Voter(1);
		Voter v2 = new Voter(2);
		Voter v3 = new Voter(3);
		Voter v4 = new Voter(4);
		Voter v5 = new Voter(5);
		Voter v6 = new Voter(6);
		ArrayList<Alternative> list1 = new ArrayList<Alternative>();
		ArrayList<Alternative> list2 = new ArrayList<Alternative>();
		list1.add(a1);
		list1.add(a2);
		list1.add(a3);
		list2.add(a3);
		list2.add(a2);
		list2.add(a1);
		StrictPreference pref1 = new StrictPreference(list1);
		StrictPreference pref2 = new StrictPreference(list2);
		profile.addProfile(v1, pref1);
		profile.addProfile(v2, pref1);
		profile.addProfile(v3, pref1);
		profile.addProfile(v4, pref1);
		profile.addProfile(v5, pref2);
		profile.addProfile(v6, pref2);
		StrictPreference sPref = new Borda().getSocietyStrictPreference(profile);
		
		assertTrue(sPref.equals(pref1));
		
	}
	
	
	@Test
	public void testgetScoresPref() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		List<Alternative> alternatives = new ArrayList<Alternative>();
		alternatives.add(a1);
		alternatives.add(a2);
		alternatives.add(a3);
		StrictPreference sPref = new StrictPreference(alternatives);
		Map<Alternative,Integer> scores = new Borda().getScores(sPref);
		
		Map<Alternative,Integer> scoresTest = new HashMap<>();
		scoresTest.put(a1, 2);
		scoresTest.put(a2, 1);
		scoresTest.put(a3, 0);
		
		assertTrue(scores.equals(scoresTest));
	}
	
	@Test
	public void testgetScoresProfile() {
		StrictProfile profile = new StrictProfile();
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Voter v1 = new Voter(1);
		Voter v2 = new Voter(2);
		Voter v3 = new Voter(3);
		Voter v4 = new Voter(4);
		Voter v5 = new Voter(5);
		Voter v6 = new Voter(6);
		ArrayList<Alternative> list1 = new ArrayList<Alternative>();
		ArrayList<Alternative> list2 = new ArrayList<Alternative>();
		list1.add(a1);
		list1.add(a2);
		list1.add(a3);
		list2.add(a3);
		list2.add(a2);
		list2.add(a1);
		StrictPreference pref1 = new StrictPreference(list1);
		StrictPreference pref2 = new StrictPreference(list2);
		profile.addProfile(v1, pref1);
		profile.addProfile(v2, pref1);
		profile.addProfile(v3, pref1);
		profile.addProfile(v4, pref1);
		profile.addProfile(v5, pref2);
		profile.addProfile(v6, pref2);
		Map<Alternative,Integer> scores = new Borda().getScores(profile);
		
		Map<Alternative,Integer> scoresTest = new HashMap<>();
		scoresTest.put(a1, 8);
		scoresTest.put(a2, 6);
		scoresTest.put(a3, 4);
		
		assertTrue(scores.equals(scoresTest));

	}
	
	
	@Test
	public void testdescendingOrder(){
		Map<Alternative,Integer> listScores = new HashMap<>();
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		listScores.put(a1,2);
		listScores.put(a2,0);
		listScores.put(a3,6);
		Map<Alternative,Integer> listScoresSorted = new Borda().descendingOrder(listScores);
		
		Map<Alternative,Integer> scores = new HashMap<>();
		scores.put(a3,6);
		scores.put(a1,2);
		scores.put(a2,0);
		
		assertTrue(scores.equals(listScoresSorted));
		
	}
	
	
	@Test
	public void testgetMax(){
		Map<Alternative,Integer> listScores = new HashMap<>();
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		listScores.put(a1,2);
		listScores.put(a2,0);
		listScores.put(a3,6);
		
		assertTrue(new Borda().getMax(listScores).equals(a3));
	}
	
	
	
	
	@Test
	public void testgetSortedScores(){
		StrictProfile profile = new StrictProfile();
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Voter v1 = new Voter(1);
		Voter v2 = new Voter(2);
		Voter v3 = new Voter(3);
		Voter v4 = new Voter(4);
		Voter v5 = new Voter(5);
		Voter v6 = new Voter(6);
		ArrayList<Alternative> list1 = new ArrayList<Alternative>();
		ArrayList<Alternative> list2 = new ArrayList<Alternative>();
		list1.add(a1);
		list1.add(a2);
		list1.add(a3);
		list2.add(a3);
		list2.add(a2);
		list2.add(a1);
		StrictPreference pref1 = new StrictPreference(list1);
		StrictPreference pref2 = new StrictPreference(list2);
		profile.addProfile(v1, pref1);
		profile.addProfile(v2, pref1);
		profile.addProfile(v3, pref1);
		profile.addProfile(v4, pref1);
		profile.addProfile(v5, pref2);
		profile.addProfile(v6, pref2);
		
		Map<Alternative,Integer> scoresTest = new HashMap<>();
		scoresTest.put(a1, 8);
		scoresTest.put(a2, 6);
		scoresTest.put(a3, 4);
		
		Map<Alternative,Integer> listScoresSorted = new Borda().getSortedScores(profile);
				
		
		assertTrue(scoresTest.equals(listScoresSorted));
		
	}
	
	
	
	
}

