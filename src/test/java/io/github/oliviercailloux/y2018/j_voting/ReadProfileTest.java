package io.github.oliviercailloux.y2018.j_voting;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.Test;

public class ReadProfileTest {

	@Test
	public void testFromSOCorSOI() throws IOException {
		List<String> expectedResultSOC = new ArrayList<String>();
		List<String> expectedResultSOI = new ArrayList<String>();
		expectedResultSOC.add("5");
		expectedResultSOC.add("1");
		expectedResultSOC.add("5");
		expectedResultSOC.add("9");
		expectedResultSOC.add("1");
		expectedResultSOC.add("8");
		expectedResultSOC.add("2, 2, 2");
		expectedResultSOC.add("1, 1, 5, 9, 1, 8");
		expectedResultSOC.add("1, 1, 5, 9, 1, 8");
		
		expectedResultSOI.add("4");
		expectedResultSOI.add("1");
		expectedResultSOI.add("3");
		expectedResultSOI.add("42");
		expectedResultSOI.add("100,100,5");
		expectedResultSOI.add("42,42,3,2");
		expectedResultSOI.add("12,3,42,1,2");
		expectedResultSOI.add("8,2,3,1");
		expectedResultSOI.add("22,3,42,2");
		expectedResultSOI.add("15,1,2");
		assertTrue(expectedResultSOC.equals(ReadProfile.fromSOCorSOI("~/src/main/resources/io/github/oliviercailloux/y2018/j_voting/profil.soc")) && expectedResultSOI.equals(ReadProfile.fromSOCorSOI("~/src/main/resources/io/github/oliviercailloux/y2018/j_voting/profil.soi")));
	}

/*	@Test
	public void testDisplayProfileFromReadFile() {
		fail("Not yet implemented");
	}*/

	@Test
	public void testGetAlternatives() {
		List<String> file = new ArrayList<String>();
		file.add("1");
		file.add("2");
		file.add("3");
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		List<Alternative> alternatives = new ArrayList<Alternative>();
		alternatives.add(a1);
		alternatives.add(a2);
		alternatives.add(a3);
		StrictPreference pref = new StrictPreference(alternatives);
		assertTrue(pref.equals(ReadProfile.getAlternatives(3, file)));
	}

	@Test
	public void testGetStatsVoters() {
		String s = "4,4,3";
		assertTrue(ReadProfile.getStatsVoters(s).get(0) == 4 && ReadProfile.getStatsVoters(s).get(1) == 4 && ReadProfile.getStatsVoters(s).get(2) == 3);
	}

	@Test
	public void testGetPreferences() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		List<Alternative> alternatives = new ArrayList<Alternative>();
		alternatives.add(a1);
		alternatives.add(a2);
		alternatives.add(a3);
		StrictPreference pref = new StrictPreference(alternatives);
		List<Alternative> alternatives2 = new ArrayList<Alternative>();
		alternatives2.add(a2);
		alternatives2.add(a1);
		alternatives2.add(a3);
		StrictPreference pref2 = new StrictPreference(alternatives2);
		assertTrue(pref.equals(ReadProfile.getPreferences(pref2, "4,1,2,3")));
	}

	@Test
	public void testAddVotes() {
		StrictProfile profile = new StrictProfile();
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Voter v1 = new Voter(1);
		Voter v2 = new Voter(2);
		List<Alternative> alternatives = new ArrayList<Alternative>();
		alternatives.add(a1);
		alternatives.add(a2);
		alternatives.add(a3);
		StrictPreference pref = new StrictPreference(alternatives);
		ReadProfile.addVotes(pref,2,profile);
		assertTrue(profile.contains(v1) && profile.contains(v2) && profile.getPreference(v1).equals(pref) && profile.getPreference(v2).equals(pref));
	}

	@Test
	public void testBuildProfile() {
		List<String> file = new ArrayList<String>();
		file.add("2,1,2,3");
		file.add("1,3,2,1");
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Voter v1 = new Voter(1);
		Voter v2 = new Voter(2);
		Voter v3 = new Voter(3);
		List<Alternative> alternatives = new ArrayList<Alternative>();
		alternatives.add(a1);
		alternatives.add(a2);
		alternatives.add(a3);
		StrictPreference pref = new StrictPreference(alternatives);
		List<Alternative> alternatives2 = new ArrayList<Alternative>();
		alternatives2.add(a3);
		alternatives2.add(a2);
		alternatives2.add(a1);
		StrictPreference pref2 = new StrictPreference(alternatives2);
		StrictProfile profile = ReadProfile.buildProfile(file,pref,3);
		assertTrue(profile.contains(v1) && profile.contains(v2) && profile.contains(v3) && profile.getPreference(v1).equals(pref) && profile.getPreference(v2).equals(pref) && profile.getPreference(v3).equals(pref2));
	}

	@Test
	public void testCreateProfileFromReadFile() {
		List<String> fileRead = new ArrayList<String>();
		fileRead.add("3");
		fileRead.add("1");
		fileRead.add("2");
		fileRead.add("3");
		fileRead.add("3,3,2");
		fileRead.add("2,1,2,3");
		fileRead.add("1,3,2,1");
		StrictProfile profile = ReadProfile.createProfileFromReadFile(fileRead);
		
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Voter v1 = new Voter(1);
		Voter v2 = new Voter(2);
		Voter v3 = new Voter(3);
		List<Alternative> alternatives = new ArrayList<Alternative>();
		alternatives.add(a1);
		alternatives.add(a2);
		alternatives.add(a3);
		StrictPreference pref = new StrictPreference(alternatives);
		List<Alternative> alternatives2 = new ArrayList<Alternative>();
		alternatives2.add(a3);
		alternatives2.add(a2);
		alternatives2.add(a1);
		StrictPreference pref2 = new StrictPreference(alternatives2);
		assertTrue(profile.contains(v1) && profile.contains(v2) && profile.contains(v3) && profile.getPreference(v1).equals(pref) && profile.getPreference(v2).equals(pref) && profile.getPreference(v3).equals(pref2));
	}

}
