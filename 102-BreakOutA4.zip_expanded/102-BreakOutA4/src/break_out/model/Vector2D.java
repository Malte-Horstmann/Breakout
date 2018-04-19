package break_out.model;

import break_out.Constants;


/**
 * @author Malte Horstmann
 * 
 * Abgabegruppe 102
 */

public class Vector2D {
	
	/**
	 * x coordinate
	 */
	private double dx;
	
	/**
	 * y coordinate
	 */
	private double dy;
	
	/**
	 * Constructor for a direct vector construction
	 * @param dx
	 * @param dy
	 */	
	public Vector2D (double dx, double dy)
	{
		this.dx = dx;
		this.dy = dy;
	}
		
	/**
	 * constructor for a vector between two points
	 * @param pos1
	 * @param pos2
	 */
	public Vector2D(Position pos1, Position pos2)
	{
		this.dx = pos2.getX()-pos1.getX();
		this.dy = pos2.getY()-pos1.getY();
	}
		
	/**
	 * Setter for the x coordinate
	 * @param dx the new x coordinate
	 */	
	public void setDx(double dx)
	{
		this.dx = dx;
	}
	
	/**
	 * Getter for the x coordinate
	 * @return dx
	 */
	public double getDx()
	{
		return dx;
	}
	
	/**
	 * Setter for the y coordinate
	 * @param dy the new y coordinate
	 */
	public void setDy (double dy)
	{
		this.dy = dy;
	}
	
	/**
	 * Getter for the y coordinate
	 * @return dy
	 */
	public double getDy()
	{
		return dy;
	}
	
	/**
	 * Rescales the vector to the ball speed
	 */
	public void rescale()
	{
		// Length of the vector
		double length = Math.sqrt(dx*dx + dy*dy);
		
		// Normalizes the vector and multiplies it by the ball speed
		dx = (dx/length) * Constants.BALL_SPEED;
		dy = (dy/length) * Constants.BALL_SPEED;
	}
}
