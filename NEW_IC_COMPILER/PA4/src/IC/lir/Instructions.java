package IC.lir;

public class Instructions {
	public Instructions(){
		
	}
	
	public String Move(String op1, String op2){
		StringBuffer output = new StringBuffer();
		output.append("Move ");
		output.append(op1);
		output.append(",");
		output.append(op2);
		return output.toString(); 
	}
	
	public String MoveArray(String op1, String op2){
		StringBuffer output = new StringBuffer();
		output.append("MoveArray ");
		output.append(op1);
		output.append(",");
		output.append(op2);
		return output.toString(); 
	}
	
	public String MoveArray(String op1,String op1Offset, String op2){
		StringBuffer output = new StringBuffer();
		output.append("MoveArray ");
		output.append(op1);
		output.append("["+op1Offset+"]");
		output.append(",");
		output.append(op2);
		return output.toString(); 
	}	
	
	
	public String MoveField(String op1, String op2){
		StringBuffer output = new StringBuffer();
		output.append("MoveField ");
		output.append(op1);
		output.append(",");
		output.append(op2);
		return output.toString(); 
	}
	
	public String ArrayLength(String op1, String op2){
		StringBuffer output = new StringBuffer();
		output.append("ArrayLength ");
		output.append(op1);
		output.append(",");
		output.append(op2);
		return output.toString(); 
	}
	
	public String Add(String op1, String reg){
		StringBuffer output = new StringBuffer();
		output.append("Add ");
		output.append(op1);
		output.append(",");
		output.append(reg);
		return output.toString(); 
	}
	
	public String Sub(String op1, String reg){
		StringBuffer output = new StringBuffer();
		output.append("Sub ");
		output.append(op1);
		output.append(",");
		output.append(reg);
		return output.toString(); 
	}
}
