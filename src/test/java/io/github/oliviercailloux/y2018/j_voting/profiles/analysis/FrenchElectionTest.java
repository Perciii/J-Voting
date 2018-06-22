package io.github.oliviercailloux.y2018.j_voting.profiles.analysis;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import io.github.oliviercailloux.y2018.j_voting.Alternative;
import io.github.oliviercailloux.y2018.j_voting.Preference;
import io.github.oliviercailloux.y2018.j_voting.StrictPreference;
import io.github.oliviercailloux.y2018.j_voting.Voter;
import io.github.oliviercailloux.y2018.j_voting.profiles.ImmutableStrictProfileI;
import io.github.oliviercailloux.y2018.j_voting.profiles.management.StrictProfileBuilder;

public class FrenchElectionTest {

	@Test
	public void testGetSocietyPreference() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Alternative a4 = new Alternative(4);
		List<Set<Alternative>> l1 = new ArrayList<>();
		Set<Alternative> s1 = new HashSet<>();
		Set<Alternative> s2 = new HashSet<>();
		Set<Alternative> s3 = new HashSet<>();
		s1.add(a1);
		s2.add(a2);
		s3.add(a3);
		l1.add(s1);
		l1.add(s2);
		l1.add(s3);
		Preference pref1 = new Preference(l1);

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
		ImmutableStrictProfileI resultProf = (ImmutableStrictProfileI) profBuild.createStrictProfileI();

		assertEquals(pref1, new FrenchElection().getSocietyPreference(resultProf));
	}

}
