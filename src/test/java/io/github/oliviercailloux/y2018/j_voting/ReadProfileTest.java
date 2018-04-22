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
		expectedResultSOC.add("3");
		expectedResultSOC.add("1,Shrek (Full-screen) ");
		expectedResultSOC.add("2,The X-Files: Season 2 ");
		expectedResultSOC.add("3,The Punisher ");
		expectedResultSOC.add("664,664,6");
		expectedResultSOC.add("263,2,1,3");
		expectedResultSOC.add("249,1,2,3");
		expectedResultSOC.add("78,1,3,2");
		expectedResultSOC.add("46,2,3,1");
		expectedResultSOC.add("17,3,1,2");
		expectedResultSOC.add("11,3,2,1");
		List<String> actualResultSOC = ReadProfile.fromSOCorSOI("io/github/oliviercailloux/y2018/j_voting/profil.soc");
		
		expectedResultSOI.add("3");
		expectedResultSOI.add("1,Candidate 1 ");
		expectedResultSOI.add("2,Candidate 2 ");
		expectedResultSOI.add("3,Candidate 3 ");
		expectedResultSOI.add("129,129,8");
		expectedResultSOI.add("52,1,3,2");
		expectedResultSOI.add("48,1");
		expectedResultSOI.add("11,1,2,3");
		expectedResultSOI.add("5,3,2,1");
		expectedResultSOI.add("4,2");
		expectedResultSOI.add("4,3,1,2");
		expectedResultSOI.add("3,2,3,1");
		expectedResultSOI.add("2,3");
		List<String> actualResultSOI = ReadProfile.fromSOCorSOI("io/github/oliviercailloux/y2018/j_voting/profil.soi");
		assertTrue(expectedResultSOC.equals(actualResultSOC) && expectedResultSOI.equals(actualResultSOI));
	}

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
		StrictProfile p = new StrictProfile();
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
		ReadProfile.addVotes(pref,2,p);
		assertTrue(p.contains(v1) && p.contains(v2) && p.getPreference(v1).equals(pref) && p.getPreference(v2).equals(pref));
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
