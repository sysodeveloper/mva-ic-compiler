package IC.myTypes;

import java.util.Comparator;

public abstract class MyPrimitiveType extends MyType {
	
	@Override
	public boolean subtypeOf(MyType type) {
		// TODO Auto-generated method stub
		return this == type;
	}
	
	public String toString(){
		return this.getUnique_id()+": Primitive type: "+this.getName();
	}
	
	public abstract MyPrimitiveType clone();
	
}
