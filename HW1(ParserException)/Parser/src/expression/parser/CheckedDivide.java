package expression.parser;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OperationApplyingException;
import expression.exceptions.OverflowException;

public class CheckedDivide extends BinaryOperator {

    public CheckedDivide(TripleExpression exp1, TripleExpression exp2) {
        super(exp1, exp2);
    }

    private void check(int x, int y) throws OperationApplyingException{
        if (y == 0) {
            throw new DivisionByZeroException();
        }
        if (y == -1 && x == Integer.MIN_VALUE) {
            throw new OverflowException();
        }
    }

    int calculate(int x, int y) throws OperationApplyingException {
        check(x, y);
        return x / y;
    }
}
