package IC.lir;

import IC.AST.ASTNode;

public class DownType {
	private ClassLayout currentClassLayout;
	private boolean loadOrStore; // 0 - load , 1 - store
	private ASTNode prevNode;
	
	public DownType(ClassLayout currentClass,boolean loadOrStore /*0-load,1-store*/,ASTNode prevNode /*optional*/ ){
		currentClassLayout = currentClass;
		this.loadOrStore = loadOrStore;
		this.prevNode = prevNode;
	}
}
