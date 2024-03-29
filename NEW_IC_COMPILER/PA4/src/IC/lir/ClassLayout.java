package IC.lir;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
		/*String methodSymbolicName = makeSymbolicName(methodName);
		if(methodToOffset.containsKey(methodSymbolicName)){
			return methodToOffset.get(methodSymbolicName);
		}*/
		for(String mName: methodToOffset.keySet()){
			if(mName.split("_")[2].compareTo(methodName)==0)
				return methodToOffset.get(mName);
		}
		return -1;
	}	
	
	public boolean hasMethodInLayout(String methodName){
		for(String mName : methodToOffset.keySet()){
			if(mName.split("_")[2].compareTo(methodName)==0)
				return true;
		}
		return false;
	}
	
	public boolean hasVirtaulMethos(){
		return this.methodToOffset.size() > 0;
	}
	
	public String printDispatchVector(){
		StringBuffer dispatchVector = new StringBuffer();
		//if(hasVirtaulMethos()){
			dispatchVector.append("_DV_"+this.className+": [");
			// sort by offsets :
			class Cmp implements Comparator<Entry<String,Integer>>{
				@Override
				public int compare(Entry<String, Integer> o1,
						Entry<String, Integer> o2) {
					return o1.getValue()-o2.getValue();
					
				}
				
			}
			Set<Entry<String,Integer>> ent = methodToOffset.entrySet();
			List<Entry<String,Integer>>list =new ArrayList<Map.Entry<String,Integer>>();
			for(Entry<String,Integer> e : ent){
				list.add(e);
			}
			Collections.sort(list, new Cmp());
			for(Entry<String, Integer> methodName : list){
				dispatchVector.append(methodName.getKey());
				dispatchVector.append(",");
			}
			if(dispatchVector.charAt(dispatchVector.length()-1)==',')
				dispatchVector.deleteCharAt(dispatchVector.length()-1);
		//}
			dispatchVector.append("]")	;
		return dispatchVector.toString();
	}
	
	public int getLayoutSize(){
		return nextField;
	}
}
