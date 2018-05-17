package io.github.oliviercailloux.y2018.j_voting;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;



/**
 * This class is immutable
 * Contains an integer being the id of the voter
 */
public class Voter {
	private int id;
	private static Logger LOGGER = LoggerFactory.getLogger(Voter.class.getName());
	
	/**
	 * Creates a new object Voter with the id given as a parameter
	 * @param id int <code>not null</code>
	 */
	public Voter(int id) {
		this.id = Preconditions.checkNotNull(id);
	}
	
	/**
	 * @return the id of the object Voter
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @param voter <code> not null</code>
	 * @return whether two voters are equal, ie have the same id.
	 */
	@Override
	public boolean equals(Object voter) {
		LOGGER.debug("Voter - equals : ");
		Preconditions.checkNotNull(voter);
		if(!(voter instanceof Voter)) {
			return false;
		}
		Voter v = (Voter) voter;
		return(v.getId() == id);
	}
	
	@Override
	public int hashCode() {
		return id;
	}
}
