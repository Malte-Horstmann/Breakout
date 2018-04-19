package break_out.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import break_out.Constants;
import net.miginfocom.swing.MigLayout;

/**
 * @author Malte Horstmann
 * 
 * Abgabegruppe 102
 */
public class LevelEditor extends JPanel
{

	/**
	 * Automatic generated serial version UID
	 */
	private static final long serialVersionUID = 1299533319355135682L;
    
	/**
	 * The currently selected stone type
	 */
	int stoneType;
	
	/**
	 * The editable field
	 */
	private SectionPanel field;
	
	/**
	 * The selection section
	 */
	private SectionPanel selection;
	
	/**
	 * The save button
	 */
	private JButton save;
	
	/**
	 * The cancel button
	 */
	private JButton cancel;
	
	/**
	 * The button from which the player selects the stone color
	 */
	private JRadioButton[] selections = {new JRadioButton("", true), new JRadioButton("", true), new JRadioButton("", true), new JRadioButton("", true), new JRadioButton("", true)};
	
	/**
	 * The button group for the buttons mentioned above
	 */
	private ButtonGroup stoneSelection;
	
	/**
	 * The colors of the stones
	 */
	private Color[] stoneColors = {Color.YELLOW, Color.BLUE, Color.GREEN, Color.RED, Color.BLACK};
	
	/**
	 * Empty JLabels for the purpose of easily aligning the constant stones below the buttons
	 */
	private JLabel[] stonePositions = {new JLabel(""), new JLabel(""), new JLabel(""), new JLabel(""), new JLabel("")};
	
	/**
	 * The stone matrix, also holds the stone types
	 */
	private int[][] stones;
	
	/**
	 * The label that tells the player to select a level number
	 */
	private JLabel levelText;
	
	/**
	 * The list from which the user selects the number of his level
	 */
	private JScrollPane levelSelection;
	
	/**
	 * The options for the level selection
	 */
	JList<String> optionList;
	
	/**
	 * The list model for the option list
	 */
	DefaultListModel<String> listModel;
	
    /**
     * The last level number
     */
    private int maxLevel;
    
    /**
     * The currently selected level number
     */
    private int selectedLevel;
	
	
	/**
	 * Constructor
	 */
	public LevelEditor()
	{
		super();
		
		initialize();
		initializeField();
		initializeSelection();
	}
	
	/**
	 * Getter for the radio buttons
	 * @return selections The radio buttons from the selection
	 */
	public JRadioButton[] getSelections()
	{
		return selections;
	}
	
	/**
	 * Getter for the cancel button
	 * @return cancel The button that cancels the level edition
	 */
	public JButton getCancelButton()
	{
		return cancel;
	}
	
	/**
	 * Getter for the save button 
	 * @return save The button that saves the created level
	 */
	public JButton getSaveButton()
	{
		return save;
	}
	
	/**
	 * Getter for the list that holds the options for the level selection 
	 * @return optionList
	 */
	public JList<String> getlevelOptions()
	{
		return optionList;
	}
	
    /**
     * Getter for the maximum amount of levels
     * @return maxLevel The number of the final level
     */
    public int getMaxLevel()
    {
        return maxLevel;
    }
	
	/**
	 * Changes the selected stone type
	 * @param type The new stone type
	 */
	public void setStoneType(int type)
	{
		stoneType = type;
	}
	
	/**
	 * Changes the selected Level number
	 * @param selectedLevel The currently selected level number
	 */
	public void setSelectedLevel(int selectedLevel)
	{
		this.selectedLevel = selectedLevel;
	}
	
	/**
	 * Initializes the general layout
	 */
	private void initialize()
	{
		double w = Constants.SCREEN_WIDTH;
		double h = Constants.SCREEN_HEIGHT;
		
		setPreferredSize(new Dimension((int) w, (int) h));
		setMaximumSize(new Dimension((int) w, (int) h));
		setMinimumSize(new Dimension((int) w, (int) h));
		setFocusable(true);
		
		setLayout(new MigLayout("","10[center, grow, fill]10","10[85%, center, grow, fill]10[15%, center, grow, fill]10"));
		setBackground(Constants.BACKGROUND);
		
		stones = new int[Constants.SQUARES_Y][Constants.SQUARES_X];
		selectedLevel = 1;
		
		resetEditor();
	}
	
	/**
	 * Initializes the field
	 */
	private void initializeField()
	{
		field = new SectionPanel();
		field.shady = false;
		
		add(field, "cell 0 0");
	}
	
