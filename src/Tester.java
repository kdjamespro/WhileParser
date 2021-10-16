public class Tester
{
    public static void main(String[] args)
    {
        try
        {
            System.out.println("Test1: ");
            SyntaxAnalyzer n = new SyntaxAnalyzer("test1.txt");
            n.compileWhile();
        }
        catch(SyntaxErrorException e)
        {
            e.getMessage();
        }
        System.out.println();
        try
        {
            System.out.println("Test2: ");
            SyntaxAnalyzer n = new SyntaxAnalyzer("test2.txt");
            n.compileWhile();
        }
        catch(SyntaxErrorException e)
        {
            e.getMessage();
        }
        System.out.println();
        try
        {
            System.out.println("Test3: ");
            SyntaxAnalyzer n = new SyntaxAnalyzer("test3.txt");
            n.compileWhile();
        }
        catch(SyntaxErrorException e)
        {
            e.getMessage();
        }
        System.out.println();
        try
        {
            System.out.println("Test4: ");
            SyntaxAnalyzer n = new SyntaxAnalyzer("test4.txt");
            n.compileWhile();
        }
        catch(SyntaxErrorException e)
        {
            e.getMessage();
        }
        System.out.println();
        try
        {
            System.out.println("Test5: ");
            SyntaxAnalyzer n = new SyntaxAnalyzer("test5.txt");
            n.compileWhile();
        }
        catch(SyntaxErrorException e)
        {
            e.getMessage();
        }
        System.out.println();
        try
        {
            System.out.println("Test6: ");
            SyntaxAnalyzer n = new SyntaxAnalyzer("test6.txt");
            n.compileWhile();
        }
        catch(SyntaxErrorException e)
        {
            e.getMessage();
        }
        System.out.println();
        try
        {
            System.out.println("Test7: ");
            SyntaxAnalyzer n = new SyntaxAnalyzer("test7.txt");
            n.compileWhile();
        }
        catch(SyntaxErrorException e)
        {
            e.getMessage();
        }
        System.out.println();
        try
        {
            System.out.println("Test8: ");
            SyntaxAnalyzer n = new SyntaxAnalyzer("test8.txt");
            n.compileWhile();
        }
        catch(SyntaxErrorException e)
        {
            e.getMessage();
        }
        System.out.println();
        try
        {
            System.out.println("Test9: ");
            SyntaxAnalyzer n = new SyntaxAnalyzer("test9.txt");
            n.compileWhile();
        }
        catch(SyntaxErrorException e)
        {
            e.getMessage();
        }
    }
}
