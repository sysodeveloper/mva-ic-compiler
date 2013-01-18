package IC.lir;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import IC.mySymbolTable.MySymbolRecord;
import IC.mySymbolTable.MySymbolRecord.Kind;
import IC.mySymbolTable.MySymbolTable;

public class LayoutsManager {
	private List<ClassLayout> classLayouts;
	
	public LayoutsManager(){
		classLayouts = new ArrayList<ClassLayout>();
	}
	
	
	public void createClassLayouts(ClassLayout superClassLayout, MySymbolTable table ){
		for(MySymbolTable child : table.getChildren()){			
			
			if(table.Lookup(child.getDescription()).getKind() == Kind.Class){
				ClassLayout newClass = new ClassLayout(child.getDescription(),superClassLayout);				
				
				for(Entry<String,MySymbolRecord> entry : child.getEntries().entrySet()){
					if(entry.getValue().getKind()==Kind.Field)
						newClass.addField(entry.getKey());
					else
						newClass.addMethod(entry.getKey());
				}
				
				classLayouts.add(newClass);
				createClassLayouts(newClass,child);
			}
		}
		
	}


	public List<ClassLayout> getClassLayouts() {
		return classLayouts;
	}
	
	public void printLayouts(){
		for(ClassLayout c : classLayouts){
			System.out.println("---- class name "+c.getClassName()+" -----");
			for(Entry<String,Integer> f : c.getFieldToOffset().entrySet()){
				System.out.println("  field name "+f.getKey()+" offset "+f.getValue());
			}
			for(Entry<String,Integer> m : c.getMethodToOffset().entrySet()){
				System.out.println("  method name "+m.getKey()+" offset "+m.getValue());
			}
		}
	}
}
