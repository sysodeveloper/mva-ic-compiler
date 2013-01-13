package IC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java_cup.Main;

import IC.AST.ICClass;
import IC.AST.Method;
import IC.AST.MethodType;
import IC.AST.Type;

public class MyTypeTable {
	private int uniqueId = 6;
	// Maps element types to array types
	private Map<MyType,MyArrayType> uniqueArrayTypes;
    private Map<String,MyClassType> uniqueClassTypes;
    private Map<String,MyMethodType> uniqueMethodTypes;
    private LinkedList<MyPrimitiveType> uniquePrimitiveType;
	
	public MyTypeTable(){
		uniqueArrayTypes = new LinkedHashMap<MyType, MyArrayType>();
		uniqueClassTypes = new LinkedHashMap<String, MyClassType>();
		uniqueMethodTypes = new LinkedHashMap<String, MyMethodType>();
		uniquePrimitiveType = new LinkedList<MyPrimitiveType>(); 		
	}
	
	// Returns unique array type object
	public  MyArrayType arrayType(MyArrayType elemType) {
		if (uniqueArrayTypes.containsKey(elemType)) {
			// array type object already created – return it
			return uniqueArrayTypes.get(elemType);
			}
		else {
			// object doesn’t exist – put it inside and return it
			uniqueArrayTypes.put(elemType,elemType);
			return elemType;
			}		
	}
	
	// Returns unique class type object
	public MyClassType classType(MyClassType elemType){
		if(uniqueClassTypes.containsKey(elemType.getName()))
			return uniqueClassTypes.get(elemType.getName());		
		
		if(elemType.hasSuperClass()){
			MyClassType superClass = elemType.getSuperClass();
			if(uniqueClassTypes.containsKey(elemType.getSuperClass().getName()))
				superClass = uniqueClassTypes.get(elemType.getSuperClass().getName());
			elemType.setSuperClass(superClass);
		}
		uniqueClassTypes.put(elemType.getName(), elemType);
		return elemType;
	}
	
	// Returns unique method type object
	public MyMethodType methodType(MyMethodType elemType){
		if(uniqueMethodTypes.containsKey(elemType.getName()))
			return uniqueMethodTypes.get(elemType.getName());		
		
		uniqueMethodTypes.put(elemType.getName(), elemType);
		return elemType;
	}
	
	// Returns unique primitive type object
	public MyPrimitiveType primitiveType(MyPrimitiveType pType){
		if(uniquePrimitiveType.contains(pType)){			
			//System.out.println("already here");
			return uniquePrimitiveType.get(uniquePrimitiveType.indexOf(pType));
		}
		uniquePrimitiveType.add(pType);		
		return pType;
	}
	
	public MyType insertType(MyType type){
		MyType returnType = null;
		if(type instanceof MyPrimitiveType )
			returnType = primitiveType((MyPrimitiveType)type);
		else if (type instanceof MyClassType )
			returnType = classType((MyClassType)type);
		else if(type instanceof MyArrayType)
			returnType = arrayType((MyArrayType)type);
		else if (type instanceof MyMethodType)
			returnType = methodType((MyMethodType)type);
		else
			returnType = null;
		return returnType;
	
	}
	
	public void printTypeTable(){
		System.out.println("Type Table: " + Compiler.getICFileParsed());
		Collections.sort(uniquePrimitiveType, new Comparator<MyPrimitiveType>() {

			@Override
			public int compare(MyPrimitiveType o1, MyPrimitiveType o2) {
				return o1.getUnique_id()-o2.getUnique_id();
			}
			
		});
		for(MyPrimitiveType t :uniquePrimitiveType){
			System.out.print("\t");
			System.out.println(t);
		}
		for(MyClassType t :uniqueClassTypes.values()){
			System.out.print("\t");
			System.out.println(t);
		}
		for(MyArrayType t :uniqueArrayTypes.values()){
			System.out.print("\t");
			System.out.println(t);
		}
		for(MyMethodType t:uniqueMethodTypes.values()){
			System.out.print("\t");
			System.out.println(t);
		}
	}
	
}
