package IC;

import java.util.Comparator;

public abstract class MyPrimitiveType extends MyType {
	
	@Override
	boolean subtypeOf(MyType type) {
		// TODO Auto-generated method stub
		return this == type;
	}
	
	public String toString(){
		return "Primitive type: "+this.getName();
	}
	
}
