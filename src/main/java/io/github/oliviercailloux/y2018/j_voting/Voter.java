package io.github.oliviercailloux.y2018.j_voting;

import java.util.Objects;



/**
 * This class is immutable
 * Contains an integer being the id of the voter
 */
public class Voter {
	private int id;
	
	/**
	 * Creates a new object Voter with the id given as a parameter
	 * @param id int <code>not null</code>
	 */
	public Voter(int id) {
		this.id = Objects.requireNonNull(id);
	}
	
	/**
	 * @return the id of the object Voter
	 */
	public int getId() {
		return id;
	}
}
