package IC.Parser;

public class SyntaxError extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * The line number in the origin text..
	 */
	private int m_lineNumber;
	
	/**
	 * The last token scanned before the syntax error
	 */
	private Token tok;
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
		return "Line " + this.getLineNumber() + ": Syntax Error; unexpected " + this.getToken().toString(); 
	}

	/**
	 * Create new syntax error.
	 * @param message The error message.
	 */
    public SyntaxError(Token tok,int lineNumber) {
    	this.tok = tok;
    	m_lineNumber = lineNumber;
    }
    
	public Token getToken() {
		return tok;
	}

}
