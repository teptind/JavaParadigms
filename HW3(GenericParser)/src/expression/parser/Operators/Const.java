package expression.parser.Operators;

import expression.parser.TripleExpression;

public class Const<T> implements TripleExpression<T> {
    private T value;
    public Const(T v1) {
        value = v1;
    }
    public T evaluate(T v1, T v2, T v3) {
        return value;
    }
}
