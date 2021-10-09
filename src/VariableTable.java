import java.util.HashMap;

public class VariableTable
{
    private HashMap<String, String> table;

    public VariableTable()
    {
        table = new HashMap<>();
    }

    public boolean contains(String key)
    {
        return table.containsKey(key);
    }

    public String getValue(String key)
    {
        return table.get(key);
    }

    public void add(String key, String value)
    {
        table.put(key, value);
    }

}
