package projectMain;

import guiComponents.ComponentPanel;
import guiComponents.Output;
import guiComponents.SliderPanel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import utilities.InnerMethod;


import dspComponents.Compressor;
import dspComponents.Osc;
import dspComponents.Redux;





/**
 * The main gui for our program. 
 * Layout etc is all defined here. 
 * @author dave
 *
 */
public class Gui extends JFrame implements ActionListener {

	public static final int OUTPUT_WIDTH = 1000;
	public static final int OUTPUT_HEIGHT = 256; 

	public static final int SLIDER_WIDTH = 10;
	public static final int SLIDER_HEIGHT = 100;


	public  final int DEFAULT_SLIDER_GRAINS = 200; 


	public  final int GRIDLAYOUT_GAP = 5; 

	private Output displayPanel; 

	public void refresh()
	{
		displayPanel.refresh(); 
	}

	public Gui (String title, Engine engine)
	{

		/* create the initial frame*/
		setTitle(title);
		setSize(300, 200);
		this.setLayout(new FlowLayout());

		JPanel outerPanel = new JPanel();
		outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS)); 

		/*panel for all the component controls*/
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS)); 
		outerPanel.add(controlPanel);

		/*panel for displaying the waveform*/
		displayPanel = new Output(engine);
		displayPanel.setPreferredSize(new Dimension(OUTPUT_WIDTH, OUTPUT_HEIGHT)); 
		displayPanel.setBorder(BorderFactory.createLineBorder(Color.black)); 
		outerPanel.add(displayPanel); 

		/*add component panels to control panel*/
		controlPanel.add(createOscPanel(engine.getOsc1())); 
		controlPanel.add(createCompressorPanel(engine.getCompressor1())); 
		controlPanel.add(createReduxPanel(engine.getRedux1())); 

		/*final stuff*/
		add(outerPanel); 
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.setVisible(true); 
		this.refresh(); 

	}

	private JPanel createCompressorPanel(final Compressor c)
	{
		JPanel panel = new ComponentPanel(c.NAME, 1, 3, this); 

		panel.add(new SliderPanel(c.getThreshold(), DEFAULT_SLIDER_GRAINS, this)); 
		panel.add(new SliderPanel(c.getRatio(), DEFAULT_SLIDER_GRAINS, this)); 
		panel.add(new SliderPanel(c.getGain(), DEFAULT_SLIDER_GRAINS, this)); 

		return panel; 
	}

	private JPanel createReduxPanel(final Redux r)
	{
		JPanel panel = new ComponentPanel(r.NAME, 1, 2, this); 
		panel.add(new SliderPanel(r.getSampleRate(),  DEFAULT_SLIDER_GRAINS, this))	;
		panel.add(new SliderPanel(r.getBitDepth(),  DEFAULT_SLIDER_GRAINS, this));

		return panel; 
	}

	private JPanel createOscPanel(final Osc o)
	{
		JPanel panel = new ComponentPanel(o.NAME, 2, 4, this); 


		/*set up combobox panel*/
		JPanel cbPanel = new JPanel(); 
		cbPanel.setLayout(new BoxLayout(cbPanel, BoxLayout.Y_AXIS));

		/*label*/
		JPanel lbPanel = new JPanel();
		lbPanel.setLayout(new BoxLayout(lbPanel, BoxLayout.X_AXIS)); 
		lbPanel.setAlignmentY(LEFT_ALIGNMENT); 
		JLabel cbLabel = new JLabel("Wave Type:");
		lbPanel.add(cbLabel);
		cbPanel.add(lbPanel);

		/*combobox*/
		final String[] comboItems = {"Sine", "Custom", "FSaw", "FSquare"};
		JComboBox cb = new JComboBox(comboItems);
		cb.setMaximumSize(new Dimension (100, 20)); 
	
		cbPanel.add(cb); 

		/*add sliders*/
		final SliderPanel widthPanel = new SliderPanel(o.getWidth(), DEFAULT_SLIDER_GRAINS, this);
		final SliderPanel curvePanel = new SliderPanel(o.getCurve(), DEFAULT_SLIDER_GRAINS, this);
		final SliderPanel slopePanel = (new SliderPanel(o.getSlope(), DEFAULT_SLIDER_GRAINS, this));
		final SliderPanel fourierPanel = (new SliderPanel(o.getFourier(), (o.getFourier().MAX - o.getFourier().MIN), this)); 



		panel.add(new SliderPanel(o.getAmp(), DEFAULT_SLIDER_GRAINS, this)); 
		panel.add(new SliderPanel(o.getFreq(), DEFAULT_SLIDER_GRAINS, this)); 
		panel.add(new SliderPanel(o.getPhase(), DEFAULT_SLIDER_GRAINS, this));
		panel.add(cbPanel);
		panel.add(widthPanel);
		panel.add(slopePanel); 
		//panel.add(curvePanel); //removed as not yet functional 
		panel.add(new JPanel()); 
		panel.add(fourierPanel);  
		
		/*enable and disable various sliders when needed/not needed*/ 
		final InnerMethod<Void, Void> disableFourierSlider = new InnerMethod<Void, Void> ()
				{
			public Void apply (Void v)
			{
				fourierPanel.getSlider().setEnabled(false); 
				return null; 
			}
				};
		
		final InnerMethod<Void, Void> enableFourierSlider = new InnerMethod<Void, Void> ()
				{
			public Void apply(Void v)
			{
				fourierPanel.getSlider().setEnabled(true); 
				return null; 
				
			}
				}; 

		final InnerMethod<Void, Void> disableCustomSliders = new InnerMethod<Void, Void>()
				{
			public Void apply(Void v)
			{
				widthPanel.getSlider().setEnabled(false); 
				slopePanel.getSlider().setEnabled(false); 
				curvePanel.getSlider().setEnabled(false); 
				
				return null; 
			}
				}; 

		final InnerMethod<Void, Void> enableCustomSliders = new InnerMethod<Void, Void>()
				{
			public Void apply(Void v)
			{
				widthPanel.getSlider().setEnabled(true); 
				slopePanel.getSlider().setEnabled(true); 
				fourierPanel.getSlider().setEnabled(true); 
				
				return null; 
			}
				}; 		


		/*combo box action event*/
		cb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
				int value = cb.getSelectedIndex();

				switch(value)
				{
				case 0: {
					o.setWaveShape(Osc.WaveShape.SINE);
					disableCustomSliders.apply(null);
					disableFourierSlider.apply(null);}
				break;
				case 1: {
					o.setWaveShape(Osc.WaveShape.CUSTOM);
					enableCustomSliders.apply(null);
					disableFourierSlider.apply(null); }
				break;
				case 2: {
					o.setWaveShape(Osc.WaveShape.FSAW); 
					disableCustomSliders.apply(null);
					enableFourierSlider.apply(null);} 
				break;
				case 3: {
					o.setWaveShape(Osc.WaveShape.FSQUARE);
					disableCustomSliders.apply(null);
					enableFourierSlider.apply(null); 
					} break;

				}

				refresh();
			}
		}); 

		cb.setSelectedIndex(0); //trigger combobox select event, to disable sliders. 
		return panel; 

	}

	public void actionPerformed(ActionEvent e) {
		JComboBox cb = (JComboBox)e.getSource();
		int value = cb.getSelectedIndex();


	}

}
