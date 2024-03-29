package IC.myTypes;

import java.util.ArrayList;

import IC.AST.Formal;
import IC.AST.Method;
import IC.AST.MethodType;
import IC.AST.Type;
import IC.*;
import IC.mySymbolTable.*;
import IC.myTypes.*;
import IC.semanticChecks.*;

public class MyMethodType extends MyType {
	
	private ArrayList<MyType> paramTypes;
	private MyType returnType;

	
	public MyMethodType clone(){
		MyMethodType t = new MyMethodType();
		t.setName(this.getName());
		t.setDimention(this.getDimention());
		t.setUnique_id(this.getUnique_id());
		t.setParamTypes((ArrayList<MyType>) getParamTypes().clone());
		t.setReturnType(getReturnType().clone());
		t.setFullName();
		return t;
	}
	
	public MyMethodType(){
		this.setUnique_id(this.run_id++);
	}
	
	@Override
	public boolean subtypeOf(MyType type) {
		// TODO Auto-generated method stub
		return false;
	}
	public ArrayList<MyType> getParamTypes() {
		return paramTypes;
	}
	public void setParamTypes(ArrayList<MyType> paramTypes) {
		this.paramTypes = paramTypes;
	}
	public MyType getReturnType() {
		return returnType;
	}
	public void setReturnType(MyType returnType) {
		this.returnType = returnType;
	}
	
	public void setFullName(){
		StringBuilder formals = new StringBuilder();
		boolean first = true;
		if(paramTypes.isEmpty()) {
			formals.append(" ");
		}
		else {
			for (MyType fType : paramTypes) {
				if(!first) {
					formals.append(", ");
				}
				formals.append(fType.getName());
				first = false;
			}
		}
		this.setName( "{" + formals.toString()  + " -> " + 
			getReturnType().getName() + "}");
	}
	
	public String toString(){
		if(this.getParamTypes().size() > 0){
			return this.getUnique_id()+": Method type: "+this.getName();
		}
		String str = this.getName();
		str = str.replaceAll("  ", " ");
		return this.getUnique_id()+": Method type: "+str;
	}
}
