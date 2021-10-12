import java.util.Stack;

public class SyntaxAnalyzer
{
    private final LexicalAnalyzer lex;

    public SyntaxAnalyzer(String filename) throws SyntaxErrorException
    {
        lex = new LexicalAnalyzer(filename);
    }

    public void compileWhile() throws SyntaxErrorException
    {
        match("while");
        match("(");
        compileCondition();
        match(")");
        if(lex.hasMoreLexeme())
        {
            if(lex.contains("{"))
            {
                if(!lex.contains("}"))
                {
                    throw new SyntaxErrorException("} is expected");
                }
            }
            System.out.println("Valid while loop statement");
        }
        else
        {
            throw new SyntaxErrorException("Statement expected");
        }
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
                boolean isCondition = compileExpression();
                if(!isCondition)
                {
                    throw new SyntaxErrorException("Cannot apply ! operator to " + lex.getTokenType());
                }
            }
        }
        else
        {
            boolean isCondition = compileExpression();
            if(!isCondition)
            {
                throw new SyntaxErrorException("This expression does not evaluate to boolean value");
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

    private boolean compileExpression() throws SyntaxErrorException
    {
        String type = lex.getTokenType();
        boolean isBoolean = compileTerm();

        while(lex.getTokenType().equals("numerical operator") || lex.getTokenType().equals("boolean operator"))
        {
            String nextType = lex.getNextTokenType(1);
            if(lex.currentLexeme().equals("!") && (!nextType.equals("boolean")) && !isBoolean)
            {
                throw new SyntaxErrorException("Cannot apply ! operator to " + nextType);
            }
            else if((type.equals("boolean") || nextType.equals("boolean") || type.equals("String") || nextType.equals("String")) && !lex.currentLexeme().equals("!"))
            {
                throw new SyntaxErrorException("The operator " + lex.currentLexeme() + " cannot be applied to the " + type + " " + nextType);
            }
            if(!isBoolean && lex.getTokenType().equals("boolean operator"))
            {
                isBoolean = true;
            }
            else if(isBoolean && lex.getTokenType().equals("boolean operator"))
            {
                throw new SyntaxErrorException("The operator " + lex.currentLexeme() + " cannot be applied to the expression");
            }
            lex.next();
            compileTerm();
        }
        return isBoolean;
    }

    private boolean compileTerm() throws SyntaxErrorException
    {
        String type = lex.getTokenType();
        boolean truthy = false;

        if(type.equals("int") || type.equals("char") || type.equals("double"))
        {
            lex.next();
        }
        else if(lex.currentLexeme().equals("("))
        {
            lex.next();
            truthy = compileExpression();
            match(")");

        }
        else if(lex.getTokenType().equals("boolean"))
        {
            truthy = true;
            lex.next();
        }
        return truthy;
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
