public class WhileParser
{
    public static void main(String[] args) throws SyntaxErrorException
    {
        try
        {
            SyntaxAnalyzer n = new SyntaxAnalyzer("test_err1.txt");
            n.compileWhile();
        }
        catch(SyntaxErrorException e)
        {
            e.getMessage();
        }
    }
}
