package expression.parser;

public class ExpressionElement {

    private Tokenizer.Token token;
    private int position;
    private String name;
    private Number value;

    public ExpressionElement(Tokenizer.Token token1, int position1) {
        token = token1;
        position = position1;
        name = "";
    }

    public ExpressionElement(Tokenizer.Token token1, int position1, String name1) {
        token = token1;
        position = position1;
        name = name1;
    }

    public ExpressionElement(Tokenizer.Token token1, int position1, Number value1) {
        token = token1;
        position = position1;
        value = value1;
    }

    public Tokenizer.Token getToken() {
        return token;
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value.intValue();
    }
}
