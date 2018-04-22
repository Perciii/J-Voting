package io.github.oliviercailloux.y2018.j_voting;

import static org.junit.Assert.*;

import org.junit.Test;

public class VoterTest {

	/**
	 * Tests whether method getId() returns the value of the id declared at the creation of this Voter
	 */
	@Test
	public void testGetId() {
		Voter v = new Voter(4);
		assertEquals(v.getId(), 4);
	}
	
	/**
	 * Tests whether method equals() applied in both ways to two Voters with the same ids returns true
	 */
	@Test
	public void testEquals() {
		Voter v = new Voter(3);
		Voter v2 = new Voter(3);
		assertTrue(v.equals(v2) && v2.equals(v));
	}

}
