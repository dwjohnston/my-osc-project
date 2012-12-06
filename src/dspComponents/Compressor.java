package dspComponents;


/**A compressor component. Values above the threshold are compressed by the given ratio. 
 * This is currently a zero-attack, zero-release compressor. 
 * TODO: add attack and release funtionality. 
 *  
 * @author dave
 *
 *
 */
public class Compressor extends Component {

	private Parameter<Double> threshold;
	private Parameter<Double> gain; 
	private Parameter<Double> ratio;
			
	public Parameter<Double> getThreshold()			{return this.threshold;}
	public Parameter<Double> getGain() 				{return this.gain; }
	public  Parameter<Double> getRatio() 				{return this.ratio; }
	
	public Compressor(String name, Parameter<Double> threshold, Parameter<Double> ratio, Parameter<Double> gain)
	{
		super(name); 
		this.threshold = threshold; 
		this.ratio = ratio; 
		this.gain = gain;
	}
	
	/**
	 * Takes an input value applies compression to it. Returns the compressed value. 
	 * @param v
	 * @return
	 */
	public double getValue(double v)
	{
		
		
		double rValue = v; 
		double exceedThreshold; 
		/* compression applied to postive values*/
		if (v >0)		
		{
			exceedThreshold = v - this.threshold.getValue(); 
			if (exceedThreshold > 0)
			{
				rValue = this.threshold.getValue() + exceedThreshold/this.ratio.getValue();
			}
		}

		/* compression applied to negative values*/ 
		else
		{
			exceedThreshold = v + this.threshold.getValue(); 
			if (exceedThreshold< 0)
			{
				rValue = this.threshold.getValue() *-1 + exceedThreshold/this.ratio.getValue(); 
			}
		}
		
		
		return rValue * this.gain.getValue(); 
	}
	
	
	

}
