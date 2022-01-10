/**
 * Main_Program.Java
 * CIS22C_Spring 2020_FINAL PART 1
 * 
 * GROUP: 22C||!22C
 * 
 * @author Thanh LE
 * @author Jun Jie CHONG
 * @author Tun Pyay Sone LIN
 * 
 * 
 * This is the main program flow for the PROFILE App (A Facebook spin-off version).
 * PROFILE App allow user to create an account, find friends, and view their 
 * friend's status. Each user will have the ability to Create, Read, Update, Delete 
 * their own profile as well as the account on their friendList. 
 * NOTE: Adding and deleting an account from user friend list will effect user and 
 * the chosen friend's friend list since the action one action mutual for both account.
 * Deleting user profile will remove the user account from the database and whoever have them 
 * 
 * 
 */
package ProfileApp;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Scanner;

import DataStructure.LinkedBag;
import DataStructure.LinkedDictionary;
import DataStructure.UndirectedGraph;

public class Main_Program {
	

	//MAIN DATABASE OF PROFILES
	private  LinkedDictionary<String, Profile> ID_profile = new LinkedDictionary<String, Profile>();
	
	//FRIEND GRAPH
	private  UndirectedGraph<String> friendGraph = new UndirectedGraph<String>(); 
	
	//PROFILE PICTURE 
	private ProfilePIC profileImg;
	private String folderPath;
	
	
	public void mainProgram() throws IOException {
		
		String choice, ID, filePath;
		boolean isFileExist;
		Scanner input;

		File infile = new File("masterList.txt");
		input = new Scanner(infile);
		populateProfiles(input);
		// d.displayProfileTest(ID_profile); //WORK

		input = new Scanner(System.in);

		System.out.println("Welcome to PROFILE");
		System.out.println("\nBefore we get start");
		do {
			System.out.println("Please enter the location of the PROFILE_PIC folder (ex: /User/Desktop/):");
			folderPath = input.nextLine(); 
			filePath = folderPath + "default.png";
			isFileExist = Helper.fileValidation(filePath);
			// System.out.println ("\n" +filePath );//WORK
		} while (!isFileExist);
		choice = mainMenu(input);
		while (!choice.equalsIgnoreCase("X")) {
			if (choice.equalsIgnoreCase("A")) {
				Profile userProfile;
				ID = logInMenu(input);
				if (ID.equals("Empty")) {
					choice = mainMenu(input);
				} else {
					userProfile = ID_profile.getValue(ID);
					System.out.println("\n\nWelcome back " + userProfile.getName().toUpperCase());
					profileMainMenu(input, ID, userProfile);
					choice = mainMenu(input);
				}

			} else {
				System.out.println("\n-----------------------------------------------------------------------------------");
				System.out.println("REGISTER");
				System.out.println("-----------------------------------------------------------------------------------");
				registerProfile(input);
				choice = mainMenu(input);
			}
		}
		writeToFile();//Update masterList.txt before closing

		input.close();
		System.exit(0);

	}
	
