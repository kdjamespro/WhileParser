
import java.util.StringTokenizer;

public class WhileDo extends Grammar
{
    public String keyword;
    public String leftParent;
    public Expression booleanExpression;
    public String rightParent;


//
//    public WhileDo(String keyword, String leftParent, Expression booleanExpression, String rightParent) throws SyntaxErrorException
//    {
//            if(!keyword.equals("while"))
//            {
//                throw new SyntaxErrorException("This is not a while loop");
//            }
//            if(!leftParent.equals("("))
//            {
//                throw new SyntaxErrorException("'(' is expected");
//            }
//    }

    public static Object[] getGrammar()
    {
        return new Object[]{"", "", new BooleanExpression(), ""};
    }
}
