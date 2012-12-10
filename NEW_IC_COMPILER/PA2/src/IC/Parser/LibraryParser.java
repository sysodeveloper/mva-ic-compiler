
//----------------------------------------------------
// The following code was generated by CUP v0.11a beta 20060608
// Mon Dec 10 16:51:06 IST 2012
//----------------------------------------------------

package IC.Parser;

import IC.AST.*;
import IC.DataTypes;
import java_cup.runtime.*;
import java.util.*;

/** CUP v0.11a beta 20060608 generated parser.
  * @version Mon Dec 10 16:51:06 IST 2012
  */
public @SuppressWarnings(value={"all"}) class LibraryParser extends java_cup.runtime.lr_parser {

  /** Default constructor. */
  public LibraryParser() {super();}

  /** Constructor which sets the default scanner. */
  public LibraryParser(java_cup.runtime.Scanner s) {super(s);}

  /** Constructor which sets the default scanner. */
  public LibraryParser(java_cup.runtime.Scanner s, java_cup.runtime.SymbolFactory sf) {super(s,sf);}

  /** Production table. */
  protected static final short _production_table[][] = 
    unpackFromStrings(new String[] {
    "\000\016\000\002\002\007\000\002\002\004\000\002\003" +
    "\012\000\002\003\012\000\002\003\002\000\002\004\003" +
    "\000\002\004\003\000\002\004\003\000\002\004\003\000" +
    "\002\004\005\000\002\005\005\000\002\005\002\000\002" +
    "\006\006\000\002\006\002" });

  /** Access to production table. */
  public short[][] production_table() {return _production_table;}

  /** Parse-action table. */
  protected static final short[][] _action_table = 
    unpackFromStrings(new String[] {
    "\000\044\000\004\031\004\001\002\000\004\057\007\001" +
    "\002\000\004\002\006\001\002\000\004\002\000\001\002" +
    "\000\004\012\010\001\002\000\006\013\ufffd\050\011\001" +
    "\002\000\014\014\020\027\021\051\014\055\017\057\016" +
    "\001\002\000\004\013\013\001\002\000\004\002\001\001" +
    "\002\000\006\010\ufffa\025\ufffa\001\002\000\006\010\032" +
    "\025\041\001\002\000\006\010\ufff9\025\ufff9\001\002\000" +
    "\004\025\022\001\002\000\006\010\ufffb\025\ufffb\001\002" +
    "\000\006\010\ufffc\025\ufffc\001\002\000\004\006\023\001" +
    "\002\000\014\007\ufff6\014\020\027\021\051\014\057\016" +
    "\001\002\000\006\010\032\025\031\001\002\000\004\007" +
    "\026\001\002\000\004\047\027\001\002\000\006\013\ufffd" +
    "\050\011\001\002\000\004\013\ufffe\001\002\000\006\007" +
    "\ufff4\016\034\001\002\000\004\011\033\001\002\000\006" +
    "\010\ufff8\025\ufff8\001\002\000\012\014\020\027\021\051" +
    "\014\057\016\001\002\000\004\007\ufff7\001\002\000\006" +
    "\010\032\025\037\001\002\000\006\007\ufff4\016\034\001" +
    "\002\000\004\007\ufff5\001\002\000\004\006\042\001\002" +
    "\000\014\007\ufff6\014\020\027\021\051\014\057\016\001" +
    "\002\000\004\007\044\001\002\000\004\047\045\001\002" +
    "\000\006\013\ufffd\050\011\001\002\000\004\013\uffff\001" +
    "\002" });

  /** Access to parse-action table. */
  public short[][] action_table() {return _action_table;}

  /** <code>reduce_goto</code> table. */
  protected static final short[][] _reduce_table = 
    unpackFromStrings(new String[] {
    "\000\044\000\004\002\004\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\004" +
    "\003\011\001\001\000\004\004\014\001\001\000\002\001" +
    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\006\004\023\005\024" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\004\003\027\001\001\000\002\001\001\000\004" +
    "\006\034\001\001\000\002\001\001\000\002\001\001\000" +
    "\004\004\035\001\001\000\002\001\001\000\002\001\001" +
    "\000\004\006\037\001\001\000\002\001\001\000\002\001" +
    "\001\000\006\004\023\005\042\001\001\000\002\001\001" +
    "\000\002\001\001\000\004\003\045\001\001\000\002\001" +
    "\001" });

  /** Access to <code>reduce_goto</code> table. */
  public short[][] reduce_table() {return _reduce_table;}

  /** Instance of action encapsulation class. */
  protected CUP$LibraryParser$actions action_obj;

  /** Action encapsulation object initializer. */
  protected void init_actions()
    {
      action_obj = new CUP$LibraryParser$actions(this);
    }

  /** Invoke a user supplied parse action. */
  public java_cup.runtime.Symbol do_action(
    int                        act_num,
    java_cup.runtime.lr_parser parser,
    java.util.Stack            stack,
    int                        top)
    throws java.lang.Exception
  {
    /* call code in generated class */
    return action_obj.CUP$LibraryParser$do_action(act_num, parser, stack, top);
  }

  /** Indicates start state. */
  public int start_state() {return 0;}
  /** Indicates start production. */
  public int start_production() {return 1;}

  /** <code>EOF</code> Symbol index. */
  public int EOF_sym() {return 0;}

  /** <code>error</code> Symbol index. */
  public int error_sym() {return 1;}


  /** Scan to get the next Symbol. */
  public java_cup.runtime.Symbol scan()
    throws java.lang.Exception
    {

	Token t = lexer.next_token();
	if (printTokens)
		System.out.println(t.getLineNumber() + ":" + t);
	return t; 

    }


	/** Causes the parser to print every token it reads.
	 * This is useful for debugging.
	 */
	public boolean printTokens;
	
	private Lexer lexer;

	public LibraryParser(Lexer lexer) {
		super(lexer);
		this.lexer = lexer;
	}
	
	public int getLine() {
		return lexer.getLineNumber();
	}
	
    public void syntax_error(Symbol s) {
    return;
        //Token tok = (Token) s;
        /*
        System.out.println("Line " + tok.getLineNumber()+": Syntax error; unexpected " + tok);
        */
    }
    
    public void unrecovered_syntax_error(Symbol s) throws SyntaxError{
    	Token tok = (Token) s;
    	throw new SyntaxError(tok,tok.getLineNumber());
    }

	
	

}