	//
	// MAIN MENU - LOG IN | REGISTER | EXIT
	//
	/**
	 * mainMenu will take in user selection and validate it before return to the program
	 * @param input - Scanner to read from console
	 * @return choice - the validated input for option selection
	 */
	public String mainMenu(Scanner input) {
		String choice;
		System.out.println("\n-----------------------------------------------------------------------------------");
		System.out.println("Already have a Profile?   Don't have Profile?    Close Program");
		System.out.println("	A. Log in 	     B. Register            X. Exit");
		System.out.print("\nYour choice: ");
		choice = input.nextLine();
		while (!choice.equalsIgnoreCase("A") && !choice.equalsIgnoreCase("B") && !choice.equalsIgnoreCase("X")) {
			System.out.print("\nInvalid input!\nPlease choose again: ");
			choice = input.nextLine();
		}
		return choice.toUpperCase();

	}
	
	
	/**
	 * This program allows user to create a profile if ID is not existed
	 * @param input - Scanner to read from console
	 * 
	 */
	public void registerProfile(Scanner input) {
		String ID;
		String pw;
		String firstName, lastName;
		String status = "Active"; // default value for new user
		String fileName = "default.png";// default value for new user

		ID = Helper.inputStringValidation (input,"Create Profile Username: ");
		ID = ID.toLowerCase();
		//validate ID
		while (ID_profile.contains(ID)) {
			System.out.println("\nUsername already exists! Try another Username");
			//System.out.print("Create Profile Username: ");
			ID = Helper.inputStringValidation (input,"Create Profile Username: ");
			ID = ID.toLowerCase();
		}

		pw = Helper.inputStringValidation (input,"Create Profile Password: ");
		firstName = Helper.inputStringValidation (input,"Enter First name: ");
		lastName =  Helper.inputStringValidation (input,"Enter Last name: ");
		//Create profile
		Profile profile = new Profile(pw, firstName, lastName, status, fileName);
		//Add profile & ID to dictionary
		ID_profile.add(ID, profile);
		friendGraph.addVertex(ID);

		System.out.println("\nProfile successfully created!");
	}
	
	
	/**
	 * logInMenu will prompt user for ID and pass input
	 * Program will validate the input by compare it to the dictionary data
	 * Program will only allows for 3 log-in try. 
	 * More than 3 login attempts (any combination) will return user to the WELCOME PANEL
	 * @param input - Scanner to get console input
	 * @return ID or "Empty" to the main program
	 */
	public String logInMenu(Scanner input) {
		String ID, passwd;
		boolean successful_name, successful_pw;
		int counter = 1;
		// let user try to log-in 3 times then stop. increase every time fail to log-in
		do {
			successful_name = false;
			System.out.print("\nEnter Login Name: ");
			ID = input.nextLine();
			ID = ID.toLowerCase(); // id is converted to lowercase 

			System.out.print("Enter Login Password (Case Sensitive): ");
			passwd = input.nextLine();

			if (ID_profile.contains(ID)) {
				successful_name = true;
				do {
					successful_pw = passwd.equals(ID_profile.getValue(ID).getPassword());
					if (successful_pw) {
						System.out.println("Successful login");
						successful_name = true;
						return ID;
					} else {
						System.out.println("Incorrect password\n");
						System.out.print("Enter login password: ");
						passwd = input.nextLine();
						counter++;
					}
				} while (!successful_pw && counter < 3);
				successful_pw = passwd.equals(ID_profile.getValue(ID).getPassword());
                if (successful_pw)
                    return ID;
			} else {
				System.out.println("Login name is invalid.\n");
				counter++;
			}

		} while (!successful_name && counter < 4);

		return "Empty"; // Fail to log in 3 times
	}
	
	
	//
	// SECONDARY MENU UNDER LOGIN - VIEW PROFILE - UPDATE PROFILE - VIEW FRIEND -
	// LOG OUT ******PROFILE DISPLAY MESSAGE *******PROFILE MAIN MENU
	//
	
