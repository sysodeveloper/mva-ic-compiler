package IC.Parser;

import com.sun.org.apache.bcel.internal.classfile.LineNumber;

/**
 * Lexical error class.
 *
 */
public class LexicalError extends Exception
{
	/**
	 * The class version.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The error message.
	 */
	private String m_msg;
	
	/**
	 * The line number in the origin text..
	 */
	private int m_lineNumber;
	
	/**
	 * Get the line number.
	 * @return The line number.
	 */
	public int getLineNumber() {
		return m_lineNumber;
	}
	
	/**
	 * get the error message.
	 */
	public String getMessage() {
		return m_msg;
	}
	
	/**
	 * Set the error message.
	 * @param msg The error message.
	 */
	public void setMessage(String msg) {
		m_msg = msg;
	}
	
	/**
	 * Create new lexical error.
	 * @param message The error message.
	 */
    public LexicalError(String message, int lineNumber) {
    	m_msg = message;
    	m_lineNumber = lineNumber;
    }
    
    /**
	 * Create new lexical error.
	 * @param message The error message.
	 */
    public LexicalError(String message) {
    	this(message, -1);
    }
}

