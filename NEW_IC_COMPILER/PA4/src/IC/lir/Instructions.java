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
	
	
	public String MoveFieldLoad(String op1,String op1Ofsset, String op2){
		StringBuffer output = new StringBuffer();
		output.append("MoveField ");
		output.append(op1);
		output.append(".");
		output.append(op1Ofsset);
		output.append(",");
		output.append(op2);
		return output.toString(); 
	}
	
	public String MoveFieldStore(String op1, String op2,String op2Ofsset){
		StringBuffer output = new StringBuffer();
		output.append("MoveField ");
		output.append(op1);
		output.append(",");
		output.append(op2);
		output.append(".");
		output.append(op2Ofsset);		
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
	
	public String Mul(String op1, String reg){
		StringBuffer output = new StringBuffer();
		output.append("Mul ");
		output.append(op1);
		output.append(",");
		output.append(reg);
		return output.toString(); 
	}
	
	public String Div(String op1, String reg){
		StringBuffer output = new StringBuffer();
		output.append("Div ");
		output.append(op1);
		output.append(",");
		output.append(reg);
		return output.toString(); 
	}
	
	public String Mod(String op1, String reg){
		StringBuffer output = new StringBuffer();
		output.append("Mod ");
		output.append(op1);
		output.append(",");
		output.append(reg);
		return output.toString(); 
	}
	
	public String Inc(String reg){
		StringBuffer output = new StringBuffer();
		output.append("Inc ");
		output.append(reg);
		return output.toString(); 
	}
	
	public String Dec(String reg){
		StringBuffer output = new StringBuffer();
		output.append("Dec ");
		output.append(reg);
		return output.toString(); 
	}
	
	public String Neg(String reg){
		StringBuffer output = new StringBuffer();
		output.append("Neg ");
		output.append(reg);
		return output.toString(); 
	}
	public String Not(String reg){
		StringBuffer output = new StringBuffer();
		output.append("Not ");
		output.append(reg);
		return output.toString(); 
	}
	
	public String And(String op1, String reg){
		StringBuffer output = new StringBuffer();
		output.append("And ");
		output.append(op1);
		output.append(",");
		output.append(reg);
		return output.toString(); 
	}
	
	public String Or(String op1, String reg){
		StringBuffer output = new StringBuffer();
		output.append("Or ");
		output.append(op1);
		output.append(",");
		output.append(reg);
		return output.toString(); 
	}
	
	public String Xor(String op1, String reg){
		StringBuffer output = new StringBuffer();
		output.append("Xor ");
		output.append(op1);
		output.append(",");
		output.append(reg);
		return output.toString(); 
	}
	
	public String Compare(String op1, String reg){
		StringBuffer output = new StringBuffer();
		output.append("Compare ");
		output.append(op1);
		output.append(",");
		output.append(reg);
		return output.toString(); 
	}
	
	public String Jump(String label){
		StringBuffer output = new StringBuffer();
		output.append("Jump ");
		output.append(label);		
		return output.toString(); 
	}
}
