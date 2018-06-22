package io.github.oliviercailloux.y2018.j_voting;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AlternativeTest {

	/**
	 * Tests whether method getId() returns the value of the id declared at the
	 * creation of this Alternative
	 */
	@Test
	public void testGetId() {
		Alternative a = new Alternative(7);
		assertEquals(a.getId(), 7);
	}

	/**
	 * Tests whether method equals() returns true for two Alternatives having the
	 * same id
	 */
	@Test
	public void testEqualsAlternative() {
		Alternative a = new Alternative(7);
		Alternative b = new Alternative(7);
		assertEquals(a, b);
	}

	/**
	 * Tests whether method toString() returns the id declared at the creation of
	 * this Alternative
	 */
	@Test
	public void testToString() {
		Alternative a = new Alternative(7);
		assertEquals(a.toString(), Integer.toString(7));
	}
}
