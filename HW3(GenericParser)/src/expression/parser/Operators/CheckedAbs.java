package expression.parser.Operators;

import expression.exceptions.OperationException;
import expression.parser.Operations.Operation;
import expression.parser.TripleExpression;

public class CheckedAbs<T> extends UnaryOperator<T> {

    public CheckedAbs(TripleExpression<T> exp, Operation<T> op) {
        super(exp, op);
    }

    T calculate(T x) throws OperationException {
        return operation.abs(x);
    }
}
