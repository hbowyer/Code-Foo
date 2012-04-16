import javax.swing.JApplet;
import javax.swing.JFrame;

/**
 * Starts the program. Supports applications(frames) and applets.
 * @author Harry Bowyer
 *
 */
@SuppressWarnings("serial")
public class Connect4App extends JApplet {

	/**
	 * @param args None. Supplied by operating system or HTML code, but none necessary.
	 */
	 public static void main(String[] args) {
		 //Schedule a job for the event-dispatching thread:
	     //creating and showing this application's GUI.
	     javax.swing.SwingUtilities.invokeLater(new Runnable() {
	     public void run() {
	    	Connect4App connect4App = new Connect4App();
	    	JFrame frame = new JFrame("Connect 4");
	    	frame.setResizable(false);
	    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    	frame.setBounds(0, 0, APPWIDTH, APPHEIGHT + 38); //38 pixels for the frame title
	    	connect4App.init(); //applet & application initialization: game start.
	    	frame.getContentPane().add(connect4App);
	    	frame.setVisible(true);
	     	}
	     });
	}
	 
	@SuppressWarnings("unused")
	public void init() {
		Game game = new Game(getContentPane(), getGlassPane());
	}
	
	public void destroy() {}
	
	public void start() {
		getContentPane().validate();
	}
	
	public static final int APPHEIGHT = 600, APPWIDTH = 600;
}

