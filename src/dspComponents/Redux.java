package dspComponents;

/**
 * Redux component. Reduces the horizontal (sampling rate) and vertical (bit depth) granularity of the incoming signal
 * @author dave
 *
 */
public class Redux extends Component {
	
	private Parameter<Double> sampleRate;
	private Parameter<Integer> bitDepth; 
	
	public Parameter<Double> getSampleRate() {return this.sampleRate; }
	public Parameter<Integer> getBitDepth() { return this.bitDepth; }
		
	public Redux(String name, Parameter<Double> sampleRate, Parameter<Integer> bitDepth)
	{
		super(name); 
		this.sampleRate = sampleRate; 
		this.bitDepth = bitDepth; 
	}
	
	
	
	
	private double last; 
	/**
	 * For a given time and amplitude value, the redux will apply the redux DSP according to it's set parameters. 
	 * @param t
	 * @param value
	 * @return
	 */
	public double getValue(int t, double value)
	{	
		/* sample rate reduction*/ 
		if (t%(int)(this.getSampleRate().MAX / this.getSampleRate().getValue()) == 0)
		{
			last = value;
		}
		
		/*bit depth reduction*/		
		int nGrains = (int)Math.pow(2, this.getBitDepth().getValue());
		int nGrainsMax = (int)Math.pow(2, this.getBitDepth().MAX);
		int grainSize = nGrainsMax/nGrains; 
		
		/*convert value to a large integer*/
		int fakeLast = (int)(last * nGrainsMax/2);
		
		/*round it down to the next lowest grain. */
		int adjust = (fakeLast%grainSize); 
		fakeLast = fakeLast -adjust; 
		
		/*convert back to small double*/ 
		last = (double)fakeLast/(nGrainsMax/2);

		return last; 
	}
	
	
	
	
	

}
