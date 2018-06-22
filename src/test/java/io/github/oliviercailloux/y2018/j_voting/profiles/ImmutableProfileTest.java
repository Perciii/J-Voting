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
import io.github.oliviercailloux.y2018.j_voting.Voter;

public class ImmutableProfileTest {

	public static ImmutableProfile createIPToTest() {
		Map<Voter, Preference> profile = new HashMap<>();
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Voter v1 = new Voter(1);
		Voter v2 = new Voter(2);
		Voter v3 = new Voter(3);
		Voter v4 = new Voter(4);
		Voter v5 = new Voter(5);
		Voter v6 = new Voter(6);
		List<Set<Alternative>> list1 = new ArrayList<>();
		List<Set<Alternative>> list2 = new ArrayList<>();
		Set<Alternative> s1 = new HashSet<>();
		Set<Alternative> s2 = new HashSet<>();
		Set<Alternative> s3 = new HashSet<>();
		Set<Alternative> s4 = new HashSet<>();
		s1.add(a1);
		s1.add(a2);
		s2.add(a3);
		s3.add(a2);
		s4.add(a1);
		s4.add(a3);
		list1.add(s1);
		list1.add(s2);
		list2.add(s3);
		list2.add(s4);
		Preference pref1 = new Preference(list1);
		Preference pref2 = new Preference(list2);
		profile.put(v1, pref1);
		profile.put(v2, pref1);
		profile.put(v3, pref1);
		profile.put(v4, pref1);
		profile.put(v5, pref2);
		profile.put(v6, pref2);
		return new ImmutableProfile(profile);
	}

	@Test
	public void testGetNbAlternatives() {
		assertEquals(createIPToTest().getNbAlternatives(), 3);
	}

	@Test
	public void testGetAlternatives() {
		Set<Alternative> alters = new HashSet<>();
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		alters.add(a1);
		alters.add(a2);
		alters.add(a3);
		assertEquals(alters, createIPToTest().getAlternatives());
	}
}
