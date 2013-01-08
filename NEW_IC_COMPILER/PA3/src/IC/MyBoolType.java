package IC;

public class MyBoolType extends MyPrimitiveType {

	@Override
	boolean subtypeOf(MyType type) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public  MyBoolType(){
		this.setName("boolean");
	}

	

	

}
