package guiComponents;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import projectMain.Gui;

/**
 * A panel for representing components on our gui. 
 * This is a grid layout panel. 
 * @author dave
 *
 */
public class ComponentPanel extends JPanel {
	
	private Gui parentGui; 

	/**
	 * Create a component panel that will hold the given number of rows and columns of child components. 
	 * @param name - Name to be displayed in the title border. 
	 * @param rows
	 * @param columns
	 * @param parentGui
	 */
	public ComponentPanel(String name,int rows, int columns,  Gui parentGui)
	{
		this.parentGui = parentGui; 
		
		this.setLayout(new GridLayout(rows, columns, parentGui.GRIDLAYOUT_GAP, parentGui.GRIDLAYOUT_GAP)); 
		
		TitledBorder title;
		title = BorderFactory.createTitledBorder(name);
		this.setBorder(title);

	}
	
}
