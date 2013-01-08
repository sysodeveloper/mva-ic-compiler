package IC;

public class MyVoidType extends MyPrimitiveType {

	@Override
	boolean subtypeOf(MyType type) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public MyVoidType(){
		this.setName("void");
	}

}
