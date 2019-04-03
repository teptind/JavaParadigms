package expression.parser;

public class Variable implements TripleExpression{
    private String var;

    public Variable(String name) {
        var = name;
    }

    public int evaluate(int v1, int v2, int v3) {
        switch (var) {
            case "x":
                return v1;
            case "y":
                return v2;
            case "z":
                return v3;
        }
        return 0;
    }
}
