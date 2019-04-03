package expression.parser;

import expression.exceptions.OperationApplyingException;

public abstract class UnaryOperator implements TripleExpression {
    private TripleExpression exp1;

    public UnaryOperator(TripleExpression exp) {
        exp1 = exp;
    }

    abstract int calculate(int x) throws OperationApplyingException;

    public int evaluate(int x, int y, int z) throws OperationApplyingException {
        return calculate(exp1.evaluate(x, y, z));
    }
}
