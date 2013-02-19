package IC.lir;

import IC.AST.ASTNode;

public class DownType {
	private ClassLayout currentClassLayout;
	private boolean loadOrStore; // 0 - load , 1 - store
	private ASTNode prevNode;
	private int nextFreeRegister;
	
	public DownType(ClassLayout currentClass,boolean loadOrStore /*0-load,1-store*/,ASTNode prevNode /*optional*/,int  nextFreeRegister){
		currentClassLayout = currentClass;
		this.loadOrStore = loadOrStore;
		this.prevNode = prevNode;
		this.nextFreeRegister = nextFreeRegister;
	}
	
	private String registerDescription(int registerNumber){
		return "R"+registerNumber;
	}
	
	public String nextRegister(){
		String next = registerDescription(nextFreeRegister++);
		return next;
	}
	
	public void freeRegister(){
		nextFreeRegister--;
	}
	
	public void freeRegisters(int numToFree){
		nextFreeRegister-=numToFree;
	}
		
}
