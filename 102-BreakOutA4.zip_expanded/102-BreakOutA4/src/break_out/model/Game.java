package break_out.model;

import java.util.List;
import java.util.ArrayList;

import break_out.controller.Controller;
import break_out.view.View;

/**
 * This object contains information about the game (the model in MVC)
 * 
 * @author dmlux, modified by I. Schumacher
 * @author Malte Horstmann
 * 
 * Abgabegruppe 102
 */
public class Game{

    /**
	 * A list of observer objects
	 */
	private List<View> observers = new ArrayList<View>();

	/**
     * The controller of the game
     */
    private Controller controller;
	
    /**
   	 * The current level
   	 */
    private Level level;
    
    /**
     * The first levelnumber
     */
    private int firstLevel = 1;
    
    /**
     * The last levelnumber
     */
    private int maxLevel;  
       
    /**
	 * The total score of the game
	 */
    private int score = 0;
    
       
    /**
     * Constructor
     * @param controller The assigned controller
     */
    public Game(Controller controller, int maxLevel)
    {
        this.controller = controller;
        this.maxLevel = maxLevel;
        createLevel(firstLevel, 0);
    }

    
    // The three methods of the Observer ----------------
    public void addObserver(View observer)
    {
		observers.add(observer);
	}

	public void removeObserver(View observer)
	{
		observers.remove(observer);
	}

	public void notifyObservers()
	{
		for (View observer : observers)
			observer.modelChanged(this);
	}
	// -------------------------------------------------------
	
	/**
	 * Getter for the Controller
	 * @return controller The controller of this game
	 */
     public Controller getController()
     {
    	 return controller;
     }
     
     /**
      * Getter for the current Level
      * @return level The current level of the game
      */
     public Level getLevel()
     {
    	 return level;
     }
     
    /**
     * Getter for the total score
     * @return score The current score of the game
     */
    public int getScore()
    {
        return score;
    }
    
    /**
     * Setter for the total score
     * @param The new score
     */
    public void setScore(int score)
    {
        this.score = score;
    }
    
    /**
     * Setter for the maximum amount of levels
     * @param The new final level
     */
    public void setMaxLevel(int maxLevel)
    {
    	this.maxLevel = maxLevel;
    }
    
    /**
     * Creates the first or next level, if the levelnr is not greater than maxLevel
     * otherwise end the game and show the startscreen
     * @param levelnr The number of the new level
     * @param score The score that gets passed from the last level
     */
    public void createLevel(int levelnr, int score)
    {
    	this.score = score;
    	
    	if (levelnr <= maxLevel)
    	{
    		level = new Level(this, levelnr, score);
    		// calls the run-method of the level
        	level.start();
            // Go to playground
            controller.toPlayground();
    	}
    	else
    	{
    		// Go to startscreen
    		controller.endGame();
    		controller.toStartScreen();
    	}
    	
    }
    
}
