package parser;

import java.util.*;
import syntaxTree.*;
import exceptions.*;
import tokenizer.*;
import debug.*;

/**
This class defines a recursive descent parser for the language PDef-light with context free grammar as follows:

    Program     --> Block
    Block       --> lcbT StmtList rcbT
    StmtList    --> Stmt { commaT Stmt }
    Stmt        --> Declaration | Assignment | Block
    Declaration --> typeT identT
    Assignment  --> identT assignT identT
    
In this grammar the terminals lcbT, rcbT, commaT, identT, typeT, assignT represent tokens in the lexical description for PDef-light.

STATE:

The state of an object includes a variable currentToken holding the last read token from the input stream (received from the tokenizer via the method getNextToken) and the input stream itself tokenStream -- in addition there is a object debug to control the display of debug output (see class Debug for information):

   ParserDebug debug
   TokenType currentToken
   Tokenizer tokenStream
   
INTERFACE:

The interface of this class includes the constructor and the parse method for the non-terminal (Program) that acts as the start symbol for the grammar:
    
   Parser(Tokenizer tokenStream)  
   void parseProgram()
   
HELPER METHODS:
   
The Helper methods in this class include a parse method for each of the grammar non-terminals (other than Program):

   void parseBlock()
   void parseStmtList()
   void parseStmt()
   void parseDeclaration()
   void parseAssignmentment()
   
There are two additional helper methods:

   void error() 
        called when a parse error is detected -- it displays a generic
        error message and then halts the program (calls System.exit)
   void consume(Token.TokenType type)
        this method checks that the value of currentToken matches 
        the argument type and if it does reads a new value for
        currentToken -- if it doesn't match a call to error() is made
   
CLASS INVARIANT:  

The class has an invariant which specifies requirements for the parse methods relative to the value of the state variable currentToken and the input stream tokenStream.

    Class Invariant: 
          When a parse function is called the next 
          token to be considered is already read 
          into currentToken.
          
@author J. Mead -- July '08
*/


public class Parser {

    // Class Invariant: 
    //       When a parse function is called the next 
    //       token to be considered is already read 
    //       into currentToken
    
    // State

    ParserDebug debug;      // control display of debug output
    Token currentToken;     // value of next token to be processed
    Tokenizer tokenStream;  // reference to the input token stream

    // Interface
    
    // Constructor
    public Parser(Tokenizer tokenStream)  
    // Pre:  tokenStream has a value
    // Post: debug == new ParserDebug() AND 
    //       this.tokenStream == tokenStream AND
    //       class invariant is true
    {  
        this.debug = new ParserDebug(); 
        this.tokenStream = tokenStream;

        currentToken = tokenStream.getNextToken();
             // This makes the class invariant true
             // for the call to parseProgram
    }

    public BlockST parseProgram() throws ParseException
    // Grammar Rule:  Program --> Block eofT
    {		
        debug.show(">>> Entering parseProgram");

        BlockST block = parseBlock();
        consume(Token.TokenType.EOT_T);

        debug.show("<<< Leaving parseProgram");
        return block;
    }


    private BlockST parseBlock() throws ParseException
    // Grammar Rule:  Block --> lcbT Block rcbT
    {
        debug.show(">>> Entering parseBlock");

        consume(Token.TokenType.LCB_T);
        LinkedList<StmtST> list = parseStmtList();
        consume(Token.TokenType.RCB_T);
        
        debug.show("<<< Leaving parseBlock");
        return new BlockST(list);
    }
    
    
    private LinkedList<StmtST> parseStmtList()  throws ParseException
    // Grammar Rule: Block --> Stmt { commaT Stmt }
    {
        debug.show(">>> Entering parseStmtList");
        
        LinkedList<StmtST> list = new LinkedList<StmtST>();
        StmtST stmt = parseStmt();
        list.addLast(stmt);
        while (currentToken.getType() == Token.TokenType.COMMA_T) {
            consume(Token.TokenType.COMMA_T);
            stmt = parseStmt();
            list.addLast(stmt);
        }

        debug.show("<<< Leaving parseStmtList");
        return list;
    }


    private StmtST parseStmt() throws ParseException
    // Grammar Rule:  Stmt --> Declaration
    //                     --> Assignment
    //                     --> Block
    {
        debug.show(">>> Entering parseStmt");

        StmtST stmt = null;
        switch (currentToken.getType()) {
        case IDENT_T: // parseAssignment checks for IDENT_T
            stmt = parseAssignment();
            break;
        case TYPE_T: // parseDeclaration checks for TYPE_T
            stmt = parseDeclaration();
            break;
        case LCB_T: // parseBlock checks for LCB_T
            stmt = parseBlock();
            break;
        default:
            throw new ParseException
                        ("Expected type, ident, or left brace", 
                         currentToken);
        }
        debug.show("<<< Leaving parseStmt");
        return stmt;
    }


