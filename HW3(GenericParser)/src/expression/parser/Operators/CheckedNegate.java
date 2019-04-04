package expression.parser.Operators;

import expression.exceptions.OperationException;
import expression.parser.Operations.Operation;
import expression.parser.TripleExpression;

public class CheckedNegate<T> extends UnaryOperator<T> {

    public CheckedNegate(TripleExpression<T> exp, Operation<T> op) {
        super(exp, op);
    }

    T calculate(T x) throws OperationException {
        return operation.negate(x);
    }
}
