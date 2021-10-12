public class Practice
{
    public static void main(String[] args) throws SyntaxErrorException
    {
      // LexicalAnalyzer s = new LexicalAnalyzer("test1.txt");
//        for (int i = 0; i < s.lexemes.size(); i++)
//        {
//            System.out.println(s.lexemes.get(i) + " -> " + s.tokens.get(i));
//        }
        SyntaxAnalyzer n = new SyntaxAnalyzer("test1.txt");
    }
}
