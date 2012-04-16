import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

/**
 * BoardView uses Board to create a window and all graphic elements related to the current game with the current board.
 * Also included are any MouseListeners for handling mouse events in the current game.
 * @author Harry Bowyer
 *
 */
public class BoardView {
	public BoardView(Board board, Container pane, Component glassPane) {
		this.board = board;
		mainPane = pane;
		this.glassPane = glassPane;
		
		//Menu Bar
		createMenuBar();
		
		//Game: initial show
		gamePane = new JPanel();
		gamePane.setLayout(new GridLayout(Board.ROWS, Board.COLUMNS));
		mainPane.add(gamePane, BorderLayout.CENTER);
		show();
		glassPane.setVisible(true);
	}
	
	private void createMenuBar() {
		//Initial construction of classes and variables
		JMenuBar jmb = new JMenuBar();
		JMenu[] jm = { new JMenu("Game"), new JMenu("AI Difficulty"), new JMenu("Board Settings") };
		JMenuItem[] jmi1 = { new JMenuItem("New Game") };
		JMenuItem[] jmi2 = { new JRadioButtonMenuItem("Easy"), new JRadioButtonMenuItem("Medium"), new JRadioButtonMenuItem("Hard") };
		ButtonGroup groupForJmi2 = new ButtonGroup();
		JLabel[] jmi3l = {new JLabel("Columns: "), new JLabel("Rows: ") };
		JTextField[] jmi3t = new JTextField[jmi3l.length]; //create text boxes for each label
		for(int i = 0; i < jmi3l.length; i++)
			jmi3t[i] = new JTextField();
		
		//Initialize values
		jmi3t[0].setText(String.valueOf(Board.COLUMNS));
		jmi3t[1].setText(String.valueOf(Board.ROWS));
		jmi2[0].setSelected(true); //easy difficulty AI
		
		//Create listeners
		jmi1[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				newGame();
			}});
		jmi2[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AI.difficulty = 1;
			}});
		jmi2[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AI.difficulty = 2;
			}});
		jmi2[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AI.difficulty = 3;
			}});
		jmi3t[0].addActionListener(new TextBoxListener(jm[2]) {
			public void actionPerformed(ActionEvent arg0) {
				int x = super.getInt(arg0);
				if(x > 0 && x < 21) {
					Board.COLUMNS = x;
					board.game.createNew();
					((JPopupMenu) ((JTextField)arg0.getSource()).getParent()).setVisible(false); //safe: arg0 is always a JTextField for this listener
				}
				else {
					((JTextField)arg0.getSource()).setText(String.valueOf(Board.COLUMNS));
					super.actionPerformed(arg0);
				}
			}
		});
		jmi3t[1].addActionListener(new TextBoxListener(jm[2]) {
			public void actionPerformed(ActionEvent arg0) {
				int x = super.getInt(arg0);
				if(x > 0 && x < 21) {
					Board.ROWS = x;
					board.game.createNew();
					((JPopupMenu) ((JTextField)arg0.getSource()).getParent()).setVisible(false); //safe: arg0 is always a JTextField for this listener
				}
				else {
					((JTextField)arg0.getSource()).setText(String.valueOf(Board.ROWS));
					super.actionPerformed(arg0);
				}
			}
		});
		
		//Add items to groups, items to menus, and menus to the menubar
		for(JMenuItem jmi : jmi1)
			jm[0].add(jmi);
		for(JMenuItem jmi : jmi2) {
			groupForJmi2.add(jmi);
			jm[1].add(jmi);
		}
		for(int i = 0; i < jmi3l.length; i++) {
			jm[2].add(jmi3l[i]);
			jm[2].add(jmi3t[i]);
		}
		for(JMenu j : jm)
			jmb.add(j);
		
		mainPane.setLayout(new BorderLayout());
		mainPane.add(jmb, BorderLayout.NORTH);
	}
	
	/**
	 * Reloads the gamePane with many GamePanels, including listeners.
	 */
	public void show() {
		gamePane.removeAll();
		for(int y = Board.ROWS - 1; y >= 0; y--) {
			for(int x = 0; x < Board.COLUMNS; x++) {
				GamePanel j = new GamePanel(board.column[x].cell[y], this);
				board.column[x].cell[y].gamePanel_ptr = j;
				
				j.setBorder(BorderFactory.createBevelBorder(EtchedBorder.LOWERED));
				/* Listen for the player taking a turn. Let the AI go afterwards. */
				j.addMouseListener(new MoveListener());
				gamePane.add(j);
			}
		}
		gamePane.revalidate();
	}
	
	
	/**
	 * Creates a new game, but preserves BoardView assets.
	 */
	protected void newGame() { 
		board.game.fastNew();
		show();
	}
	
	/**
	 * Removes all MouseListeners in GamePanels. Also redraws the GlassPane to get rid of highlights. (GamePanel graphics are preserved)
	 */
	public void cleanup_endgame() {
		for(int i = 0; i < gamePane.getComponentCount(); i++)
			if(gamePane.getComponent(i).getClass() == GamePanel.class)
				for(MouseListener m : gamePane.getComponent(i).getMouseListeners())
					gamePane.getComponent(i).removeMouseListener(m);
		glassPane.repaint();
	}
	
	
	
	/**
	 * Places a "Draw!" message on the board.
	 * Should only be called using Game's draw() because Game will remove ActionListeners first, which would interfere with the message.
	 * (Side note: game win conditions are handled differently and uses current components.. this adds a component.)
	 * (Also, I tried using a GlassPane's Graphics first, but it does not update properly.. sometimes it partially shows, other times nothing. -HB)
	 */
	public void draw() {
		JLabel label = new JLabel("Draw!");
		Font f = label.getFont();
		
		f = f.deriveFont((float)80);
		label.setFont(f);
		label.setForeground(Color.magenta);
		label.setBounds(Connect4App.APPWIDTH / 2 - label.getPreferredSize().width / 2, Connect4App.APPHEIGHT / 2 - label.getPreferredSize().height + 38, 
				label.getPreferredSize().width, label.getPreferredSize().height);
		label.setOpaque(false);

		mainPane.setLayout(null); //this allows absolute positioning.. and a layout is no longer required
		mainPane.add(label, 0);
	}
	
	/* Classes */
	
	/**
	 * Allows the player to take a move, and passes control to the AI when done.
	 * Also highlights columns as the mouse moves over them.
	 * Attached to each GamePanel in GamePane.
	 * @author Harry Bowyer
	 *
	 */
	public class MoveListener implements MouseListener {
		public void mouseClicked(MouseEvent arg0) {
			//Attempt to move. If it works, the AI can probably go.
			BoardView.GamePanel g = (BoardView.GamePanel) arg0.getComponent();
			boolean AIGo = g.cellptr.columnOwner.addPiece(g.boardViewptr.board.game.player[0]);
			
			//Check for a win or a draw. If neither happens, let the AI take a turn.
			if(board.checkWin() != null) {
				Board.Column c = g.cellptr.columnOwner;
				int x = Board.ROWS - 1;
				//Find the exact move done by the player.
				while(c.cell[x].owner == null)
					x--;
				//Set the cell's gamePanel's winningPiece property, which causes the panel to display a win message on it specifically.
				c.cell[x].gamePanel_ptr.winningPiece = board.game.player[0];
				c.cell[x].gamePanel_ptr.repaint();
				cleanup_endgame();
			}
			else {
				show(); //finally allow the BoardViewer to update
				if(board.isDraw())
					board.game.draw();
				else if(AIGo)
					board.game.player[1].move();
			}
		}
		
		/**
		 * Display a yellow highlight around a column
		 */
		public void mouseEntered(MouseEvent arg0) {
			if(enteredComponent != arg0.getComponent()) {
				BoardView.GamePanel gp = (BoardView.GamePanel) arg0.getComponent();
				Graphics2D g = (Graphics2D) glassPane.getGraphics();
				
				g.setColor(Color.yellow);
				g.drawRect(gp.getX(), 23,  gp.getWidth(), gp.getHeight()*Board.ROWS -1); //23 pixels for the JMenuBar, 4 pixels for bottom border
				g.drawRect(gp.getX() + 2, 25,  gp.getWidth() - 4, gp.getHeight()*Board.ROWS -3); //thicker box!
				
				enteredComponent = arg0.getComponent();
			}
		}
		
		/**
		 * Remove the yellow highlight
		 */
		public void mouseExited(MouseEvent arg0) {
			if(enteredComponent != null)
				if(enteredComponent == arg0.getComponent()) {
					show();
					board.game.mainPane.repaint(); //is sometimes 1 pixel off (due to columns/rows being increased).. repaint for cleanup
				}
		}
		
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
		
		private Component enteredComponent = null;
	}
	
	/**
	 * Adds upon a normal TextField ActionListener by cleaning up the JMenu, JPopupMenu, and ensures valid integers after entry.
	 * @author Harry Bowyer
	 *
	 */
	public class TextBoxListener implements ActionListener {
		TextBoxListener(JMenu callingJMenu) {
			super();
			hostJMenu = callingJMenu;
		}
		
		/**
		 * Upon hitting enter in a TextField in a menu, the JMenu still is selected, and the menu is still dropped down. This cleans that up.
		 */
		public void actionPerformed(ActionEvent arg0) {
			JPopupMenu pm = (JPopupMenu) ((JTextField) arg0.getSource()).getParent();
			pm.setVisible(false);
			hostJMenu.setSelected(false);
			show(); //redraws game board to get rid of after images of the menu
		}
		
		/**
		 * Convenience method to get an integer out of the store TextField's box
		 * @param arg0 The passed ActionEvent
		 * @return An integer, or -1 if it is not within acceptable bounds (0-20, no text).
		 */
		private int getInt(ActionEvent arg0) {
			String s = arg0.getActionCommand();
			int x = -1;
			
			try{
				x = Integer.valueOf(s);
			} catch (Exception NumberFormatException) {
				x = -1;
			}
			if(x > 0 && x < 21) {
				return x;
			}
			else
				return -1;
		}
		
		private JMenu hostJMenu;
	}
	
	/**
	 * A customized JPanel that displays a circular "play piece" depending on the owner attribute.
	 * @author Harry Bowyer
	 *
	 */
	@SuppressWarnings("serial")
	protected class GamePanel extends JPanel {
		protected GamePanel(Board.Cell cell, BoardView boardView) {
			this.boardViewptr = boardView;
			cellptr = cell;
		}
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(cellptr.owner != null) {
				g.setColor(cellptr.owner.color);
				g.fillOval(2, 2, Connect4App.APPWIDTH / Board.COLUMNS - 6, Connect4App.APPHEIGHT / Board.ROWS - 8); //8 for borders
				if(winningPiece != null) {
					String str;
					if(winningPiece == board.game.player[0])
						str = "You Win!";
					else
						str = "You Lose";
					g.setColor(Color.pink);
					g.drawChars(str.toCharArray(), 0, 8, 
							(Connect4App.APPWIDTH / Board.COLUMNS) / 2 - 24, (Connect4App.APPHEIGHT / Board.ROWS) / 2 + 1); //-24, +1 = font centering adjustments
				}
			}
		}
		
		protected Player winningPiece = null;
		protected Board.Cell cellptr;
		protected BoardView boardViewptr;
	}
	
	private Board board;
	private Container mainPane;
	private Component glassPane;
	private JPanel gamePane;
}
