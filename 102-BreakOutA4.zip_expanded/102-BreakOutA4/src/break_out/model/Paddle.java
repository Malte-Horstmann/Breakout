package break_out.model;

//Import of the required Public constants, used in the constructor
import break_out.Constants;

/**
 * @author Malte Horstmann
 * 
 * Abgabegruppe 102
 *
 */

public class Paddle {
	
	/**
	 * The position on the field
	 */
	private Position paddlePos;
	
	/**
	 * The motion direction 
	 */
	private int motionDirection;
	
	
	/**
	 * Constructor
	 */
	public Paddle()
	{
		paddlePos = new Position (Constants.SCREEN_WIDTH / 2 - Constants.PADDLE_WIDTH/2,
									Constants.SCREEN_HEIGHT - Constants.PADDLE_HEIGHT);
		motionDirection = 0;
	}
	
	/**
	 * Getter for the position on the field
	 * @return paddlePos The current position
	 */	
	public Position getPaddlePos()
	{
		return paddlePos;
	}
	
	/**
	 * Getter for the motion direction
	 * @return motionDirection The current direction in which the paddle moves
	 */
	public int getDirection()
	{
		return motionDirection;
	}
	
	/**
	 * Setter for the motion direction
	 * @param dir The new motion direction
	 */
	public void setDirection(int dir)
	{
		motionDirection = dir;
	}

	/**
	 * Holds the paddle within the field
	 */	
	public void reactOnBorder() 
	{		
		// Check right border
		if(getPaddlePos().getX() > Constants.SCREEN_WIDTH - Constants.PADDLE_WIDTH)
			getPaddlePos().setX(Constants.SCREEN_WIDTH - Constants.PADDLE_WIDTH);
		else
		// Check left border
		if(getPaddlePos().getX() < 0)
			getPaddlePos().setX(0);
	}
	
	/**
	 * Moves the paddle
	 */	
	public void updatePosition()
	{	
		paddlePos.setX(paddlePos.getX() + motionDirection * Constants.DX_MOVEMENT);
		reactOnBorder();
	}
}
