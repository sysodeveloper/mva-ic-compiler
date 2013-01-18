package IC.mySymbolTable;

import IC.AST.Type;

public class MyTypeRecord {
	private Type type;
	private int id;
	
	public MyTypeRecord(int id, Type type){
		this.id = id;
		this.type = type;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}

}
