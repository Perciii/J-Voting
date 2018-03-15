package contract1;
/**
 * This class is immutable
 * Contains an integer which corresponds to a voting possibility
 * 
 */
public class Alternative {
	private int id;
	
	/**
	 * Creates a new Alternative with the id given as a parameter
	 * @param id
	 */
	public Alternative(int id) {
		this.id=id;
	}
	
	/**
	 * 
	 * @return the id of the Alternative
	 */
	public int getId() {
		return id;
	}
}
