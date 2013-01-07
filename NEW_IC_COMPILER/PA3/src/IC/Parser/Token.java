package IC.Parser;



/**
 * Represent a token parsed by the lexer.
 *
 */
public class Token extends java_cup.runtime.Symbol {
	/**
	 * The original text value of the token.
	 */
	private String m_value;
	
	/**
	 * The line number in the origin text..
	 */
	private int m_lineNumber;
	
	/**
	 * Get the value of the token.
	 * @return The string value.
	 */
	public String getValue() {
		return m_value;
	}
	
	/**
	 * Get the line number.
	 * @return The line number.
	 */
	public int getLineNumber() {
		return m_lineNumber;
	}
	
	/**
	 * Create a new token.
	 * @param id   The id of the token.
	 * @param line  The line of the token.
	 * @param value 
	 */
    public Token(int id, int line,String value) {
    	super(id,line,0,value);
        this.m_value = value;
        this.m_lineNumber=line+1;	//flex starts at 0 line
        //this.value = this.toString();
        this.value = value;
    }
    
    /**
     * Convert the token to string.
     */
    public String toString(){
    	String buffer = null;
    	switch(this.sym){
	    	case IC.Parser.sym.LP: buffer = "LP";break;
	    	case IC.Parser.sym.RP: buffer = "RP";break;
	    	case IC.Parser.sym.LB: buffer = "LB";break;
	    	case IC.Parser.sym.RB: buffer = "RB";break;
	    	case IC.Parser.sym.LCBR: buffer = "LCBR";break;
	    	case IC.Parser.sym.RCBR: buffer = "RCBR";break;
	    	case IC.Parser.sym.EOF: buffer = "EOF";break;
	    	case IC.Parser.sym.ASSIGN: buffer = "ASSIGN";break;
	    	case IC.Parser.sym.BOOLEAN: buffer = "BOOLEAN";break;
	    	case IC.Parser.sym.BREAK: buffer = "BREAK";break;
	    	case IC.Parser.sym.COMMA: buffer = "COMMA";break;
	    	case IC.Parser.sym.DOT: buffer = "DOT";break;
	    	case IC.Parser.sym.EQUAL: buffer = "EQUAL";break;
	    	case IC.Parser.sym.EXTENDS: buffer = "EXTENDS";break;
	    	case IC.Parser.sym.ELSE: buffer = "ELSE";break;
	    	case IC.Parser.sym.FALSE: buffer = "FALSE";break;
	    	case IC.Parser.sym.GT: buffer = "GT";break;
	    	case IC.Parser.sym.GTE: buffer = "GTE";break;
	    	case IC.Parser.sym.ID: buffer = "ID("+this.m_value+")";break;
	    	case IC.Parser.sym.IF: buffer = "IF";break;
	    	case IC.Parser.sym.INT: buffer = "INT";break;
	    	case IC.Parser.sym.INTEGER: buffer = "INTEGER("+this.m_value+")";break;
	    	case IC.Parser.sym.LAND: buffer = "LAND";break;
	    	case IC.Parser.sym.CLASS: buffer = "CLASS";break;
	    	case IC.Parser.sym.LENGTH: buffer = "LENGTH";break;
	    	case IC.Parser.sym.NEW: buffer = "NEW";break;
	    	case IC.Parser.sym.LNEG: buffer = "LNEG";break;
	    	case IC.Parser.sym.LOR: buffer = "LOR";break;
	    	case IC.Parser.sym.LT: buffer = "LT";break;
	    	case IC.Parser.sym.LTE: buffer = "LTE";break;
	    	case IC.Parser.sym.MINUS: buffer = "MINUS";break;
	    	case IC.Parser.sym.MOD: buffer = "MOD";break;
	    	case IC.Parser.sym.MULTIPLY: buffer = "MULTIPLY";break;
	    	case IC.Parser.sym.NEQUAL: buffer = "NEQUAL";break;
	    	case IC.Parser.sym.NULL: buffer = "NULL";break;
	    	case IC.Parser.sym.PLUS: buffer = "PLUS";break;
	    	case IC.Parser.sym.RETURN: buffer = "RETURN";break;
	    	case IC.Parser.sym.SEMI: buffer = "SEMI";break;
	    	case IC.Parser.sym.STATIC: buffer = "STATIC";break;
	    	case IC.Parser.sym.STRING: buffer = "STRING";break;
	    	case IC.Parser.sym.QUOTE: buffer = "QUOTE("+this.m_value+")";break;
	    	case IC.Parser.sym.THIS: buffer = "THIS";break;
	    	case IC.Parser.sym.TRUE: buffer = "TRUE";break;
	    	case IC.Parser.sym.VOID: buffer = "VOID";break;
	    	case IC.Parser.sym.WHILE: buffer = "WHILE";break;
	    	case IC.Parser.sym.CLASS_ID: buffer = "CLASS_ID("+this.m_value+")";break;
	    	case IC.Parser.sym.PLUSPLUS: buffer = "PLUSPLUS";break;
	    	case IC.Parser.sym.MINUSMINUS: buffer = "MINUSMINUS";break;
	    	case IC.Parser.sym.PLUSEQUAL: buffer = "PLUSEQUAL";break;
	    	case IC.Parser.sym.MINUSEQUAL: buffer = "MINUSEQUAL";break;
	    	case IC.Parser.sym.MULTIPLYEQUAL: buffer = "MULTIPLYEQUAL";break;
	    	case IC.Parser.sym.CONTINUE: buffer = "CONTINUE";break;
	    	case IC.Parser.sym.DIVIDE: buffer = "DIVIDE";break;
	    	case IC.Parser.sym.DIVIDEEQUAL: buffer = "DIVIDEEQUAL";break;
	    	default: buffer="UNKOWN";
    	}
    	return buffer;
    }
}