	/**
	 * Initializes the selection
	 */
	private void initializeSelection()
	{
		loadMaxLevel();
		
		selection = new SectionPanel();
		selection.shady = false;	
		selection.setLayout(new MigLayout("fill","[][][][][][][]", "[center,grow,fill]1[center,grow,fill]"));
		
		listModel = new DefaultListModel<String>();
		optionList = new JList<String>(listModel);
		optionList.setVisibleRowCount(1);
		optionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		stoneSelection = new ButtonGroup();
		
		save = new JButton("save");
		cancel = new JButton("cancel");
		
		levelText = new JLabel("Level number:");
		levelSelection = new JScrollPane();
			
		updateLevelSelection();
		
		selection.add(save, "cell 6 1, sg 1, center,grow");
		selection.add(cancel, "cell 5 1, sg 1, center,grow");
		selection.add(levelText, "cell 5 0, center");
		selection.add(levelSelection, "cell 6 0, center");
		
		for(int i = 0; i < selections.length; i++)
		{
			selections[i].setOpaque(false);
			selection.add(selections[i], "cell "+ i +" 0, center");
			stoneSelection.add(selections[i]);
		}
		
		for(int i = 0; i < stonePositions.length; i++)
		{
			selection.add(stonePositions[i], "cell "+ i +" 1, center, grow, gapleft 10, gapright 10");
		}
		
		add(selection, "cell 0 1");
	}
	
	/**
	 * Adds the given actionListener to the save and cancel button
	 * @param l The actionListener
	 */
	public void addActionListenerToSelectioButtons(ActionListener l)
	{
		save.addActionListener(l);
		cancel.addActionListener(l);
	}
	
	/**
	 * Adds the given itemListener to the radio buttons
	 * @param l The itemListener
	 */
	public void addItemListenerToSelectios(ItemListener l)
	{
		for(int i = 0; i < selections.length; i++)
		{
			selections[i].addItemListener(l);		
		}
	}
	
	/**
	 * Adds the given ListSelectionListener to the optionList
	 * @param l The ListSelectionListener
	 */
	public void addListSelectionListenerToSelection(ListSelectionListener l)
	{
		optionList.addListSelectionListener(l);
	}
	
	/**
	 * Adds the given mouseListener to the field 
	 * @param l The mouseListener
	 */
	public void addMouseListenerToField(MouseListener l)
	{
		field.addMouseListener(l);
	}
	
	@Override
	protected void paintChildren(Graphics g)
	{
		super.paintChildren(g);

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
		
		// Draws the background
		g2.setColor(Color.WHITE);
		g2.fillRect(field.getX(), field.getY(), field.getWidth(), field.getHeight());

		drawGrid(g2);
		
		drawSelectionStones(g2);
		
		drawStones(g2);
	}
	
	/**
	 * Draws the Grid
	 * @param g2 The Graphics2D object that does the drawing
	 */
	private void drawGrid(Graphics2D g2)
	{
		int width = field.getWidth();
		int height = field.getHeight();
		int x = field.getX();
		int y = field.getY();
		
		g2.setColor(new Color(200, 200, 200));
		
		// Draws the horizontal lines of the grid
		for (int i = x; i <= width + x; i += width / Constants.SQUARES_X)
		{
			g2.drawLine(i, y, i, height + y);
		}
		
		// Draws the Vertical lines of the grid
		for (double j = y; j <= height + y; j += (double)height / Constants.SQUARES_Y)
		{
			Shape l = new Line2D.Double(x, j, width + x, j);
			g2.draw(l);
		}
	}
		
	/**
	 * Draws the stones on the field
	 * @param g2 The Graphics2D object that does the drawing
	 */
	private void drawStones(Graphics2D g2)
	{
		// The width and height of a stone
		double width = (double)field.getWidth() / Constants.SQUARES_X;
		double height = (double)field.getHeight() / Constants.SQUARES_Y;

		for(int y = 0; y < stones.length; y++)
		{
			for(int x = 0; x < stones[y].length; x++)
			{
				// -1 means there is no stone at this point
				if(stones[y][x] != -1)
				{
					Shape stone = new Rectangle2D.Double(x * width + 12, y * height + 12, width-3.5, height-3.5);
					g2.setColor(stoneColors[stones[y][x]]);
					g2.fill(stone);
				}				
			}		
		}
	}
	
	
	/**
	 * Draws the constant stones on the selection
	 * @param g2 The Graphics2D object that does the drawing
	 */
	private void drawSelectionStones(Graphics2D g2)
	{
		int x, y, width, height;
		for(int i = 0; i < stonePositions.length; i++)
		{
			JLabel position = stonePositions[i];
			
			x = position.getX() + 10;
			y = field.getHeight() + position.getY() + 20;	
			width = position.getWidth();
			height = position.getHeight();
			
			g2.setColor(stoneColors[i]);
			g2.fillRect(x, y, width, height);
			
			g2.setColor(Color.GRAY);
			g2.drawRect(x, y, width, height);
		}	
	}
	
