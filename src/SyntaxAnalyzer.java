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
                    throw new SyntaxErrorException("} is expected", lex.getLineNumber("}"));
                }
                System.out.println("\n\tGROUP 8 DEBUGGER: Valid while loop statement");
            }
            else if(nextLexeme.equals(";"))
            {
                throw new SyntaxErrorException("Unreachable Statement", lex.getLineNumber(";"));
            }
        }
        else
        {
            throw new SyntaxErrorException("Statement expected", lex.getLineNumber());
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
                    throw new SyntaxErrorException("Cannot apply ! operator to " + lex.getTokenType(), lex.getLineNumber());
                }
            }
        }
        else
        {
            boolean isCondition = compileExpression();
            if(!isCondition)
            {
                throw new SyntaxErrorException("This expression does not evaluate to boolean value", lex.getLineNumber());
            }
        }
        if(lex.getTokenType().equals("logical operator"))
        {
            lex.next();
            compileCondition();
        }
    }

    private boolean compileExpression() throws SyntaxErrorException
    {
        String type = lex.getTokenType();
        boolean isBoolean = compileTerm();

        while(isOperator(lex.currentLexeme()))
        {
            String nextType = lex.getNextTokenType(1);
            if(lex.currentLexeme().equals("!") && (!nextType.equals("boolean")) && !isBoolean)
            {
                throw new SyntaxErrorException("Cannot apply ! operator to " + nextType, lex.getLineNumber());
            }
            else if(bothString(type, nextType))
            {
                if(!lex.currentLexeme().equals("==") && !lex.currentLexeme().equals("!="))
                {
                    throw new SyntaxErrorException("The operator " + lex.currentLexeme() + " cannot be applied to the " + type + " " + nextType, lex.getLineNumber());
                }
            }
            else if((!isNumericalValues(type, nextType) && !isSymbol(type, nextType)) &&  isOperator(lex.currentLexeme()) && !lex.currentLexeme().equals("!"))
            {
                if(!(type.equals("boolean") && nextType.equals("boolean")))
                {
                    throw new SyntaxErrorException("The operator " + lex.currentLexeme() + " cannot be applied to the " + type + " " + nextType, lex.getLineNumber());
                }
            }
            else if((type.equals("String") || nextType.equals("String")) && bothString(type, nextType))
            {
                if(!lex.currentLexeme().equals("==") || !lex.currentLexeme().equals("!="))
                {
                    throw new SyntaxErrorException("The operator " + lex.currentLexeme() + " cannot be applied to the expression", lex.getLineNumber());
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
                    throw new SyntaxErrorException("The operator " + lex.currentLexeme() + " cannot be applied to boolean " + "and " + nextType, lex.getLineNumber());
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
            throw new SyntaxErrorException("Expecting " + word + " but the input is " + lex.currentLexeme(), lex.getLineNumber());
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

    private boolean isOperator(String op)
    {
        return lex.getTokenType().equals("numerical operator") || lex.getTokenType().equals("boolean operator");
    }
}
