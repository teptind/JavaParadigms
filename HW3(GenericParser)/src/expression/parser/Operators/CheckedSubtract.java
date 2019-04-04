package expression.parser.Operators;

import expression.exceptions.OperationException;
import expression.parser.Operations.Operation;
import expression.parser.TripleExpression;

public class CheckedSubtract<T> extends BinaryOperator<T> {

    public CheckedSubtract(TripleExpression<T> exp1, TripleExpression<T> exp2, Operation<T> op) {
        super(exp1, exp2, op);
    }

    T calculate(T x, T y) throws OperationException {
        return operation.substract(x, y);
    }
}

