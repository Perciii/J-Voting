package io.github.oliviercailloux.y2018.j_voting.profiles;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import io.github.oliviercailloux.y2018.j_voting.Alternative;
import io.github.oliviercailloux.y2018.j_voting.StrictPreference;
import io.github.oliviercailloux.y2018.j_voting.Voter;

public class ImmutableStrictProfileTest {

	/**
	 * 
	 * @return an ImmutableStrictProfileI to test
	 */
	public static ImmutableStrictProfile createISPToTest() {
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
		list2.add(a1);
		StrictPreference pref1 = new StrictPreference(list1);
		StrictPreference pref2 = new StrictPreference(list2);
		profile.put(v1, pref1);
		profile.put(v2, pref1);
		profile.put(v3, pref1);
		profile.put(v4, pref1);
		profile.put(v5, pref2);
		profile.put(v6, pref2);
		return new ImmutableStrictProfile(profile);
	}

	@Test
	public void testGetNbAlternatives() {
		assertEquals(createISPToTest().getNbAlternatives(), 3);
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
		assertEquals(alters, createISPToTest().getAlternatives());
	}

}
