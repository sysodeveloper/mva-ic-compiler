package IC.mySymbolTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import IC.AST.ASTNode;
import IC.AST.INameable;
import IC.AST.LocalVariable;
import IC.AST.MethodType;
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
		entries = new LinkedHashMap<String, MySymbolRecord>();
		children = new ArrayList<MySymbolTable>();
	}
	
	public Map<String,MySymbolRecord> getEntries(){
		return this.entries;
	}
	
	public String getDescription(){
		return this.description;
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
		//System.out.print("***** tyring to insert " + key + " " + record.getKind() + " to table: " + this.id+ " line:" + record.getNode().getLine());
		if(this.entries.get(key) == null){
			this.entries.put(key, record);
			//System.out.print(" OK \n");
		}else{
			//System.out.print(" FAILED \n");
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
		//Map<Integer, String> childrenMap = new HashMap<Integer, String>();
		int i = 0;
		for(String key : entries.keySet()){
			boolean printType = true;
			boolean printTypeFirst = true;
			if(key.compareTo("$ret") == 0){
				continue;
			}
			switch(this.entries.get(key).getKind()){
			case Class:
				i = 0;
				printType = false;
				break;
			case Field:
				i = 1;
				break;
			case Static_Method:
				i = 2;
				printTypeFirst = false;
				break;
			case Library_Method:
				i = 2;
				printTypeFirst = false;
				break;
			case Virtual_Method:
				i = 3;
				printTypeFirst = false;
				break;
			case Parameter:
					i = 4;
					break;
			case Local_Variable:
				i = 5;
				break;
			}
			chunks[i].append("\t");
			chunks[i].append(entries.get(key).getKindToString());
			chunks[i].append(": ");
			if(printType && printTypeFirst){
				chunks[i].append(entries.get(key).getType().getFullName());
				chunks[i].append(" ");
				chunks[i].append(key);
			}else if(!printTypeFirst && printType){
				chunks[i].append(key);
				chunks[i].append(" ");
				if(entries.get(key).getType() instanceof MethodType){
					String str = entries.get(key).getType().getFullName();
					str = str.replaceAll("  ", " ");
					chunks[i].append(str);
				}else{
					chunks[i].append(entries.get(key).getType().getFullName());
				}
			}else{
				chunks[i].append(key);
			}
			chunks[i].append("\n");
			//childrenMap.put(entries.get(key).getNode().enclosingScope().getId(),key);
		}
		for(int j=0;j<chunks.length;j++){
			output.append(chunks[j]);
		}
		if(this.children.size() > 0){
			output.append("Children tables: " );
			for(MySymbolTable child : this.children){
				output.append(child.getDescription());
				output.append(", ");
			}
			output.deleteCharAt(output.length()-1);
			output.deleteCharAt(output.length()-1);
			output.append("\n");
		}
		return output.toString();
	}
}
