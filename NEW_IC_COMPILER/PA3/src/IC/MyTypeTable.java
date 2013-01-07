package IC;

import java.util.HashMap;
import java.util.Map;

import IC.AST.Type;

public class MyTypeTable {
	private int uniqueId = 0;
	private Map<Type, MyTypeRecord> types;
	
	public MyTypeTable(){
		types = new HashMap<Type, MyTypeRecord>();
	}
	
	public MyTypeRecord getMyTypeRecord(Type t){
		return types.get(t);
	}
	
	public void insertType(Type newType){
		if(types.get(newType)==null){
			types.put(newType, new MyTypeRecord(uniqueId++,newType));
		}
	}
}