	/**
	 * Creates or deletes a stone at the position of the coordinates
	 * 
	 * @param x the x value of the position
	 * @param y the y value of the position
	 */
	public void editField(int x, int y)
	{
		
		double stoneWidth = (double)field.getWidth() / Constants.SQUARES_X;
		double stoneHeight = (double)field.getHeight() / Constants.SQUARES_Y;
		
		//Calculating matrix position
		int mX = (int)(x / stoneWidth);
		int mY = (int)(y / stoneHeight);
		
		if(stones[mY][mX] != stoneType)
		{
			// create/change a stone at this point
			stones[mY][mX] = stoneType;
		}
		else
		{
			// delete the stone at that point
			stones[mY][mX] = -1;
		}
		
		repaint();
	}
	
	/**
	 * Saves the level to .json and the maxLevel to .txt
	 */
	@SuppressWarnings("unchecked")
	public void save()
	{
		int maxDrops = 3;
		
		JSONObject obj = new JSONObject();
		
		JSONArray stoneList = new JSONArray();
		JSONArray stoneListRow;
		JSONArray stoneTypeList = new JSONArray();
		JSONArray stoneTypeListRow;
		
		for(int y = 0; y < Constants.SQUARES_Y; y++)
		{
			stoneListRow = new JSONArray();
			stoneTypeListRow = new JSONArray();
			
			for( int x = 0; x < Constants.SQUARES_X; x++)
			{		
				if(stones[y][x] != -1)
				{
					stoneListRow.add(1);
				}
				else
				{
					stoneListRow.add(0);
				}
				
				stoneTypeListRow.add(stones[y][x]);
			}
			
			stoneList.add(stoneListRow);
			stoneTypeList.add(stoneTypeListRow);
		}
		
		obj.put("fields", stoneList);
		obj.put("types", stoneTypeList);
		obj.put("maxDrops", maxDrops);
		
		try(FileWriter writer = new FileWriter("res\\level" + selectedLevel + ".json"))
		{
			writer.write(obj.toString());
			writer.flush();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		saveMaxLevel();
		updateLevelSelection();
	}
	
	/**
	 * Resets the editor
	 */
	public void resetEditor()
	{
		// Delete all stones
		for(int y = 0; y < stones.length; y++)
		{
			for(int x = 0; x < stones[0].length; x++)
			{
				stones[y][x] = -1;
			}
		}
		
		// Select the default type
		selections[0].setSelected(true);
		for(int i = 1; i < selections.length; i++)
		{
			selections[i].setSelected(false);
		}
	}
	
	/**
	 * Updates the level selection 
	 */
	private void updateLevelSelection()
	{	
		listModel.clear();
		
		for(int i = 0; i <= maxLevel; i++)
		{
			listModel.addElement( "Level " + (i+1));
		}
		
		levelSelection.setViewportView(optionList);
	}
	
	/**
	 * Loads the number of the highest level and saves it in maxLevel
	 */
	private void loadMaxLevel()
    {
    	// The name of the file to open.
        String fileName = "res\\maxLevel.txt";

        try
        {
            FileInputStream inputStream = new FileInputStream(fileName);

            maxLevel = inputStream.read(); 
            inputStream.close();        
        }
        catch(FileNotFoundException ex)
        {
        	maxLevel = 0;
        }
        catch(IOException ex)
        {
        	maxLevel = 0;
        }
    }
    
	/**
	 * Saves the number of the highest level to .txt
	 */
    private void saveMaxLevel()
    {
		if(selectedLevel > maxLevel)
		{
			maxLevel = selectedLevel;
		}
		
    	String fileName = "res\\maxLevel.txt";

        try
        {
            FileWriter fileWriter = new FileWriter(fileName);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(maxLevel);
            bufferedWriter.close();
        }
        catch(IOException ex)
        {
            System.out.println("Error writing to file '" + fileName + "'");
        }
    }
}
