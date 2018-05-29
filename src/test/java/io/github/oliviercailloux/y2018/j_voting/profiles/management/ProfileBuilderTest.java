package io.github.oliviercailloux.y2018.j_voting.profiles.management;

import static org.junit.Assert.*;
import io.github.oliviercailloux.y2018.j_voting.*;
import io.github.oliviercailloux.y2018.j_voting.profiles.*;
import java.util.*;
import org.junit.Test;

public class ProfileBuilderTest {

	
	@Test
	public void testCreateProfileI() {
		//---beginning of creation of a ProfileI with ProfileBuilder
		ProfileBuilder profileBuilder = new ProfileBuilder();
		
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
		
		profileBuilder.addVote(v1, pref1);
		profileBuilder.addVote(v2, pref1);
		profileBuilder.addVote(v3, pref1);
		profileBuilder.addVote(v4, pref1);
		profileBuilder.addVote(v5, pref2);
		profileBuilder.addVote(v6, pref2);
	
		ProfileI immutableProfileI = profileBuilder.createProfileI();
		//---end of creation of  a ProfileI with ProfileBuilder
		
		ImmutableProfileI testProfileI = ImmutableProfileITest.createIPIToTest();
		
		assertEquals(immutableProfileI,testProfileI);
	}

	@Test
	public void testCreateProfile() {
		//---beginning of creation of a Profile with ProfileBuilder
		ProfileBuilder profileBuilder = new ProfileBuilder();
		
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
		
		profileBuilder.addVote(v1, pref1);
		profileBuilder.addVote(v2, pref1);
		profileBuilder.addVote(v3, pref1);
		profileBuilder.addVote(v4, pref1);
		profileBuilder.addVote(v5, pref2);
		profileBuilder.addVote(v6, pref2);
		
		Profile immutableProfile = profileBuilder.createProfile();
		//---end of creation of a Profile with ProfileBuilder
		
		ImmutableProfile testProfile = ImmutableProfileTest.createIPToTest();
		
		assertEquals(immutableProfile,testProfile);
	}

	@Test
	public void testCreateStrictProfileI() {
		//---beginning of creation of a StrictProfileI with ProfileBuilder
		ProfileBuilder profileBuilder = new ProfileBuilder();
		
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
		
		profileBuilder.addVote(v1, pref1);
		profileBuilder.addVote(v2, pref1);
		profileBuilder.addVote(v3, pref1);
		profileBuilder.addVote(v4, pref1);
		profileBuilder.addVote(v5, pref2);
		profileBuilder.addVote(v6, pref2);
		
		StrictProfileI immutableStrictProfileI = profileBuilder.createStrictProfileI();
		//---end of creation of a StrictProfileI with ProfileBuilder
		
		ImmutableStrictProfileI testStrictProfileI = ImmutableStrictProfileITest.createISPIToTest();
		
		assertEquals(immutableStrictProfileI,testStrictProfileI);
	}

	@Test
	public void testCreateStrictProfile() {
		//---beginning of creation of a StrictProfile with ProfileBuilder
		ProfileBuilder profileBuilder = new ProfileBuilder();
		
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
		
		profileBuilder.addVote(v1, pref1);
		profileBuilder.addVote(v2, pref1);
		profileBuilder.addVote(v3, pref1);
		profileBuilder.addVote(v4, pref1);
		profileBuilder.addVote(v5, pref2);
		profileBuilder.addVote(v6, pref2);
		
		StrictProfile immutableStrictProfile = profileBuilder.createStrictProfile();
		//---end of creation of a StrictProfile with ProfileBuilder
		
		ImmutableStrictProfile testStrictProfile = ImmutableStrictProfileTest.createISPToTest();
		
		assertEquals(immutableStrictProfile,testStrictProfile);
	}

}