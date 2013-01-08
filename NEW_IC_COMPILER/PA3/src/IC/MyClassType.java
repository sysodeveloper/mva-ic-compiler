package IC;

import IC.AST.ICClass;

public class MyClassType extends MyType {
	
	private ICClass classAST;
	private MyClassType superClass=null;
	@Override
	boolean subtypeOf(MyType type) {
		// TODO Auto-generated method stub
		return false;
	}
	public ICClass getClassAST() {
		return classAST;
	}
	public void setClassAST(ICClass classAST) {
		this.classAST = classAST;
	}
	
	public String toString(){
		String r = "Class: "+this.getName();
		if(hasSuperClass())
			r+=", Superclass Name: "+getSuperClass().getName();
		return r;
	}
	public MyClassType getSuperClass() {
		return superClass;
	}
	public void setSuperClass(MyClassType superClass) {
		this.superClass = superClass;
	}
	
	public boolean hasSuperClass(){
		return !(superClass==null);
	}
	
}
