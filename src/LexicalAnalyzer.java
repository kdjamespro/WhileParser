import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class LexicalAnalyzer
{
    public int lineNumber;
    public ArrayList<String> tokens;
    public ArrayList<String> lexemes;
    public SymbolTable symbols = new SymbolTable();
    private VariableTable var = new VariableTable();
    private int num;

    public LexicalAnalyzer(String fileName)
    {
        lexemes = new ArrayList<>();
        tokens = new ArrayList<>();
        lineNumber = 0;
        num = 0;
        try(BufferedReader read = new BufferedReader(new FileReader(new File(fileName))))
        {
            String line;

            while((line = read.readLine().trim()) != null)
            {
                lineNumber += 1;
                List<String> s = Collections.list(new StringTokenizer(line, " \n[(+-*/%=;&|.)", true)).stream()
                        .map(token -> (String) token).collect(Collectors.toList());

                for(int i = 0; i < s.size(); i++)
                {
                    String lexeme = s.get(i);

                    if(i < s.size() - 1 && (isNumericalOperator(lexeme) || isBooleanOperator(lexeme))&& s.get(i+1).equals("="))
                    {
                        i += 1;
                        lexeme += s.get(i);
                    }
                    else if(i < s.size() - 1 && (lexeme.equals("+") || lexeme.equals("-")) && s.get(i + 1).equals(lexeme))
                    {
                        i += 1;
                        lexeme += s.get(i);
                    }
                    else if(i < s.size() - 1 && lexeme.equals("|") && s.get(i+1).equals("|"))
                    {
                        i += 1;
                        lexeme += s.get(i);
                    }
                    else if(i < s.size() - 1 && lexeme.equals("&") && s.get(i+1).equals("&&"))
                    {
                        i += 1;
                        lexeme += s.get(i);
                    }
                    if(isDatatype(lexeme))
                    {
                        int j = 1;
                        String identifier = s.get(i + j);
                        while (identifier.matches("[\\s+]"))
                        {
                            j+= 1;
                            identifier = s.get(i + j);
                        }
                        if(isIdentifier(identifier))
                        {
                            if(var.contains(identifier))
                            {
                                throw new SyntaxErrorException("Line " + lineNumber + ": " + identifier + " Identifier is already defined.");
                            }
                            else
                            {
                                var.add(identifier, lexeme);
                            }
                        }
                        else
                        {
                            throw new SyntaxErrorException("Line " + lineNumber + ": " + identifier + " is an invalid identifier name.");
                        }
                    }

                    if(lexeme.matches("\\p{IsWhiteSpace}"))
                    {
                        continue;
                    }

                    if(symbols.contains(lexeme))
                    {
                        tokens.add(symbols.getValue(lexeme));
                    }
                    else if(isInteger(lexeme))
                    {
                        tokens.add("int");
                    }
                    else if(isDouble(lexeme))
                    {
                        tokens.add("double");
                    }
                    else if(isChar(lexeme))
                    {
                        tokens.add("char");
                    }
                    else if(isString(lexeme))
                    {
                        if(lexeme.charAt(0) == '\"' && lexeme.charAt(lexeme.length() - 1) == '\"')
                        {
                            tokens.add("string");
                        }
                        else
                        {
                            throw new SyntaxErrorException("Line " + lineNumber + ": "+ "Unclosed quotation mark (\")");
                        }
                    }
                    else if(isBooleanOperator(lexeme))
                    {
                        tokens.add("boolean operator");
                    }
                    else if(isLogicalOperator(lexeme))
                    {
                        tokens.add("logical operator");
                    }
                    else if(isNumericalOperator(lexeme))
                    {
                        tokens.add("numerical operator");
                    }
                    else if (isIdentifier(lexeme))
                    {
                        tokens.add("identifier");
                    }
                    else
                    {
                        throw new SyntaxErrorException("Line " + lineNumber + ": " + lexeme + " unexpected token.");
                    }
                    lexemes.add(lexeme);
                }
            }
        }

        catch(Exception e)
        {
            e.getMessage();
        }
    }

    public ArrayList<String> getLexemes()
    {
        return (ArrayList<String>) tokens.clone();
    }

    private boolean isInteger(String num)
    {
        if(num.contains("."))
        {
            return false;
        }
        return num.matches("\\d+");
    }

    private boolean isDouble(String num)
    {
        return num.matches("\\d+\\.\\d+");
    }

    public boolean isNumericalOperator(String op)
    {
        return op.matches("\\+\\+|--|<<|>>|[\\-+/*^%]=|[\\[\\]+/*\\-^%;]");
    }

    public boolean isBooleanOperator(String op)
    {
        return op.matches("!|<|<=|>|>=|==|!=");
    }

    private boolean isLogicalOperator(String op)
    {
        return op.matches("\\|[|]?|&[&]?");
    }

    private boolean isString(String s)
    {
        return s.contains("\"");
    }

    private boolean isChar(String c)
    {
        return c.contains("'") && c.length() == 3;
    }

    private boolean isIdentifier(String identifier)
    {
        return identifier.matches("[a-zA-Z_$][a-zA-Z\\\\d_$]*");
    }

    private boolean isDatatype(String datatype)
    {
        return datatype.equals("String") || datatype.equals("int") || datatype.equals("double") || datatype.equals("boolean")
                || datatype.equals("char");
    }

    public String currentLexeme()
    {
        if(hasMoreLexeme())
        {
            return lexemes.get(num);
        }
        return null;
    }

    public String getTokenType()
    {
        if(hasMoreLexeme())
        {
            return tokens.get(num);
        }
        return null;
    }

    public String getNextTokenType(int i)
    {
        if(num + i < tokens.size())
        {
            return tokens.get(num + i);
        }
        return null;
    }

    public String lookAhead(int i)
    {
        if(num + i < lexemes.size())
        {
            return lexemes.get(num + i);
        }
        return null;
    }

    public void next()
    {
        num += 1;
    }

    public boolean hasMoreLexeme()
    {
        return num < lexemes.size();
    }

    public String getIdentifierDataType(String identifier)
    {
        return var.getValue(identifier);
    }

    public boolean isOperator()
    {
        return getTokenType().equals("numerical operator") || getTokenType().equals("boolean operator");
    }

}
