package io.github.oliviercailloux.y2018.j_voting;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test;

public class DictatorTest {

	@Test
	public void getSocietyPreferenceTest() {
		Voter v1 = new Voter(1);
		Voter v2 = new Voter(2);
		Voter v3 = new Voter(3);
		
		Dictator d1 = new Dictator(v1);
		
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		
		List<Set<Alternative>> list1 = new ArrayList<>();
		List<Set<Alternative>> list2 = new ArrayList<>();
		List<Set<Alternative>> list3 = new ArrayList<>();
		
		Set<Alternative> s1 = new HashSet<>();
		Set<Alternative> s2 = new HashSet<>();
		Set<Alternative> s3 = new HashSet<>();
		
		s1.add(a1);
		s1.add(a2);
		s2.add(a3);
		s3.add(a1);
		s3.add(a3);
		
		list1.add(s1);
		list1.add(s2);		
		list2.add(s2);
		list3.add(s2);
		list3.add(s3);
		
		Preference pref1 = new Preference(list1);
		Preference pref2 = new Preference(list2);
		Preference pref3 = new Preference(list3);
		
		ProfileBuilder prof = new ProfileBuilder();
		
		prof.addProfile(v1, pref1);
		prof.addProfile(v2, pref2);
		prof.addProfile(v3, pref3);
	
		ProfileI profile = prof.createProfileI();
		
		assertEquals(d1.getSocietyPreference(profile),pref1);
	
	}
	
	
}
