import java.awt.Color;
import java.awt.Component;
import java.awt.Container;

/**
 * A game instance that includes one board, one board viewer, and two players(one human and one AI)
 * @author Harry Bowyer
 *
 */
public class Game {
	/**
	 * Create a board, 2 players, and a board viewer.
	 * @param mainPane The content pane.
	 * @param glassPane The glass pane, used for highlighting columns.
	 */
	public Game(Container mainPane, Component glassPane) {
		this.mainPane = mainPane;
		this.glassPane = glassPane;
		
		board = new Board(this);
		
		player[0] = new Human();
		player[1] = new AI(this);
		player[0].color = Color.blue;
		player[1].color = Color.red;

		boardView = new BoardView(board, mainPane, glassPane);
	}
	
	/**
	 * Reset the game, but BoardView assets do not need recreated. Called by BoardView.
	 */
	protected void fastNew() {
		board.reset();
		mainPane.repaint();
	}
	
	/**
	 * Reset the game, including assets for new column/row values.
	 */
	public void createNew() {
		mainPane.removeAll();
		board = new Board(this);
		boardView = new BoardView(board, mainPane, glassPane);
		boardView.show();
	}
	
	/**
	 * End the game in a draw.
	 */
	public void draw() {
		boardView.cleanup_endgame();
		boardView.draw();
		}
	
	/* Class variables. */
	protected BoardView boardView;
	protected Board board;
	protected Player player[] = new Player[2];
	protected Container mainPane;
	protected Component glassPane;
}
