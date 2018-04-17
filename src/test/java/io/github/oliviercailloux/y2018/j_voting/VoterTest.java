package io.github.oliviercailloux.y2018.j_voting;

import static org.junit.Assert.*;

import org.junit.Test;

public class VoterTest {

	@Test
	public void testGetId() {
		Voter v = new Voter(4);
		assertEquals(v.getId(),4);
	}

}
