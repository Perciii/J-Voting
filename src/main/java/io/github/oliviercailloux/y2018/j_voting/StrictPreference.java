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
	
	private Iterable<Alternative> preferences= new LinkedHashSet<Alternative>();
	
	/**
	 * Creates a set of strict preferences with the set given as a parameter
	 * @param preferences a set of alternatives
	 */
	public StrictPreference(Iterable<Alternative> preference) {
		preferences = new LinkedHashSet<Alternative>(); 
		Objects.requireNonNull(preference); 		
		boolean bool = false;
		Iterator<Alternative> iterator = preference.iterator();
		Iterator<Alternative> iterator2 = preferences.iterator();
		while (iterator.hasNext()) {
		      Alternative CurrentAlternative = iterator.next();
		      iterator2 = preferences.iterator();
		      while(iterator2.hasNext()){
		    	  Alternative CurrentAlternative2 = iterator2.next();
		    	  //System.out.println("current alternative : " + CurrentAlternative + "\n preference : " + preferences+"\n this.preference : " + this.preferences);
		    	  //System.out.println("id :" + CurrentAlternative.getId() + " id2 :" + CurrentAlternative2.getId());
		    	  if(CurrentAlternative.getId() == CurrentAlternative2.getId()){
		    		  //System.out.println("id :" + CurrentAlternative.getId() + "id2 :" + CurrentAlternative2.getId());
		    		  bool = true;
		    	  }
		      }
		     
		      if(bool){
		    	  bool = false;
		    	  //System.out.println("true");
		      }
		      else{
		    	  ((LinkedHashSet<Alternative>) this.preferences).add(CurrentAlternative);
		    	  //System.out.println("false");
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
		      s = s + ", "+CurrentAlternative.toString();
		}
		return s;
	}
	
	public int size() {
		return ((LinkedHashSet<Alternative>) preferences).size();
	}
	
	public boolean equals(StrictPreference pref) {
		Objects.requireNonNull(pref);
		Iterator<Alternative> i1 = preferences.iterator();
		Iterator<Alternative> i2 = pref.getPreferences().iterator();
		while(i1.hasNext()) {
			if(!i2.hasNext()) {
				return false;
			}
			if(i1.next().getId() != i2.next().getId()) {
				return false;
			}
		}
		if(i2.hasNext()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param pref
	 * @return true if all the alternatives in the calling Strict Preference are in the Strict Preference given as a parameter.
	 */
	public boolean isIncluded(StrictPreference pref) {
		Objects.requireNonNull(pref);
		Iterator<Alternative> i1 = this.getPreferences().iterator();
		boolean found=false;
		while(i1.hasNext()) {
			Alternative alter = i1.next();
			Iterator<Alternative> i2 = pref.getPreferences().iterator();
			while(i2.hasNext()) {
				if(alter.getId() == i2.next().getId()) {
					found = true;
					break;
				}
			}
			if(!found) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param pref
	 * @return whether both Strict preferences are about the same alternatives exactly (in the same order or not).
	 */
	public boolean hasSameAlternatives(StrictPreference pref) {
		return (this.isIncluded(pref) && pref.isIncluded(this));
	}

}
