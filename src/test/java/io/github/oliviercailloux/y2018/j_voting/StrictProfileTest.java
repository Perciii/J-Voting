package io.github.oliviercailloux.y2018.j_voting;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class StrictProfileTest {

	/**
	 * creates a StrictProfile to run the tests on.
	 * @return the new StrictProfile
	 */
	public static StrictProfile createProfileToTest() {
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
		
		ArrayList<Alternative> list1 = new ArrayList<>();
		ArrayList<Alternative> list2 = new ArrayList<>();
		
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
		
		return profile;
	}
	
	/**
	 * Tests whether the addProfile() method make the profile contain the StrictPreference given at initialization (through method getPreference())
	 */
	@Test
	public void testAddProfile() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		
		Voter v7 = new Voter(7);
		
		ArrayList<Alternative> list3 = new ArrayList<>();
		list3.add(a3);
		list3.add(a1);
		list3.add(a2);
		StrictPreference pref3 = new StrictPreference(list3);
		
		StrictProfile profile = createProfileToTest();
		profile.addProfile(v7, pref3);
		
		assertTrue(profile.getPreference(v7).equals(pref3));
	}


	/**
	 * Tests whether method getPreference() gives a set of Alternatives equal to the initial set
	 */
	@Test
	public void testGetPreference() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		
		Voter v1 = new Voter(1);
		
		ArrayList<Alternative> list1 = new ArrayList<>();
		list1.add(a1);
		list1.add(a2);
		list1.add(a3);
		StrictPreference pref1 = new StrictPreference(list1);
		
		assertTrue(createProfileToTest().getPreference(v1).equals(pref1));
	}

	/**
	 * Tests whether method contains() return true if the Voter called in the method is present in the calling StrictProfile
	 */
	@Test
	public void testContains() {
		Voter voter = new Voter(1);
		assertTrue(createProfileToTest().contains(voter));
	}
	
	/**
	 * Tests whether method getAllVoters() returns all the Voters present in the calling StrictProfile
	 */
	@Test
	public void testGetAllVoters() {
		Voter v1 = new Voter(1);
		Voter v2 = new Voter(2);
		Voter v3 = new Voter(3);
		Voter v4 = new Voter(4);
		Voter v5 = new Voter(5);
		Voter v6 = new Voter(6);
		assertTrue(createProfileToTest().getNbVoters() == 6 && createProfileToTest().contains(v1) && createProfileToTest().contains(v2) && createProfileToTest().contains(v3) && createProfileToTest().contains(v4) && createProfileToTest().contains(v5) && createProfileToTest().contains(v6));
	}

	/**
	 * Tests whether method getNbVoters() returns the number of Voters present in the calling StrictProfile
	 */
	@Test
	public void testGetNbVoters() {
		assertEquals(createProfileToTest().getNbVoters(), 6);
	}

	/**
	 * Tests whether method getSumVoteCount() returns the number of votes present in the calling StrictProfile (the nb of voters)
	 */
	@Test
	public void testGetSumVoteCount() {
		assertEquals(createProfileToTest().getSumVoteCount(), 6);
	}

	/**
	 * Tests whether method getUniquePreferences returns only every StrictPreference present once in the calling StrictProfile
	 */
	@Test
	public void testGetUniquePreferences() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		
		ArrayList<Alternative> list1 = new ArrayList<>();
		ArrayList<Alternative> list2 = new ArrayList<>();
		
		list1.add(a1);
		list1.add(a2);
		list1.add(a3);
		list2.add(a3);
		list2.add(a2);
		list2.add(a1);
		
		StrictPreference pref1 = new StrictPreference(list1);
		StrictPreference pref2 = new StrictPreference(list2);
		
		List<StrictPreference> preferencelist = new ArrayList<>();
		for(StrictPreference p : createProfileToTest().getUniquePreferences()) {
			preferencelist.add(p);
		}
		
		boolean case1 = preferencelist.get(0).equals(pref1) && preferencelist.get(1).equals(pref2);
		boolean case2 = preferencelist.get(1).equals(pref1) && preferencelist.get(0).equals(pref2);
		
		assertTrue(case1 || case2);
	}

	/**
	 * Tests whether method getNbUniquePreferences returns the number of unique preferences in the calling StrictProfile
	 */
	@Test
	public void testGetNbUniquePreferences() {
		assertEquals(createProfileToTest().getNbUniquePreferences(),2);
	}

	/**
	 * Tests whether method getNbAlternativesComplete returns the number of alternatives of the calling complete StrictProfile
	 */
	@Test
	public void testGetNbAlternativesComplete() {
		assertEquals(createProfileToTest().getNbAlternativesComplete(),3);
	}

	/**
	 * Tests whether method getAlternativesComplete returns the alternatives of the calling complete StrictProfile
	 */
	@Test
	public void testGetAlternativesComplete() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		
		StrictPreference prefscomplete = new StrictPreference(createProfileToTest().getAlternativesComplete());
		
		assertTrue(prefscomplete.contains(a1) && prefscomplete.contains(a2) && prefscomplete.contains(a3));
	}

	@Test
	public void testIsComplete() {
		assertTrue(createProfileToTest().isComplete());
	}

	@Test
	public void testGetNbVoterByPreference() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		
		ArrayList<Alternative> list2 = new ArrayList<>();
		list2.add(a3);
		list2.add(a2);
		list2.add(a1);
		StrictPreference pref2 = new StrictPreference(list2);
		
		assertEquals(createProfileToTest().getNbVoterByPreference(pref2), 2);
	}

	@Test
	public void testToSOC() {
		//all these strings are correct (the list of possible strings is not complete but these are the ones that
		//could be given by the functions)
		String soc = "3\n" + "1\n2\n3\n" + "6,6,2\n"  + "2,3,2,1\n" + "4,1,2,3\n";
		String soc2 = "3\n" + "3\n2\n1\n" + "6,6,2\n" + "4,1,2,3\n" + "2,3,2,1\n";
		String soc3 = "3\n" + "1\n2\n3\n" + "6,6,2\n"+ "4,1,2,3\n"  + "2,3,2,1\n" ;
		String soc4 = "3\n" + "3\n2\n1\n" + "6,6,2\n" + "2,3,2,1\n" + "4,1,2,3\n";
		
		StrictProfile prof = createProfileToTest();
		boolean isSoc = prof.toSOC().equals(soc);
		boolean isSoc2 = prof.toSOC().equals(soc2);
		boolean isSoc3 = prof.toSOC().equals(soc3);
		boolean isSoc4 = prof.toSOC().equals(soc4);
		
		assertTrue(isSoc || isSoc2 || isSoc3 || isSoc4);
	}

}
