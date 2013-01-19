package IC.lir;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import IC.mySymbolTable.MySymbolRecord;
import IC.mySymbolTable.MySymbolRecord.Kind;
import IC.mySymbolTable.MySymbolTable;

public class LayoutsManager {
	private List<ClassLayout> classLayouts;
	//Maybe we need to save classlayouts in a map? classname -> classlayout?
	//we need for the translation to get each class it's classlayout and pass it to each accept
	//for now i've added a function that gets a class name and returns it's layout, we can keep it
	//if we want
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
					if(entry.getValue().getKind()==Kind.Virtual_Method)
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
	
	public ClassLayout getClassLayout(String className){
		for(ClassLayout cl : classLayouts){
			if(cl.getClassName().compareTo(className) == 0){
				return cl;
			}
		}
		return null;
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
