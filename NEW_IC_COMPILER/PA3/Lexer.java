/* The following code was generated by JFlex 1.4.3 on 12/10/12 8:37 PM */

package IC.Parser;
import java_cup.runtime.*;

/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.4.3
 * on 12/10/12 8:37 PM from the specification file
 * <tt>src/IC/Parser/IC.lex</tt>
 */
public @SuppressWarnings(value={"all"}) class Lexer implements java_cup.runtime.Scanner {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int ST_COMMENT = 2;
  public static final int YYINITIAL = 0;
  public static final int ST_STRING = 4;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1,  1,  2, 2
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\63\1\7\1\0\1\10\1\7\22\0\1\10\1\60\1\13"+
    "\2\0\1\33\1\61\1\0\1\20\1\21\1\12\1\57\1\26\1\56"+
    "\1\5\1\11\1\4\11\1\1\0\1\32\1\27\1\30\1\31\2\0"+
    "\10\3\1\53\21\3\1\22\1\14\1\23\1\0\1\6\1\0\1\35"+
    "\1\46\1\37\1\52\1\41\1\40\1\54\1\44\1\36\1\2\1\47"+
    "\1\42\1\2\1\16\1\50\2\2\1\17\1\34\1\15\1\43\1\51"+
    "\1\45\1\55\2\2\1\24\1\62\1\25\uff82\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\3\0\1\1\1\2\1\3\1\4\1\2\1\5\1\6"+
    "\1\7\1\10\1\11\3\3\1\12\1\13\1\14\1\15"+
    "\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25"+
    "\11\3\1\4\1\26\1\27\1\30\2\1\1\6\1\31"+
    "\1\32\1\31\1\33\1\0\1\34\5\3\1\35\1\36"+
    "\1\37\2\3\1\40\12\3\1\4\1\41\1\42\1\43"+
    "\1\44\1\45\2\31\2\3\1\46\4\3\1\47\12\3"+
    "\1\4\2\50\1\51\1\52\1\53\6\3\1\54\5\3"+
    "\1\55\1\4\3\3\1\56\1\3\1\57\2\3\1\60"+
    "\1\61\1\3\1\4\1\62\1\63\1\64\2\3\1\65"+
    "\1\3\1\4\1\3\1\66\1\67\1\2\1\70";

  private static int [] zzUnpackAction() {
    int [] result = new int[142];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\64\0\150\0\234\0\320\0\u0104\0\u0138\0\u016c"+
    "\0\234\0\234\0\u01a0\0\234\0\234\0\u01d4\0\u0208\0\u023c"+
    "\0\234\0\234\0\234\0\234\0\234\0\234\0\234\0\u0270"+
    "\0\u02a4\0\u02d8\0\234\0\234\0\u030c\0\u0340\0\u0374\0\u03a8"+
    "\0\u03dc\0\u0410\0\u0444\0\u0478\0\u04ac\0\u04e0\0\234\0\234"+
    "\0\u0514\0\u0548\0\u057c\0\u05b0\0\u05e4\0\234\0\u0618\0\u064c"+
    "\0\u0680\0\234\0\u06b4\0\u06e8\0\u071c\0\u0750\0\u0784\0\234"+
    "\0\234\0\234\0\u07b8\0\u07ec\0\u0104\0\u0820\0\u0854\0\u0888"+
    "\0\u08bc\0\u08f0\0\u0924\0\u0958\0\u098c\0\u09c0\0\u09f4\0\u0a28"+
    "\0\234\0\234\0\234\0\234\0\u0a5c\0\u0a90\0\u0ac4\0\u0af8"+
    "\0\u0b2c\0\u0104\0\u0b60\0\u0b94\0\u0bc8\0\u0bfc\0\u0104\0\u0c30"+
    "\0\u0c64\0\u0c98\0\u0ccc\0\u0d00\0\u0d34\0\u0d68\0\u0d9c\0\u0dd0"+
    "\0\u0e04\0\u0e38\0\u0e6c\0\234\0\u0104\0\u0104\0\u0104\0\u0ea0"+
    "\0\u0ed4\0\u0f08\0\u0f3c\0\u0f70\0\u0fa4\0\u0104\0\u0fd8\0\u100c"+
    "\0\u1040\0\u1074\0\u10a8\0\u0104\0\u10dc\0\u1110\0\u1144\0\u1178"+
    "\0\u0104\0\u11ac\0\u0104\0\u11e0\0\u1214\0\u0104\0\u0104\0\u1248"+
    "\0\u127c\0\u0104\0\u0104\0\u0104\0\u12b0\0\u12e4\0\u0104\0\u1318"+
    "\0\u134c\0\u1380\0\u0104\0\u0104\0\u0138\0\u0104";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[142];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\4\1\5\1\6\1\7\1\10\1\11\1\4\2\12"+
    "\1\13\1\14\1\15\1\4\1\16\1\17\1\20\1\21"+
    "\1\22\1\23\1\24\1\25\1\26\1\27\1\30\1\31"+
    "\1\32\1\33\1\34\1\35\1\6\1\36\1\37\1\40"+
    "\1\41\1\42\2\6\1\43\1\44\2\6\1\45\1\6"+
    "\1\46\2\6\1\47\1\50\1\51\1\52\1\53\13\12"+
    "\1\54\51\12\7\55\1\4\3\55\1\56\1\57\46\55"+
    "\1\4\65\0\1\5\2\0\1\5\60\0\4\6\1\0"+
    "\1\6\6\0\3\6\14\0\22\6\7\0\4\7\1\0"+
    "\1\7\6\0\3\7\14\0\22\7\7\0\3\60\1\10"+
    "\1\60\7\0\3\60\14\0\22\60\17\0\1\61\1\62"+
    "\52\0\4\6\1\0\1\6\6\0\2\6\1\63\14\0"+
    "\10\6\1\64\11\6\7\0\4\6\1\0\1\6\6\0"+
    "\3\6\14\0\5\6\1\65\1\6\1\66\12\6\7\0"+
    "\4\6\1\0\1\6\6\0\3\6\14\0\5\6\1\67"+
    "\14\6\36\0\1\70\63\0\1\71\63\0\1\72\34\0"+
    "\4\6\1\0\1\6\6\0\1\73\2\6\14\0\22\6"+
    "\7\0\4\6\1\0\1\6\6\0\1\6\1\74\1\6"+
    "\14\0\4\6\1\75\15\6\7\0\4\6\1\0\1\6"+
    "\6\0\3\6\14\0\6\6\1\76\5\6\1\77\5\6"+
    "\7\0\4\6\1\0\1\6\6\0\3\6\14\0\1\6"+
    "\1\100\20\6\7\0\4\6\1\0\1\6\6\0\3\6"+
    "\14\0\6\6\1\101\12\6\1\102\7\0\4\6\1\0"+
    "\1\6\6\0\3\6\14\0\5\6\1\103\14\6\7\0"+
    "\4\6\1\0\1\6\6\0\3\6\14\0\10\6\1\104"+
    "\11\6\7\0\4\6\1\0\1\6\6\0\2\6\1\105"+
    "\14\0\14\6\1\106\5\6\7\0\4\6\1\0\1\6"+
    "\6\0\3\6\14\0\14\6\1\107\5\6\7\0\4\7"+
    "\1\0\1\7\6\0\1\7\1\110\1\7\14\0\22\7"+
    "\36\0\1\111\114\0\1\112\64\0\1\113\12\0\1\114"+
    "\52\0\7\55\1\0\3\55\1\0\47\55\1\0\7\55"+
    "\1\0\3\55\1\115\1\116\3\117\43\55\2\0\5\60"+
    "\7\0\3\60\14\0\22\60\6\0\7\61\1\12\54\61"+
    "\1\0\4\6\1\0\1\6\6\0\3\6\14\0\7\6"+
    "\1\120\12\6\7\0\4\6\1\0\1\6\6\0\3\6"+
    "\14\0\2\6\1\121\17\6\7\0\4\6\1\0\1\6"+
    "\6\0\3\6\14\0\11\6\1\122\10\6\7\0\4\6"+
    "\1\0\1\6\6\0\3\6\14\0\6\6\1\123\13\6"+
    "\7\0\4\6\1\0\1\6\6\0\1\124\2\6\14\0"+
    "\22\6\7\0\4\6\1\0\1\6\6\0\2\6\1\125"+
    "\14\0\1\6\1\126\20\6\7\0\4\6\1\0\1\6"+
    "\6\0\1\127\2\6\14\0\22\6\7\0\4\6\1\0"+
    "\1\6\6\0\3\6\14\0\1\6\1\130\20\6\7\0"+
    "\4\6\1\0\1\6\6\0\1\6\1\131\1\6\14\0"+
    "\22\6\7\0\4\6\1\0\1\6\6\0\3\6\14\0"+
    "\6\6\1\132\13\6\7\0\4\6\1\0\1\6\6\0"+
    "\3\6\14\0\1\133\21\6\7\0\4\6\1\0\1\6"+
    "\6\0\1\134\2\6\14\0\22\6\7\0\4\6\1\0"+
    "\1\6\6\0\1\6\1\135\1\6\14\0\22\6\7\0"+
    "\4\6\1\0\1\6\6\0\3\6\14\0\2\6\1\136"+
    "\17\6\7\0\4\6\1\0\1\6\6\0\3\6\14\0"+
    "\5\6\1\137\14\6\7\0\4\6\1\0\1\6\6\0"+
    "\3\6\14\0\14\6\1\140\5\6\7\0\4\6\1\0"+
    "\1\6\6\0\3\6\14\0\2\6\1\141\17\6\7\0"+
    "\4\7\1\0\1\7\6\0\1\142\2\7\14\0\22\7"+
    "\22\0\1\143\47\0\7\55\1\0\3\55\1\144\47\55"+
    "\1\0\7\55\1\0\3\55\1\0\1\116\46\55\2\0"+
    "\4\6\1\0\1\6\6\0\3\6\14\0\5\6\1\145"+
    "\14\6\7\0\4\6\1\0\1\6\6\0\3\6\14\0"+
    "\1\146\21\6\7\0\4\6\1\0\1\6\6\0\3\6"+
    "\14\0\6\6\1\147\13\6\7\0\4\6\1\0\1\6"+
    "\6\0\3\6\14\0\7\6\1\150\12\6\7\0\4\6"+
    "\1\0\1\6\6\0\3\6\14\0\2\6\1\151\17\6"+
    "\7\0\4\6\1\0\1\6\6\0\1\152\2\6\14\0"+
    "\22\6\7\0\4\6\1\0\1\6\6\0\3\6\14\0"+
    "\1\153\21\6\7\0\4\6\1\0\1\6\6\0\1\154"+
    "\2\6\14\0\22\6\7\0\4\6\1\0\1\6\6\0"+
    "\3\6\14\0\1\155\21\6\7\0\4\6\1\0\1\6"+
    "\6\0\3\6\14\0\5\6\1\156\14\6\7\0\4\6"+
    "\1\0\1\6\6\0\3\6\14\0\5\6\1\157\14\6"+
    "\7\0\4\6\1\0\1\6\6\0\3\6\14\0\20\6"+
    "\1\160\1\6\7\0\4\6\1\0\1\6\6\0\3\6"+
    "\14\0\6\6\1\161\13\6\7\0\4\6\1\0\1\6"+
    "\6\0\3\6\14\0\1\6\1\162\20\6\7\0\4\6"+
    "\1\0\1\6\6\0\3\6\14\0\6\6\1\163\13\6"+
    "\7\0\4\6\1\0\1\6\6\0\3\6\14\0\16\6"+
    "\1\164\3\6\7\0\4\7\1\0\1\7\6\0\3\7"+
    "\14\0\5\7\1\165\14\7\21\0\1\144\1\0\3\144"+
    "\45\0\4\6\1\0\1\6\6\0\2\6\1\166\14\0"+
    "\22\6\7\0\4\6\1\0\1\6\6\0\1\6\1\167"+
    "\1\6\14\0\22\6\7\0\4\6\1\0\1\6\6\0"+
    "\3\6\14\0\2\6\1\170\17\6\7\0\4\6\1\0"+
    "\1\6\6\0\3\6\14\0\1\171\21\6\7\0\4\6"+
    "\1\0\1\6\6\0\3\6\14\0\2\6\1\172\17\6"+
    "\7\0\4\6\1\0\1\6\6\0\3\6\14\0\5\6"+
    "\1\173\14\6\7\0\4\6\1\0\1\6\6\0\1\6"+
    "\1\174\1\6\14\0\22\6\7\0\4\6\1\0\1\6"+
    "\6\0\1\175\2\6\14\0\22\6\7\0\4\6\1\0"+
    "\1\6\6\0\3\6\14\0\5\6\1\176\14\6\7\0"+
    "\4\6\1\0\1\6\6\0\3\6\14\0\13\6\1\177"+
    "\6\6\7\0\4\6\1\0\1\6\6\0\3\6\14\0"+
    "\5\6\1\200\14\6\7\0\4\7\1\0\1\7\6\0"+
    "\3\7\14\0\20\7\1\201\1\7\7\0\4\6\1\0"+
    "\1\6\6\0\1\6\1\202\1\6\14\0\22\6\7\0"+
    "\4\6\1\0\1\6\6\0\3\6\14\0\20\6\1\203"+
    "\1\6\7\0\4\6\1\0\1\6\6\0\3\6\14\0"+
    "\3\6\1\204\16\6\7\0\4\6\1\0\1\6\6\0"+
    "\1\6\1\205\1\6\14\0\22\6\7\0\4\6\1\0"+
    "\1\6\6\0\3\6\14\0\16\6\1\206\3\6\7\0"+
    "\4\6\1\0\1\6\6\0\3\6\14\0\10\6\1\207"+
    "\11\6\7\0\4\6\1\0\1\6\6\0\3\6\14\0"+
    "\1\6\1\210\20\6\7\0\4\7\1\0\1\7\6\0"+
    "\3\7\14\0\5\7\1\211\14\7\7\0\4\6\1\0"+
    "\1\6\6\0\3\6\14\0\7\6\1\212\12\6\7\0"+
    "\4\6\1\0\1\6\6\0\3\6\14\0\1\213\21\6"+
    "\7\0\4\6\1\0\1\6\6\0\1\6\1\214\1\6"+
    "\14\0\22\6\7\0\4\7\1\0\1\7\6\0\2\7"+
    "\1\215\14\0\22\7\7\0\4\6\1\0\1\6\6\0"+
    "\3\6\14\0\5\6\1\216\14\6\6\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[5044];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\3\0\1\11\4\1\2\11\1\1\2\11\3\1\7\11"+
    "\3\1\2\11\12\1\2\11\5\1\1\11\2\1\1\0"+
    "\1\11\5\1\3\11\16\1\4\11\27\1\1\11\52\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[142];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;

  /* user code: */
	StringBuffer string = new StringBuffer();
	public int getLineNumber() { return yyline+1; }


  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public Lexer(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  public Lexer(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 134) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzCurrentPos*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
    }

    /* finally: fill the buffer with new input */
    int numRead = zzReader.read(zzBuffer, zzEndRead,
                                            zzBuffer.length-zzEndRead);

    if (numRead > 0) {
      zzEndRead+= numRead;
      return false;
    }
    // unlikely but not impossible: read 0 characters, but not at end of stream    
    if (numRead == 0) {
      int c = zzReader.read();
      if (c == -1) {
        return true;
      } else {
        zzBuffer[zzEndRead++] = (char) c;
        return false;
      }     
    }

	// numRead < 0
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) throws LexicalError {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new LexicalError(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  throws LexicalError {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() throws java.io.IOException {
    if (!zzEOFDone) {
      zzEOFDone = true;
      yyclose();
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public Token next_token() throws java.io.IOException, LexicalError {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      boolean zzR = false;
      for (zzCurrentPosL = zzStartRead; zzCurrentPosL < zzMarkedPosL;
                                                             zzCurrentPosL++) {
        switch (zzBufferL[zzCurrentPosL]) {
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yyline++;
          zzR = false;
          break;
        case '\r':
          yyline++;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
          }
          break;
        default:
          zzR = false;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL)
            zzInput = zzBufferL[zzCurrentPosL++];
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = zzBufferL[zzCurrentPosL++];
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 46: 
          { /*yybegin(ST_CLASS);*/ return new Token(sym.CLASS, yyline, yytext());
          }
        case 57: break;
        case 3: 
          { return new Token(sym.ID,            yyline, yytext());
          }
        case 58: break;
        case 8: 
          { return new Token(sym.MULTIPLY,      yyline, yytext());
          }
        case 59: break;
        case 35: 
          { return new Token(sym.LOR,           yyline, yytext());
          }
        case 60: break;
        case 42: 
          { return new Token(sym.THIS,          yyline, yytext());
          }
        case 61: break;
        case 55: 
          { return new Token(sym.BOOLEAN,       yyline, yytext());
          }
        case 62: break;
        case 50: 
          { return new Token(sym.RETURN,        yyline, yytext());
          }
        case 63: break;
        case 27: 
          { throw new LexicalError("Invalid number: " + yytext(), yyline);
          }
        case 64: break;
        case 45: 
          { return new Token(sym.VOID,          yyline, yytext());
          }
        case 65: break;
        case 12: 
          { return new Token(sym.LB,            yyline, yytext());
          }
        case 66: break;
        case 47: 
          { return new Token(sym.FALSE,         yyline, yytext());
          }
        case 67: break;
        case 5: 
          { return new Token(sym.DOT,           yyline, yytext());
          }
        case 68: break;
        case 4: 
          { return new Token(sym.CLASS_ID,      yyline, yytext());
          }
        case 69: break;
        case 48: 
          { return new Token(sym.WHILE,         yyline, yytext());
          }
        case 70: break;
        case 2: 
          { return new Token(sym.INTEGER,       yyline, yytext());
          }
        case 71: break;
        case 33: 
          { return new Token(sym.NEQUAL,        yyline, yytext());
          }
        case 72: break;
        case 11: 
          { return new Token(sym.RP,            yyline, yytext());
          }
        case 73: break;
        case 7: 
          { return new Token(sym.DIVIDE,        yyline, yytext());
          }
        case 74: break;
        case 31: 
          { return new Token(sym.GTE,           yyline, yytext());
          }
        case 75: break;
        case 36: 
          { yybegin(YYINITIAL);
          }
        case 76: break;
        case 30: 
          { return new Token(sym.EQUAL,         yyline, yytext());
          }
        case 77: break;
        case 1: 
          { throw new LexicalError("Unresolved Token: " + yytext(), yyline);
          }
        case 78: break;
        case 49: 
          { return new Token(sym.BREAK,         yyline, yytext());
          }
        case 79: break;
        case 16: 
          { return new Token(sym.COMMA,         yyline, yytext());
          }
        case 80: break;
        case 13: 
          { return new Token(sym.RB,            yyline, yytext());
          }
        case 81: break;
        case 52: 
          { return new Token(sym.STATIC,        yyline, yytext());
          }
        case 82: break;
        case 37: 
          { string.append('\"');
          }
        case 83: break;
        case 9: 
          { string.setLength(0); yybegin(ST_STRING);
          }
        case 84: break;
        case 20: 
          { return new Token(sym.SEMI,          yyline, yytext());
          }
        case 85: break;
        case 53: 
          { return new Token(sym.LENGTH,        yyline, yytext());
          }
        case 86: break;
        case 38: 
          { return new Token(sym.NEW,           yyline, yytext());
          }
        case 87: break;
        case 44: 
          { return new Token(sym.ELSE,          yyline, yytext());
          }
        case 88: break;
        case 32: 
          { return new Token(sym.IF,            yyline, yytext());
          }
        case 89: break;
        case 19: 
          { return new Token(sym.GT,            yyline, yytext());
          }
        case 90: break;
        case 28: 
          { yybegin(ST_COMMENT);
          }
        case 91: break;
        case 56: 
          { return new Token(sym.CONTINUE,      yyline, yytext());
          }
        case 92: break;
        case 41: 
          { return new Token(sym.TRUE,          yyline, yytext());
          }
        case 93: break;
        case 14: 
          { return new Token(sym.LCBR,          yyline, yytext());
          }
        case 94: break;
        case 21: 
          { return new Token(sym.MOD,           yyline, yytext());
          }
        case 95: break;
        case 23: 
          { return new Token(sym.PLUS,          yyline, yytext());
          }
        case 96: break;
        case 29: 
          { return new Token(sym.LTE,           yyline, yytext());
          }
        case 97: break;
        case 40: 
          { string.append(yytext());
          }
        case 98: break;
        case 24: 
          { return new Token(sym.LNEG,          yyline, yytext());
          }
        case 99: break;
        case 39: 
          { return new Token(sym.INT,           yyline, yytext());
          }
        case 100: break;
        case 22: 
          { return new Token(sym.MINUS,         yyline, yytext());
          }
        case 101: break;
        case 18: 
          { return new Token(sym.ASSIGN,        yyline, yytext());
          }
        case 102: break;
        case 10: 
          { return new Token(sym.LP,            yyline, yytext());
          }
        case 103: break;
        case 43: 
          { return new Token(sym.NULL,          yyline, yytext());
          }
        case 104: break;
        case 15: 
          { return new Token(sym.RCBR,          yyline, yytext());
          }
        case 105: break;
        case 25: 
          { string.append( yytext() );
          }
        case 106: break;
        case 34: 
          { return new Token(sym.LAND,          yyline, yytext());
          }
        case 107: break;
        case 26: 
          { yybegin(YYINITIAL); 
                                   return new Token(sym.QUOTE, 
                                   yyline,string.toString());
          }
        case 108: break;
        case 54: 
          { return new Token(sym.EXTENDS,       yyline, yytext());
          }
        case 109: break;
        case 51: 
          { return new Token(sym.STRING,        yyline, yytext());
          }
        case 110: break;
        case 6: 
          { 
          }
        case 111: break;
        case 17: 
          { return new Token(sym.LT,            yyline, yytext());
          }
        case 112: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            zzDoEOF();
              { 	if(yystate() == ST_COMMENT){
		throw new LexicalError("Comment unclosed", yyline); 
	}
	return new Token(sym.EOF, yyline, "EOF");
 }
          } 
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
