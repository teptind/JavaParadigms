package expression.parser.Operators;

import expression.parser.TripleExpression;

public class Variable<T> implements TripleExpression<T> {
    private char var;

    public Variable(char name) {
        var = name;
    }

    public T evaluate(T v1, T v2, T v3) {
        switch (var) {
            case 'x':
                return v1;
            case 'y':
                return v2;
            case 'z':
                return v3;
            default:
                return v1;
        }
    }
}
