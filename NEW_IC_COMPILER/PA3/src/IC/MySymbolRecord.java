package IC;

import java.util.ArrayList;
import java.util.List;

import IC.AST.ASTNode;
import IC.AST.Type;

public class MySymbolRecord {
	public enum Kind{
		Class,
		Local_Variable,
		Parameter,
		Field,
		Static_Method,
		Virtual_Method,
		Library_Method
		
	}
	private int id;
	private ASTNode node;	
	private Kind kind;
	private List<Object> properties;
	private Type type;
	private boolean declared = true;
	private boolean initialized = false;
	
	public MySymbolRecord(int id, ASTNode node, Kind kind, Type type){
		this.id = id;
		this.node = node;
		this.kind = kind;
		this.type = type;
		properties = new ArrayList<Object>();
		if(kind == Kind.Local_Variable || kind == Kind.Parameter)
			this.declared = false;
	}
	
	public int getId(){
		return this.id;
	}
	
	public ASTNode getNode(){
		return this.node;
	}
	
	public Kind getKind(){
		return kind;
	}
	
	public String getKindToString(){
		String strKind = kind.toString().toLowerCase();
		if(strKind.length() >= 1){
			String first = strKind.charAt(0) + "";
			first = first.toUpperCase();
			strKind = first + strKind.substring(1);
		}
		return strKind.replace('_', ' ');
	}
	
	public void insertProperty(Object p){
		this.properties.add(p);
	}
	
	public List<Object> getProperties(){
		return this.properties;
	}
	
	public Type getType(){
		return this.type;
	}
	
	public void setAsDeclared(){
		this.declared = true;
	}
	
	public void setAsInitialized(){
		this.initialized = true;
	}
	
	public boolean isDeclared(){
		return this.declared;
	}
	
	public boolean isInitialized(){
		return this.initialized;
	}
}
