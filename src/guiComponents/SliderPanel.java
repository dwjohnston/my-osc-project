package guiComponents;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import projectMain.Gui;

import dspComponents.Parameter;


/**
 * A slider panel containing a slider laber, a slider, and a value label. 
 * Slider panel allows for exponentially scaled sliders. 
 * @author dave
 *
 */
public class SliderPanel extends JPanel {
	
	
		private Gui parentGui; 
		private JSlider slider; 
		private JLabel label; 
		private JLabel valueLabel;
		private Parameter parameter; 
		private int nGrains;
	
	/**
	 * Converts a given parameter value, into it's corresponding slider value. 
	 * @param min
	 * @param max
	 * @param v
	 * @param scale
	 * @param q
	 * @param grains
	 * @return
	 */
	private int valueToSlider(double min, double max, double v, Parameter.ScaleType scale, double q, int grains)
	{
		int value = 0; 
		
		switch (scale)
		{
		case LINEAR: value = (int)((v-min)*grains*(1/(max-min)));  break;
		case EXPONENTIAL:value = (int)(Math.pow(	((v-min)*Math.pow(grains, q)/(max-min))	, (1/q)));   break;
		
		
		}
	
		return value; 
	}
	
	/**
	 * Converts a given slider value, into it's corresponding parameter value. 
	 * @param min
	 * @param max
	 * @param sliderValue
	 * @param scale
	 * @param q
	 * @param grains
	 * @return
	 */
	private double sliderToValue(double min, double max, int sliderValue, Parameter.ScaleType scale, double q, int grains)
	{
		
		double value =0; 
		switch (scale)
		{
		case LINEAR: value = sliderValue * ((max-min)/grains)+ min;   break;
		case EXPONENTIAL: value = (Math.pow(sliderValue,  q)/Math.pow(grains, q))*(max-min)+min; 
		
		}
		
		return value; 
	}
	
	/**
	 * Pointer to the Slider Panel slider. 
	 * @return
	 */
	public JSlider getSlider()
	{
		return this.slider; 
	}
	

	private void updateSliderValueLabel()
	{
		String str = null; 
		if (parameter.CLASSTYPE.equals(Double.class) ) str = String.format("%.2f", parameter.getValue());
    	if (parameter.CLASSTYPE.equals(Integer.class)){ str = parameter.getValue().toString(); }
    	valueLabel.setText(str);
    	
	}
	
	/**
	 * Requires a parameter to represent, number of grains required, and needs to know it's parent gui so it can call refresh()
	 * @param parameter
	 * @param grains
	 * @param parentGui
	 */
	public SliderPanel(@SuppressWarnings("rawtypes") final Parameter parameter, final int grains, final Gui parentGui)
	{
		this.parameter = parameter;
		this.nGrains = grains; 
		this.parentGui = parentGui; 
	
	
		/*create and assign components*/ 
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); 
		this.setBorder(BorderFactory.createRaisedBevelBorder()); 
		
		label = new JLabel(parameter.NAME); 
		valueLabel = new JLabel();

	    /*set up slider*/
		int curr = valueToSlider(parameter.MIN.doubleValue(), parameter.MAX.doubleValue(),
				parameter.getValue().doubleValue(), parameter.SCALETYPE, parameter.Q, grains);
		slider = new JSlider(JSlider.VERTICAL, 0, grains, curr);
		
		slider.setPreferredSize(new Dimension(Gui.SLIDER_WIDTH, Gui.SLIDER_HEIGHT));
		slider.setAlignmentX(Component.LEFT_ALIGNMENT); /*this here will stop it moving around*/
		
		/*add components to panel*/
		this.add(valueLabel);		
		this.add(slider);
		this.add(label);
		
		
		/*slider move event*/
		slider.addChangeListener(new ChangeListener() {
            /* (non-Javadoc)
             * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
             */
            public void stateChanged(ChangeEvent event) {
            	
            	double value = sliderToValue(parameter.MIN.doubleValue(), parameter.MAX.doubleValue(),slider.getValue(), parameter.SCALETYPE, parameter.Q, grains); 
            	
            	

            	/*set the parameter value*/
            	if (parameter.CLASSTYPE.equals(Double.class))
            		     parameter.setValue(value);
            	else if (parameter.CLASSTYPE.equals((Integer.class)))
            			parameter.setValue((int)value); 
            	
            	updateSliderValueLabel();
            	
            	parentGui.refresh(); 
            }		
		
		});
		
		/*need to reset the valueLabel to it's correct format*/
	    updateSliderValueLabel();
				
	}

}
