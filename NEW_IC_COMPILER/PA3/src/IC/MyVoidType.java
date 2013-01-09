package IC;

public class MyVoidType extends MyPrimitiveType {

		
	public MyVoidType(){
		this.setName("void");
		this.setUnique_id(5);
	}
	
	@Override
	boolean subtypeOf(MyType type) {
		// TODO Auto-generated method stub
		return false;
	}
}
