package syntaxTree;

import tokenizer.*;
import java.util.*;

/**
 * Created by Kate on 10/24/2016.
 */
public class AssignmentST extends StmtST {
    //Grammar rule: Assignment -> identT assignT identT
    //Class invariant: tok1.getType() == identT AND
    //                 tok2.getType() == assignT AND
    //                 tok3.getType() == identT
    private Token tok1;
    private Token tok2;
    private ExpressionST exp1;

    public AssignmentST(Token t1, Token t2, ExpressionST e1)
    //Pre: t1.getType() == tok1T AND
    //     t2.getType() == tok2T AND
    //     t3.getType() == tok3T
    //Post: tok1 = t1 AND tok2 = t2 AND tok3 = t3
    {
        tok1 = t1;
        tok2 = t2;
        exp1 = e1;
    }

    // for AssignmentST.toString
    public String toString(String pad)
    //Pre: pad is a string consisting of a multiple of 4 blanks
    {
        debug.show(">>> Entering AssignmentST.toString(String)");
        String val = pad;
        val += "Assignment: [ target " + tok1.getName();
        val += ", source " + exp1.toString() + " ]\n";

        debug.show("<<< Leaving AssignmentST.toString(String)");
        return val;
    }

    public void traverseST() {
        System.out.println("AssignmentST");
    }
}
