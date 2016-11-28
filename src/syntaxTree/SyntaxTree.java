package syntaxTree;

import debug.*;

/**
This abstract class is the top of the hierarchy representing the structures that make up a syntax tree.  

The idea is that the for each non-terminal in the PDef grammar there will be a subclass of this class representing the structure of the non-terminal.

STATE:

The state in this class includes a single data member

   SyntaxTreeDebug debug
   
which controls debugging printing in the syntax tree classes.  This data member is declared in this class so that it will be inherited into each subclass.
   
INTERFACE:

The interface of this class includes a display method:
    
   void printST()
   
The method, in calling 'this.toString', uses late binding to display the syntax tree starting at the node referenced by 'this'.

While there is no constructor given, Java automatically substitutes an empty constructor -- i.e.,

   SyntaxTree () { }
   
HELPER METHODS:
   
There are no helper methods.
   
CLASS INVARIANT:  

There is no class invariant
          
@author J. Mead -- September '08
*/

public abstract class SyntaxTree {
	
	protected SyntaxTreeDebug debug = new SyntaxTreeDebug();
	
	public SyntaxTree() {}

//	// for SyntaxTree.toString
//	public abstract String toString(String pad);
//
//	public String toString()
//	{
//		debug.show(">>> Entering SyntaxTree.toString(String)");
//		String val = this.toString("");
//		debug.show("<<< Leaving SyntaxTree.toString(String)");
//
//		return val;
//	}
    
//    public abstract void traverseST();

}
