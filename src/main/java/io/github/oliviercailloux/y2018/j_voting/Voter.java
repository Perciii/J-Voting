package io.github.oliviercailloux.y2018.j_voting;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * This class is immutable
 * Contains an integer being the id of the voter
 */
public class Voter {
	private int id;
	static Logger log = LoggerFactory.getLogger(ReadProfile.class.getName());
	
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
	
	/**
	 * 
	 * @param voter
	 * @return whether two voters are equal, ie have the same id.
	 */
	public boolean equals(Voter voter) {
		Objects.requireNonNull(voter);
		log.debug("equals : \n");
		log.debug("voter calling : {}, parameter voter :{}\n", id, voter.getId());
		return(voter.getId() == id);
	}
}