	/**
	 * This method displays the Profile Main Selection List, 
	 * prompt user for a selection and validate the choice
	 * @param input - Scanner for user input
	 * @return choice - int - valid choice from the selection
	 */
	public int profileMainDisplay(Scanner input) {
		int choice;
		System.out.println("\n-----------------------------------------------------------------------------------");
		System.out.println("MAIN PROFILE MENU");
		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("Choose one option: ");
		System.out.println("1. View Profile \n2. Update Profile  \n3. View Friends  \n4. Delete Profile \n5. Log-out");

		System.out.print("\nYour choice: ");

		choice = Helper.readInt(input);
		while (choice > 5 && choice < 1) {
			System.out.print("\nInvalid input!\nPlease choose again: ");
			choice = Helper.readInt(input);
		}
		return choice;
	}
/**
 * This method take the validation choice from profileMainDisplay method 
 * and perform the correspond action
 * @param input - Scanner for user input
 * @param ID - String - user ID 
 * @param profile - Profile - the profile linked with userID
 */
	public void profileMainMenu(Scanner input, String ID, Profile profile) {
        String confirmRemove;
        int choice = profileMainDisplay(input);
        input.nextLine();
        while (choice != 5) {
            if (choice == 1) {
                System.out.println("\n\n HI " + ID + "!");
                System.out.println("-----------------------------------------------------------------------------------");	
                System.out.println(" YOUR PROFILE MENU");
                System.out.println("-----------------------------------------------------------------------------------");
                displayProfile(profile, ID);                
                profileImg = new ProfilePIC(folderPath + profile.getFileName());
            } else if (choice == 2) {
                updateProfile(input, ID, profile);
            } else if (choice == 3){
                viewFriends(input, ID, profile);
            }
            else {
                System.out.println("\nDo you want to delete your profile? (Y/N)");
                System.out.print("Enter choice: ");
                confirmRemove = input.nextLine();
                while (!confirmRemove.equalsIgnoreCase("Y") && !confirmRemove.equalsIgnoreCase("N")) {
                    System.out.print("\nInvalid input!\nPlease choose again: ");
                    confirmRemove = input.nextLine();
                }
                confirmRemove = confirmRemove.toUpperCase();
                if (confirmRemove.equals("Y")) {
                    removeProfile(ID, profile);
                    System.out.print("\nYour profile has been successfully deleted.\n\n");
                    return;
                }
            }
            choice = profileMainDisplay(input);
            input.nextLine();
        }
        System.out.println("\nSuccessful Log-out!\n");
	}
	
	
	/**
	 * This methods will find go through user FriendList, find userID from each of the friend, 
	 * remove the user from the friend's FriendList before remove the user account from the database
	 * @param ID - String - user ID 
	 * @param profile - Profile tied with the userID
	 */
	public void removeProfile(String ID, Profile profile) {
		
        friendGraph.removeVerticeFromAdjacentVertices(ID);
        ID_profile.remove(ID);
    }

	//
	// SUB-MENU UPDATE PROFILE
	// UPDATE PROFILE MAIN
	// ******UPDATE NAME
	// ******UPDATE STATUS 
	// ******UPDATE PASSWORD
	// ******UPDATE PROFILE PIC
	// ******UPDATE ALL (EXCLUDE PROFILE PICTURE)
	//

	/**
	 * This method display the UPDATE PROFILE MENU and prompt user 
	 * for input. The method will validate the user selection before 
	 * performing the appropriate action
	 * @param input - Scanner for user input
	 * @param ID - String - user ID
	 * @param profile - Profile tie with the user ID
	 */
	public void updateProfile(Scanner input, String ID, Profile profile) {

		String choice;
		System.out.println("\n-----------------------------------------------------------------------------------");
		System.out.println("UPDATE PROFILE PANEL");
		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("What would you like to update?");
		System.out.println("A. Name \nB. Status \nC. Password \nD. Profile Picture \nE. All (Excluding Profile Picture)  \nX.Exit");
		System.out.print("\nYour choice: ");
		choice = input.nextLine();
		while (!choice.equalsIgnoreCase("A") && !choice.equalsIgnoreCase("B") && !choice.equalsIgnoreCase("C")
				&& !choice.equalsIgnoreCase("D") && !choice.equalsIgnoreCase("E") && !choice.equalsIgnoreCase("X")) {
			System.out.print("\nInvalid input!\nPlease choose again: ");
			choice = input.nextLine();
		}

		if (choice.equalsIgnoreCase("X"))
			return;
		else if (choice.equalsIgnoreCase("A")) {
			profile.setFirstName(profileUpdateFirstName(input));
			profile.setLastName(profileUpdateLastName(input));
			ID_profile.add(ID, profile);
		} else if (choice.equalsIgnoreCase("B")) {
			profile.setStatus(profileUpdateStatus(input));
			ID_profile.add(ID, profile);
		} else if (choice.equalsIgnoreCase("C")) {
			profile.setPassword(profileUpdatePasswd(input));
			ID_profile.add(ID, profile);

		} else if (choice.equalsIgnoreCase("D")) {
			profile.setFileName(profilePicCheck(input, folderPath));
			ID_profile.add(ID, profile);
		} else {
			profile.setFirstName(profileUpdateFirstName(input));
			profile.setLastName(profileUpdateLastName(input));
			profile.setStatus(profileUpdateStatus(input));
			profile.setPassword(profileUpdatePasswd(input));
			ID_profile.add(ID, profile);
		}
	}
	
