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
            String nextLexeme = lex.currentLexeme();
            if(nextLexeme.equals("{"))
            {
                if(!lex.contains("}"))
                {
                    throw new SyntaxErrorException("} is expected");
                }
                System.out.println("\tGROUP 8 DEBUGGER: Valid while loop statement");
            }
            else if(nextLexeme.equals(";"))
            {
                throw new SyntaxErrorException("\n\tGROUP 8 DEBUGGER: Unreachable Statement\n");
            }
        }
        else
        {
            throw new SyntaxErrorException("\n\tGROUP 8 DEBUGGER: Statement expected\n");
        }
    }

    private void compileCondition() throws SyntaxErrorException
    {
        if(lex.currentLexeme().equals("!"))
        {
            while(lex.currentLexeme().equals("!") && lex.lookAhead(1).equals("!"))
            {
                lex.next();
            }
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
            else if(bothString(type, nextType))
            {
                if(!lex.currentLexeme().equals("==") && !lex.currentLexeme().equals("!="))
                {
                    throw new SyntaxErrorException("The operator " + lex.currentLexeme() + " cannot be applied to the " + type + " " + nextType);
                }
            }
            else if((!isNumericalValues(type, nextType) && !isSymbol(type, nextType))&&  (lex.getTokenType().equals("numerical operator") || lex.getTokenType().equals("boolean operator")))
            {
                throw new SyntaxErrorException("\n\tGROUP 8 DEBUGGER: The operator " + lex.currentLexeme() + " cannot be applied to the " + type + " " + nextType +"\n");
            }
            else if((type.equals("String") || nextType.equals("String")) && bothString(type, nextType))
            {
                if(!lex.currentLexeme().equals("==") || !lex.currentLexeme().equals("!="))
                {
                    throw new SyntaxErrorException("The operator " + lex.currentLexeme() + " cannot be applied to the expression");
                }
            }
            if(!isBoolean && lex.getTokenType().equals("boolean operator"))
            {
                isBoolean = true;
            }
            else if(isBoolean && lex.getTokenType().equals("boolean operator"))
            {
                if(!lex.currentLexeme().equals("==") && !lex.currentLexeme().equals("!="))
                {
                    throw new SyntaxErrorException("\n\tGROUP 8 DEBUGGER: The operator " + lex.currentLexeme() + " cannot be applied to boolean" + "and " + nextType);
                }
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
        else if(lex.getTokenType().equals("string"))
        {
            lex.next();
        }
        return truthy;
    }

    private void match(String word) throws  SyntaxErrorException
    {
        if(!lex.currentLexeme().equals(word))
        {
            throw new SyntaxErrorException("\n\tGROUP 8 DEBUGGER: Expecting " + word + " but the input is " + lex.currentLexeme() + "\n");
        }
        lex.next();
    }

    private boolean isNumericalValues(String type, String nextType)
    {
        if(type.equals("int") || type.equals("double") || type.equals("char"))
        {
            if(nextType.equals("int") || nextType.equals("double") || nextType.equals("char"))
            {
                return true;
            }
        }
        return false;
    }

    private boolean isSymbol(String type, String nextType)
    {
        return type.equals("symbol") || nextType.equals("symbol");
    }

    private boolean bothString(String type, String nextType)
    {
        return type.equals("string") && nextType.equals("string");
    }
}
