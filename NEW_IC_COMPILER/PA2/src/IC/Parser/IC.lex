package IC.Parser;
import java_cup.runtime.*;
%%

// General Lexer Settings
///////////////////////////
%cup
%class Lexer
%public
%function next_token
%type Token
%line
%scanerror LexicalError

%{
	StringBuffer string = new StringBuffer();
%}

%{
	public int getLineNumber() { return yyline+1; }
%}


// State Definitions
//////////////////////

%state ST_COMMENT
%state ST_STRING
// Handle at EOF.
%eofval{
	if(yystate() == ST_COMMENT){
		throw new LexicalError("Comment unclosed", yyline); 
	}
	return new Token(sym.EOF, yyline, "EOF");
%eofval}

// Macros
///////////

POSITIVE_DIGIT  = [1-9]
SMALL_LETTER    = [a-z]
CAPS_LETTER     = [A-Z]
DIGIT           = 0 | {POSITIVE_DIGIT}
NUMBER          = 0+ | ({POSITIVE_DIGIT} {DIGIT}*)
IL_NUMBER       = (0+ [a-zA-Z0-9.]+)
LETTER          = {SMALL_LETTER} | {CAPS_LETTER} | _
ALPHA_NUMERIC   = {DIGIT} | {LETTER}
VARIABLE        = {SMALL_LETTER}+ {ALPHA_NUMERIC}*
CLASS_VARIABLE  = {CAPS_LETTER}+  {ALPHA_NUMERIC}*
LINE_TERMINATOR  = [\r\n"\r\n"]
INPUT_CHARACTER  = [^\r\n"\r\n"]
IGNORE          = {LINE_TERMINATOR} | [ \t\f]
LINE_COMMENT    = "//" {INPUT_CHARACTER}* {LINE_TERMINATOR}
T_COMMENT_OPEN  = "/*"
T_COMMENT_CLOSE = "*/"
QUOTE           = \"
ESCAPE_SEQ		= \\t|\\n|\\r|\\\"|\\
%%

// Lexing Rules
//////////////////

