package io.github.oliviercailloux.y2018.j_voting;

import java.util.*;

import org.slf4j.*;


/**
 * This class is immutable
 * Contains an integer which corresponds to a voting possibility
 */
public class Alternative {
	static Logger log = LoggerFactory.getLogger(Alternative.class.getName());
	private int id;
	
	/**
	 * Creates a new Alternative with the id given as a parameter
	 * @param id int <code>not null</code>
	 */
	public Alternative(int id) {
		this.id = Objects.requireNonNull(id);
	}
	
	/**
	 * @return the id of the Alternative
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @param a an alternative
	 * @return true if both alternatives are equals, ie have the same id, false if not
	 */
	public boolean equals(Alternative a){
		log.debug("Alternative : equals\n");
		Objects.requireNonNull(a);
		log.debug("id of calling alternative : {}, id of alternative parameter : {}\n", this.getId(), a.getId());
		if (a.getId() == this.getId()){
			log.debug("returns true");
			return true;
		}
		log.debug("returns false");
		return false;
	}
	
	@Override
	public String toString(){
		return Integer.toString(id);
	}
}
