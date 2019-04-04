package expression.tests;

import expression.parser.ExpressionParser;
import expression.parser.Operations.IntegerMode;
import expression.parser.Operations.Operation;

public class Main {
    public static void main(String[] args) throws Exception {
        Operation<?> mode;
        mode = new IntegerMode(false);
        Main(mode);
    }
    private static <T> void Main(Operation<T> operation) throws Exception {
        ExpressionParser<T> parser = new ExpressionParser<>(operation);
        System.out.println(parser.parse("()").evaluate(operation.getNumber("-6"),
                operation.getNumber("-15"), operation.getNumber("-19")));
    }
}
