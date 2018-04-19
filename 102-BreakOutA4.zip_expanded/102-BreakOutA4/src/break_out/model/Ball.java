package break_out.model;

import java.awt.Rectangle;
import break_out.Constants;

/** 
 * @author Malte Horstmann
 * 
 * Abgabegruppe 102
 */
public class Ball
{
	/**
	 * The position of the ball
	 */
	private Position position;
	
	/**
	 * The motion direction
	 */
	private Vector2D direction;
	
	/**
	 * The position that the ball has in the stone matrix
	 */
	private Position matrixPosition;
	
	/**
	 * The hitbox of the ball
	 */
	private Rectangle rectangle;
	
	/**
	 * The indicator that shows if the player lost a life
	 */
	private boolean lostLife;
	
    /**
     * Constructor for the ball
     */
	public Ball ()
	{
	// Sets the position to the middle of the screen
	position = new Position(Constants.SCREEN_WIDTH/2 - Constants.BALL_DIAMETER/2,
							Constants.SCREEN_HEIGHT - Constants.BALL_DIAMETER - Constants.PADDLE_HEIGHT);
	// creates the hitbox
	rectangle = new Rectangle((int)this.position.getX(), (int)this.position.getY(), (int)Constants.BALL_DIAMETER, (int)Constants.BALL_DIAMETER);
	// sets the direction
	direction = new Vector2D(-5,5);
	// normalizes the direction
	direction.rescale();
	
	this.lostLife = false;
	}
	
	/**
	 * Getter for the position
	 * @return position The current ball position
	 */
	public Position getPosition()
	{
		return position;
	}
	
	/**
	 * Getter for the matrix position
	 * @return matrixPosition The current position in the stone matrix
	 */
	public Position getMatrixPosition()
	{
		return matrixPosition;
	}
	
	/**
	 * Getter for the direction
	 * @return direction The direction in which the ball currently moves
	 */
	public Vector2D getDirection()
	{
		return direction;
	}
	
	/**
	 * Getter for the lostLife indicator
	 * @return lostLife The indicator that shows if the player lost a life
	 */
	public boolean hitBottom()
	{
		return lostLife;
	}
	
	/**
	 * Moves the ball
	 */
	public void updatePosition()
	{
		// Moves the ball in the current direction 
		position.setX (position.getX() + direction.getDx());
		position.setY (position.getY() + direction.getDy());
		//adapts the hitbox position to the ball position
		rectangle.setBounds((int)this.position.getX(),
				 			(int)this.position.getY(),
				 			(int)Constants.BALL_DIAMETER,
				 			(int)Constants.BALL_DIAMETER);
	}
	
	/**
	 * Checks if the ball hits a wall and lets it behave accordingly 
	 */	
	public void reactOnBorder ()
	{
		double PositionX = position.getX();
		double PositionY = position.getY();
		double tempDx = direction.getDx();
		double tempDy = direction.getDy();
		
		// Inverses the x- or y-direction accordingly and corrects the ball position
		
		// Case: right wall
		if (PositionX > Constants.SCREEN_WIDTH - Constants.BALL_DIAMETER)
		{
			direction.setDx(-tempDx);
			position.setX(Constants.SCREEN_WIDTH - Constants.BALL_DIAMETER);
		}
		
		// Case: left wall
		else if (PositionX < 0)
		{
		direction.setDx(-tempDx);
		position.setX(0);
		}
		
		// Case: bottom wall
		if (PositionY > Constants.SCREEN_HEIGHT - Constants.BALL_DIAMETER)
		{
			this.lostLife = true;
		}
		
		// Case: upper wall
		else if (PositionY < 0)
		{
			direction.setDy(-tempDy);
			position.setY(0);
		}
	}
	
	/**
	 * Checks if the ball hits the paddle
	 * @param p Paddle object
	 * @return true if the ball hits the paddle
	 */
	public boolean hitsPaddle (Paddle p)
	{
		
		if (position.getY() + Constants.BALL_DIAMETER >= p.getPaddlePos().getY())
		{
			
			if(position.getX() + Constants.BALL_DIAMETER >= p.getPaddlePos().getX() && position.getX() <= p.getPaddlePos().getX() + Constants.PADDLE_WIDTH)
			{	
				return true;
			}
		}
		
		return false; 
	}
	
	/**
	 * Reflects the ball on the paddle
	 * @param paddle Paddle object
	 */
	public void reflectOnPaddle (Paddle paddle)
	{
		if(hitsPaddle(paddle))
		{
			this.direction = new Vector2D (new Position(paddle.getPaddlePos().getX() + Constants.PADDLE_WIDTH / 2 - Constants.BALL_DIAMETER / 2, Constants.SCREEN_HEIGHT), getPosition());
			this.direction.rescale();
		} 
		
	}
	
	/**
	 * Checks if the ball hits a stone from the stone matrix
	 * @param stoneMatrix a 2DArray of stones
	 */
	public boolean hitsStone(Stone[][] stoneMatrix)
	{
		Rectangle stoneRec;
		Rectangle ballRec = rectangle;
		
		int ballX = (int)((this.position.getX() + Constants.BALL_DIAMETER/2) / Constants.STONE_WIDTH);
		int ballY = (int)((this.position.getY() + Constants.BALL_DIAMETER/2) / Constants.STONE_HEIGHT);
		ballX--;
		ballY--;
		
    	double tempDx = direction.getDx();
    	double tempDy = direction.getDy();
    	
		//Check if a stone is at this point
		for(int y = 0; y < 3; y++)
		{
			for(int x = 0; x < 3; x++)
			{	
				if((ballY+y) < stoneMatrix.length && (ballX+x) < stoneMatrix[0].length && ballY+y >= 0 && ballX+x >= 0)
				{
					Stone currentStone = stoneMatrix[ballY+y][ballX+x];
					
					if(currentStone != null)
					{
						stoneRec = currentStone.getRec();
						if(ballRec.intersects(stoneRec))
						{
							// Top
							if(ballRec.intersectsLine(stoneRec.x+2, stoneRec.y, stoneRec.getMaxX()-2, stoneRec.y))
							{
								position.setY(stoneRec.y - Constants.BALL_DIAMETER);
								direction.setDy(-tempDy);
								matrixPosition = currentStone.getMatrixPosition();

								return true;
							}
							// Bottom
							else if(ballRec.intersectsLine(stoneRec.x+2, stoneRec.getMaxY(), stoneRec.getMaxX()-2, stoneRec.getMaxY()))
							{
								position.setY(stoneRec.getMaxY() + Constants.BALL_DIAMETER);
								direction.setDy(-tempDy);
								matrixPosition = currentStone.getMatrixPosition();

								return true;
							}
							// Left
							if(ballRec.intersectsLine(stoneRec.x, stoneRec.y, stoneRec.x, stoneRec.getMaxY()))
							{
								position.setX(stoneRec.x - Constants.BALL_DIAMETER);
								direction.setDx(-tempDx);
								matrixPosition = currentStone.getMatrixPosition();

								return true;
							}
							// Right
							else if(ballRec.intersectsLine(stoneRec.getMaxX(), stoneRec.y, stoneRec.getMaxX(), stoneRec.getMaxY()))
							{
								position.setX(stoneRec.getMaxX());
								direction.setDx(-tempDx);
								matrixPosition = currentStone.getMatrixPosition();

								return true;
							}
							
						}
					}
				}
			}
		}
		//inform that there has been no collision
		return false;
	}
}
