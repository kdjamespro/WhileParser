public class ErrorTester
{
    public static void main(String[] args)
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
        try
        {
            SyntaxAnalyzer n = new SyntaxAnalyzer("test_err2.txt");
            n.compileWhile();
        }
        catch(SyntaxErrorException e)
        {
            e.getMessage();
        }
        try
        {
            SyntaxAnalyzer n = new SyntaxAnalyzer("test_err3.txt");
            n.compileWhile();
        }
        catch(SyntaxErrorException e)
        {
            e.getMessage();
        }
        try
        {
            SyntaxAnalyzer n = new SyntaxAnalyzer("test_err4.txt");
            n.compileWhile();
        }
        catch(SyntaxErrorException e)
        {
            e.getMessage();
        }
        try
        {
            SyntaxAnalyzer n = new SyntaxAnalyzer("test_err5.txt");
            n.compileWhile();
        }
        catch(SyntaxErrorException e)
        {
            e.getMessage();
        }
    }
}