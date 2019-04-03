package expression.parser;

import expression.exceptions.OperationApplyingException;
import expression.exceptions.OverflowException;

public class CheckedNegate extends UnaryOperator {

    public CheckedNegate(TripleExpression exp) {
        super(exp);
    }

    void check(int x) throws OperationApplyingException {
        if (x == Integer.MIN_VALUE) {
            throw new OverflowException();
        }
    }

    int calculate(int x) throws OperationApplyingException {
        check(x);
        return -x;
    }
}
