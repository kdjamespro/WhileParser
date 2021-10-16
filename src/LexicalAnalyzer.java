import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class LexicalAnalyzer
{
    private int lineNumber;
    private final ArrayList<String> tokens;
    private final ArrayList<String> lexemes;
    private final SymbolTable symbols = new SymbolTable();
    private final VariableTable var = new VariableTable();
    private int num;

    public LexicalAnalyzer(String fileName) throws SyntaxErrorException
    {
        lexemes = new ArrayList<>();
        tokens = new ArrayList<>();
        num = 0;
        try(BufferedReader read = new BufferedReader(new FileReader(new File(fileName))))
        {
            String line;

            while((line = read.readLine()) != null)
            {
                line = line.trim();
                lineNumber += 1;
                List<String> s = Collections.list(new StringTokenizer(line, " \n[(+-*/%=;&|.!><)", true)).stream()
                        .map(token -> (String) token).collect(Collectors.toList());

                for(int i = 0; i < s.size(); i++)
                {
                    String lexeme = s.get(i).trim();

                    if(lexeme.matches("\\p{IsWhiteSpace}") || lexeme.equals(""))
                    {
                        continue;
                    }

                    if(i < s.size() - 1 && (isNumericalOperator(lexeme) || isBooleanOperator(lexeme) || isAssignment(lexeme))&& s.get(i+1).equals("="))
                    {
                        i += 1;
                        lexeme += s.get(i);
                    }
                    else if(i < s.size() - 1 && (lexeme.equals("+") || lexeme.equals("-") || lexeme.equals("<") || lexeme.equals(">"))&& s.get(i + 1).equals(lexeme))
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
                        if(!(lexeme.charAt(lexeme.length() - 1) == '\"'))
                        {
                            while(i < s.size() - 2 && s.get(i + 2).charAt(s.get(i + 2).length() - 1) != '\"')
                            {
                                String p1 = lexeme.replaceAll("\"", "");
                                String p2 = s.get(i + 2).replaceAll("\"", "");
                                lexeme = "\"" + p1 + " " + p2 + "\"";
                                i += 2;
                            }
                            String p1 = lexeme.replaceAll("\"", "");
                            String p2 = s.get(i + 2).replaceAll("\"", "");
                            lexeme = "\"" + p1 + " " + p2 + "\"";
                            i += 2;
                        }
                        if(lexeme.charAt(0) == '\"' && lexeme.charAt(lexeme.length() - 1) == '\"')
                        {
                            tokens.add("string");
                        }
                        else
                        {
                            throw new SyntaxErrorException("Line " + lineNumber + ": "+ "Unclosed quotation mark (\")");
                        }
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

        catch(IOException e)
        {
            e.getMessage();
        }
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
        return op.matches("\\+\\+|--|<<|>>|[\\-+/*^%]=|[\\[\\]+/*\\-^%]");
    }

    public boolean isBooleanOperator(String op)
    {
        return op.matches("!|<|<=|>|>=|==|!=");
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

    private boolean isAssignment(String op)
    {
        return op.matches("=");
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

    public boolean contains(String item)
    {
        return lexemes.contains(item);
    }
}
