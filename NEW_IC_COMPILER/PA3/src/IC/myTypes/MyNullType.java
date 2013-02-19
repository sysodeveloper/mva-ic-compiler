package IC.myTypes;

import IC.AST.PrimitiveType;

public class MyNullType extends MyPrimitiveType {
	public MyNullType(){
		this.setName("null");
		this.setUnique_id(3);
	}
	
	@Override
	public boolean subtypeOf(MyType type) {
		if(type instanceof MyStringType){
			return true;
		}
		if(type instanceof MyNullType){
			return true;
		}
		if(type instanceof MyArrayType){
			return true;	
		}
		if(type instanceof MyClassType){
			return true;
		}
		return false;
	}
	
	public MyNullType clone(){
		MyNullType t = new MyNullType();
		t.setName(this.getName());
		t.setDimention(this.getDimention());
		t.setUnique_id(this.getUnique_id());
		return t;
	}
}
