package io.github.oliviercailloux.y2018.j_voting;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test;

public class PreferenceTest {

	public static Preference createPreferenceToTest() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Alternative a4 = new Alternative(4);
		Alternative a5 = new Alternative(5);
		Set<Alternative> set1 = new HashSet<>();
		Set<Alternative> set2 = new HashSet<>();
		Set<Alternative> set3 = new HashSet<>();
		set1.add(a1);
		set1.add(a2);
		set2.add(a3);
		set3.add(a4);
		set3.add(a5);
		List<Set<Alternative>> list = new ArrayList<>();
		list.add(set1);
		list.add(set2);
		list.add(set3);
		return new Preference(list);
	}
	
	@Test
	public void testGetPreferencesNonStrict() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Alternative a4 = new Alternative(4);
		Alternative a5 = new Alternative(5);
		Set<Alternative> set1 = new HashSet<>();
		Set<Alternative> set2 = new HashSet<>();
		Set<Alternative> set3 = new HashSet<>();
		set1.add(a1);
		set1.add(a2);
		set2.add(a3);
		set3.add(a4);
		set3.add(a5);
		List<Set<Alternative>> pref = createPreferenceToTest().getPreferencesNonStrict();
		assertTrue(Preference.alternativeSetEqual(pref.get(0),set1) && Preference.alternativeSetEqual(pref.get(1),set2) && Preference.alternativeSetEqual(pref.get(2),set3));
	}

	@Test
	public void testToString() {
		String s1 = "{1,2},{3},{4,5}";
		String s2 = "{2,1},{3},{4,5}";
		String s3 = "{1,2},{3},{5,4}";
		String s4 = "{2,1},{3},{5,4}";
		String pref = createPreferenceToTest().toString();
		assertTrue(pref.equals(s1) || pref.equals(s2) || pref.equals(s3) || pref.equals(s4));
	}

	@Test
	public void testSize() {
		assertEquals(createPreferenceToTest().size(),5);
	}

	@Test
	public void testEqualsPreference() {
		Preference p1 = createPreferenceToTest();
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Alternative a4 = new Alternative(4);
		Alternative a5 = new Alternative(5);
		Set<Alternative> set1 = new HashSet<>();
		Set<Alternative> set2 = new HashSet<>();
		Set<Alternative> set3 = new HashSet<>();
		set1.add(a1);
		set1.add(a2);
		set2.add(a3);
		set3.add(a4);
		set3.add(a5);
		List<Set<Alternative>> list = new ArrayList<>();
		list.add(set1);
		list.add(set2);
		list.add(set3);
		Preference p2 = new Preference(list);
		assertTrue(p1.equals(p2));
	}

	@Test
	public void testContains() {
		Alternative a = new Alternative(4);
		assertTrue(createPreferenceToTest().contains(a));
	}

	@Test
	public void testHasSameAlternatives() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Alternative a4 = new Alternative(4);
		Alternative a5 = new Alternative(5);
		Set<Alternative> set1 = new HashSet<>();
		Set<Alternative> set2 = new HashSet<>();
		Set<Alternative> set3 = new HashSet<>();
		set1.add(a1);
		set2.add(a2);
		set2.add(a3);
		set2.add(a4);
		set3.add(a5);
		List<Set<Alternative>> list = new ArrayList<>();
		list.add(set1);
		list.add(set2);
		list.add(set3);
		Preference pref = new Preference(list);
		assertTrue(pref.hasSameAlternatives(createPreferenceToTest()));
	}

	@Test
	public void testIsIncludedIn() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Alternative a4 = new Alternative(4);
		Set<Alternative> set1 = new HashSet<>();
		Set<Alternative> set2 = new HashSet<>();
		Set<Alternative> set3 = new HashSet<>();
		set1.add(a1);
		set1.add(a2);
		set2.add(a3);
		set3.add(a4);
		List<Set<Alternative>> list = new ArrayList<>();
		list.add(set1);
		list.add(set2);
		list.add(set3);
		Preference p = new Preference(list);
		assertTrue(p.isIncludedIn(createPreferenceToTest()));
	}

	@Test
	public void testToAlternativeSet() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Alternative a4 = new Alternative(4);
		Alternative a5 = new Alternative(5);
		Set<Alternative> set = new HashSet<>();
		set.add(a1);
		set.add(a2);
		set.add(a3);
		set.add(a4);
		set.add(a5);
		assertTrue(Preference.alternativeSetEqual(Preference.toAlternativeSet(createPreferenceToTest().getPreferencesNonStrict()),set));
	}

	@Test
	public void testAlternativeSetEqual() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Alternative a4 = new Alternative(4);
		Alternative a5 = new Alternative(5);
		Set<Alternative> set = new HashSet<>();
		set.add(a1);
		set.add(a2);
		set.add(a3);
		set.add(a4);
		set.add(a5);
		Set<Alternative> set2 = new HashSet<>();
		set2.add(a3);
		set2.add(a4);
		set2.add(a1);
		set2.add(a2);
		set2.add(a5);
		assertTrue(Preference.alternativeSetEqual(set,set2));
	}

	@Test
	public void testAlternativeSetContains() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Alternative a4 = new Alternative(4);
		Alternative a5 = new Alternative(5);
		Set<Alternative> set = new HashSet<>();
		set.add(a1);
		set.add(a2);
		set.add(a3);
		set.add(a4);
		set.add(a5);
		assertTrue(Preference.alternativeSetContains(set, a3));
	}

	@Test
	public void testSizeListSetAlternative() {
		assertEquals(Preference.size(createPreferenceToTest().getPreferencesNonStrict()), 5);
	}
}
