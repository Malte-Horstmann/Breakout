package break_out.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import break_out.Constants;
import net.miginfocom.swing.MigLayout;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;

/**
 * This screen serves the configuration of the game.
 * 
 * @author dmlux, modified by I. Schumacher
 * @author Malte Horstmann
 * 
 * Abgabegruppe 102
 */
public class StartScreen extends JPanel {

	/**
	 * Automatic generated serial version UID
	 */
	private static final long serialVersionUID = -131505828069382345L;

	/**
	 * Start game button
	 */
	private JButton startGame;

	/**
	 * Quit game button
	 */
	private JButton quitGame;

	/**
	 * Input field for the players name
	 */
	private JTextField playersName;

	/**
	 * The error label
	 */
	private JLabel error;
	
	/**
	 * The score label
	 */
	private List<JLabel> scores = new ArrayList<JLabel>();
	
	/**
	 * The player label
	 */
	private List<JLabel> players = new ArrayList<JLabel>();
	
	/**
	 * An ArrayList that holds the names of the players
	 */
	private List<String> playerNames = new ArrayList<String>();
	
	/**
	 * Level editor button
	 */
	private JButton levelEditor;
	
	/**
	 * Constructor
	 */
	public StartScreen()
	{
		super();

		double w = Constants.SCREEN_WIDTH;
		double h = Constants.SCREEN_HEIGHT;

		setPreferredSize(new Dimension((int) w, (int) h));
		setMaximumSize(new Dimension((int) w, (int) h));
		setMinimumSize(new Dimension((int) w, (int) h));

		initialize();
	}

	
	/**
	 * Initializes the settings for this screen
	 */
	private void initialize() 
	{
		// layout
		setLayout(new MigLayout("",
				"10[35%, center, grow, fill][65%, center, grow, fill]10",
				"10[center, grow, fill]10"));

		// background color
		setBackground(Constants.BACKGROUND);

		// initializes menu
		initializeLeftMenu();
		initializeScoreMenu();
	}

	/**
	 * Initializes the left menu
	 */
	private void initializeLeftMenu() 
	{
		// the layout
		SectionPanel leftMenu = new SectionPanel();
		leftMenu.shady = false;
		leftMenu.setLayout(new MigLayout("", "10[center, grow, fill]10",
				"10[center]30[center]5[center]20[center]5[center]5[center]0"));

		// adding components to the layout
		playersName = new JTextField();
		startGame = new JButton("Spiel starten");
		quitGame = new JButton("Spiel beenden");
		levelEditor = new JButton("Level editor");		
		
		error = new JLabel("");
		error.setForeground(new Color(204, 0, 0));
		error.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel menuLabel = new JLabel(Constants.APP_TITLE + " Spielmenü");
		menuLabel.setFont(new Font("Sans-serif", Font.PLAIN, 16));
		menuLabel.setHorizontalAlignment(SwingConstants.CENTER);

		leftMenu.add(menuLabel, "cell 0 0, growx");
		leftMenu.add(new JLabel("Spielername:"), "cell 0 1, growx, gapleft 5");
		leftMenu.add(playersName, "cell 0 2, growx");
		leftMenu.add(startGame, "cell 0 3, growx");
		leftMenu.add(levelEditor, "cell 0 4, growx");
		leftMenu.add(quitGame, "cell 0 5, growx");
		leftMenu.add(error, "cell 0 6, growx");
		add(leftMenu, "cell 0 0");
	}

	/**
	 * Initializes the right menu
	 */
	private void initializeScoreMenu() 
	{
		// The layout
		SectionPanel scoreMenu = new SectionPanel(Color.WHITE);
		scoreMenu.shady = false;
		scoreMenu.setLayout(new MigLayout("", "10[50%, center, grow, fill][50%, center, grow, fill]10",
				"5[center]5"));

		// adding the compoenents to the layout
		JLabel headline = new JLabel("Scores");
		headline.setFont(new Font("Sans-serif", Font.BOLD, 16));
		headline.setHorizontalAlignment(SwingConstants.CENTER);
		scoreMenu.add(headline, "cell 0 0, gaptop 20, gapbottom 20, span");
		
		loadScoreList(scoreMenu);
		
		add(scoreMenu, "cell 1 0, gapleft 5");
		
	}

