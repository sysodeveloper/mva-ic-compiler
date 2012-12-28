/**
 * 
 */
package IC.AST;

import IC.SymbolTable;

/**
 * Indicate a Symbol table container.
 *
 */
public interface SymbolTableContainer {
	/**
	 * @return The inner table.
	 */
	public SymbolTable getInnerTable();

	/**
	 * @param table The inner table to set.
	 */
	public void setInnerTable(SymbolTable table);
}