    private AssignmentST parseAssignment() throws ParseException
    // Grammar Rule:  Assignment --> identT assignT expEXP
    {
        debug.show(">>> Entering parseAssignment");

        Token t1 = currentToken;
        consume(Token.TokenType.IDENT_T);
        Token t2 = currentToken;
        consume(Token.TokenType.ASSIGN_T);
        ExpressionST exp1 = parseExp(); // uncomment this for PDef part of the lab
        
        debug.show("<<< Leaving parseAssignment");
        return new AssignmentST(t1, t2, exp1);
    }


    private DeclarationST parseDeclaration() throws ParseException
    // Grammar Rule:  Declaration --> typeT identT
    {
        debug.show(">>> Entering parseDeclaration");

        Token t1 = currentToken;
        consume(Token.TokenType.TYPE_T);
        Token t2 = currentToken;
        consume(Token.TokenType.IDENT_T);

        debug.show("<<< Leaving parseDeclaration");

        return new DeclarationST(t1, t2);
    } 
    
    
    private ExpressionST parseExp() throws ParseException
    // Grammar Rule: Exp --> Term RestExp
    {
        debug.show(">>> Entering parseExp");

        ExpressionST elt = parseTerm();
        ExpressionST list = parseRestExp(elt);

        debug.show("<<< Leaving parseExp");

        return list;
    } 
    
    
    // private void parseRestExp( ExpressionST exp ) throws ParseException // for PDef
    private ExpressionST parseRestExp(ExpressionST x) throws ParseException
    // Grammar Rule: RestExp --> (addT | subT) Term RestExp
    {
        debug.show(">>> Entering parseRestExp");

        Token tok = null;
        ExpressionST elt = null;

        if (currentToken.getType() == Token.TokenType.ADD_T || 
            currentToken.getType() == Token.TokenType.SUB_T) 
        {
            tok = currentToken;
            consume(currentToken.getType());
            elt = parseTerm();
            elt = new expEXP(x, tok, elt);
           elt = parseRestExp(elt);
        }
        else {
            elt = x;
        }

        debug.show("<<< Leaving parseRestExp");
        return elt;
    } 
    
    
    private ExpressionST parseTerm() throws ParseException
    // Grammar Rule: Term --> Factor RestTerm
    {
        debug.show(">>> Entering parseTerm");

        ExpressionST elt = parseFactor();
        ExpressionST list = parseRestTerm(elt);

        debug.show("<<< Leaving parseTerm");
        return list;
    } 
    
    
    // private ExpressionST parseRestTerm(ExpressionST x) throws ParseException // for PDef
    private ExpressionST parseRestTerm(ExpressionST x) throws ParseException
    // Grammar Rule: RestTerm --> 
    //                   (mulT | divT | modT) Factor RestTerm
    {
        debug.show(">>> Entering parseRestTerm");
        ExpressionST elt = null;
        Token tok = null;
        
        if (currentToken.getType() == Token.TokenType.MUL_T || 
            currentToken.getType() == Token.TokenType.DIV_T ||
            currentToken.getType() == Token.TokenType.MOD_T) 
        {
            tok = currentToken;
           consume(currentToken.getType());
            elt = parseFactor();
            elt = new expEXP(x, tok, elt);
            elt = parseRestTerm(elt);
        }
        else {
            elt = x;
        }

        debug.show("<<< Leaving parseRestTerm");
        return elt;
    }

    private ExpressionST parseFactor() throws ParseException
    // Grammar Rule: Factor --> identT | intT | floatT
    //                      --> lpT Exp rpT
    {
       debug.show(">>> Entering parseFactor");
        ExpressionST value = null;

       switch(currentToken.getType()) {
       case IDENT_T:
           value = new IdentEXP(currentToken);
               consume(Token.TokenType.IDENT_T);
               break;
       case INT_T:
           value = new IntEXP(currentToken);
               consume(Token.TokenType.INT_T);
               break;
       case FLOAT_T:
           value = new FloatEXP(currentToken);
               consume(Token.TokenType.FLOAT_T);
               break;
       case LP_T:
               consume(Token.TokenType.LP_T);
               value = parseExp();
               consume(Token.TokenType.RP_T);
               break;
       default:
               throw new ParseException
                     ("Expected ident, int, float, or left paren", 
                       currentToken);
       }

       debug.show("<<< Leaving parseFactor");
        return value;
    }


    private void consume(Token.TokenType type) throws ParseException
    {
        if (currentToken.getType() != type) 
           throw new ParseException("Expected " + type, 
                                    currentToken);
        currentToken = tokenStream.getNextToken();
     }

}
