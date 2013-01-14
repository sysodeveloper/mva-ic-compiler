package IC.AST;

import IC.DataTypes;
import IC.LiteralTypes;
import IC.mySymbolTable.*;
import IC.myTypes.*;
import IC.semanticChecks.*;

/**
 * Literal value AST node.
 * 
 * @author Tovi Almozlino
 */
public class Literal extends Expression {

	private LiteralTypes type;

	private Object value;

	public <UpType> UpType accept(Visitor<UpType> visitor) {
		return visitor.visit(this);
	}
	
	public <DownType, UpType> UpType accept(
			PropagatingVisitor<DownType, UpType> visitor, DownType d) {
		return visitor.visit(this, d);
	}

	/**
	 * Constructs a new literal node.
	 * 
	 * @param line
	 *            Line number of the literal.
	 * @param type
	 *            Literal type.
	 */
	public Literal(int line, LiteralTypes type) {
		super(line);
		this.type = type;
		value = type.getValue();
	}

	/**
	 * Constructs a new literal node, with a value.
	 * 
	 * @param line
	 *            Line number of the literal.
	 * @param type
	 *            Literal type.
	 * @param value
	 *            Value of literal.
	 */
	public Literal(int line, LiteralTypes type, Object value) {
		this(line, type);
		this.value = value;
	}

	public LiteralTypes getType() {
		return type;
	}
	
	public Type getICType() {
		switch(type) {
		case FALSE:
			return new PrimitiveType(line, DataTypes.BOOLEAN);
		case INTEGER:
			return new PrimitiveType(line, DataTypes.INT);
		case NULL:
			return new PrimitiveType(line, null);
		case STRING:
			return new PrimitiveType(line, DataTypes.STRING);
		case TRUE:
			return new PrimitiveType(line, DataTypes.BOOLEAN);
		}
		return null;
	}

	public Object getValue() {
		return value;
	}
	
	public MyType getMyType(){
		switch(type){
		case FALSE:
			return new MyBoolType();
		case TRUE:
			return new MyBoolType();
		case INTEGER:
			return new MyIntType();
		case NULL:
			return new MyNullType();
		case STRING:
			return new MyStringType();
		}
		return null;
	}

}
