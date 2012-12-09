package IC.Parser;

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
	 * The token that caused the lexical error.
	 */
	private String m_tok;

	/**
	 * The token that caused the lexical error.
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
		return "Line " + this.getLineNumber() + ": Lexical error; unresolved token " + m_tok + " " + m_msg;
	}
	
	/**
	 * Create new lexical error.
	 * @param message The error message.
	 */
    public LexicalError(String tok, int lineNumber) {
    	m_tok = tok;
    	m_lineNumber = lineNumber;
    	m_msg = "";
    }
    
    public LexicalError(String tok, int lineNumber, String msg) {
    	m_tok = tok;
    	m_lineNumber = lineNumber;
    	m_msg = msg;
    }
        
    /**
	 * Create new lexical error.
	 * @param message The error message.
	 */
    public LexicalError(String message) {
    	this(message, -1);
    }
}

