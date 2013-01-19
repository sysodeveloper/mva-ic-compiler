package IC.lir;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ClassLayout {
	private int nextField;
	private int nextMethod;
	private String className;
	private Map<String,Integer> fieldToOffset;
	private Map<String,Integer> methodToOffset;
	private ClassLayout superClassLayout;
	//for new class without superclasses
	
	
	public void initiallize(String className){
		fieldToOffset = new LinkedHashMap<String, Integer>();
		methodToOffset = new LinkedHashMap<String, Integer>();
		//0 is dispatch vector
		nextField=1;
		nextMethod=0;	
		this.className=className;
	}

	
	public ClassLayout(String className, ClassLayout superClassLayout ){				
		initiallize( className);		
		if(superClassLayout!=null){		
			this.superClassLayout = superClassLayout;
			this.setFieldToOffset(superClassLayout.getFieldToOffset());
			this.setFieldOffset(superClassLayout.getFieldOffset());
			this.setMethodToOffset(superClassLayout.getMethodToOffset());
			this.setMethodOffsset(superClassLayout.getMethodOffsset());
		}
	}

	public ClassLayout getSuperClassLayout(){
		return this.superClassLayout;
	}
	
	public void addField(String fieldName){
		fieldToOffset.put(fieldName, nextField++);
	}
	
	public void addMethod(String methodName){
		boolean found = false;
		String fName="";
		for(String m : methodToOffset.keySet()){
			String name = m.split("_")[2];
			if(name.compareTo(methodName)==0){
				found = true;
				fName=m;
				break;
			}
		}
		if(!found)//new method 
			methodToOffset.put(makeSymbolicName(methodName), nextMethod++);
		else{ // overridden method - remove and put current method
			int offset = methodToOffset.get(fName);
			methodToOffset.remove(fName);
			methodToOffset.put(makeSymbolicName(methodName), offset);
		}
			
	}
	
	public String makeSymbolicName(String name){
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
	
	public int getFieldOffset(String fieldName){
		if(fieldToOffset.containsKey(fieldName)){
			return fieldToOffset.get(fieldName);
		}
		return -1;
	}
	
	public int getMethodOffset(String methodName){
		String methodSymbolicName = makeSymbolicName(methodName);
		if(fieldToOffset.containsKey(methodSymbolicName)){
			return fieldToOffset.get(methodSymbolicName);
		}
		return -1;
	}	
	
	public boolean hasVirtaulMethos(){
		return this.methodToOffset.size() > 0;
	}
	
	public String printDispatchVector(){
		StringBuffer dispatchVector = new StringBuffer();
		if(hasVirtaulMethos()){
			dispatchVector.append("_DV_"+this.className+":");
			for(String methodName : this.methodToOffset.keySet()){
				dispatchVector.append(methodName);
				dispatchVector.append(",");
			}
			dispatchVector.deleteCharAt(dispatchVector.length()-1);
		}
		return dispatchVector.toString();
	}
}
