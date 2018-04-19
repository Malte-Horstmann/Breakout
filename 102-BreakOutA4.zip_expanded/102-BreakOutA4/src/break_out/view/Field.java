package break_out.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import break_out.Constants;
import break_out.model.Stone;
import net.miginfocom.swing.MigLayout;

/**
 * The field represents the board of the game. All components are on the board
 * 
 * @author dmlux, modified by iSchumacher
 * @author Malte Horstmann
 * 
 * Abgabegruppe 102
 */
public class Field extends JPanel {

	/**
	 * Automatic generated serial version UID
	 */
	private static final long serialVersionUID = 2434478741721823327L;

	/**
	 * The connected view object
	 */
	private View view;

	/**
	 * The background color
	 */
	private Color background;
	
	/**
	 * The image object for the life counter
	 */
	private BufferedImage image;
	
	/**
	 * The constructor needs a view
	 * 
	 * @param view The view of this board
	 */
	public Field(View view)
	{
		super();

		this.view = view;
		background = new Color(255, 255, 255);
		
		try
		{
			image = ImageIO.read(new File("res/heart.png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		setFocusable(true);

		// Load settings
		initialize();
	}

	/**
	 * Initializes the settings for the board
	 */
	private void initialize()
	{
		// creates a layout
		setLayout(new MigLayout("", "0[grow, fill]0", "0[grow, fill]0"));
	}

	/**
	 * Change the background color
	 * @param color The new color
	 */
	public void changeBackground(Color color)
	{
		background = color;
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		double w = Constants.SCREEN_WIDTH;
		double h = Constants.SCREEN_HEIGHT;

		setPreferredSize(new Dimension((int) w, (int) h));
		setMaximumSize(new Dimension((int) w, (int) h));
		setMinimumSize(new Dimension((int) w, (int) h));

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);

		// Draw background
		g2.setColor(background);
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		drawBall(g2);	
		
		drawPaddle(g2);
		
		drawGrid(g2);
		
		drawStones(g2);  //
		
		drawScore(g2);
				
		drawLifeCounter(g2);
	}


	/**
	 * Draws the ball
	 * @param g2 The Graphics2D object that does the drawing
	 */
	private void drawBall(Graphics2D g2)
	{
		g2.setColor(new Color(200, 200, 200));
		
		g2.fillOval((int) view.getGame().getLevel().getBall().getPosition().getX(),
					(int) view.getGame().getLevel().getBall().getPosition().getY(),
					(int) (Constants.BALL_DIAMETER),
					(int) (Constants.BALL_DIAMETER));
		
		g2.drawRect((int)view.getGame().getLevel().getBall().getPosition().getX(),
					(int)view.getGame().getLevel().getBall().getPosition().getY(),
					(int)Constants.BALL_DIAMETER,
					(int)Constants.BALL_DIAMETER);
		
		g2.setColor(Color.GREEN);
		g2.fillRect((int)view.getGame().getLevel().getBall().getPosition().getX(),
					(int)view.getGame().getLevel().getBall().getPosition().getY(),
					2,
					2);
		g2.setColor(new Color(200, 200, 200));
	}
	
	/**
	 * Draws the paddle
	 * @param g2 The Graphics2D object that does the drawing
	 */
	private void drawPaddle(Graphics2D g2)
	{
		g2.fillRoundRect((int) view.getGame().getLevel().getPaddle().getPaddlePos().getX(),
						 (int) view.getGame().getLevel().getPaddle().getPaddlePos().getY(),
						 (int) (Constants.PADDLE_WIDTH),
						 (int) (Constants.PADDLE_HEIGHT), 8, 8);
	}
		
	/**
	 * Draws the Grid
	 * @param g2 The Graphics2D object that does the drawing
	 */
	private void drawGrid (Graphics2D g2)
	{
	
		// Draws the horizontal lines of the grid
		for (int i = 0; i < Constants.SCREEN_WIDTH; i = i + (int)Constants.SCREEN_WIDTH/Constants.SQUARES_X) {
			
			g2.drawLine(i, 0, i, (int)Constants.SCREEN_HEIGHT);
		}
		
		// Draws the vertical lines of the grid
		for (int j = 0; j < Constants.SCREEN_HEIGHT; j = j + (int)Constants.SCREEN_HEIGHT/Constants.SQUARES_Y) {
			g2.drawLine(0, j, (int)Constants.SCREEN_WIDTH, j);
		}
	}
		
	/**
	 * Draws the stones
	 * @param g2 The Graphics2D object that does the drawing
	 */
	private void drawStones(Graphics2D g2)
	{
		int width = (int)Constants.STONE_WIDTH;
		int height = (int)Constants.STONE_HEIGHT;
		
		Stone[][] stoneMatrix = view.getGame().getLevel().getStoneMatrix();
		
		// Draw each stone from the stone matrix
		for(int y = 0; y < stoneMatrix.length; y++)
		{
			for(int x = 0; x < stoneMatrix[y].length; x++)
			{
				if(stoneMatrix[y][x] != null)
				{
					g2.setColor(stoneMatrix[y][x].getColor());
					g2.fillRect((int)stoneMatrix[y][x].getPosition().getX()+2, (int)stoneMatrix[y][x].getPosition().getY()+2, width-3, height-3);					
				}				
			}		
		}
	}
	
	/**
	 * Draws the score
	 * @param g2 The Graphics2D object that does the drawing
	 */
	private void drawScore(Graphics2D g2)
	{
		g2.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g2.setColor(new Color(0,0,0));
		String score = "Score: " + view.getGame().getScore();
		g2.drawString(score, (int)Constants.SCREEN_WIDTH - score.length() * 9, 20);
	}
	
	/**
	 * Draws the life counter
	 * @param g2 The Graphics2D object that does the drawing
	 */
	private void drawLifeCounter(Graphics2D g2)
	{
		int lifeCounter = view.getGame().getLevel().getLifeCounter();
		
		for(int i = 0; i < lifeCounter; i++)
		{
			g2.drawImage(image, i * 35, 5, 32, 32, null);
		}
	}
}
		
		