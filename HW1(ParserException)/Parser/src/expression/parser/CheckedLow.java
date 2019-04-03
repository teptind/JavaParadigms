package expression.parser;

import expression.exceptions.OperationApplyingException;

public class CheckedLow extends UnaryOperator {
    public CheckedLow(TripleExpression exp) {
        super(exp);
    }

    @Override
    int calculate(int x) throws OperationApplyingException {
        return Integer.lowestOneBit(x);
    }
}
