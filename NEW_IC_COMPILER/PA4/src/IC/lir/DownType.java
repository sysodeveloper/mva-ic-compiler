package IC.lir;

import java.util.ArrayList;
import java.util.List;

import IC.AST.ASTNode;

public class DownType {
	public ClassLayout currentClassLayout;
	public boolean loadOrStore; // 0 - load , 1 - store
	public ASTNode prevNode;
	public String downRegister;
	public boolean resultInRegister;	//indicates whether the result must be stored in register
	//private int nextFreeRegister;
	private RegisterManager regManager;
	
	public DownType(ClassLayout currentClass,boolean loadOrStore /*0-load,1-store*/,ASTNode prevNode /*optional*/,int  nextFreeRegister){
		currentClassLayout = currentClass;
		this.loadOrStore = loadOrStore;
		this.prevNode = prevNode;
		this.regManager = new RegisterManager();
		this.downRegister = null;
		this.resultInRegister = false;
	}
	
	//private String registerDescription(int registerNumber){
	//	return "R"+registerNumber;
	//}
	
	public String nextRegister(){
		//usedList.add(nextFreeRegister);
		//String next = registerDescription(nextFreeRegister++);
		return regManager.nextRegister();
	}
	
	public void freeRegister(String register){
		regManager.freeRegister(register);
	}
	
	public void freeRegister(){
		regManager.freeRegisters();
	}
	
	public void startScope(){
		regManager.addAllocator();
	}
	
	public void endScope(){
		//regManager.freeRegisters();
		regManager.freeAll();
	}
	
//	public void freeRegisters(int numToFree){
		//nextFreeRegister-=numToFree;
//	}
		
}
