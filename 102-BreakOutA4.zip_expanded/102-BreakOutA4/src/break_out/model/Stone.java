package break_out.model;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Random;

import break_out.Constants;

/**
 * @author Malte Horstmann
 * 
 * Abgabegruppe 102
 */
public class Stone
{
	/**
	 * The current position on the field
	 */
	private Position position;
	
	/**
	 * The position in the stone matrix
	 */
	private Position matrixPosition;
	
	/**
	 * The maximum health points
	 */
	private int hp;
	
	/**
	 * The current health points
	 */
	private int currentHp;
	
	/**
	 * The current color
	 */
	private Color color;
	
	/**
	 * The hitbox
	 */
	private Rectangle rectangle;
	
	/**
	 * The indicator that shows if the stone is a bomb
	 */
	private boolean isBomb;
	
	
	/**
	 * Constructor 
	 */
	Stone(int x, int y, int type)
	{
		if(type == -1)
		{
	    	Random rand = new Random();
	    	
	    	// Gives the stone a random type
			type = rand.nextInt(4);
			
			// 10% chance that a stone becomes a bomb
			if(rand.nextInt(10) == 0)
				type = 4;
		}
		
		// Setups the stone based on it's type
		switch(type)
		{
			case 0:
				color = new Color(255,255,0);
				hp = 1;
				break;
			case 1:
				color = new Color(0,0,255);
				hp = 2;
				break;
			case 2:
				color = new Color(0,255,0);
				hp = 3;
				break;
			case 3:
				color = new Color(255,0,0);
				hp = 4;
				break;
			case 4:
				this.isBomb = true;
				color = new Color(0,0,0);
				hp = 3;
		}
		
		currentHp = hp;
		
		matrixPosition = new Position(x,y);
		
		position = new Position(x * Constants.STONE_WIDTH, y * Constants.STONE_HEIGHT);
		
		rectangle = new Rectangle((int)position.getX(), (int) position.getY(), (int)Constants.STONE_WIDTH, (int)Constants.STONE_HEIGHT);
	}
	
	/**
	 * Getter for the maximum health points
	 * @return hp
	 */
	public int getHp()
	{
		return hp;
	}
	
	/**
	 * Getter for the current health points
	 * @return currentHp
	 */
	public int getCurrentHp()
	{
		return currentHp;
	}
	
	/**
	 * Getter for the current color
	 * @return color
	 */
	public Color getColor()
	{
		return color;
	}
	
	/**
	 * Getter for the position on the field
	 * @return position
	 */
	public Position getPosition()
	{
		return position;
	}
	
	/**
	 * Getter for the hitbox
	 * @return rectangle
	 */
	public Rectangle getRec()
	{
		return rectangle;
	}
	
	/**
	 * Getter for the position in the stone matrix 
	 * @return matrixPosition
	 */
	public Position getMatrixPosition()
	{
		return matrixPosition;
	}
	
	/**
	 * Increments the current health points by the damage given
	 * @param damage
	 */
	public void updateHp(int damage)
	{
		currentHp -= damage;
	}
	
	/**
	 * Makes the stone brighter by a factor thats
	 * depends from the stones health points
	 * Or makes the stone more red if it's a bomb 
	 */
	public void updateColor()
	{
		if(isBomb)
		{		
			color = new Color(color.getRed() + 75, 0, 0);
		}
		else
		{
			int currentR = color.getRed();
			int currentG = color.getGreen();
			int currentB = color.getBlue();
			
			float tint_factor = 1f/this.getHp();
			
			int newR = (int)(currentR + (255- currentR) * tint_factor);
			int newG = (int)(currentG + (255- currentG) * tint_factor);
			int newB = (int)(currentB + (255- currentB) * tint_factor);
			
			color = new Color(newR, newG, newB);
		}
	}
	
	/**
	 * Delete the stone or make the bomb explode
	 * @param stoneMatrix The stone matrix for deleting the stone
	 * @param level The Level object is needed for access to needed functions
	 */
	public void shatter(Stone[][] stoneMatrix, Level level)
	{
		int x = (int) matrixPosition.getX();
		int y = (int) matrixPosition.getY();
		
		if(isBomb)
		{
			//delete stone
			if(stoneMatrix[y][x] != null) //sometimes wants to delete stones that are already deleted ... the if-statement is just a temporary workaround.
			{
				stoneMatrix[y][x] = null;
				level.stoneBroke(this);
				
				//look for bordering stones
				Stone[] stones = {stoneMatrix[y][x-1], stoneMatrix[y][x+1], stoneMatrix[y-1][x], stoneMatrix[y+1][x]};
				for(Stone stone : stones)
				{
					if(stone != null)
					{
						level.damageStone(3, stone);
					}
				}
			}
		}
		else
		{
			//delete stone
			stoneMatrix[y][x] = null;
			level.stoneBroke(this);
		}
	}
}
