package IC;

public class MyNullType extends MyPrimitiveType {
	public MyNullType(){
		this.setName("null");
		this.setUnique_id(3);
	}
	
	@Override
	boolean subtypeOf(MyType type) {
		// TODO Auto-generated method stub
		return true;
	}
	
	public MyNullType clone(){
		MyNullType t = new MyNullType();
		t.setName(this.getName());
		t.setDimention(this.getDimention());
		t.setUnique_id(this.getUnique_id());
		return t;
	}
}
