package expression.parser;

import expression.exceptions.OperationApplyingException;
import expression.exceptions.OverflowException;

public class CheckedSubtract extends BinaryOperator {

    public CheckedSubtract(TripleExpression exp1, TripleExpression exp2) {
        super(exp1, exp2);
    }

    private void check(int x, int y) throws OperationApplyingException{
        if (((y < 0) && Integer.MAX_VALUE + y < x) || ((y > 0) && Integer.MIN_VALUE + y > x)) {
            throw new OverflowException();
        }
    }

    int calculate(int x, int y) throws OperationApplyingException {
        check(x, y);
        return x - y;
    }
}

