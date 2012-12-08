package IC;

import java.util.List;

import java_cup.runtime.Symbol;
import IC.AST.PrettyPrinter;
import IC.AST.Program;
import IC.Parser.Lexer;
import IC.Parser.Parser;

/**
 * Manage all the Semantic Analyzation phase.
 *
 */
public class SemanticAnalyse {
	/**
	 * The singleton instance.
	 */
	private static SemanticAnalyse m_instance = null;
	
	/**
	 * The internal root reference.
	 */
	private Program m_root;
	
	/**
	 * The errors encountered in the semantic phase.
	 */
	private List<Exception> m_errors;
	
	public static SemanticAnalyse getInstance() {
		if(m_instance == null) {
			m_instance = new SemanticAnalyse();
		}
		
		return m_instance;
	}
	
	/**
	 * @return The errors.
	 */
	public List<Exception> getErrors() {
		return m_errors;
	}

	/**
	 * @param errors The errors to set.
	 */
	public void setErrors(List<Exception> errors) {
		m_errors = errors;
	}

	/**
	 * @return the root.
	 */
	public Program getRoot() {
		return m_root;
	}

	/**
	 * @param m_root the root to set
	 */
	public void setRoot(Program root) {
		m_root = root;
	}
	
	/**
	 * Create the analyzer.
	 */
	private SemanticAnalyse() {
	}
	
	/**
	 * Start the semantic analyze process.
	 */
	public void analyze() {
		/*
		 // TODO: Check breaks. + type checking. +transformations + scope +sym table
			// assign check + undefine check. redecleration. scope checks.
			 
			  Construct and initialize global type table
				Construct class hierarchy and verify the hierarchy is  tree
				Phase 1: Symbol table construction
					Assign enclosing-scope for each AST node
				Phase 2: Scope checking
					Resolve names
					Check scope rules using symbol table
				Phase 3: Type checking
					Assign type for each AST node
				Phase 4: Remaining semantic checks
				
			  
			 variable must be declared before being used
			A variable should not be declared multiple times
			A variable should be initialized before being used
			Non-void method should contain return statement along all execution paths
			break/continue statements allowed only in loops
			this keyword cannot be used in static method
			main method should have specific signature
			semantic rules
			In an assignment, RHS and LHS must have the same type
			The type of a conditional test expression must be Boolean
			
			forward declarations
			Scope 
				Statement block
				Method body
				Class body
				Module / package / file
				Whole program (multiple modules)
			
			// TODO: bonux 2 - Check decleration statements after if, while..
			  
			  Each class has a static scope and an instance
				scope.

		 */
		
		// Phase 1: Symbol table construction.
		SymbolTable global = new SymbolTable(SymbolTable.getNextId());
		getRoot().accept(global);		
		// Phase 2: Scope checking.
		// Phase 3: Type checking.
		// Phase 4: Remaining semantic checks
	}
}