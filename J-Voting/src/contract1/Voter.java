package contract1;
/**
 * This class is immutable
 * Contains an integer
 *
 */
public class Voter {
	private int id;
	
	/**
	 * Creates a new object Voter with the id given as a parameter
	 * @param id
	 */
	public Voter(int id) {
		this.id=id;
	}
	
	/**
	 * 
	 * @return the id of the object Voter
	 */
	public int getId() {
		return id;
	}
}
