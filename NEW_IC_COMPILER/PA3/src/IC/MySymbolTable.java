package IC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import IC.AST.ASTNode;
import IC.AST.INameable;
import IC.AST.LocalVariable;
import IC.AST.StaticMethod;
import IC.AST.Type;

public class MySymbolTable {
	private Map<String,MySymbolRecord> entries;
	private int id;
	private MySymbolTable parent;
	private List<MySymbolTable> children;
	private String description;
	public MySymbolTable(int id, String description){
		this.id = id;
		this.description = description;
		entries = new HashMap<String, MySymbolRecord>();
		children = new ArrayList<MySymbolTable>();
	}
	
	public Map<String,MySymbolRecord> getEntries(){
		return this.entries;
	}
	
	public int getId(){
		return this.id;
	}
	
	public MySymbolTable getParent(){
		return this.parent;
	}
	
	public void setParent(MySymbolTable p){
		this.parent = p;
	}
	
	public void addChild(MySymbolTable child){
		this.children.add(child);
	}
	
	public List<MySymbolTable> getChildren(){
		return this.children;
	}
	
	public boolean InsertRecord(String key, MySymbolRecord record){
		System.out.print("***** tyring to insert " + key + " " + record.getKind() + " to table: " + this.id+ " line:" + record.getNode().getLine());
		if(this.entries.get(key) == null){
			this.entries.put(key, record);
			System.out.print(" OK \n");
		}else{
			System.out.print(" FAILED \n");
			return false;
		}
		return true;
	}
	
	public MySymbolRecord Lookup(String key){
		//looks up string key in the current and parents symbol tables
		MySymbolRecord rec = this.entries.get(key);
		if(rec != null){
			return rec;
		}else{
			//look parent symbol table
			MySymbolTable parent = this.parent;
			while(parent != null){
				rec = parent.Lookup(key);
				if(rec != null){
					return rec;
				}else{
					parent = parent.getParent();
				}
			}
		}
		return null;
	}
	
	public String toString(){
		StringBuffer output = new StringBuffer();
		StringBuffer[] chunks = { new StringBuffer(),new StringBuffer(),new StringBuffer(),new StringBuffer(),new StringBuffer(),new StringBuffer() };
		Map<Integer, String> childrenMap = new HashMap<Integer, String>();
		int i = 0;
		for(String key : entries.keySet()){
			switch(this.entries.get(key).getKind()){
			case Class:
				i = 0;
				break;
			case Field:
				i = 1;
				break;
			case Method:
				if(this.entries.get(key).getNode() instanceof StaticMethod){
					i = 2;
				}else{
					i = 3;
				}
				break;
			case Variable:
				if(this.entries.get(key).getNode() instanceof LocalVariable){
					i = 5;
				}else{
					i = 4;
				}
				break;
			}
			chunks[i].append("\t");
			chunks[i].append(entries.get(key).getKind());
			chunks[i].append(": ");
			chunks[i].append(key);
			chunks[i].append("\n");
			childrenMap.put(entries.get(key).getNode().enclosingScope().getId(),key);
		}
		for(int j=0;j<chunks.length;j++){
			output.append(chunks[j]);
		}
		output.append("Children tables ("+this.children.size()+"): " );
		MySymbolRecord rec;
		for(MySymbolTable child : this.children){
			
		}
		output.deleteCharAt(output.length()-1);
		output.append("\n");
		return output.toString();
	}
}
