package IC.AST;

import java.util.*;

/** Represents a state during the evaluation of a program. 
 */
public class Environment {
	/** Maps the names of variables to integer values.
	 * The same variable may appear in different VarExpr objects.  We use the
	 * name of the variable as a way of ensuring we a consistent mapping
	 * for each variable. 
	 */
	private Map<Location,Integer> m_varToValue;
	
	/** Maps the names of variables to boolean values.
	 * The same variable may appear in different VarExpr objects.  We use the
	 * name of the variable as a way of ensuring we a consistent mapping
	 * for each variable. 
	 */
	private Map<Location,Integer> m_varToBool = new HashMap<Location,Integer>();
	
	/**
	 * @return The varToValue.
	 */
	public Map<Location, Integer> getVarToValue() {
		return m_varToValue;
	}

	/**
	 * @param varToValue The varToValue to set.
	 */
	public void setVarToValue(Map<Location, Integer> varToValue) {
		m_varToValue = varToValue;
	}

	/**
	 * @return The varToBool.
	 */
	public Map<Location, Integer> getVarToBool() {
		return m_varToBool;
	}

	/**
	 * @param varToBool The varToBool to set.
	 */
	public void setVarToBool(Map<Location, Integer> varToBool) {
		m_varToBool = varToBool;
	}
	
	public Environment() {
		setVarToValue(new HashMap<Location,Integer>());
		setVarToBool(new HashMap<Location,Integer>());
	}
	
	/** Updates the value of a variable.
	 * 
	 * @param v A variable expression.
	 * @param newValue The updated value.
	 */
	public void update(Location l, int newValue) {
		getVarToValue().put(l, new Integer(newValue));
	}
	
	/** Retrieves the value of the given variable.
	 * If the variable has not been initialized an exception is thrown.
	 * 
	 * @param v A variable expression.
	 * @return The value of the given variable in this state.
	 */
	public Integer get(Location l) {
		if (!getVarToValue().containsKey(l)) {
			throw new RuntimeException("Attempt to access uninitialized variable: " + l);
		}
		return getVarToValue().get(l);
	}
}