package io.github.oliviercailloux.y2018.j_voting;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class StrictPreferenceTest {

	@Test
	public void testGetPreferences() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		List<Alternative> prefs = new ArrayList<>();
		prefs.add(a1);
		prefs.add(a2);
		prefs.add(a3);
		StrictPreference p = new StrictPreference(prefs);
		assertEquals(p.getAlternatives(), prefs);
	}

	@Test
	public void testListAlternativeToListSetAlternative() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		List<Alternative> prefs = new ArrayList<>();
		prefs.add(a1);
		prefs.add(a2);
		prefs.add(a3);
		Set<Alternative> set1 = new HashSet<>();
		Set<Alternative> set2 = new HashSet<>();
		Set<Alternative> set3 = new HashSet<>();
		set1.add(a1);
		set2.add(a2);
		set3.add(a3);
		List<Set<Alternative>> list = StrictPreference.listAlternativeToListSetAlternative(prefs);
		assertEquals(set1, list.get(0));
		assertEquals(set2, list.get(1));
		assertEquals(set3, list.get(2));
	}

	@Test
	public void testToString() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		List<Alternative> prefs = new ArrayList<>();
		prefs.add(a1);
		prefs.add(a2);
		prefs.add(a3);
		StrictPreference strict = new StrictPreference(prefs);
		assertEquals(strict.toString(), "1,2,3");
	}

	@Test
	public void testGetAlternative() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		List<Alternative> prefs = new ArrayList<>();
		prefs.add(a1);
		prefs.add(a2);
		prefs.add(a3);
		StrictPreference strict = new StrictPreference(prefs);
		assertEquals(strict.getAlternative(1), a2);
	}
}
