package IC;

import java.util.ArrayList;
import java.util.List;

import IC.AST.ASTNode;
import IC.AST.Type;

public class MySymbolRecord {
	public enum Kind{
		Class,
		Method,
		Variable,
		Field
	}
	private int id;
	private ASTNode node;	
	private Kind kind;
	private List<Object> properties;
	private Type type;
	
	public MySymbolRecord(int id, ASTNode node, Kind kind, Type type){
		this.id = id;
		this.node = node;
		this.kind = kind;
		this.type = type;
		properties = new ArrayList<Object>();
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
	
	public void insertProperty(Object p){
		this.properties.add(p);
	}
	
	public List<Object> getProperties(){
		return this.properties;
	}
	
	public Type getType(){
		return this.type;
	}
}
