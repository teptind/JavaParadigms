package expression.parser.Operations;

import expression.exceptions.OperationException;
import expression.exceptions.ParsingException;

public interface Operation<T> {
    T add(T x, T y) throws OperationException;

    T substract(T x, T y) throws OperationException;

    T multiply(T x, T y) throws OperationException;

    T divide(T x, T y) throws OperationException;

    T negate(T x) throws OperationException;

    T getNumber(String str) throws ParsingException;

    T square(T x) throws OperationException;

    T abs(T x) throws OperationException;

    T mod(T x, T y) throws OperationException;
}
