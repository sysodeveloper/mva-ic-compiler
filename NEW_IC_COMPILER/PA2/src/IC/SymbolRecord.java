package IC;

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
		FORMAL,
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
	 * The parameters for method.
	 */
	private List<Formal> m_parameters;
	/**
	 * The type of the symbol or return type of a function.
	 */
	private Type m_type;
	/**
	 * Additional properties.
	 */
	private List<Object> m_properties;
	
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
	 * @return The parameters.
	 */
	public List<Formal> getParameters() {
		return m_parameters;
	}
	/**
	 * @param m_parameters The m_parameters to set.
	 */
	public void setParameters(List<Formal> parameters) {
		m_parameters = parameters;
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
	
	/**
	 * The main constructor.
	 * @param id The id of the table.
	 * @param table The table contains the record.
	 * @param symbol The symbol name.
	 * @param kind The kind of the record (Field, Method, Class).
	 * @param type The type if field or return type for method.
	 * @param properties Additionally properties.
	 * @param parameters The parameters type (if method).
	 */
	public SymbolRecord(int id, SymbolTable table, String symbol, Kind kind,
			Type type, List<Object> properties, 
			List<Formal> parameters) {
		setId(id);
		setTable(table);
		setSymbol(symbol);
		setKind(kind);
		setParameters(parameters);
		setType(type);
		setProperties(properties);
	}
	
	/**
	 * The constructor with no parameters.
	 * @param id The id of the table.
	 * @param table The table contains the record.
	 * @param symbol The symbol name.
	 * @param kind The kind of the record (Field, Method, Class).
	 * @param type The type if field or return type for method.
	 * @param properties Additionally properties.
	 */
	public SymbolRecord(int id, SymbolTable table, String symbol, Kind kind,
			Type type, List<Object> properties) {
		this(id, table, symbol, kind, type, properties, null);
	}
	
	/**
	 * The constructor for field (with no properties and no parameters).
	 * @param id The id of the table.
	 * @param table The table contains the record.
	 * @param symbol The symbol name.
	 * @param kind The kind of the record (Field, Method, Class).
	 * @param type The type if field or return type for method.
	 */
	public SymbolRecord(int id, SymbolTable table, String symbol, Kind kind, Type type) {
		this(id, table, symbol, kind, type, null, null);
	}
}
