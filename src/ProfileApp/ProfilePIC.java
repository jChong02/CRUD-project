package ProfileApp;
/**
 * ProfilePIC.Java
 * CIS22C_Spring 2020_FINAL PART 1
 * This class contains all the information for a user profile
 * 
 * GROUP: 22C||!22C
 * @author Thanh LE
 * @author Jun Jie CHONG
 * @author Tun Pyay Sone LIN
 */

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

public class ProfilePIC extends JFrame{
	/**
	 * VARIABLES
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private final int maxWidth = 300;
	private final int maxHeight = 300;
	private File file;
	private String filePath;
	
	/**
	 * CONSTRUCTORS
	 */
	
	public ProfilePIC(File file){
		initialization(file);
	}
	public ProfilePIC(String filePath){
		File file = new File (filePath);
		initialization(file);
	}
	
/**
 * This methods will generate the pop up GUI to display profile picture
 * @param file - File - profile picture file
 */
	public void initialization(File file) {
		frame = new JFrame();
		 BufferedImage img = null;
		 try {
		     img = ImageIO.read(file);
		     Image dimg = img.getScaledInstance(maxWidth, maxHeight,Image.SCALE_SMOOTH);
			 ImageIcon icon = new ImageIcon(dimg);
			 
			  JLabel label = new JLabel(icon);
			  frame.add(label);
			  frame.setDefaultCloseOperation
			         (JFrame.DISPOSE_ON_CLOSE);
			  frame.setSize(300, 300);
			  frame.setVisible(true);
			 frame.setTitle("Profile picture"); 
		 
		 } catch (IOException e) {
		     e.printStackTrace();
		 }
	}
	
	/**
	 * ACCESSOR
	 */
	public File getFile() {
		return file;
	}
	public JFrame getFrame() {
		return frame;
	}
	public String getFilePath() {
		return filePath;
	}
	/**
	 * MUTATORS
	 */
	public void setFile(File file) {
		this.file = file;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	
	
	
}