	/**
	 * Adds an action listener to the start button
	 * @param l The actionListener
	 */
	public void addActionListenerToStartButton(ActionListener l) 
	{
		startGame.addActionListener(l);
	}

	/**
	 * Returns the start button
	 * @return startGame The button for starting the game
	 */
	public JButton getStartButton() 
	{
		return startGame;
	}

	/**
	 * Adds an action listener to the quit button
	 * @param l The actionListener
	 */
	public void addActionListenerToQuitButton(ActionListener l) 
	{
		quitGame.addActionListener(l);
	}

	/**
	 * Returns the quit button
	 * @return quitGame The button for ending the game
	 */
	public JButton getQuitButton() 
	{
		return quitGame;
	}
	
	/**
	 * Adds an action listener to the level editor button
	 * @param l The actionListener
	 */
	public void addActionListenerToEditorButton(ActionListener l) 
	{
		levelEditor.addActionListener(l);
	}

	/**
	 * Returns the level editor button
	 * @return levelEditor The button for opening the level editor
	 */
	public JButton getEditorButton() 
	{
		return levelEditor;
	}
	
	/**
	 * Returns the players name
	 * @return The name of the player in the JTextField playersName
	 */
	public String getPlayersName() 
	{
		return playersName.getText();
	}

	/**
	 * Shows an error in the menu
	 * @param message The String to be shown
	 */
	public void showError(String message) 
	{
		error.setText(message);
	}

	/**
	 * Removes error message from the screen
	 */
	public void hideError() 
	{
		error.setText("");
	}
	
	/**
	 * Creates an empty score list
	 * @param scoreMenu The menu, to which the score list gets added
	 */
	public void createEmptyScoreList(SectionPanel scoreMenu)
	{
		JLabel scoreTab;
		JLabel playerTab;
		
		for(int i = 1; i <= 10; i++)
		{
			playerTab = new JLabel("");
			playerTab.setHorizontalAlignment(SwingConstants.RIGHT);
			playerTab.setFont(new Font("Sans-serif", Font.PLAIN, 14));	
			
			scoreTab = new JLabel("-1");
			scoreTab.setHorizontalAlignment(SwingConstants.LEFT);
			scoreTab.setFont(new Font("Sans-serif", Font.PLAIN, 14));
			scoreTab.setVisible(false);
			
			scoreMenu.add(playerTab, "cell 0 "+ i +", gaptop 15, gapbottom 15");
			scoreMenu.add(scoreTab, "cell 1 "+ i +", gaptop 15, gapbottom 15, gapleft 15");
			
			playerNames.add("");
			players.add(playerTab);
			scores.add(scoreTab);
		}
	}
	
	/**
	 * Adds the given score to the score list
	 * if certain conditions are met
	 * @param score The score that should get added
	 */
	public void addScore(int score)
	{
		int scoresIndexValue;
		// Iterate trough all existing scores
		for(int i = 0; i < scores.size(); i++)
		{
			// The int value of the score from scores
			scoresIndexValue = Integer.parseInt(scores.get(i).getText());
			if(score > scoresIndexValue)
			{
				// Add the score to the list, if he's greater 
				// than a score from the list
				dropHighscores(i, score);
				break;
			}
			// He still get's added if there are less than
			// 10 scores in the list
			else if(scores.get(i).getText() == "")
			{
				players.get(i).setText((i+1) + ".  " + playersName.getText() + ":");
				playerNames.set(i, playersName.getText());
				
				scores.get(i).setText(score + "");
				scores.get(i).setVisible(true);
				break;
			}
		}
		
		saveScoreList();
	}
	
