import java.util.HashMap;

public class SymbolTable
{
    public HashMap<String, String> symbolTable;

    public SymbolTable()
    {
        symbolTable = new HashMap<>();
        addKeywords();
        addSymbols();
        addOperators();
    }

    private void addOperators()
    {
        symbolTable.put("+", "numerical operator");
        symbolTable.put("++", "numerical operator");
        symbolTable.put("-", "numerical operator");
        symbolTable.put("--", "numerical operator");
        symbolTable.put("*", "numerical operator");
        symbolTable.put("/", "numerical operator");
        symbolTable.put("%", "numerical operator");
        symbolTable.put("+=", "numerical operator");
        symbolTable.put("-=", "numerical operator");
        symbolTable.put("*=", "numerical operator");
        symbolTable.put("/=", "numerical operator");
        symbolTable.put("%=", "numerical operator");
        symbolTable.put("<<", "numerical operator");
        symbolTable.put(">>", "numerical operator");
        symbolTable.put("^", "numerical operator");
        symbolTable.put("^=", "numerical operator");
        symbolTable.put("==", "boolean operator");
        symbolTable.put("!=", "boolean operator");
        symbolTable.put(">", "boolean operator");
        symbolTable.put(">=", "boolean operator");
        symbolTable.put("<", "boolean operator");
        symbolTable.put("<=", "boolean operator");
        symbolTable.put("!", "boolean operator");
        symbolTable.put("|", "logical operator");
        symbolTable.put("||", "logical operator");
        symbolTable.put("&", "logical operator");
        symbolTable.put("&&", "logical operator");
    }

    private void addKeywords()
    {
        symbolTable.put("while", "keyword");
        symbolTable.put("int", "keyword");
        symbolTable.put("double", "keyword");
        symbolTable.put("String", "keyword");
        symbolTable.put("char", "keyword");
        symbolTable.put("boolean", "keyword");
        symbolTable.put("true", "boolean");
        symbolTable.put("false", "boolean");

    }

    private void addSymbols()
    {
        symbolTable.put("{", "symbol");
        symbolTable.put("}", "symbol");
        symbolTable.put("(", "symbol");
        symbolTable.put(")", "symbol");
        symbolTable.put(";", "symbol");
        symbolTable.put(".", "symbol");
    }

    public boolean contains(String key)
    {
        return symbolTable.containsKey(key);
    }

    public String getValue(String key)
    {
        return symbolTable.get(key);
    }

    public void add(String key, String value)
    {
        symbolTable.put(key, value);
    }


}
