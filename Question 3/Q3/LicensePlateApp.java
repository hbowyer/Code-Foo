import javax.swing.JApplet;
import javax.swing.JFrame;

/**
 * Creates and starts the application or applet.
 * @author Harry Bowyer
 *
 */
public class LicensePlateApp extends JApplet {

	 public static void main(String[] args) {
		 //Schedule a job for the event-dispatching thread:
	     //creating and showing this application's GUI.
	     javax.swing.SwingUtilities.invokeLater(new Runnable() {
	    	 public void run() {
	    		 //Set the applet settings, some of which normally done in HTML
	    		 LicensePlateApp licensePlateApp = new LicensePlateApp();
	    		 JFrame frame = new JFrame("Code Foo - Question 2: License Plate Generator");
	    		 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    		 frame.setBounds(0, 0, APPWIDTH, APPHEIGHT + 38); //38 pixels is the frame's title bar
	    		 licensePlateApp.init(); //applet entry point
	    		 frame.getContentPane().add(licensePlateApp);
	    		 frame.setVisible(true);
	    	 }
	     });
	}
	 
	public void init() {
		UserInterface UI = new UserInterface(getContentPane());
	}
	
	public void destroy() {
	}

	public void start() {
		getContentPane().validate();
	}
	
	final static int APPWIDTH = 700;
	final static int APPHEIGHT = 300;
}
