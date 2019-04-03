package expression.parser;

import expression.exceptions.OperationApplyingException;

public abstract class BinaryOperator implements TripleExpression {
    private TripleExpression exp1, exp2;

    public BinaryOperator(TripleExpression first, TripleExpression second) {
        exp1 = first;
        exp2 = second;
    }

    abstract int calculate(int x, int y) throws OperationApplyingException;

    public int evaluate(int x, int y, int z) throws OperationApplyingException {
        return calculate(exp1.evaluate(x, y, z), exp2.evaluate(x, y, z));
    }
}
