/**
 * Logic to handle AI's movement. Includes all AI difficulties. Note: difficulty is for all AI opponents (current limit of 1)
 * @author Harry Bowyer
 *
 */
public class AI extends Player {
	public AI(Game game) {
		this.game = game;
	}

	/**
	 * The computer player takes a turn.
	 */
	void move() {
		switch(difficulty) {
		case 1:
			easyMove();
			break;
		case 2:
			mediumMove();
			break;
		case 3:
			hardMove();
			break;
		default:
			debugMove();
			break;
		}
		if(game.board.isDraw())
			game.draw();
	}
	
	/**
	 * Tries to add to the first column only. For debugging purposes.
	 */
	void debugMove() {
		if(game.board.column[0].addPiece(this)) {
			checkWinAI(game.board.column[0]);
		}
	}
	
	/* Easy, medium and hard moves are broken down into difficulty. While they are the same for now, it is easy to add upon a recommended move. */
	void easyMove() {
		Board.Column c;
		c = recommendMove();
		if(c.addPiece(this)) {
			checkWinAI(c);
		}
	}
	
	void mediumMove() {
		Board.Column c;
		c = recommendMove();
		if(c.addPiece(this)) {
			checkWinAI(c);
		}
		
	}
	
	void hardMove() {
		Board.Column c;
		c = recommendMove();
		if(c.addPiece(this)) {
			checkWinAI(c);
		}
	}
	
	/**
	 * Tries to find the best possible move, according to AI difficulty.
	 * @return The recommended column to add to.
	 */
	Board.Column recommendMove() {
		//Error detection: Check for a draw to prevent further recommendations if the board is full.
		if(game.board.isDraw()) {
			return game.board.column[0];
		}
				
		//Check for winning moves.
		for(int i = 0; i < Board.COLUMNS; i++) 
			if(game.board.column[i].testDoesPieceWin(game.board.game.player[1], difficulty))
				return game.board.column[i];
			
		//Check for next turn winning moves to block.
		for(int i = 0; i < Board.COLUMNS; i++) 
			if(game.board.column[i].testDoesPieceWin(game.board.game.player[0], difficulty)) 
				return game.board.column[i];
		
		//Important checks done.. return random. Could return null if easyMove, mediumMove, and hardMove are improved upon.
		Board.Column c = null;
		while(c == null) {
			c = game.board.column[(int) (Math.random() * Board.COLUMNS)];
			if(c.cell[Board.ROWS-1].owner != null)
				c = null;
		}
		return c;
	}
	
	/**
	 * Check to see if the computer just won the game. Use after the AI moves.
	 * @param c The column which possibly won the game. It sets the gamePanel's winningPiece attribute so that a message appears directly on the piece.
	 */
	void checkWinAI(Board.Column c) {
		Player p = game.board.checkWin();
		if(p != null) {
			int x = Board.ROWS - 1;
			while(c.cell[x].owner == null)
				x--;
			c.cell[x].gamePanel_ptr.winningPiece = game.player[1];
			c.cell[x].gamePanel_ptr.repaint();
			c.cell[x].gamePanel_ptr.boardViewptr.cleanup_endgame();
		}
	}
	
	private Game game;
	public static int difficulty = 1;
}