	/**
	 * This method will updates the user new first name
	 * @param input - Scanner for userInput
	 * @return newFirstName - String - User new first name
	 */

	public String profileUpdateFirstName(Scanner input) {
		String newFirstName;
		newFirstName = Helper.inputStringValidation (input, "Enter your new first name: ");
		return newFirstName;
	}

	/**
	 * This method will updates the user new first name
	 * @param input - Scanner for userInput
	 * @return newFirstName - String - User new first name
	 */
	
	public String profileUpdateLastName(Scanner input) {
		String newLastName;
		newLastName = Helper.inputStringValidation (input, "Enter your new last name: ");
		return newLastName;
	}

	/**
	 * This method will updates the user new first name
	 * @param input - Scanner for userInput
	 * @return newLastName - String - User new last name
	 */
	public String profileUpdateStatus(Scanner input) {
		String statusChoice, newStatus;
		System.out.print("Choose your status: ");
		System.out.println("    A. Online        B. Offline        C. Busy");
		System.out.print("\nYour choice: ");
		statusChoice = input.nextLine();

		while (!statusChoice.equalsIgnoreCase("A") && !statusChoice.equalsIgnoreCase("B")
				&& !statusChoice.equalsIgnoreCase("C")) {
			System.out.print("\nInvalid input!\nPlease choose again:");
			statusChoice = input.nextLine();
		}
		if (statusChoice.equalsIgnoreCase("A"))
			newStatus = "Online";
		else if (statusChoice.equalsIgnoreCase("B"))
			newStatus = "Offline";
		else
			newStatus = "Busy";
		return newStatus;

	}

	/**
	 * This method will updates the user new first name
	 * @param input - Scanner for userInput
	 * @return newPassword - String - User new password
	 */
	public String profileUpdatePasswd(Scanner input) {
		String newPasswd;
		newPasswd = Helper.inputStringValidation (input, "Enter your new password: ");
		return newPasswd;
	}

	/**
	 * 
	 * @param input - Scanner- user input
	 * @param folderPath - String - path to access to Profile picture folder
	 * @return fileName - String - User new profile pictures file
	 */
	public String profilePicCheck(Scanner input, String folderPath) {
		String filePath, fileName;
		boolean isFileExist;

		System.out.print("\nPlease copy your picture to the PROFILE_PIC folder before continue");
		System.out.print("\nDone? Please enter any key to continue");
		input.nextLine();
		System.out.print("Enter file name with extension (ex: example.jpg): ");
		fileName = input.nextLine();
		filePath = folderPath + fileName;
		isFileExist = Helper.fileValidation(filePath);
		while (!isFileExist) {
			System.out.print("\nINVALID FILE");
			System.out.print("\nPlease make sure your picture is in the PROFILE_PIC folder");
			System.out.print("\nEnter profile picture file name with file extension (ex: example.jpg): ");
			fileName = input.nextLine();
			filePath = folderPath + fileName;
			isFileExist = Helper.fileValidation(filePath);
		}
		return fileName;
	}
	
	
	//
	// SUB-MENU VIEW FRIEND 
	// VIEW FRIEND MAIN 
	// ********VIEW FRIENDLIST
	// *********SEARCH FROFILES - See Detail Profile - with Add friend Option
	// *********DELETE FRIENDS
	// *********EXIT
	//

