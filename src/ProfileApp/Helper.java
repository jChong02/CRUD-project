package ProfileApp;
/**
 * Helper.java
 * CIS22C_Spring 2020_FINAL PART 1
 * This class contain general helper classes that support the main program
 * 
 * GROUP: 22C||!22C
 * @author Thanh LE
 * @author Jun Jie CHONG
 * @author Tun Pyay Sone LIN
 */


import java.io.File;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Helper {
	/**
	 * Read a integer from the console.
	 * 
	 * @param input  Scanner object to read from.
	 * @param prompt User prompt to show before input
	 * @return A double entered by the user.
	 */
	public static int readInt(Scanner input, String prompt) {
		int number = 0;
		boolean more = true;
		while (more) {
			try {
				System.out.print(prompt);
				number = input.nextInt();
				input.hasNextLine();
				more = false;
			} catch (InputMismatchException e) {
				System.out.println("Error -- enter digits only!");
				input.nextLine(); // clear the buffer
			} catch (NoSuchElementException e) {
				System.out.println("\nThat element doesn't exist!");
				input.nextLine();
			}
		}
		return number;
	}

	/**
	 * Read a integer from console.
	 * 
	 * @param input  Scanner object to read from.
	 * @param prompt User prompt to show before input
	 * @return A double entered by the user.
	 */
	public static int readInt(Scanner input) {
		int number = 0;
		boolean more = true;
		while (more) {
			try {
				number = input.nextInt();
				input.hasNextLine();
				more = false;
			} catch (InputMismatchException e) {
				System.out.println("Error -- enter digits only!");
				input.nextLine(); // clear the buffer
			} catch (NoSuchElementException e) {
				System.out.println("\nThat element doesn't exist!");
				input.nextLine();
			}
		}
		return number;
	}
	
	/**
	 * Method will convert the filePath to a file and read it. 
	 * If the file exist, return true otherwise return false
	 * @param filePath
	 * @return exists - boolean - if the file exists
	 */
	public static boolean fileValidation (String filePath) {
		File file = null; 
			file = new File (filePath);
			boolean exists = file.exists();
		return exists;
		
	}
	
	public static String inputStringValidation (Scanner input, String prompt) {
		String inputString; 
		System.out.print(prompt);
		inputString = input.nextLine();
		while (inputString.contains(":")||inputString.contains("/")) {
			System.out.print("Invalid input. Input cannot have *$?:/ \n");
			System.out.print("\n" + prompt);
			inputString = input.nextLine();
		}
		return inputString;
	}

}
