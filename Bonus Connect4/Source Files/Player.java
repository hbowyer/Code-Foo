import java.awt.Color;

/**
 * An abstract class for Humans or AIs.
 * @author Harry Bowyer
 *
 */
public abstract class Player {
	public Player() {}
	
	abstract void move();

	protected Color color;
}
