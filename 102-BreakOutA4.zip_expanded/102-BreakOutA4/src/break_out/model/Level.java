package break_out.model;

import break_out.controller.JSONReader;

/**
 * This object contains information about the running game
 * 
 * @author dmlux
 * @author I. Schumacher
 * @author Malte Horstmann
 * 
 * Abgabegruppe 102
 */
public class Level extends Thread
{

    /**
     * The game to which the level belongs 
     */
    private Game game;
	 
    /**
   	 * The number of the level
   	 */
    private int levelnr;
       
    /**
	 * The score of the level
	 */
    private int score;
    
    /**
     * The current ball in the level
     */
    private Ball ball;
    
    /**
     * The current paddle in the level
     */
    private Paddle paddle;
    
    /**
     * Holds the current amount of stones 
     */
    private int stones;
    
    /**
     * The matrix of stones object
     */
    private Stone[][] stoneMatrix;
    
    /**
     * Flag that shows if the ball was started
     */
    private boolean ballWasStarted = false;
    
    /**
     * Determines if the thread stops 
     */
    private boolean shouldStop = false;
    
    /**
     * Determines how many times the Ball can Hit the bottom
     */
    private int lifeCounter;

        
    /**
     * Constructor
     * @param game the assigned game
     * @param levelnr The level number
     * @param score The so far achieved score
     */
    public Level(Game game, int levelnr, int score)
    {
    	this.game = game;
    	this.levelnr = levelnr;
    	this.score = score;
    	shouldStop = false;
    	stones = 0;
        ball = new Ball();
        paddle = new Paddle();
        
        loadLevelData(levelnr);
    }
        
    
    /**
     * Getter for the ball
     * @return ball The current ball of the level
     */
   public Ball getBall()
   {
	   return ball;
   }
   
   /**
    * Getter for the Paddle
    * @return paddle The current paddle of the level
    */
   public Paddle getPaddle()
   {
	   return paddle;
   }
    
    /**
     * Gets the movement state of the ball
     * @return ballWasStarted True if the ball is moving
     */
    public boolean ballWasStarted()
    {
        return ballWasStarted;
    }
    
    /**
     * Getter for the stone matrix
     * @return stoneMatrix
     */
    public Stone[][] getStoneMatrix()
    {
    	return stoneMatrix;
    }
    
    /**
     * Getter for the life counter
     * @return lifeCounter
     */
    public int getLifeCounter()
    {
    	return lifeCounter;
    }
    
    /**
     * Starts the ball
     */
    public void startBall()
    {
        ballWasStarted = true;
    }

    /**
     * Stops the ball
     */
    public void stopBall()
    {
        ballWasStarted = false;
    }
    
    /**
     * Informs the level that a stone broke
     * @param stone The stone that broke
     */
    public void stoneBroke(Stone stone)
    {
    	score += stone.getHp() * 10;
    	game.setScore(score);
    	stones--;
    }
    
    /**
     * Damages the stone at the current position of the ball
     * @param damage The amout of damage
     */
    public void damageStone(int damage)
    {		
		//get the stone object at the ball position
    	int x = (int)ball.getMatrixPosition().getX();
    	int y = (int)ball.getMatrixPosition().getY();
		Stone stone = stoneMatrix[y][x];
		
		stone.updateHp(damage);
		
		if(stone.getCurrentHp() <= 0)
		{
			//delete stone
			stone.shatter(stoneMatrix, this);
		}
		else
		{
			stone.updateColor();
		}
    }
    
    /**
     * Damages the given stone
     * @param damage The amount of damage
     * @param stone The stone that gets damaged
     */
    public void damageStone(int damage, Stone stone)
    {		
		stone.updateHp(damage);
		
		if(stone.getCurrentHp() <= 0)
		{
			//delete stone
			stone.shatter(stoneMatrix, this);
		}
		else
		{
			stone.updateColor();
		}
    }
    
    /**
     * Stops the level thread
     */
    public void stopThread()
    {
    	shouldStop = true;
    }
    
    /**
     * Checks if the player has any lives left
     * if thats true resets the paddle and the ball
     * otherwise switches to the startscreen 
     */
    public void handleDeath()
    {
    	lifeCounter--;
    	if(lifeCounter <= 0)
    	{
    		game.getController().endGame();
    		game.getController().toStartScreen();
    	}
    	else
    	{
    		ballWasStarted = false;
    		ball = new Ball();
    		paddle = new Paddle();
    	}
    }

    /**
     * This method directs what's happening in the thread
     */
    public void run()
    {	
    	game.notifyObservers();
    
    	// Until the ball should stop
    	while (!shouldStop)
    	{
    		// When the ball should move
	        if (ballWasStarted)
	        {
	        	ball.reactOnBorder();
	        	
	        	ball.updatePosition();
	        	
	        	// If the player lost a life
	        	if(ball.hitBottom())
	        	{
	        		handleDeath();
	        	}
	        	
	        	// when the ball hit a stone
	         	if(ball.hitsStone(stoneMatrix))
	        	{	
	        		damageStone(1);
	        	}	  
	         	
	         	// If all stones are destroyed
	        	if(stones <= 0)
	        	{
	        		shouldStop = true;
	        		game.createLevel(++levelnr, score);
	        	}
	        	
	        	ball.reflectOnPaddle(paddle);
	        	
	        	paddle.updatePosition();

	        	paddle.reactOnBorder();
	        	
	            // Das als Observer angemeldete View-Objekt wird informiert, damit ein Neuzeichnen (repaint)
	        	// des Spielfeldes vorgenommen wird.
	            game.notifyObservers(); 
	            
	            
	        }
	        
	        // wait a short time
	        try {
	            Thread.sleep(4);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
    	} 
}

    /**
    * Load the whole data of the given level
    * @param levelnr The number X for the LevelX.json file
    */
    private void loadLevelData(int levelnr)
    {
        JSONReader reader = new JSONReader("res/Level"+levelnr+".json");
        
    	lifeCounter = reader.getLifeCounter();
    	
    	int[][] stonepattern = reader.getStones2DArray();
    	int[][] stoneTpyes = reader.getStoneTypes2DArray();
    	
    	stoneMatrix = new Stone[stonepattern.length][stonepattern[0].length];
    	
    	// Iterates through the stone pattern and creates
    	// the stone matrix from it. Also uses stoneTypes
    	// to get the type of the stone at the current 
    	// position in the stone pattern
    	for(int y = 0; y < stonepattern.length; y++)
    	{
    		for(int x = 0; x < stonepattern[y].length; x++)
        	{
    			if(stonepattern[y][x] == 1)
    			{ 	   				
    				stoneMatrix[y][x] = new Stone(x, y, stoneTpyes[y][x]);
    				stones++;
    			}
    			else
    			{
    				stoneMatrix[y][x] = null;
    			}
        	}
    	}		
    }   
}