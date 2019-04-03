package expression.parser;

public class Const implements TripleExpression {
    private Number value;
    public Const(Number v1) {
        value = v1;
    }
    public int evaluate(int v1, int v2, int v3) {
        return value.intValue();
    }
}
