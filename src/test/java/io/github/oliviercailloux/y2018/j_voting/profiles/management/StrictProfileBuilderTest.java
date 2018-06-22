package io.github.oliviercailloux.y2018.j_voting.profiles.management;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import io.github.oliviercailloux.y2018.j_voting.Alternative;
import io.github.oliviercailloux.y2018.j_voting.StrictPreference;
import io.github.oliviercailloux.y2018.j_voting.Voter;
import io.github.oliviercailloux.y2018.j_voting.profiles.ImmutableStrictProfileI;

public class StrictProfileBuilderTest {

	@Test
	public void testCreateOneAlternativeProfile() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Alternative a4 = new Alternative(4);
		List<Alternative> list1 = new ArrayList<>();
		List<Alternative> list2 = new ArrayList<>();
		List<Alternative> list3 = new ArrayList<>();
		list1.add(a1);
		list1.add(a2);
		list1.add(a3);
		list2.add(a2);
		list2.add(a1);
		list2.add(a3);
		list2.add(a4);
		list3.add(a3);
		list3.add(a2);
		list3.add(a4);
		StrictPreference p1 = new StrictPreference(list1);
		StrictPreference p2 = new StrictPreference(list2);
		StrictPreference p3 = new StrictPreference(list3);
		Voter v1 = new Voter(1);
		Voter v2 = new Voter(2);
		Voter v3 = new Voter(3);
		Voter v4 = new Voter(4);
		Voter v5 = new Voter(5);
		Voter v6 = new Voter(6);
		StrictProfileBuilder profBuild = new StrictProfileBuilder();
		profBuild.addVote(v1, p1);
		profBuild.addVote(v2, p1);
		profBuild.addVote(v3, p1);
		profBuild.addVote(v4, p2);
		profBuild.addVote(v5, p2);
		profBuild.addVote(v6, p3);
		ImmutableStrictProfileI resultProf = profBuild.createOneAlternativeProfile();

		Map<Voter, StrictPreference> map = new HashMap<>();
		List<Alternative> l1 = new ArrayList<>();
		List<Alternative> l2 = new ArrayList<>();
		List<Alternative> l3 = new ArrayList<>();
		l1.add(a1);
		l2.add(a2);
		l3.add(a3);
		StrictPreference pref1 = new StrictPreference(l1);
		StrictPreference pref2 = new StrictPreference(l2);
		StrictPreference pref3 = new StrictPreference(l3);
		map.put(v1, pref1);
		map.put(v2, pref1);
		map.put(v3, pref1);
		map.put(v4, pref2);
		map.put(v5, pref2);
		map.put(v6, pref3);
		ImmutableStrictProfileI profile = new ImmutableStrictProfileI(map);
		assertEquals(resultProf, profile);
	}

}
