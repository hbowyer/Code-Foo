import java.awt.Color;

/**
 * Handles the game logic. Only one board is currently supported at a time.
 * @author Harry Bowyer
 *
 */
public class Board {
	public Board(Game game) {
		this.game = game;
		for(int i = 0; i < COLUMNS; i++)
			column[i] = new Column();
	}
	
	/**
	 * Remove cell owners. Cells will display properly when a viewer refreshes itself.
	 */
	public void reset() {
		for(Column n : column)
			for(Cell c : n.cell)
				c.owner = null;
	}
	
	/**
	 * Checks all directions for a 4 of a kind and returns a winner, or null if none.
	 * @return The winning player, or null if none.
	 */
	public Player checkWin() {
		Player winner = null;
		winner = checkRows();
		if(winner == null)
			winner = checkColumns();
		if(winner == null)
			winner = checkDiagonalUp();
		if(winner == null)
			winner = checkDiagonalDown();
		if(winner != null) 
			return winner;
		return null;
	}
	
	/**
	 * Checks rows for a 4 of kind match.
	 * @return The winning player, or null if no winner yet.
	 */
	public Player checkRows() {
		int count = 0; //a count of 3 means 3 matches were found from where it checked (4 in a row)
		Player player = null, newp = null;
		
		for(int x = 0; x < Board.ROWS; x++) {
			for(int y = 0; y < Board.COLUMNS; y++) {
				newp = column[y].cell[x].owner;
				if(newp == null) {
					player = null;
					count = 0;
				}
				else {
					if(newp.equals(player))
						count++;
					else {
						player = newp;
						count = 0;
					}
					if(count == 3)
						return player;
				}
			}
			newp = null;
			player = null;
			count = 0;
		}
		
		return null;
	}
	
	/**
	 * Checks columns for a 4 of kind match.
	 * @return The winning player, or null if no winner yet.
	 */
	public Player checkColumns() {
		int count = 0; //a count of 3 means 3 matches were found from where it checked (4 in a row)
		Player player = null, newp = null;
		
		for(int y = 0; y < Board.COLUMNS; y++) {
			for(int x = 0; x < Board.ROWS; x++) {
				newp = column[y].cell[x].owner;
				if(newp == null) {
					player = null;
					count = 0;
				}
				else {
					if(newp.equals(player))
						count++;
					else {
						player = newp;
						count = 0;
					}
					if(count == 3)
						return player;
				}
			}
			newp = null;
			player = null;
			count = 0;
		}
		
		return null;
	}
	
	/**
	 * Checks diagonals upwards for a 4 of kind match.
	 * @return The winning player, or null if no winner yet.
	 */
	public Player checkDiagonalUp() {
		for(int y = 0; y + 3 < Board.COLUMNS; y++) {
			for(int x = 0; x + 3 < Board.ROWS; x++) {
				if(column[y].cell[x].owner != null &&
						column[y].cell[x].owner == column[y+1].cell[x+1].owner &&
						column[y].cell[x].owner == column[y+2].cell[x+2].owner &&
						column[y].cell[x].owner == column[y+3].cell[x+3].owner) 
					return column[y].cell[x].owner;
			}
		}
		
		return null;
	}
	
	/**
	 * Checks diagonals downwards for a 4 of kind match.
	 * @return The winning player, or null if no winner yet.
	 */
	public Player checkDiagonalDown() {
		for(int y = 0; y + 3 < Board.COLUMNS; y++) {
			for(int x = 3; x < Board.ROWS; x++) {
				if(column[y].cell[x].owner != null &&
						column[y].cell[x].owner == column[y+1].cell[x-1].owner &&
						column[y].cell[x].owner == column[y+2].cell[x-2].owner &&
						column[y].cell[x].owner == column[y+3].cell[x-3].owner) 
					return column[y].cell[x].owner;
			}
		}
		
		return null;
	}
	
	/**
	 * Check to see if the board is filled up.
	 * @return True if filled up (is a draw), false if not.
	 */
	public boolean isDraw() {
		boolean isDraw = true;
		for(int i = 0; i < Board.COLUMNS; i++)
			if(game.board.column[i].cell[Board.ROWS - 1].owner == null)
				isDraw = false;
		return isDraw;
	}
	
	/**
	 * A public class containing cells spanning upwards.
	 * @author Harry Bowyer
	 *
	 */
	public class Column {
		public Column() {
			for(int i = 0; i < ROWS; i++)
				cell[i] = new Cell(this);
		}
		
		/**
		 * Attempts to add a piece to the specific column, filling from the bottom up.
		 * @param player The player who wants to add a piece.
		 * @return Returns true if a piece was entered, false if there was no room.
		 */
		public boolean addPiece(Player player) {
			for(int i = 0; i < ROWS; i++){
				if(cell[i].owner == null) {
					cell[i].owner = player;
					return true;
				}
			}
			return false;
		}
		
		/**
		 * Checks what will happen if you place a piece in a certain column
		 * @param player The player's move to check
		 * @param depth The higher the depth, the further the checks. Needs a value of 1 to work, and increase by 1 for each level of AI.
		 * @return It returns true if the piece will win the game, false if not.
		 */
		public boolean testDoesPieceWin(Player player, int depth) {
			int pos = -1, i = 0;
			Player p = null;
			
			//Add a piece to the top of this column(the parent). NOTE: Must be removed before returning. This is not an actual move, but a test.
			while(i < ROWS && pos == -1) {
				if(cell[i].owner == null) {
					cell[i].owner = player;
					pos = i;
				}
				i++;
			}
			if(pos == -1) { //no room!
				return false;
			}
			
			//Quick parameter check.
			if(depth <= 0) {
				cell[pos].owner = null;
				System.out.println("Error");
				return false;
			}
			
			//Perform checks until the depth limit is reached.
			p = checkRows();
			depth--;
			if(p != null) {
				cell[pos].owner = null;
				return true;
			}
			if(depth == 0) {
				cell[pos].owner = null;
				return false;
			}
			
			p = checkColumns();
			depth--;
			if(p != null) {
				cell[pos].owner = null;
				return true;
			}
			if(depth == 0) {
				cell[pos].owner = null;
				return false;
			}
						
			p = checkDiagonalUp();
			if(p == null)
				p = checkDiagonalDown();
			if(p != null) {
				cell[pos].owner = null;
				return true;
			}
			
			//Out of checks.
			cell[pos].owner = null;
			return false;
		}
		
		protected Cell[] cell = new Cell[ROWS];
	}
	
	public class Cell {
		public Cell(Column c) {
			columnOwner = c;
			color = Color.LIGHT_GRAY;
		}
		
		protected Column columnOwner;
		protected Player owner = null;
		protected Color color;
		protected BoardView.GamePanel gamePanel_ptr; //points to the cell's graphical component
	}
	
	protected Column[] column = new Column[COLUMNS];
	protected Game game;
	public static int COLUMNS = 7, ROWS = 6;
}
