package io.github.oliviercailloux.y2018.j_voting.profiles;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import io.github.oliviercailloux.y2018.j_voting.Alternative;
import io.github.oliviercailloux.y2018.j_voting.Preference;
import io.github.oliviercailloux.y2018.j_voting.StrictPreference;
import io.github.oliviercailloux.y2018.j_voting.Voter;

public class ImmutableStrictProfileITest {

	/**
	 * 
	 * @return an ImmutableStrictProfileI to test
	 */
	public static ImmutableStrictProfileI createISPIToTest() {
		Map<Voter, StrictPreference> profile = new HashMap<>();
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
		StrictPreference pref1 = new StrictPreference(list1);
		StrictPreference pref2 = new StrictPreference(list2);
		profile.put(v1, pref1);
		profile.put(v2, pref1);
		profile.put(v3, pref1);
		profile.put(v4, pref1);
		profile.put(v5, pref2);
		profile.put(v6, pref2);
		return new ImmutableStrictProfileI(profile);
	}

	/**
	 * 
	 * @return a map of Voter and Preference to test
	 */
	public static Map<Voter, Preference> createNonStrictMap() {
		Map<Voter, Preference> map = new HashMap<>();
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Voter v1 = new Voter(1);
		Voter v2 = new Voter(2);
		Voter v3 = new Voter(3);
		Set<Alternative> s1 = new HashSet<>();
		Set<Alternative> s2 = new HashSet<>();
		Set<Alternative> s3 = new HashSet<>();
		s1.add(a1);
		s2.add(a2);
		s3.add(a3);
		List<Set<Alternative>> list1 = new ArrayList<>();
		List<Set<Alternative>> list2 = new ArrayList<>();
		list1.add(s1);
		list1.add(s2);
		list1.add(s3);
		list2.add(s3);
		list2.add(s2);
		list2.add(s1);
		Preference p1 = new Preference(list1);
		Preference p2 = new Preference(list2);
		map.put(v1, p1);
		map.put(v2, p1);
		map.put(v3, p2);
		return map;
	}

	/**
	 * 
	 * @return a map of Voter and StrictPreference to test
	 */
	public static Map<Voter, StrictPreference> createStrictMap() {
		Map<Voter, StrictPreference> map = new HashMap<>();
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Voter v1 = new Voter(1);
		Voter v2 = new Voter(2);
		Voter v3 = new Voter(3);
		List<Alternative> list1 = new ArrayList<>();
		List<Alternative> list2 = new ArrayList<>();
		list1.add(a1);
		list1.add(a2);
		list1.add(a3);
		list2.add(a3);
		list2.add(a2);
		list2.add(a1);
		StrictPreference p1 = new StrictPreference(list1);
		StrictPreference p2 = new StrictPreference(list2);
		map.put(v1, p1);
		map.put(v2, p1);
		map.put(v3, p2);
		return map;
	}

	@Test
	public void testGetPreferenceVoter() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Voter v1 = new Voter(1);
		ArrayList<Alternative> list1 = new ArrayList<>();
		list1.add(a1);
		list1.add(a2);
		list1.add(a3);
		StrictPreference pref1 = new StrictPreference(list1);
		assertEquals(pref1, createISPIToTest().getPreference(v1));
	}
}
