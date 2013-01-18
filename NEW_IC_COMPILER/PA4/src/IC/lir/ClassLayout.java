package IC.lir;

import java.util.LinkedHashMap;
import java.util.Map;

public class ClassLayout {
	private int nextField;
	private int nextMethod;
	private String className;
	private Map<String,Integer> fieldToOffset;
	private Map<String,Integer> methodToOffset;
	
	//for new class without superclasses
	
	
	public void initiallize(String className){
		fieldToOffset = new LinkedHashMap<String, Integer>();
		methodToOffset = new LinkedHashMap<String, Integer>();
		nextField=0;
		nextMethod=0;	
		this.className=className;
	}

	
	public ClassLayout(String className, ClassLayout superClassLayout ){				
		initiallize( className);		
		if(superClassLayout!=null){			
			this.setFieldToOffset(superClassLayout.getFieldToOffset());
			this.setFieldOffset(superClassLayout.getFieldOffset());
			this.setMethodToOffset(superClassLayout.getMethodToOffset());
			this.setMethodOffsset(superClassLayout.getMethodOffsset());
		}
	}

	
	
	public void addField(String fieldName){
		fieldToOffset.put(fieldName, nextField++);
	}
	
	public void addMethod(String methodName){
		if(!methodToOffset.containsKey(methodName))
			methodToOffset.put(methodName, nextMethod++);
	}
	
	private String makeSymbolicName(String name){
		return "_"+this.className+"_"+name;
	}
	
	
	public int getFieldOffset() {
		return nextField;
	}

	public void setFieldOffset(int fieldOffset) {
		this.nextField = fieldOffset;
	}

	public int getMethodOffsset() {
		return nextMethod;
	}

	public void setMethodOffsset(int methodOffsset) {
		this.nextMethod = methodOffsset;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Map<String, Integer> getFieldToOffset() {
		return fieldToOffset;
	}

	public void setFieldToOffset(Map<String, Integer> fieldToOffset) {
		this.fieldToOffset.putAll(fieldToOffset);
	}

	public Map<String, Integer> getMethodToOffset() {
		return methodToOffset;
	}

	public void setMethodToOffset(Map<String, Integer> methodToOffset) {
		this.methodToOffset.putAll(methodToOffset);
	}
	
	
}
