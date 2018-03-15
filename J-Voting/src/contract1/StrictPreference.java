package contract1;

import java.util.ArrayList;
/**
 * This class is immutable
 * Contains a list of Alternatives sorted by preferences 
 * Two alternatives can't be equally ranked
 * You can't store the same alternative several times in the list
 * Every alternative is an integer and corresponds to a voting choice
 *
 */
public class StrictPreference {
	private ArrayList<Alternative> preferences;
	
	/**
	 * Creates a list of strict preferences with the list given as a parameter
	 * @param preferences
	 */
	public StrictPreference(ArrayList<Alternative> preferences) {
		for(int i=0;i<preferences.size();i++) {
			if(preferences.lastIndexOf(preferences.get(i))!=i) {
				throw new IllegalArgumentException("Une liste de pr�f�rences ne peut pas contenir plusieurs fois la m�me alternative");
			}
		}
		this.preferences=preferences;
	}
	
	/**
	 * 
	 * @return the list of strict preferences
	 */
	public ArrayList<Alternative> getPreferences(){
		return preferences;
	}
}
