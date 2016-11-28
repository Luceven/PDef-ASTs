package syntaxTree;

import tokenizer.*;
import java.util.*;

/**
 * Created by Kate on 10/24/2016.
 */
public class DeclarationST extends StmtST {
    //Grammar rule: Declaration -> typeT identT
    //Class invariant: tok1.getType() == typeT AND
    //                 tok2.getType() == identT
    private Token tok1;
    private Token tok2;

    public DeclarationST(Token t1, Token t2)
    //Pre: t1.getType() == tok1T AND
    //     t2.getType() == tok2T
    //Post: tok1 = t1 AND tok2 = t2
    {
        tok1 = t1;
        tok2 = t2;
    }

    public String toString(String pad)
    //Pre: pad is a string consisting of a multiple of 4 blanks
    {
        debug.show(">>> Entering DeclarationST.toString(String)");
        String val = pad;
        val += "Declaration: [ type " + tok1.getName();
        val += ", name " + tok2.getName() + " ]\n";
        debug.show("<<< Leaving DeclarationST.toString(String)");
        return val;
    }

    public void traverseST() {
        System.out.println("DeclarationST");
    }
}