public class SyntaxErrorException extends Exception
{
    public SyntaxErrorException(String message, int lineNumber)
    {
        super("\n\tGROUP 8 DEBUGGER: Line " + lineNumber + ": "  + message + "\n");
        System.err.println(this);
    }
}
