package utilities;

/**
 * Interface used to create method in methods. 
 * @author dave
 *
 * @param <A> return type
 * @param <B> method arguement
 */
public interface InnerMethod<A, B> {
	
	/**
	 * Inner method functionality goes here. 
	 * @param b
	 * @return
	 */
	public A apply(B b);


}
