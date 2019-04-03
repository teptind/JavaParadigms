package expression.parser;

import expression.exceptions.OperationApplyingException;

public class CheckedHigh extends UnaryOperator {
    public CheckedHigh(TripleExpression exp) {
        super(exp);
    }

    int calculate(int x) throws OperationApplyingException {
        return Integer.highestOneBit(x);
    }
}
