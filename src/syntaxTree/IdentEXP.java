package syntaxTree;

import tokenizer.*;

/**
 * Created by Kate on 10/26/2016.
 */
public class IdentEXP extends ExpressionST {

    private Token tok1;

    public IdentEXP(Token t1) { tok1 = t1; }

    public String toString()
    {
        debug.show(">>> Entering IdentEXP.toString(String)");
        String val = "";
        val += tok1.getName();
        debug.show("<<< Leaving IdentEXP.toString(String)");
        return val;
    }
}
