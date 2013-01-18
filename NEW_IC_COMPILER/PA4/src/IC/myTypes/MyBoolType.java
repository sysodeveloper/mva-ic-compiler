package IC.myTypes;

public class MyBoolType extends MyPrimitiveType {

	
	
	public  MyBoolType(){
		this.setName("boolean");
		this.setUnique_id(2);
	}
	public MyBoolType clone(){
		MyBoolType t = new MyBoolType();
		t.setName(this.getName());
		t.setDimention(this.getDimention());
		t.setUnique_id(this.getUnique_id());
		return t;
	}
	

	

}
