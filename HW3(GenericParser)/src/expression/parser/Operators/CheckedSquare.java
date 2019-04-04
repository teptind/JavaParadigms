package expression.parser.Operators;

import expression.exceptions.OperationException;
import expression.parser.Operations.Operation;
import expression.parser.TripleExpression;

public class CheckedSquare<T> extends UnaryOperator<T> {

    public CheckedSquare(TripleExpression<T> exp, Operation<T> op) {
        super(exp, op);
    }

    T calculate(T x) throws OperationException {
        return operation.square(x);
    }
}
