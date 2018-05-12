package io.github.oliviercailloux.y2018.j_voting;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.*;
import org.junit.Test;

public class StrictPreference2Test {

	@Test
	public void testListAlternativeToListSetAlternative() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		List<Alternative> prefs = new ArrayList<Alternative>();
		prefs.add(a1);
		prefs.add(a2);
		prefs.add(a3);
		Set<Alternative> set1 = new HashSet<>();
		Set<Alternative> set2 = new HashSet<>();
		Set<Alternative> set3 = new HashSet<>();
		set1.add(a1);
		set2.add(a2);
		set3.add(a3);
		List<Set<Alternative>> list = StrictPreference2.listAlternativeToListSetAlternative(prefs);
		assertTrue(Preference.alternativeSetEqual(set1, list.get(0)) && Preference.alternativeSetEqual(set2, list.get(1)) && Preference.alternativeSetEqual(set3, list.get(2)));
	}

	@Test
	public void testToString() {
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		List<Alternative> prefs = new ArrayList<Alternative>();
		prefs.add(a1);
		prefs.add(a2);
		prefs.add(a3);
		StrictPreference2 strict = new StrictPreference2(prefs);
		assertEquals(strict.toString(),"1,2,3");
	}
}
