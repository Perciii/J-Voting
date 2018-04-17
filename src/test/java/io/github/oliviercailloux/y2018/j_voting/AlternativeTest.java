package io.github.oliviercailloux.y2018.j_voting;

import static org.junit.Assert.*;
import org.junit.Test;

public class AlternativeTest {


	@Test
	public void testGetId() {
		Alternative a = new Alternative(7);
		assertEquals(a.getId(),7);
	}

	@Test
	public void testEqualsAlternative() {
		Alternative a = new Alternative(7);
		Alternative b = new Alternative(7);
		assertTrue(a.equals(b));
	}

	@Test
	public void testToString() {
		Alternative a = new Alternative(7);
		assertEquals(a.toString(),Integer.toString(7));
	}

}
