package expression.tests;

import expression.exceptions.OperationApplyingException;
import expression.exceptions.ParsingException;
import expression.parser.ExpressionParser;

public class Main extends ExpressionParser {
    public static void main(String[] args) throws ParsingException, OperationApplyingException {
        new Main();
    }
    private Main() throws ParsingException, OperationApplyingException {
        System.out.println(parse("- x").evaluate(1, 0, 0));
    }
}
