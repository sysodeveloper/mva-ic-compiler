package IC;

import IC.AST.ASTNode;
import IC.AST.Formal;
import IC.AST.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Single record from the symbol table of one scope.
 *
 */
public class SymbolRecord {
	/**
	 * The kind of records.
	 *
	 */
	public enum Kind {
		FIELD,
		PARAMETER,
		STATIC_METHOD,
		VIRTUAL_METHOD,
		LIBRARY_METHOD,
		CLASS, 
		LOCAL_VARIABLE
	}
	
	/**
	 * The id of the record.
	 */
	private int m_id;
	
	/**
	 * Get the table contains the record.
	 */
	private SymbolTable m_table;

	/**
	 * The symbol name.
	 */
	private String m_symbol;
	/**
	 * The kind of the record. (Field, Method, class).
	 */
	private Kind m_kind;

	/**
	 * The type of the symbol.
	 */
	private Type m_type;
	
	/**
	 * Additional properties.
	 */
	private List<Object> m_properties;
	
	/**
	 * The node of the record.
	 */
	private ASTNode m_node;
	
	/**
	 * Flag which indicates whether the symbol has been declared previously in the program
	 * Used in semantic checks 
	 */
	private boolean declared = false;
	
	/**
	 * @return The id.
	 */
	public int getId() {
		return m_id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(int id) {
		m_id = id;
	}
	
	/**
	 * @return the node
	 */
	public ASTNode getNode() {
		return m_node;
	}
	/**
	 * @param m_node the node to set
	 */
	public void setNode(ASTNode m_node) {
		this.m_node = m_node;
	}
	
	/**
	 * @return The symbol.
	 */
	public String getSymbol() {
		return m_symbol;
	}
	/**
	 * @param symbol The symbol to set.
	 */
	public void setSymbol(String symbol) {
		m_symbol = symbol;
	}
	/**
	 * @return The kind.
	 */
	public Kind getKind() {
		return m_kind;
	}
	/**
	 * @param kind The kind to set.
	 */
	public void setKind(Kind kind) {
		m_kind = kind;
	}
	
	/**
	 * @return The type.
	 */
	public Type getType() {
		return m_type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(Type type) {
		m_type = type;
	}
	/**
	 * @return The properties.
	 */
	public List<Object> getProperties() {
		return m_properties;
	}
	/**
	 * @param properties The properties to set.
	 */
	public void setProperties(List<Object> properties) {
		m_properties = properties;
	}
	
	/**
	 * @return The m_table.
	 */
	public SymbolTable getTable() {
		return m_table;
	}
	/**
	 * @param table The table to set.
	 */
	public void setTable(SymbolTable table) {
		m_table = table;
	}
	
	public boolean is_declared(){
		return this.declared;
	}
	
	public void declared(){
		this.declared = true;
	}
	
	/**
	 * The main constructor.
	 * @param id The id of the table.
	 * @param node The node of the record.
	 * @param table The table contains the record.
	 * @param symbol The symbol name.
	 * @param kind The kind of the record 
	 *   (Field, Static | Virtual | Library Method, 
	 *   Class, Local Variable, Parameter).
	 * @param type The type.
	 * @param properties Additionally properties.
	 */
	public SymbolRecord(int id, ASTNode node, SymbolTable table, 
			String symbol, Kind kind,
			Type type, List<Object> properties) {
		setId(id);
		setNode(node);
		setTable(table);
		setSymbol(symbol);
		setKind(kind);
		setType(type);
		setProperties(properties);
	}
	
	/**
	 * The constructor for field (with no properties and no parameters).
	 * @param id The id of the table.
	 * @param node The node of the record.
	 * @param table The table contains the record.
	 * @param symbol The symbol name.
	 * @param kind The kind of the record (Field, Method, Class).
	 * @param type The type.
	 */
	public SymbolRecord(int id, ASTNode node, SymbolTable table, 
			String symbol, Kind kind, Type type) {
		this(id, node, table, symbol, kind, type, null);
		this.declared = true;
	}
}
