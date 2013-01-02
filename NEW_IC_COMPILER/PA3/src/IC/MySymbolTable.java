package IC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import IC.AST.ASTNode;
import IC.AST.Type;

public class MySymbolTable {
	private Map<String,MySymbolRecord> entries;
	private int id;
	private MySymbolTable parent;
	private List<MySymbolTable> children;
	
	public MySymbolTable(int id){
		this.id = id;
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
		Map<Integer, String> children = new HashMap<Integer, String>();
		for(String key : entries.keySet()){
			output.append("\t");
			output.append(entries.get(key).getKind());
			output.append(": ");
			output.append(key);
			output.append("\n");
			children.put(entries.get(key).getId(),key);
		}
		output.append("Children tables: ");
		for(MySymbolTable child : this.children){
			
		}
		output.append("\n");
		return output.toString();
	}
}
