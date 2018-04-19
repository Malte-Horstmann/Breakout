package break_out.controller;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import javax.swing.JRadioButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputListener;

import break_out.model.Game;
import break_out.view.Field;
import break_out.view.LevelEditor;
import break_out.view.StartScreen;
import break_out.view.View;

/**
 * The controller takes care of the input events and reacts on those events by
 * manipulating the view and updates the model.
 * 
 * @author dmlux, modified by I.Schumacher
 * @author Malte Horstmann
 * 
 * Abgabegruppe 102
 */
public class Controller implements ActionListener, KeyListener, ItemListener, MouseInputListener, ListSelectionListener {

    /**
     * The game as model that is connected to this controller
     */
    private Game game;

    /**
     * The view that is connected to this controller
     */
    private View view;
    
    /**
     * The constructor expects a view to construct itself.
     * 
     * @param view The view that is connected to this controller
     */
    public Controller(View view)
    {
        this.view = view;
        this.game = view.getGame();
        // Assigning the listeners
        assignListeners();
    }

    /**
     * The controller gets all buttons out of the view with this method and adds
     * this controller as an action listener. Every time the user pushed a
     * button the action listener (this controller) gets an action event.
     */
    private void assignListeners()
    {
        // Get the start screen to add this controller as action
        // listener to the buttons.
        view.getStartScreen().addActionListenerToStartButton(this);
        view.getStartScreen().addActionListenerToQuitButton(this);
        view.getStartScreen().addActionListenerToEditorButton(this);
        
        // Get the field to add this controller as KeyListener
        view.getField().addKeyListener(this);
        
        // Get the level editor to add this controller as ItemListener
        // to the drop down list
        view.getLevelEditor().addItemListenerToSelectios(this);
        
        // Get the level editor to add this controller as MouseListener
        // to the field
        view.getLevelEditor().addMouseListenerToField(this);
        
        view.getLevelEditor().addActionListenerToSelectioButtons(this);
        
        view.getLevelEditor().addListSelectionListenerToSelection(this);
    }
    
    /**
     * Gets called when the player selects a different stone type
     */
	@Override
	public void itemStateChanged(ItemEvent e)
	{
		LevelEditor levelEditor = view.getLevelEditor();
		
		if(e.getStateChange() == ItemEvent.SELECTED)
		{
			JRadioButton[] selections = levelEditor.getSelections();
			for(int i = 0; i < selections.length; i++)
			{
				if(e.getSource().equals(selections[i]))
				{
					levelEditor.setStoneType(i);
				}
			}	
		}
	} 
	
	/**
	 * Gets called when the player selects a different option from the level selection
	 */
	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		LevelEditor levelEditor = view.getLevelEditor();
		
		int selectedLevel = (levelEditor.getlevelOptions().getSelectedIndex()+1);
		levelEditor.setSelectedLevel(selectedLevel);
	}
	
    /**
     * Gets called when the player clicks a button
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        // Points to the startScreen object to use it's functions
        StartScreen startScreen = view.getStartScreen();
        
        // The start button got clicked
        if (startScreen.getStartButton().equals(e.getSource()))
        {
            // Reads the player name 
            String playersName = startScreen.getPlayersName();
            playersName = playersName.trim();
            if (playersName.length() < 1 || playersName.length() > 30)
            {
                // Display an error message if the name too long or empty
                startScreen.showError("Der Name ist ungültig");
            }
            else
            {    
            	// Starts a new game
    	        game = new Game(this, view.getLevelEditor().getMaxLevel());
    	        view.setGame(game);
            }
        }

        // The quit button got clicked
        else if (startScreen.getQuitButton().equals(e.getSource()))
        {
            System.exit(0);
        }
        // The level editor button got clicked
        else if(startScreen.getEditorButton().equals(e.getSource()))
        {
        	toLevelEditor();
        }
        // The cancel button for the level editor got clicked
        else if(e.getSource().equals(view.getLevelEditor().getCancelButton()))
        {
        	view.getLevelEditor().resetEditor();
        	toStartScreen();
        }
        // The save button for the level editor got clicked
        else if(e.getSource().equals(view.getLevelEditor().getSaveButton()))
        {
        	view.getLevelEditor().save();
        	toStartScreen();
        }
    }
    
    /**
     * Gets called when the player presses a key
     */
    @Override
    public void keyPressed(KeyEvent e)
    {
    	// The space key got pressed
    	if (e.getKeyCode() == KeyEvent.VK_SPACE)
    	{
    		if (game.getLevel().ballWasStarted())
    		{
    			// Stops the all if he's moving
    			game.getLevel().stopBall();
    		}
    		else
    		{
    			// Starts the ball movement if he's not already moving
    			game.getLevel().startBall();
    		}
    	}
    	// The left arrow key got pressed
    	if(e.getKeyCode() == KeyEvent.VK_LEFT)
    	{
    		// Moves the paddle to the left
    		game.getLevel().getPaddle().setDirection(-1);
    	}
    	// The right arrow-key got pressed
    	if(e.getKeyCode() == KeyEvent.VK_RIGHT)
    	{
    		// Moves the paddle to the right
    		game.getLevel().getPaddle().setDirection(+1);
    	}
    	// The escape key got pressed
    	if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
    	{
    		// Show the startscreen
    		endGame();
    		toStartScreen();
    	}
    }

    /**
     * Gets called when the player releases a key
     */
    @Override
    public void keyReleased(KeyEvent e)
    {
    	// Stops the paddle
    	game.getLevel().getPaddle().setDirection(0);
        
    }
    
    /**
     * Ends the games and adds the current score to the score list if a game is running
     */
    public void endGame()
    {
    	try
		{
        	game.getLevel().stopThread();
        	view.getStartScreen().addScore(game.getScore());
		}
		catch(Exception e)
		{
			System.out.println(".");
		}
    }
    
    /**
     * Displays the startscreen
     */
    public void toStartScreen()
    {
    	view.showScreen(StartScreen.class.getName());
    	view.getStartScreen().requestFocusInWindow();
    }
    
    /**
     * Displays the playground(field)
     */
    public void toPlayground()
    {
    	view.showScreen(Field.class.getName());
    	view.getField().requestFocusInWindow();
    }
    
    /**
     * Displays the level editor
     */
    public void toLevelEditor()
    {
    	view.showScreen(LevelEditor.class.getName());
    	view.getField().requestFocusInWindow();
    }
    
	@Override
	public void keyTyped(KeyEvent arg0)
	{
		// TODO Auto-generated method stub	
	}
    
    public void mouseClicked(MouseEvent e)
    {
    	// TODO Auto-generated method stub
    }
    
	@Override
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// Creates a stone at the position of the mouse
		view.getLevelEditor().editField(e.getX(), e.getY());
	}

	@Override
	public void mouseDragged(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseMoved(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
	}
}