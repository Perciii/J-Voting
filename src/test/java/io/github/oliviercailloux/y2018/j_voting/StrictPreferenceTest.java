package io.github.oliviercailloux.y2018.j_voting;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class StrictPreferenceTest {

	/**
	 * creates a StrictPreference to test the class methods
	 * @return the new StrictPreference
	 */
	public static StrictPreference createPrefToTest() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Alternative a4 = new Alternative(4);
		Alternative a5 = new Alternative(5);
		ArrayList<Alternative> prefs = new ArrayList<Alternative>();
		prefs.add(a1);
		prefs.add(a2);
		prefs.add(a3);
		prefs.add(a4);
		prefs.add(a5);
		return new StrictPreference(prefs);
	}
	
	@Test
	public void testGetPreferences() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Alternative a4 = new Alternative(4);
		Alternative a5 = new Alternative(5);
		List<Alternative> preferences = createPrefToTest().getPreferences();
		assertTrue(preferences.get(0).equals(a1) && preferences.get(1).equals(a2) && preferences.get(2).equals(a3) && preferences.get(3).equals(a4) && preferences.get(4).equals(a5));
	}

	@Test
	public void testToString() {
		assertEquals(createPrefToTest().toString(),"[1, 2, 3, 4, 5]");
	}

	@Test
	public void testSize() {
		assertEquals(createPrefToTest().size(),5);
	}

	@Test
	public void testEqualsStrictPreference() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Alternative a4 = new Alternative(4);
		Alternative a5 = new Alternative(5);
		ArrayList<Alternative> prefs = new ArrayList<Alternative>();
		prefs.add(a1);
		prefs.add(a2);
		prefs.add(a3);
		prefs.add(a4);
		prefs.add(a5);
		StrictPreference pref = new StrictPreference(prefs);
		assertTrue(pref.equals(createPrefToTest()));
	}

	@Test
	public void testIsIncludedIn() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		ArrayList<Alternative> prefs = new ArrayList<Alternative>();
		prefs.add(a3);
		prefs.add(a1);
		prefs.add(a2);
		StrictPreference pref = new StrictPreference(prefs);
		assertTrue(pref.isIncludedIn(createPrefToTest()));
	}

	@Test
	public void testHasSameAlternatives() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		Alternative a4 = new Alternative(4);
		Alternative a5 = new Alternative(5);
		ArrayList<Alternative> prefs = new ArrayList<Alternative>();
		prefs.add(a1);
		prefs.add(a3);
		prefs.add(a2);
		prefs.add(a5);
		prefs.add(a4);
		StrictPreference pref = new StrictPreference(prefs);
		assertTrue(pref.hasSameAlternatives(createPrefToTest()));
	}

	@Test
	public void testContains() {
		assertTrue(createPrefToTest().contains(new Alternative(1)));
	}
}
