public class Practice
{
    public static void main(String[] args) throws SyntaxErrorException
    {
        SyntaxAnalyzer n = new SyntaxAnalyzer("test_err4.txt");
        n.compileWhile();
    }
}
