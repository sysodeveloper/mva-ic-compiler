package IC.semanticChecks;

/**
 *  Semantic errors.
 */
public class SemanticError extends Exception {
	/**
	 * Create a new semantic error.
	 * @param msg - The message of the error.
	 * @param line - The line of the error.
	 */
	public SemanticError(String msg, int line) {
		super("semantic error at line " + line + ": " + msg);
	}

	/**
	 * Create a new semantic error.
	 * @param line - The line of the error.
	 */
	public SemanticError(int line) {
		this("", line);
	}
	
	public String toString(){
		return this.getMessage();
	}
}
