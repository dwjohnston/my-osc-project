package projectMain;
import java.awt.BorderLayout;

import javax.swing.*;

/**
 * The program start. Simply initialises the {@link Engine Engine} and opens the {@link Gui Gui} 
 * @author dave
 *
 */
public class Main{
	
	
	private static final String APPLICATION_TITLE = "Visual Oscillator V1.0";
	


	    public static void main(String[] args) 
	    {
	        SwingUtilities.invokeLater(new Runnable() 
	        {
	            public void run() 
	            {
	            	Engine engine = new Engine(Gui.OUTPUT_WIDTH); 
	                Gui mainWindow = new Gui(APPLICATION_TITLE, engine); 
	                
	            }
	        });
	    }
}
