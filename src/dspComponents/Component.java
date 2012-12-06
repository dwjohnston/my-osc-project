package dspComponents;

/**Abstract class to represent an DSP components. 
 * @author dave
 * 
 * 
 *
 */
public abstract class Component {
	
	/**
	 * The given name for our component. 
	 */
	public final String NAME; 
	
	/**
	 * 
	 * @param name
	 */
	public Component(String name)
	{
		this.NAME = name; 
	}
	

}
