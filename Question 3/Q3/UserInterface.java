import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

/**
 * Handles all user input and output.
 * Has a main JPanel, which contains 3 custom JPanels:
 * The top panel is text.
 * The middle panel is a "license plate" with Z's and #'s for letters and numbers.
 * The bottom panel accepts the user's input(population).
 * @author Harry Bowyer
 *
 */
public class UserInterface {
	public UserInterface(Container contentPane) {
		UIMainPanel.setLayout(null);
		contentPane.add(UIMainPanel);
		
		topPanel.setBounds(0, 0, 
				LicensePlateApp.APPWIDTH, LicensePlateApp.APPHEIGHT / 3);
		middlePanel.setBounds(0, LicensePlateApp.APPHEIGHT / 3, 
				LicensePlateApp.APPWIDTH, LicensePlateApp.APPHEIGHT / 3);
		bottomPanel.setBounds(0, 2 * LicensePlateApp.APPHEIGHT / 3, 
				LicensePlateApp.APPWIDTH, LicensePlateApp.APPHEIGHT / 3);
		
		UIMainPanel.add(topPanel);
		UIMainPanel.add(middlePanel);
		UIMainPanel.add(bottomPanel);
	}
	
	public class TopPanel extends JPanel {
		public TopPanel() {
			setBorder(BorderFactory.createBevelBorder(EtchedBorder.LOWERED));
			setBackground(Color.LIGHT_GRAY);
			setLayout(null);
		}
		
		/**
		 * Displays the results in a "Letters: x and Numbers: y" format. Completely centered.
		 * @param inString "AA,00eEE" string to format and display.
		 */
		public void display(String inString) {
			this.removeAll();
			
			JLabel j = new JLabel("Letters: " + LicensePlateGen.letters(inString) + "     Numbers: " 
					+ LicensePlateGen.numbers(inString));
			Dimension d = j.getPreferredSize();
			int x = LicensePlateApp.APPWIDTH / 2 - d.width / 2;
			int y = LicensePlateApp.APPHEIGHT / 6 - d.height / 2;
			j.setBounds(x, y, d.width, d.height);
			
			JLabel k = new JLabel("Excess: " + LicensePlateGen.excess(inString));
			k.setBounds(x, y + d.height, d.width, d.height);
			
			JLabel i = new JLabel("Population: " + textBox.getText());
			i.setBounds(x, y - d.height, i.getPreferredSize().width, d.height);
			
			add(i);
			add(j);
			add(k);
			repaint();
		}
	}
	
	public class MidPanel extends JPanel {
		public MidPanel() {
			setBorder(BorderFactory.createBevelBorder(EtchedBorder.LOWERED));
			setBackground(Color.LIGHT_GRAY);
			setLayout(null);
		}
		
		public void display(String inString) {
			String outString = new String();
			long letters, numbers;
			
			removeAll();
			
			letters = Long.parseLong(LicensePlateGen.letters(inString));
			numbers = Long.parseLong(LicensePlateGen.numbers(inString));
			
			if(letters > 400 || numbers > 400) {
				outString = "License plate too big to display";
			}
			else {
				while(letters-- > 0)
					outString = outString + "Z";
				while(numbers-- > 0)
					outString = outString + "#";
			}
			
			JLabel j = new JLabel(outString);
			j.setBorder(BorderFactory.createBevelBorder(EtchedBorder.RAISED));
			Dimension d = j.getPreferredSize();
			if(d.width > LicensePlateApp.APPWIDTH) {
				j = new JLabel("License plate too big to display");
				d = j.getPreferredSize();
			}
			j.setBounds(LicensePlateApp.APPWIDTH / 2 - d.width / 2, LicensePlateApp.APPHEIGHT / 6 - d.height / 2,
					d.width, d.height);
			add(j);
			repaint();
		}
	}
	
	public class BotPanel extends JPanel {
		public BotPanel() {
			setBorder(BorderFactory.createBevelBorder(EtchedBorder.LOWERED));
			setBackground(Color.green);
			setLayout(null);
			
			textBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String s = LicensePlateGen.calc(textBox.getText());
					if(s != null) {
						topPanel.display(s);
						middlePanel.display(s);
					}
					else
						textBox.setText("<19 numbers");
					textBox.selectAll();
				}
			});
			textBox.setBounds(LicensePlateApp.APPWIDTH / 2, 
					LicensePlateApp.APPHEIGHT / 6 - 20 / 2, 80, 20);
			
			JLabel l = new JLabel("Population?");
			l.setFont(new Font("Arial", Font.PLAIN, 18));
			l.setBounds(LicensePlateApp.APPWIDTH / 2 - l.getPreferredSize().width, 
					LicensePlateApp.APPHEIGHT / 6 - l.getPreferredSize().height / 2, 
					l.getPreferredSize().width, l.getPreferredSize().height);
			
			add(l);
			add(textBox);
		}
	}
	
	TextField textBox = new TextField();
	JPanel UIMainPanel = new JPanel();
	TopPanel topPanel = new TopPanel();
	MidPanel middlePanel = new MidPanel();
	BotPanel bottomPanel = new BotPanel();
}
