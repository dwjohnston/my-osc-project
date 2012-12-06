package dspComponents;


/**An oscillator component. Generates an amplitude value for a given time value, according to it's set parameters. 
 * @author dave
 *
 *
 */
public class Osc extends Component {
	
	public enum WaveShape
	{
		SINE, CUSTOM, FSAW, FSQUARE
	}

	private Parameter<Double> amp; 
	private Parameter<Double> freq; 
	private Parameter<Double> phase; 
	private Parameter<Double> curve; 
	private Parameter<Double> width; 
	private Parameter<Double> slope; 
	private Parameter<Integer> fourier;
	private WaveShape waveShape; 
	
	
	
	/* constructor sets osc to it's default values*/
	public Osc(String name, 
			Parameter<Double> amp, 
			Parameter<Double> freq,
			Parameter<Double> phase, 
			Parameter<Double> curve, 
			Parameter<Double> width, 
			Parameter<Double> slope,
			Parameter<Integer> fourier)
	{
		super(name); 
		
		this.amp = amp;
		this.freq = freq;  
		this.phase = phase; 
		this.curve = curve;  
		this.width = width;  
		this.slope = slope;  
		this.fourier =fourier;  
		
		this.waveShape = WaveShape.SINE; 
	}
	
	/*getter methods*/
	public Parameter<Double> getAmp() 				{ return this.amp;}
	public Parameter<Double> getFreq()				{ return this.freq;}
	public Parameter<Double> getPhase() 			{ return this.phase;}
	public Parameter<Double> getCurve() 			{ return this.curve;}
	public Parameter<Double> getWidth()				{ return this.width;}
	public Parameter<Double> getSlope() 			{ return this.slope;}
	public WaveShape getWaveShape()					{ return this.waveShape;}
	public Parameter<Integer> getFourier() 			{ return this.fourier;}

	/*waveShape setter*/
	public void setWaveShape(WaveShape w)	{ this.waveShape = w; }
	
	
	private double getToRad(double t)
	{
		double oneCycle = 1/this.getFreq().getValue(); 
		double toRad = (t/oneCycle)*(2*Math.PI);
		return toRad; 
	}
	
	/**
	 * A given time value is adjusted to account for the phase setting. 
	 * @param t
	 * @return
	 */
	private double phaseAdjust(double t)
	{
		double value =(this.getPhase().getValue()/360)*(1/this.getFreq().getValue()) + t;
		
		/*make it positive it's below zero*/
		if (value < 0)
		{
			value = value + 1/this.getFreq().getValue(); 
		}
		
		return value; 
	
	}
	
	/**
	 * Returns an amplitude value for the given time using the SINE calculation. 
	 * @param t
	 * @return
	 */
	private double getSine(double t)
	{ 					
		return (this.getAmp().getValue()*Math.sin(getToRad(phaseAdjust(t)))); 
	}
	
	
	/**
	 * Returns an amplitude value for the given time using the FOURIER SQUARE calculation. 
	 * @param t
	 * @return
	 */
	private double getFSquare(double t)
	{
		double value = 0; 
				
		for (int i = 1; i<=this.getFourier().getValue(); i++)
		{
			int j = (2*i) - 1; 
			value += (Math.sin(2*Math.PI*(j)*this.getFreq().getValue()*phaseAdjust(t)))
					/(j);
		}
		
		value = (4/Math.PI)* value; 
		return this.getAmp().getValue()*  value; 
	
	}
	
	/**
	 * Returns an amplitude value for the given time using the FOURIER SAW calculation. 
	 * @param t
	 * @return
	 */
	private double getFSaw(double t)
	{
		double value = 0; 	
		
		for (int i = 1; i<=this.getFourier().getValue(); i++)
		{
			value +=Math.pow(-1, i+1)*(Math.sin(2*Math.PI*i*this.getFreq().getValue()*phaseAdjust(t)))/i; 
		}
		
		value = (2 / Math.PI )* value; 
		
		return value * this.getAmp().getValue(); 
		
	}
	
	
	/**
	 * Returns an amplitude value for the given time using the CUSTOM calcuation. 
	 * @param t
	 * @return
	 */
	private double getCustom(double t)
	{
		
		/*length of a cycle in seconds*/
		double oneCycle = 1/this.getFreq().getValue(); 
		double currentPlace = phaseAdjust(t); 
		/*change the t value back to a single cycle value*/ 
		while (currentPlace- oneCycle >0) currentPlace = currentPlace - oneCycle; 
		
		/*define points*/
		double p2 = this.getWidth().getValue()* oneCycle; 
		double p1 = p2*this.getSlope().getValue(); 
		double p3 = p2 + (oneCycle - p2) * this.getSlope().getValue(); 
	
		/*work out what phase it's in*/
		int phase = 0; 
		if (currentPlace<p1) phase = 0; 
		if (currentPlace>= p1 && currentPlace < p2) phase = 1; 
		if (currentPlace>= p2 && currentPlace <p3) phase = 2; 
		if (currentPlace>= p3) phase = 3; 
		
		double value = 0; 
		
		/*different function for each phase*/
		switch (phase)
		{
		case 0: value = -1 + (2.0/p1)* currentPlace; break; 
		case 1: value = 1; break; 
		case 2: value = 1+ (-2.0/(p3-p2))*(currentPlace - p2); break; 
		case 3: value = -1; break; 
		}
		
		return value * this.getAmp().getValue(); 
	}
	
	/**
	 * Returns an amplitude value for the given time, using the oscillators parameter settings. 
	 * @param t
	 * @return
	 */
	public double getValue(double t)
	{
		double value = 0; 
		
		switch (this.waveShape)
		{
		case SINE: value = getSine(t); 
			break;
		case FSQUARE: value = getFSquare(t); 
			break;
		case FSAW: value = getFSaw(t); 
			break;
		case CUSTOM: value = getCustom (t); 
			break; 
		
		
		}
		
		return value; 
	}
	
	
	
	

}