	/**
	 * This method displays the VIEW FRIEND MENU, prompts user for 
	 * a selection, validates it and returns the selection to the program
	 * @param input - Scanner for user input
	 * @return choice - String - validated selection
	 */
	public String viewFriendMenuDisplay(Scanner input) {
		String choice;
		System.out.println("\n-----------------------------------------------------------------------------------");
		System.out.println("FRIENDLIST MENU");
		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("What would you like to do?");
		System.out.println("A. View Friend List	\nB. Search Profiles \nC. Delete Friend \nX. Exit");
		System.out.print("\nYour choice: ");

		choice = input.nextLine();
		while (!choice.equalsIgnoreCase("A") && !choice.equalsIgnoreCase("B") && !choice.equalsIgnoreCase("C")
				&& !choice.equalsIgnoreCase("X")) {
			System.out.print("\nInvalid input!\nPlease choose again: ");
			choice = input.nextLine();
		}
		return choice.toUpperCase();
	}

	/**
	 * This method takes the valid choice from viewFriendMenuDisplay
	 * and performs the appropriate action
	 * @param input - Scanner for user input
	 * @param ID - String - userID
	 * @param profile - Profile tie with userID
	 */
	
	public void viewFriends(Scanner input, String ID, Profile profile) { // profile here is my profile
		String choice = viewFriendMenuDisplay(input);
		while (!choice.equals("X")) {
			if (choice.equals("A")) {
				System.out.println("\nYOUR CURRENT FRIENDLIST:\n");
				friendGraph.displayAdjacentVertices(ID);
				System.out.println("\n\n\nPEOPLE YOU MAY KNOW:\n");
				displaySuggestedFriends(ID);
			} else if (choice.equals("B")) {
				profileSearchFriends(input, ID, profile);
			} else {
				profileDeleteFriend(input, ID);
			}
			System.out.println("\n");
			choice = viewFriendMenuDisplay(input);
		}
	}
	
	
	
	/**
	 * This method performs the search friend action by prompting user for another account ID
	 * The methods will search the database to see if the accountID exist.
	 * If the accountID exits, the methods will search if the accountID is in the user friendList. 
	 * If yes, the method will display the information associated with the accountID
	 * If no, the method will display the profile associated with the accountID and ask user if 
	 * user want to add friend and performs accordingly
	 * If user searches for their own account, the method will display the user's profile information.
	 * @param input - Scanner for the user input
	 * @param ID - String - userID
	 * @param profile - Profile associate to useID
	 */
	public void profileSearchFriends(Scanner input, String ID, Profile profile) {
		String accountID;
		String tryChoice;

		//System.out.println("Search Profiles\n ");
		System.out.print("\nEnter a username to search: ");
		accountID = input.nextLine();
		
		System.out.println("\n-----------------------------------------");
		System.out.println("SEARCH RESULT for " + accountID.toUpperCase());
		System.out.println("-----------------------------------------");

		if (!ID_profile.contains(accountID)) {
			do {
				System.out.println("User does not exist! \n\nDo you want to try again? (Y/N)");
				System.out.print("Enter choice: ");
				tryChoice = input.nextLine();
				while (!tryChoice.equalsIgnoreCase("Y") && !tryChoice.equalsIgnoreCase("N")) {
					System.out.print("\nInvalid input!\nPlease choose again:");
					tryChoice = input.nextLine();
				}
				tryChoice = tryChoice.toUpperCase();
				if (tryChoice.equals("Y")) {
					System.out.print("\nEnter a username to search: ");
					accountID = input.nextLine();
					System.out.println("\n-----------------------------------------");
					System.out.println("SEARCH RESULT for " + accountID.toUpperCase());
					System.out.println("-----------------------------------------");
				}
			} while (tryChoice.equals("Y") && !ID_profile.contains(accountID));
			
			if (tryChoice.equals("N"))
				System.out.println("\nRETURNING to FRIENDLIST MENU...");
		}

		if (ID_profile.contains(accountID) && !ID.equals(accountID)) {
			System.out.println("\nUser found...");
			
			System.out.println(ID_profile.getValue(accountID).toString());//This is your profile
			System.out.println("\nFRIEND LIST:");
			friendGraph.displayAdjacentVertices(accountID);
			System.out.println("\n");
			friendGraph.printMutualVertices(ID, accountID);
			profileImg = new ProfilePIC(folderPath + ID_profile.getValue(accountID).getFileName());

			if (friendGraph.hasEdge(ID, accountID)) {
				System.out.println("\n(User is already a friend...)");
			}

			else {
				System.out.println("\n(User is not a friend...)");
				profileAddFriend(input, accountID, ID);
			}
		} else if (ID.equals(accountID)){
			System.out.println("\nThis is the information of your PROFILE");
			displayProfile(profile, ID);     
			profileImg = new ProfilePIC(folderPath + profile.getFileName());
		} 
		
	}
	
