import java.util.Stack;

public class SyntaxAnalyzer
{
    private final LexicalAnalyzer lex;

    public SyntaxAnalyzer(String filename) throws SyntaxErrorException
    {
        lex = new LexicalAnalyzer(filename);
        compileWhile();
    }

    private void compileWhile() throws SyntaxErrorException
    {
        match("while");
        match("(");
        compileCondition();
        match(")");
    }

    private void compileCondition() throws SyntaxErrorException
    {
        if(lex.currentLexeme().equals("!"))
        {
            lex.next();
            if(lex.getTokenType().equals("boolean"))
            {
                lex.next();
            }
            else
            {
                if(lex.currentLexeme().equals("("))
                {
                    lex.next();
                    compileBooleanExpression();
                    match(")");
                }
                else
                {
                    throw new SyntaxErrorException("Cannot apply ! operator to " + lex.getTokenType());
                }
            }
        }
        else if(lex.getTokenType().equals("boolean"))
        {
            lex.next();
        }
        else
        {
            if(lex.currentLexeme().equals("("))
            {
                lex.next();
                compileBooleanExpression();
                match(")");
            }
            else
            {
                compileBooleanExpression();
            }
        }
        if(lex.getTokenType().equals("logical operator"))
        {
            lex.next();
            compileCondition();
        }
    }

    private void compileBooleanExpression() throws SyntaxErrorException
    {
        compileExpression();
            if(lex.currentLexeme().equals(")"))
            {
                throw new SyntaxErrorException("This expression does not evaluate to boolean");
            }
        if(!lex.getTokenType().equals("boolean operator"))
        {
            throw new SyntaxErrorException("Expected a boolean operator but got " + lex.currentLexeme());
        }
        lex.next();
        compileExpression();
    }

    private void compileExpression() throws SyntaxErrorException
    {
//        if(lex.currentLexeme().equals("("))
//        {
//            lex.next();
//            compileExpression();
//            match(")");
//        }
        compileTerm();
        while(lex.getTokenType().equals("numerical operator"))
        {
            lex.next();
            compileTerm();
        }
    }

    private void compileTerm() throws SyntaxErrorException
    {
        String type = lex.getTokenType();

        if(type.equals("int") || type.equals("char") || type.equals("double"))
        {
            lex.next();
        }
        else if(lex.currentLexeme().equals("("))
        {
            lex.next();
            compileExpression();
            match(")");
        }
    }


    private void compileNumericalExpression()
    {
        do
        {
            String left = lex.currentLexeme();
            lex.next();
            String op = lex.currentLexeme();
            lex.next();

        }while(lex.lookAhead(1).matches("\\+ | - | * | /"));

    }


    private void match(String word) throws  SyntaxErrorException
    {
        if(!lex.currentLexeme().equals(word))
        {
            throw new SyntaxErrorException(" Expecting " + word + " but the input is " + lex.currentLexeme());
        }
        lex.next();
    }

    private boolean isDatatype()
    {
        String currentLex = lex.currentLexeme();
        return currentLex.equals("int") || currentLex.equals("double") || currentLex.equals("String") || currentLex.equals("char")
                || currentLex.equals("boolean");
    }

    private boolean isNumericalExpression()
    {
       String nextTokenType = lex.getNextTokenType(1);
       String currTokenType = lex.getTokenType();

       if(nextTokenType.equals("numerical operator"))
       {
           if(currTokenType.equals("int") || currTokenType.equals("double"))
           {
                return true;
           }
           else if(currTokenType.equals("identifier"))
           {
               if(isIdNumber())
               {
                    return true;
               }
           }
       }
       else if(currTokenType.equals("numerical operator"))
       {

       }
       return false;
    }

    private boolean isBooleanExpression()
    {
        return false;
    }

    private boolean isIdNumber()
    {
        String type = lex.getIdentifierDataType(lex.currentLexeme());
        return type.equals("int") || type.equals("double");
    }


}
