package expression.parser.Operators;

import expression.exceptions.OperationException;
import expression.parser.Operations.Operation;
import expression.parser.TripleExpression;

public abstract class BinaryOperator<T> implements TripleExpression<T> {
    private TripleExpression<T> exp1, exp2;
    protected Operation<T> operation;

    public BinaryOperator(TripleExpression<T> first, TripleExpression<T> second, Operation<T> op) {
        exp1 = first;
        exp2 = second;
        operation = op;
    }

    abstract T calculate(T x, T y) throws OperationException;

    public T evaluate(T x, T y, T z) throws OperationException {
        return calculate(exp1.evaluate(x, y, z), exp2.evaluate(x, y, z));
    }
}
