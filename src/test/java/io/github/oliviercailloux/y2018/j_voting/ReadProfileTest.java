package io.github.oliviercailloux.y2018.j_voting;

import static org.junit.Assert.*;
import java.io.IOException;
import java.util.*;
import org.junit.Test;

public class ReadProfileTest {

	@Test
	public void testGetAlternatives() {
		ReadProfile rp = new ReadProfile();
		List<String> file = new ArrayList<>();
		file.add("1");
		file.add("2");
		file.add("3");
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		List<Alternative> alternatives = new ArrayList<>();
		alternatives.add(a1);
		alternatives.add(a2);
		alternatives.add(a3);
		StrictPreference pref = new StrictPreference(alternatives);
		assertTrue(pref.equals(rp.getAlternatives(file)));
	}

	@Test
	public void testGetStatsVoters() {
		ReadProfile rp = new ReadProfile();
		String s = "4,4,3";
		assertTrue(rp.getStatsVoters(s).get(0) == 4 && rp.getStatsVoters(s).get(1) == 4 && rp.getStatsVoters(s).get(2) == 3);
	}

	@Test
	public void testGetPreferences() {
		ReadProfile rp = new ReadProfile();
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		List<Alternative> alternatives = new ArrayList<>();
		alternatives.add(a1);
		alternatives.add(a2);
		alternatives.add(a3);
		StrictPreference pref = new StrictPreference(alternatives);
		List<Alternative> alternatives2 = new ArrayList<>();
		alternatives2.add(a2);
		alternatives2.add(a1);
		alternatives2.add(a3);
		StrictPreference pref2 = new StrictPreference(alternatives2);
		assertTrue(pref.equals(rp.getPreferences(pref2, "4,1,2,3")));
	}

	@Test
	public void testAddVotes() {
		ReadProfile rp = new ReadProfile();
		StrictProfileBuilder p = new StrictProfileBuilder();
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Voter v1 = new Voter(1);
		Voter v2 = new Voter(2);
		List<Alternative> alternatives = new ArrayList<>();
		alternatives.add(a1);
		alternatives.add(a2);
		alternatives.add(a3);
		StrictPreference pref = new StrictPreference(alternatives);
		rp.addVotes(pref,2,p);
		StrictProfileI prof = p.createStrictProfileI();
		assertTrue(prof.contains(v1) && prof.contains(v2) && prof.getPreference(v1).equals(pref) && prof.getPreference(v2).equals(pref));
	}

	@Test
	public void testBuildProfile() {
		ReadProfile rp = new ReadProfile();
		List<String> file = new ArrayList<>();
		file.add("2,1,2,3");
		file.add("1,3,2,1");
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Voter v1 = new Voter(1);
		Voter v2 = new Voter(2);
		Voter v3 = new Voter(3);
		List<Alternative> alternatives = new ArrayList<>();
		alternatives.add(a1);
		alternatives.add(a2);
		alternatives.add(a3);
		StrictPreference pref = new StrictPreference(alternatives);
		List<Alternative> alternatives2 = new ArrayList<>();
		alternatives2.add(a3);
		alternatives2.add(a2);
		alternatives2.add(a1);
		StrictPreference pref2 = new StrictPreference(alternatives2);
		StrictProfileI profile = rp.buildProfile(file, pref, 3);
		assertTrue(profile.contains(v1) && profile.contains(v2) && profile.contains(v3) && profile.getPreference(v1).equals(pref) && profile.getPreference(v2).equals(pref) && profile.getPreference(v3).equals(pref2));
	}

	@Test
	public void testCreateProfileFromStream() throws IOException {
		ReadProfile rp = new ReadProfile();
		ProfileI profile = rp.createProfileFromStream(getClass().getResourceAsStream("profileToRead.soc"));
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Voter v1 = new Voter(1);
		Voter v2 = new Voter(2);
		Voter v3 = new Voter(3);
		List<Alternative> alternatives = new ArrayList<>();
		alternatives.add(a1);
		alternatives.add(a2);
		alternatives.add(a3);
		StrictPreference pref = new StrictPreference(alternatives);
		List<Alternative> alternatives2 = new ArrayList<>();
		alternatives2.add(a3);
		alternatives2.add(a2);
		alternatives2.add(a1);
		StrictPreference pref2 = new StrictPreference(alternatives2);
		assertTrue(profile.contains(v1) && profile.contains(v2) && profile.contains(v3) && profile.getPreference(v1).equals(pref) && profile.getPreference(v2).equals(pref) && profile.getPreference(v3).equals(pref2));
	}
}