/** Cup generated class to encapsulate user supplied action code.*/
class CUP$LibraryParser$actions {
  private final LibraryParser parser;

  /** Constructor */
  CUP$LibraryParser$actions(LibraryParser parser) {
    this.parser = parser;
  }

  /** Method with the actual generated action code. */
  public final java_cup.runtime.Symbol CUP$LibraryParser$do_action(
    int                        CUP$LibraryParser$act_num,
    java_cup.runtime.lr_parser CUP$LibraryParser$parser,
    java.util.Stack            CUP$LibraryParser$stack,
    int                        CUP$LibraryParser$top)
    throws java.lang.Exception
    {
      /* Symbol object for return from actions */
      java_cup.runtime.Symbol CUP$LibraryParser$result;

      /* select the action based on the action number */
      switch (CUP$LibraryParser$act_num)
        {
          /*. . . . . . . . . . . . . . . . . . . .*/
          case 13: // typelist ::= 
            {
              Object RESULT =null;
		 RESULT = new ArrayList<Formal>(); 
              CUP$LibraryParser$result = parser.getSymbolFactory().newSymbol("typelist",4, ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), RESULT);
            }
          return CUP$LibraryParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 12: // typelist ::= COMMA type ID typelist 
            {
              Object RESULT =null;
		int tleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-2)).left;
		int tright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-2)).right;
		Object t = (Object)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-2)).value;
		int ileft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-1)).left;
		int iright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-1)).right;
		Object i = (Object)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-1)).value;
		int lstleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()).left;
		int lstright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()).right;
		Object lst = (Object)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.peek()).value;
		 	ArrayList<Formal> lstFormals = (ArrayList<Formal>)lst;
	lstFormals.add(new Formal((Type)t,i.toString()));
	RESULT = lstFormals; 
              CUP$LibraryParser$result = parser.getSymbolFactory().newSymbol("typelist",4, ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-3)), ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), RESULT);
            }
          return CUP$LibraryParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 11: // formals ::= 
            {
              Object RESULT =null;
		 RESULT = new ArrayList<Formal>(); 
              CUP$LibraryParser$result = parser.getSymbolFactory().newSymbol("formals",3, ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), RESULT);
            }
          return CUP$LibraryParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 10: // formals ::= type ID typelist 
            {
              Object RESULT =null;
		int tleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-2)).left;
		int tright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-2)).right;
		Object t = (Object)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-2)).value;
		int ileft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-1)).left;
		int iright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-1)).right;
		Object i = (Object)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-1)).value;
		int lstleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()).left;
		int lstright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()).right;
		Object lst = (Object)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.peek()).value;
			ArrayList<Formal> lstFormals = (ArrayList<Formal>)lst;
	lstFormals.add(new Formal((Type)t,i.toString()));
	RESULT = lstFormals;

              CUP$LibraryParser$result = parser.getSymbolFactory().newSymbol("formals",3, ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-2)), ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), RESULT);
            }
          return CUP$LibraryParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 9: // type ::= type LB RB 
            {
              Object RESULT =null;
		int tleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-2)).left;
		int tright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-2)).right;
		Object t = (Object)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-2)).value;
		((Type)t).incrementDimension(); RESULT = t; 
              CUP$LibraryParser$result = parser.getSymbolFactory().newSymbol("type",2, ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-2)), ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), RESULT);
            }
          return CUP$LibraryParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 8: // type ::= CLASS_ID 
            {
              Object RESULT =null;
		int cleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()).left;
		int cright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()).right;
		Object c = (Object)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.peek()).value;
		 RESULT = new UserType(parser.getLine(),c.toString()); 
              CUP$LibraryParser$result = parser.getSymbolFactory().newSymbol("type",2, ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), RESULT);
            }
          return CUP$LibraryParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 7: // type ::= STRING 
            {
              Object RESULT =null;
		 RESULT = new PrimitiveType(parser.getLine(), DataTypes.STRING); 
              CUP$LibraryParser$result = parser.getSymbolFactory().newSymbol("type",2, ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), RESULT);
            }
          return CUP$LibraryParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 6: // type ::= BOOLEAN 
            {
              Object RESULT =null;
		 RESULT = new PrimitiveType(parser.getLine(), DataTypes.BOOLEAN); 
              CUP$LibraryParser$result = parser.getSymbolFactory().newSymbol("type",2, ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), RESULT);
            }
          return CUP$LibraryParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 5: // type ::= INT 
            {
              Object RESULT =null;
		 RESULT = new PrimitiveType(parser.getLine(), DataTypes.INT); 
              CUP$LibraryParser$result = parser.getSymbolFactory().newSymbol("type",2, ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), RESULT);
            }
          return CUP$LibraryParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 4: // libmethod ::= 
            {
              Object RESULT =null;
		 RESULT = new ArrayList<LibraryMethod>(); 
              CUP$LibraryParser$result = parser.getSymbolFactory().newSymbol("libmethod",1, ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), RESULT);
            }
          return CUP$LibraryParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 3: // libmethod ::= STATIC VOID ID LP formals RP SEMI libmethod 
            {
              Object RESULT =null;
		int ileft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-5)).left;
		int iright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-5)).right;
		Object i = (Object)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-5)).value;
		int lstFormalsleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-3)).left;
		int lstFormalsright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-3)).right;
		Object lstFormals = (Object)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-3)).value;
		int lstMethodsleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()).left;
		int lstMethodsright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()).right;
		Object lstMethods = (Object)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.peek()).value;
		  ArrayList<LibraryMethod> methods = (ArrayList<LibraryMethod>)lstMethods;
	methods.add(new LibraryMethod(new PrimitiveType(parser.getLine(),DataTypes.VOID), i.toString(), (ArrayList<Formal>)lstFormals));
	RESULT = methods; 
              CUP$LibraryParser$result = parser.getSymbolFactory().newSymbol("libmethod",1, ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-7)), ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), RESULT);
            }
          return CUP$LibraryParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 2: // libmethod ::= STATIC type ID LP formals RP SEMI libmethod 
            {
              Object RESULT =null;
		int tleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-6)).left;
		int tright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-6)).right;
		Object t = (Object)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-6)).value;
		int ileft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-5)).left;
		int iright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-5)).right;
		Object i = (Object)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-5)).value;
		int lstFormalsleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-3)).left;
		int lstFormalsright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-3)).right;
		Object lstFormals = (Object)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-3)).value;
		int lstMethodsleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()).left;
		int lstMethodsright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()).right;
		Object lstMethods = (Object)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.peek()).value;
		  ArrayList<LibraryMethod> methods = (ArrayList<LibraryMethod>)lstMethods;
	LibraryMethod sm = new LibraryMethod((Type)t,i.toString(), (ArrayList<Formal>)lstFormals); 
	methods.add(sm);
	RESULT = methods; 
              CUP$LibraryParser$result = parser.getSymbolFactory().newSymbol("libmethod",1, ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-7)), ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), RESULT);
            }
          return CUP$LibraryParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 1: // $START ::= libic EOF 
            {
              Object RESULT =null;
		int start_valleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-1)).left;
		int start_valright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-1)).right;
		Object start_val = (Object)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-1)).value;
		RESULT = start_val;
              CUP$LibraryParser$result = parser.getSymbolFactory().newSymbol("$START",0, ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-1)), ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), RESULT);
            }
          /* ACCEPT */
          CUP$LibraryParser$parser.done_parsing();
          return CUP$LibraryParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 0: // libic ::= CLASS CLASS_ID LCBR libmethod RCBR 
            {
              Object RESULT =null;
		int idleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-3)).left;
		int idright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-3)).right;
		Object id = (Object)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-3)).value;
		int lstMethodsleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-1)).left;
		int lstMethodsright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-1)).right;
		Object lstMethods = (Object)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-1)).value;
		
	if(id.toString().compareTo("Library") != 0){ parser.syntax_error((Symbol) id); };	
	RESULT = new ICClass(parser.getLine(), id.toString(),(List<Field>)new ArrayList<Field>(),(List<Method>) lstMethods);

              CUP$LibraryParser$result = parser.getSymbolFactory().newSymbol("libic",0, ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-4)), ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), RESULT);
            }
          return CUP$LibraryParser$result;

          /* . . . . . .*/
          default:
            throw new Exception(
               "Invalid action number found in internal parse table");

        }
    }
}

