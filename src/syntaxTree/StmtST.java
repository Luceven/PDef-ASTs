package syntaxTree;

import debug.*;

/**
This abstract class is the represents objects that represent one of the three statement forms.  

STATE:

This class has no state
   
INTERFACE:

This class has no methods and no constructor
   
HELPER METHODS:
   
There are no helper methods.
   
CLASS INVARIANT:  

There is no class invariant
          
@author J. Mead -- September '08
*/

public abstract class StmtST extends SyntaxTree {
    public abstract String toString(String pad);

    public String toString()
    {
        debug.show(">>> Entering SyntaxTree.toString(String)");
        String val = this.toString("");
        debug.show("<<< Leaving SyntaxTree.toString(String)");

        return val;
    }

    public abstract void traverseST();
}