<YYINITIAL>  {IGNORE}          {}
<ST_COMMENT> {IGNORE}          {}
<YYINITIAL>  {NUMBER}          { return new Token(sym.INTEGER,       yyline, yytext()); }
<YYINITIAL>  {IL_NUMBER}       { throw new LexicalError("Invalid number: " + yytext(), yyline); }
<YYINITIAL>  "("               { return new Token(sym.LP,            yyline, yytext()); }
<YYINITIAL>  ")"               { return new Token(sym.RP,            yyline, yytext()); }
<YYINITIAL>  "["               { return new Token(sym.LB,            yyline, yytext()); }
<YYINITIAL>  "]"               { return new Token(sym.RB,            yyline, yytext()); }
<YYINITIAL>  "{"               { return new Token(sym.LCBR,          yyline, yytext()); }
<YYINITIAL>  "}"               { return new Token(sym.RCBR,          yyline, yytext()); }
<YYINITIAL>  ","               { return new Token(sym.COMMA,         yyline, yytext()); }
<YYINITIAL>  "."               { return new Token(sym.DOT,           yyline, yytext()); }
<YYINITIAL>  "<="              { return new Token(sym.LTE,           yyline, yytext()); }
<YYINITIAL>  "<"               { return new Token(sym.LT,            yyline, yytext()); }
<YYINITIAL>  ">="              { return new Token(sym.GTE,           yyline, yytext()); }
<YYINITIAL>  ">"               { return new Token(sym.GT,            yyline, yytext()); }
<YYINITIAL>  ";"               { return new Token(sym.SEMI,          yyline, yytext()); }
<YYINITIAL>  "%"               { return new Token(sym.MOD,           yyline, yytext()); }
<YYINITIAL>  "static"          { return new Token(sym.STATIC,        yyline, yytext()); }
<YYINITIAL>  "if"              { return new Token(sym.IF,            yyline, yytext()); }
<YYINITIAL>  "else"            { return new Token(sym.ELSE,          yyline, yytext()); }
<YYINITIAL>  "true"            { return new Token(sym.TRUE,          yyline, yytext()); }
<YYINITIAL>  "false"           { return new Token(sym.FALSE,         yyline, yytext()); }
<YYINITIAL>  "this"            { return new Token(sym.THIS,          yyline, yytext()); }
<YYINITIAL>  "while"           { return new Token(sym.WHILE,         yyline, yytext()); }
<YYINITIAL>  "break"           { return new Token(sym.BREAK,         yyline, yytext()); }
<YYINITIAL>  "continue"        { return new Token(sym.CONTINUE,      yyline, yytext()); }
<YYINITIAL>  "int"             { return new Token(sym.INT,           yyline, yytext()); }
<YYINITIAL>  "void"            { return new Token(sym.VOID,          yyline, yytext()); }
<YYINITIAL>  "Integer"         { return new Token(sym.INTEGER,       yyline, yytext()); }
<YYINITIAL>  "boolean"         { return new Token(sym.BOOLEAN,       yyline, yytext()); }
<YYINITIAL>  "string"          { return new Token(sym.STRING,        yyline, yytext()); }
<YYINITIAL>  "static"          { return new Token(sym.STATIC,        yyline, yytext()); }
<YYINITIAL>  "new"             { return new Token(sym.NEW,           yyline, yytext()); }
<YYINITIAL>  "extends"         { return new Token(sym.EXTENDS,       yyline, yytext()); }
<YYINITIAL>  "null"            { return new Token(sym.NULL,          yyline, yytext()); }
<YYINITIAL>  "return"          { return new Token(sym.RETURN,        yyline, yytext()); }
<YYINITIAL>  "length"          { return new Token(sym.LENGTH,        yyline, yytext()); }
//<YYINITIAL>  "++"              { return new Token(sym.PLUSPLUS,      yyline, yytext()); }
//<YYINITIAL>  "+="              { return new Token(sym.PLUSEQUAL,     yyline, yytext()); }
//<YYINITIAL>  "--"              { return new Token(sym.MINUSMINUS,    yyline, yytext()); }
//<YYINITIAL>  "-="              { return new Token(sym.MINUSEQUAL,    yyline, yytext()); }
//<YYINITIAL>  "*="              { return new Token(sym.MULTIPLYEQUAL, yyline, yytext()); }
<YYINITIAL>  "-"               { return new Token(sym.MINUS,         yyline, yytext()); }
<YYINITIAL>  "+"               { return new Token(sym.PLUS,          yyline, yytext()); }
<YYINITIAL>  "*"               { return new Token(sym.MULTIPLY,      yyline, yytext()); }
<YYINITIAL>  "!="              { return new Token(sym.NEQUAL,        yyline, yytext()); }
<YYINITIAL>  "!"               { return new Token(sym.LNEG,          yyline, yytext()); }
//<YYINITIAL>  "/="              { return new Token(sym.DIVIDEEQUAL,   yyline, yytext()); }
<YYINITIAL>  "/"               { return new Token(sym.DIVIDE,        yyline, yytext()); }
<YYINITIAL>  "&&"              { return new Token(sym.LAND,          yyline, yytext()); }
<YYINITIAL>  "||"              { return new Token(sym.LOR,           yyline, yytext()); }
<YYINITIAL>  "=="              { return new Token(sym.EQUAL,         yyline, yytext()); }
<YYINITIAL>  "="               { return new Token(sym.ASSIGN,        yyline, yytext()); }
<YYINITIAL>  "class"           {/*yybegin(ST_CLASS);*/ return new Token(sym.CLASS, yyline, yytext()); }
<YYINITIAL>  {CLASS_VARIABLE}  { return new Token(sym.CLASS_ID,      yyline, yytext()); }
<YYINITIAL>  {VARIABLE}        { return new Token(sym.ID,            yyline, yytext()); }
<YYINITIAL> {LINE_COMMENT}    { }
<YYINITIAL>  {T_COMMENT_OPEN}  { yybegin(ST_COMMENT); }
<ST_COMMENT> {T_COMMENT_CLOSE} { yybegin(YYINITIAL); }
<ST_COMMENT> [^"*/"]           {}
<ST_COMMENT> [*/]              {}
<YYINITIAL>  {QUOTE}   		   { string.setLength(0); yybegin(ST_STRING); }
<ST_STRING>{
  {QUOTE}                             { yybegin(YYINITIAL); 
                                   return new Token(sym.QUOTE, 
                                   yyline,string.toString()); }
                                   
 [^\n\r\t\"]+                   { string.append( yytext() ); }
  \\t                            { string.append('\t'); }
  \\n                            { string.append('\n'); }
  {ESCAPE_SEQ}\\\"						 {string.append(yytext()); }
  \\\"{ESCAPE_SEQ}						 {string.append(yytext()); }
  							
  \\r                            { string.append('\r'); }
  \\\"                           { string.append('\"'); }
   \\                             { string.append('\\'); }
}
// Handle any other situation.
.|\n                           { throw new LexicalError("Unresolved Token: " + yytext(), yyline); }