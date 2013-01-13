package IC.myTypes;

public class MyVoidType extends MyPrimitiveType {

		
	public MyVoidType(){
		this.setName("void");
		this.setUnique_id(5);
	}
	
	@Override
	public boolean subtypeOf(MyType type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public MyPrimitiveType clone() {
		// TODO Auto-generated method stub
		MyVoidType t = new MyVoidType();
		t.setDimention(getDimention());
		t.setName(getName());
		t.setUnique_id(getUnique_id());
		return t;
	}
}
