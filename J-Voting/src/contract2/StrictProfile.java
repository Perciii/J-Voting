package contract2;

import contract1.StrictPreference;
import contract1.Alternative;

/**
 * 
 * @author quentinsauvage & vincentnavales
 *
 */
public class StrictProfile {
	private StrictPreference personalPreferences;
	private int voterId;
	
	/**
	 * default constructor
	 */
	public StrictProfile(){
		this.personalPreferences = new StrictPreference();
		this.voterId = 0;
	}
	
	/**
	 * @param voterId the id of the voter
	 * @param strictPreference the strict preferences of this voter
	 */
	public StrictProfile(int voterId, StrictPreference strictPreference){
		this.personalPreferences = strictPreference;
		this.voterId = voterId;
	}
	
	/**
	 * getters and setters
	 */
	public StrictPreference getPersonalPreferences() {
		return personalPreferences;
	}
	public void setPersonalPreferences(StrictPreference personalPreferences) {
		this.personalPreferences = personalPreferences;
	}
	public int getVoterId() {
		return voterId;
	}
	public void setVoterId(int voterId) {
		this.voterId = voterId;
	}
}
