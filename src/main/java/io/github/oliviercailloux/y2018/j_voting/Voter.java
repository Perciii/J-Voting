package io.github.oliviercailloux.y2018.j_voting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * This class is immutable Contains an integer being the id of the voter
 */
public class Voter implements Comparable<Voter> {
	private int id;
	private static final Logger LOGGER = LoggerFactory.getLogger(Voter.class.getName());

	/**
	 * Creates a new object Voter with the id given as a parameter
	 * 
	 * @param id
	 *            int <code>not null</code>
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
	 * @param voter
	 *            <code> not null</code>
	 * @return whether two voters are equal, ie have the same id.
	 */
	@Override
	public boolean equals(Object voter) {
		LOGGER.debug("Voter - equals : ");
		Preconditions.checkNotNull(voter);
		if (!(voter instanceof Voter)) {
			return false;
		}
		Voter v = (Voter) voter;
		return (v.getId() == id);
	}

	@Override
	public int hashCode() {
		return id;
	}

	/**
	 * 
	 * 
	 * @param v2
	 *            not <code> null </code>
	 * @return an integer : 0 if the voters have the same id, <0 if the calling
	 *         voter is smaller than the parameter, else >0.
	 */
	@Override
	public int compareTo(Voter v2) {
		LOGGER.debug("compare:");
		Preconditions.checkNotNull(v2);
		LOGGER.debug("calling voter : v1 {}, parameter v2 {}", this.getId(), v2.getId());
		return this.getId() - v2.getId();
	}
}
