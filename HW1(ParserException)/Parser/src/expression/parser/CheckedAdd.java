package expression.parser;

import expression.exceptions.OperationApplyingException;
import expression.exceptions.OverflowException;

public class CheckedAdd extends BinaryOperator {

    public CheckedAdd(TripleExpression exp1, TripleExpression exp2) {
        super(exp1, exp2);
    }

    private void check(int x, int y) throws OperationApplyingException{
        if (((x > 0) && Integer.MAX_VALUE - x < y) || ((x < 0) && Integer.MIN_VALUE - x > y)) {
            throw new OverflowException();
        }
    }

    int calculate(int x, int y) throws OperationApplyingException {
        check(x, y);
        return x + y;
    }
}
