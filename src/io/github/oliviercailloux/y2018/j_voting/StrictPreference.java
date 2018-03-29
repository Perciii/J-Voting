package io.github.oliviercailloux.y2018.j_voting;

import java.util.*;
import java.lang.Iterable;

/**
 * This class is immutable
 * Contains a list of Alternatives sorted by preferences 
 * Two alternatives can't be equally ranked
 * You can't store the same alternative several times in the list 
 * Every alternative is an integer and corresponds to a voting choice
 */
public class StrictPreference {
	
	private Iterable<Alternative> preferences= new LinkedHashSet<>();
	
	/**
	 * Creates a set of strict preferences with the set given as a parameter
	 * @param preferences a set of alternatives
	 */
	public StrictPreference(Iterable<Alternative> preferences) {
		this.preferences = Objects.requireNonNull(preferences);
		Iterator<Alternative> iterator = preferences.iterator();
		while (iterator.hasNext()) {
		      Alternative CurrentAlternative = iterator.next();
		      if (!(((LinkedHashSet<Alternative>) this.preferences).contains(CurrentAlternative))){
		    	  //((LinkedHashSet)this.preferences).add(CurrentAlternative);
			  }
		}
		
	}
	
	/**
	 * @return the list of strict preferences
	 */
	public Iterable<Alternative> getPreferences() {
		return preferences;
	}
	
	public String toString(){
		Iterator<Alternative> iterator = preferences.iterator();
		String s = "";
		Alternative CurrentAlternative;
		while(iterator.hasNext()){
			CurrentAlternative = iterator.next();
		      //System.out.println(CurrentAlternative.toString());
		      s=s+", "+CurrentAlternative.toString();
		      //System.out.println(s+" "+size);
		}
		return s;
	}
	
	public int size() {
		return ((LinkedHashSet<Alternative>) preferences).size();
	}

}
