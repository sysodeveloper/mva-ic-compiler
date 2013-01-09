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
}
