package io.github.oliviercailloux.y2018.j_voting;

import java.util.*;

/**
 * This class is immutable
 * Contains an integer which corresponds to a voting possibility
 */
public class Alternative {
	
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
	 * 
	 * @param a
	 * @return 1 if the id are the same 
	 */
	
	/*@Override
	public boolean equals(Alternative a){
		if (a.getId()==this.getId()){
			return true;
		}
		return false;
	}*/
	 
	//hashcode
	
	
	
	public String toString(){
		return Integer.toString(id);
	}
}
