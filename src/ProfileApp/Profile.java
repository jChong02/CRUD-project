package ProfileApp;
/**
 * Profile.Java
 * CIS22C_Spring 2020_FINAL PART 1
 * 
 * This class contains all the information for a user profile
 * 
 * GROUP: 22C||!22C
 * @author Thanh LE
 * @author Jun Jie CHONG
 * @author Tun Pyay Sone LIN
 */

public class Profile {
	private String firstName, lastName, status,password; 
	private String fileName; 
	
	/**
	 * CONSTRUSTOR - default constructor
	 */
	public Profile() {
		this ("No Password","No firstname","No lastName","No status","default.png"); 
	}
	
	/**
	 * Full data constructor
	 * @param password - String - user's profile password
	 * @param firstName - String - user's profile first name
	 * @param lastName - String - user's profile last name
	 * @param status - String - user's profile status
	 * @param friendList - AList<String> - user's profile friend List
	 * @param fileName - String - user's profile profile picture name
	 */
	public Profile(String password, String firstName, String lastName, String status, String fileName) {
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.status = status;
		this.fileName = fileName;
	}
	
	/**
	 * ACCESSORS
	 */
	
	public String getFirstName() {
		return firstName;
	}
	public String getPassword() {
		return password;
	}
	public String getLastName() {
		return lastName;
	}
	//Get full name 
	public String getName() {
		return firstName + " " +lastName;
	}
	public String getStatus() {
		return status;
	}
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * MUTATORS
	 */
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * This Method override Object toString to display
	 * user's profile information
	 */
	@Override 
	public String toString() {
		return "\nProfile name:    " + firstName + " " + lastName + "\nCurrent Status:  " + status;
	}
	
	

}
