package io.github.oliviercailloux.y2018.j_voting;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class StrictProfileTest {

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
		profile.addProfile(v1,pref1);
		profile.addProfile(v2,pref1);
		profile.addProfile(v3,pref1);
		profile.addProfile(v4,pref1);
		profile.addProfile(v5,pref2);
		profile.addProfile(v6,pref2);
		return profile;
	}
	
	@Test
	public void testAddProfile() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Voter v7 = new Voter(7);
		ArrayList<Alternative> list3 = new ArrayList<Alternative>();
		list3.add(a3);
		list3.add(a1);
		list3.add(a2);
		StrictPreference pref3 = new StrictPreference(list3);
		StrictProfile profile = createProfileToTest();
		profile.addProfile(v7, pref3);
		assertEquals(profile.getPreference(v7),pref3);
	}

/*	@Test
	public void testWriteToSOC() {

	}*/

	@Test
	public void testGetPreference() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Voter v1 = new Voter(1);
		ArrayList<Alternative> list1 = new ArrayList<Alternative>();
		list1.add(a1);
		list1.add(a2);
		list1.add(a3);
		StrictPreference pref1 = new StrictPreference(list1);
		assertEquals(createProfileToTest().getPreference(v1),pref1);
	}

	@Test
	public void testGetAllVoters() {
		Voter v1 = new Voter(1);
		Voter v2 = new Voter(2);
		Voter v3 = new Voter(3);
		Voter v4 = new Voter(4);
		Voter v5 = new Voter(5);
		Voter v6 = new Voter(6);
		Set<Voter> voters = new HashSet<Voter>();
		voters.add(v1);
		voters.add(v2);
		voters.add(v3);
		voters.add(v4);
		voters.add(v5);
		voters.add(v6);
		assertEquals(createProfileToTest().getAllVoters(),voters);
	}

	@Test
	public void testGetNbVoters() {
		assertEquals(createProfileToTest().getNbVoters(),6);
	}

	@Test
	public void testGetSumVoteCount() {
		assertEquals(createProfileToTest().getSumVoteCount(),6);
	}

	@Test
	public void testGetUniquePreferences() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
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
		Iterable<StrictPreference> uniquePref = new LinkedHashSet<StrictPreference>();
		((LinkedHashSet<StrictPreference>) uniquePref).add(pref1);
		((LinkedHashSet<StrictPreference>) uniquePref).add(pref2);
		assertEquals(uniquePref,createProfileToTest().getUniquePreferences());
}

	@Test
	public void testGetNbUniquePreferences() {
		assertEquals(createProfileToTest().getNbUniquePreferences(),2);
	}

	@Test
	public void testGetNbAlternativesComplete() {
		assertEquals(createProfileToTest().getNbAlternativesComplete(),3);
	}

	@Test
	public void testGetAlternativesComplete() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		ArrayList<Alternative> list1 = new ArrayList<Alternative>();
		list1.add(a1);
		list1.add(a2);
		list1.add(a3);
		StrictPreference pref = new StrictPreference(list1);
		assertEquals(pref,createProfileToTest().getAlternativesComplete());
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
		ArrayList<Alternative> list2 = new ArrayList<Alternative>();
		list2.add(a3);
		list2.add(a2);
		list2.add(a1);
		StrictPreference pref2 = new StrictPreference(list2);
		assertEquals(createProfileToTest().getNbVoterByPreference(pref2),2);
	}

	@Test
	public void testToSOC() {
		String soc = "3\n" + "1\n2\n3\n" + "6,6,2\n" + "4,1,2,3\n" + "2,3,2,1";
		assertEquals(createProfileToTest().toSOC(),soc);
	}

}
