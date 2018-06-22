package io.github.oliviercailloux.y2018.j_voting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * This class is immutable Contains an integer which corresponds to a voting
 * possibility
 */
public class Alternative {
	private static final Logger LOGGER = LoggerFactory.getLogger(Alternative.class.getName());
	private int id;

	/**
	 * Creates a new Alternative with the id given as a parameter
	 * 
	 * @param id
	 *            int <code>not null</code>
	 */
	public Alternative(int id) {
		this.id = Preconditions.checkNotNull(id);
	}

	/**
	 * @return the id of the Alternative
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param a
	 *            an alternative
	 * @return true if both alternatives are equals, i.e. have the same id, false if
	 *         not
	 */
	@Override
	public boolean equals(Object o) {
		LOGGER.debug("Alternative : equals");
		Preconditions.checkNotNull(o);
		if (!(o instanceof Alternative)) {
			LOGGER.debug("returns false");
			return false;
		}
		Alternative a = (Alternative) o;
		LOGGER.debug("id of calling alternative : {}, id of alternative parameter : {}", this.getId(), a.getId());
		if (a.getId() == this.getId()) {
			LOGGER.debug("returns true");
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return Integer.toString(id);
	}

	@Override
	public int hashCode() {
		return id;
	}
}
