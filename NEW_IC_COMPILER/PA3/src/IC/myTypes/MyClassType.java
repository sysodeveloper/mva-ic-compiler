package IC.myTypes;
import IC.*;
import IC.mySymbolTable.*;
import IC.myTypes.*;
import IC.semanticChecks.*;
import IC.AST.ICClass;

public class MyClassType extends MyType {
	
	private ICClass classAST;
	private MyClassType superClass=null;
	
	public MyClassType clone(){
		MyClassType t = new MyClassType();
		t.setName(this.getName());
		t.setDimention(this.getDimention());
		t.setUnique_id(this.getUnique_id());
		t.setClassAST(getClassAST());
		t.setSuperClass(getSuperClass());
		return t;
	}
	
	public MyClassType(){
		this.setUnique_id(this.run_id++);
	}
	
	@Override
	public boolean subtypeOf(MyType type) {
		// TODO Auto-generated method stub
		if(!(type instanceof MyClassType))
			return false;
		if(this == type)
			return true;
		if(superClass == null)
			return false;
		MyClassType c = superClass;
		while(c!=null){
			if(c == type)
				return true;
			c = c.getSuperClass();	
		}
		if(((MyClassType)type).hasSuperClass())
			return subtypeOf(((MyClassType)type).getSuperClass());
		return false;
	}
	
	public ICClass getClassAST() {
		return classAST;
	}
	public void setClassAST(ICClass classAST) {
		this.classAST = classAST;
	}
	
	public String toString(){
		String r = this.getUnique_id()+": Class: "+this.getName();
		if(hasSuperClass())
			r+=", Superclass ID: "+getSuperClass().getUnique_id();
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
