package syntaxTree;

import tokenizer.*;
import java.util.*;
import java.beans.Expression;

/**
 * Created by Kate on 10/26/2016.
 */
public class expEXP extends ExpressionST {
    private ExpressionST exp1;
    private Token tok1;
    private ExpressionST exp2;

    public expEXP(ExpressionST e1, Token t1, ExpressionST e2) { exp1 = e1; tok1 = t1; exp2 = e2; }

    public String toString()
    {
        debug.show(">>> Entering IdentEXP.toString(String)");
        String val = "";
        val += exp1.toString();
        val += tok1.getName();
        val += exp2.toString();
        debug.show("<<< Leaving IdentEXP.toString(String)");
        return val;
    }

    public void traverseST() { System.out.println("expEXP");}
}
