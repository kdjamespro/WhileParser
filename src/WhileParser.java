public class WhileParser
{
    public static void main(String[] args) throws SyntaxErrorException
    {
        try
        {
            SyntaxAnalyzer n = new SyntaxAnalyzer("test10.txt");
            n.compileWhile();
        }
        catch(SyntaxErrorException e)
        {
            e.getMessage();
        }
    }
}