	/**
	 * This method prompts user selection to add friend. 
	 * If yes, the method will add the accountID's profile to user's FriendList 
	 * and vice versa.
	 * @param input - Scanner for use input
 	 * @param accountID - String - account to be added
	 * @param ID -String - userID
	 */

	public void profileAddFriend(Scanner input, String accountID, String ID) {
		String choice;

		System.out.println("\nWant to add?");
		System.out.println("A.Add Friend		\nX.Exit     ");
		System.out.print("\nYour choice: ");

		choice = input.nextLine();
		while (!choice.equalsIgnoreCase("A") && !choice.equalsIgnoreCase("X")) {
			System.out.print("\nInvalid input!\nPlease choose again: ");
			choice = input.nextLine();
		}
		choice = choice.toUpperCase();

		if (choice.equals("A")) {
			friendGraph.addEdge(ID, accountID);
			System.out.println("\nFriend is successfully added! \n" + ID.toUpperCase() + " and " + accountID.toUpperCase() + " are now friends.");
		}
	}
	
	/**
	 * 
	 * This method will display the friend of a friend from user list and states
	 * how many common friends does the user and the suggested friend has 
	 * @param ID - user ID 
	 */
	public void displaySuggestedFriends(String ID)
    {
        String friendID;
        int mutualFriendsCount;
        LinkedBag<String> sugggestedFriends = new LinkedBag<String>();
        sugggestedFriends = friendGraph.farNeighborsBag(ID);
        System.out.printf("%-15s %-20s %-10s \n\n", "USER ID", "USER NAME", "# OF MUTUAL FRIENDS");

        while(!sugggestedFriends.isEmpty())
        {
        	//Get the identity of the suggested friend
            friendID = sugggestedFriends.remove();
            mutualFriendsCount = 1;
            // Get the correct friend count before removed
            mutualFriendsCount += sugggestedFriends.getFrequencyOf(friendID);
            //Remove all instance of the suggested friend 
            while(sugggestedFriends.getFrequencyOf(friendID) > 0) {
            	sugggestedFriends.remove(friendID);
            }
            //Print the result to the console
            System.out.printf("%-15s %-29s %-10s \n", friendID, ID_profile.getValue(friendID).getName(), mutualFriendsCount);
        }
    }
	
	
	/**
	 * This method performs the search friend to be removed by prompting user for another account ID
	 * The methods will search if the accountID is in the user friendList and prompt user for another 
	 * try if friend account is not found. If found, the program will delete the friend account from 
	 * your list as well as delete your account from their friendList. 
	 * @param input - Scanner for the user input
	 * @param ID - String - userID
	 */
	
