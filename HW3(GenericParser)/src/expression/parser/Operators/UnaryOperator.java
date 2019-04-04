package expression.parser.Operators;

import expression.exceptions.OperationException;
import expression.parser.Operations.Operation;
import expression.parser.TripleExpression;

public abstract class UnaryOperator<T> implements TripleExpression<T> {
    private TripleExpression<T> exp1;
    protected Operation<T> operation;

    public UnaryOperator(TripleExpression<T> exp, Operation<T> op) {
        exp1 = exp;
        operation = op;
    }

    abstract T calculate(T x) throws OperationException;

    public T evaluate(T x, T y, T z) throws OperationException {
        return calculate(exp1.evaluate(x, y, z));
    }
}
