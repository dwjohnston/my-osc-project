package dspComponents;
import java.lang.reflect.ParameterizedType;

/**
 * One dimensional parameter, with defined bounds and scale type. 
 * @author dave
 *
 * @param <T> The parameter type, Integer or Double. 
 */
public class Parameter<T extends Number & Comparable<T>>{
	public final T MIN;
	public final T MAX;
	public final ScaleType SCALETYPE; 
	public final double Q;
	public final String NAME; 

	public final Class<T> CLASSTYPE;
	private T value;
	
	
	public enum ScaleType {LINEAR, EXPONENTIAL}
	
	/**
	 * Constructor for LINEAR parameters.
	 * @param min
	 * @param max
	 * @param current
	 * @param name
	 */
	public Parameter(Class<T> clazz, T min, T max, T current, String name)
	{
		this(clazz, min, max, current, name, 0, ScaleType.LINEAR); 
	}
	
	/**
	 * Constructor for EXPONENTIAL parameters. Q value required. 
	 * @param min
	 * @param max
	 * @param current
	 * @param name
	 * @param q Scaling value for exponential parameters. The higher this value, the greater the difference
	 * in scale between the upper and lower ends of the parameter. 
	 */
	public Parameter(Class<T> clazz, T min, T max,T current, String name, double q)
	{

		
		this(clazz, min, max, current, name, q, ScaleType.EXPONENTIAL); 
	}
	
	/**
	 * Constructor that does the actual setting. 
	 * @param min
	 * @param max
	 * @param current
	 * @param name
	 * @param q
	 * @param scaleType
	 */
	protected Parameter(Class<T> clazz, T min, T max, T current, String name, double q, ScaleType scaleType)
	{
		this.MIN = min;
		this.MAX = max;
		this.value = current;
		this.NAME = name;
		
		System.out.println(">>- "+ this.NAME);
		
		this.Q = q; 
		this.SCALETYPE = scaleType;
		this.CLASSTYPE = clazz; 


	}
	
	
	public void setValue(T v)
	{
		this.value = v; 
	}
	
	public T getValue()
	{
		return this.value;
	}
	
}