	public void profileDeleteFriend(Scanner input, String ID) {
		String friendID;
		String tryChoice;

		System.out.print("\nEnter a username to delete friend: ");
		friendID = input.nextLine();

		if (!friendGraph.hasEdge(ID, friendID)) { // Search dictionary
			do {
				System.out.println("User does not exist in your friend list! \n\nDo you want to try again? (Y/N)");
				System.out.print("Enter choice: ");
				tryChoice = input.nextLine();
				while (!tryChoice.equalsIgnoreCase("Y") && !tryChoice.equalsIgnoreCase("N")) {
					System.out.print("\nInvalid input!\nPlease choose again:");
					tryChoice = input.nextLine();
				}
				tryChoice = tryChoice.toUpperCase();
				if (tryChoice.equals("Y")) {
					System.out.print("\nEnter a username to delete friend: ");
					friendID = input.nextLine();
				}
			} while (tryChoice.equals("Y") && !ID_profile.contains(friendID));
			if (tryChoice.equals("N"))
				System.out.println("\nRETURN to FRIENDLIST MENU...");
		}

		if (friendGraph.hasEdge(ID, friendID)) {
			//Remove edge both way
			friendGraph.removeEdge(ID, friendID);
			friendGraph.removeEdge(friendID,ID);
			System.out.println("\nFriend is successfully deleted");
			System.out.println(ID.toUpperCase() + " and " + friendID.toUpperCase() + " are no longer friends.");
			System.out.println("\nYOUR CURRENT FRIENDLIST:\n");
			friendGraph.displayAdjacentVertices(ID);
		}
	}
	
	/**
	 * This methods will display the friend List from the graph
	 * @param profile - profile of the user
	 * @param ID - user ID
	 */
	public void displayProfile(Profile profile, String ID)
	{
		System.out.println(profile);
        System.out.print("Friend list:\n");
        friendGraph.displayAdjacentVertices(ID);
        System.out.print("\n");
	}
	
	/**
	 * This methods will populate the data from masterList.txt to the dictionary 
	 * @param input - Scanner to read from file
	 */
	public void populateProfiles(Scanner input){
		
		String ID = null, pass = null, firstName = null, lastName = null;
		String status = null, fID = null, fileName = null;
		Profile profile;

		input.useDelimiter(":"); // Use this delimiter to allow user to have space in the input
		while (input.hasNext()) {
			ID = input.next();
			pass = input.next();
			firstName = input.next();
			lastName = input.next();
			status = input.next();
			fileName = input.next();
			profile = new Profile(pass, firstName, lastName, status, fileName);
			// Add profile and ID to the dictionary database
			ID_profile.add(ID, profile);
			friendGraph.addVertex(ID);
			fID = input.next();
			while (!fID.equals("/")) { // end of line condition
				friendGraph.addEdge(ID,fID);
				fID = input.next();
			}
			input.nextLine();// manually shift to next line of text
		}
		input.close();
	}
	
	/**
	 * This methods will write the data from the dictionary back to masterList.txt 
	 * This methods will update masterList.txt for next run after user log out
	 */
	public void writeToFile() throws IOException {
		String ID, pw, firstName, lastName, status, fileName;
		Profile profile;

		try {
			FileWriter writeProfile = new FileWriter("masterList.txt");
			PrintWriter out = new PrintWriter(writeProfile);
			Iterator<String> keyIterator = ID_profile.getKeyIterator();
			Iterator<Profile> valueIterator = ID_profile.getValueIterator();
			// Iterator<String> valueIteratorPass = ID_passwd.getValueIterator();

			while (keyIterator.hasNext() && valueIterator.hasNext()) {
				ID = keyIterator.next();
				profile = valueIterator.next();
				pw = profile.getPassword();
				firstName = profile.getFirstName();
				lastName = profile.getLastName();
				status = profile.getStatus();
				fileName = profile.getFileName();
				out.print(ID + ":" + pw + ":" + firstName + ":" + lastName + ":" + status + ":" + fileName + ":");
				friendGraph.writeAdjacentVertices(ID, out);
				out.print("/" + ":" + "\n"); //end of line (for 1 account) condition 
			}

			System.out.println("\nBYE! See you again.\n");//successfully close program  and update the masterList
			writeProfile.close();
		} catch (IOException e) {
			System.out.println("Error saving profile!");
		}
	}
}
