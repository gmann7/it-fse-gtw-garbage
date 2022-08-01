/**
 * 
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.exceptions;

/**
 * @author AndreaPerquoti
 * 
 * Generic business exception.
 *
 */
public class BusinessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2442931640461052346L;

	/**
	 * Message constructor.
	 * 
	 * @param msg Message to be shown.
	 */
	public BusinessException(final String msg) {
		super(msg);
	}
	
	/**
	 * Complete constructor.
	 * 
	 * @param msg	Message to be shown.
	 * @param e		Exception to be shown.
	 */
	public BusinessException(final String msg, final Exception e) {
		super(msg, e);
	}
	
	/**
	 * Exception constructor.
	 * 
	 * @param e	Exception to be shown.
	 */
	public BusinessException(final Exception e) {
		super(e);
	}
	
}
