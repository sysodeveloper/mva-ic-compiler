package IC;

import java.util.ArrayList;

import IC.AST.Formal;
import IC.AST.Method;
import IC.AST.MethodType;
import IC.AST.Type;

public class MyMethodType extends MyType {
	
	private ArrayList<MyType> paramTypes;
	private MyType returnType;
	
	public MyMethodType(){
		this.setUnique_id(this.run_id++);
	}
	
	@Override
	boolean subtypeOf(MyType type) {
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
		return this.getUnique_id()+": Method type: "+this.getName();
	}
}