	/**
	 * Inserts a given score at a given index in the score list
	 * then drops all scores by 1
	 * @param index
	 * @param newScore
	 */
	private void dropHighscores(int index, int newScore)
	{
		String nextBestScore;
		// Iterate from the bottom to the given index through the score list
		for(int i = 9; i > index; i--)
		{
			// Get the next higher score
			nextBestScore = scores.get(i-1).getText();
			// Set Drop it by 1
			scores.get(i).setText(nextBestScore);
			
			if(Integer.parseInt(nextBestScore) != -1)
			{
				// Make the score visible, when
				// he's not empty(-1) 
				scores.get(i).setVisible(true);
			}
		}
		// Insert the given score at the given position
		scores.get(index).setText(newScore + "");
		scores.get(index).setVisible(true);
		

		String nextBestPlayer;
		// Iterate from the bottom to the given index through the score list
		for(int i = 9; i > index; i--)
		{
			// Get the next best player name
			nextBestPlayer = playerNames.get(i-1);
			if(!nextBestPlayer.isEmpty())
			{
				//Drop it by 1
				players.get(i).setText((i+1) + ".  " + nextBestPlayer + ":");
				playerNames.set(i, nextBestPlayer);
			}	
		}
		players.get(index).setText((index+1) + ".  " + playersName.getText() + ":");
		playerNames.set(index, playersName.getText());
	}
	
	/**
	 * Loads the score list from .json
	 * @param scoreMenu The menu, to which the score list gets added
	 * @return true If the loading was successful
	 */
	@SuppressWarnings("unchecked")
	private boolean loadScoreList(SectionPanel scoreMenu)
	{
		JSONParser parser = new JSONParser();
		JLabel playerTab;
		JLabel scoreTab;
		String playerName;
		int index = 1;
		try
		{
			Object obj = parser.parse(new FileReader("res\\scoreList.json"));
			JSONObject jsonObject = (JSONObject) obj;
			
			JSONArray array = (JSONArray) jsonObject.get("players");
			Iterator<String> iterator = array.iterator();
			
			while(iterator.hasNext())
			{
				playerName = iterator.next();
				playerNames.add(playerName);

				if(playerName.isEmpty())
				{
					playerTab = new JLabel("");
					playerTab.setHorizontalAlignment(SwingConstants.RIGHT);
				}
				else
				{
					playerTab = new JLabel(index + ".  " + playerName + ":");
					playerTab.setHorizontalAlignment(SwingConstants.RIGHT);
				}
				
				playerTab.setFont(new Font("Sans-serif", Font.PLAIN, 14));	
				players.add(playerTab);
				
				index++;
			}
			
			array = (JSONArray) jsonObject.get("scores");
			iterator = array.iterator();	
			
			while(iterator.hasNext())
			{
				scoreTab = new JLabel(iterator.next());
				scoreTab.setHorizontalAlignment(SwingConstants.LEFT);
				
				if(Integer.parseInt(scoreTab.getText()) < 0)
				{
					scoreTab.setVisible(false);
				}
				
				scores.add(scoreTab);
			}
		}
		catch(Exception e)
		{
			createEmptyScoreList(scoreMenu);
		}
		
		for(int i = 0; i < 10; i++)
		{
			scoreMenu.add(players.get(i), "cell 0 "+ (i+1) +", gaptop 15, gapbottom 15");
			scoreMenu.add(scores.get(i), "cell 1 "+ (i+1) +", gaptop 15, gapbottom 15, gapleft 15");
		}
		
		return true;
	}
	
	/**
	 * Saves the score list to .json
	 */
	@SuppressWarnings("unchecked")
	private void saveScoreList()
	{
		JSONObject obj = new JSONObject();
		
		JSONArray scoreList = new JSONArray();
		JSONArray playerList = new JSONArray();
		
		for(int i = 0; i < scores.size(); i++)
		{
			scoreList.add(scores.get(i).getText());
		}
		
		for(int i = 0; i < playerNames.size(); i++)
		{
			playerList.add(playerNames.get(i));
		}
		
		obj.put("scores", scoreList);
		obj.put("players", playerList);
		
		try(FileWriter writer = new FileWriter("res\\scoreList.json"))
		{
			writer.write(obj.toString());
			writer.flush();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
