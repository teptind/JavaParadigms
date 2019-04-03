package expression.parser;

import expression.exceptions.ParsingException;

import java.util.List;

public class ExpressionParser implements Parser {
    private List<ExpressionElement> expression;
    private int ind;
    private String str;
    private ExpressionElement curElem;

    private ExpressionElement getNextElem() {
        if (ind >= expression.size()) {
            return new ExpressionElement(Tokenizer.Token.END, str.length());
        } else {
            ind++;
            return expression.get(ind - 1);
        }

    }

    public TripleExpression parse(String in) throws ParsingException {
        str = in;
        Tokenizer tokenizer = new Tokenizer(in);
        expression = tokenizer.getElements();
        ind = 0;
        curElem = null;
        return addSub();
    }

    private TripleExpression addSub() {
        TripleExpression res = mulDiv();
        while (true) {
            switch (curElem.getToken()) {
                case ADD:
                    res = new CheckedAdd(res, mulDiv());
                    break;
                case SUB:
                    res = new CheckedSubtract(res, mulDiv());
                    break;
                default:
                    return res;
            }
        }
    }

    private TripleExpression mulDiv() {
        TripleExpression res = unary();
        while (true) {
            switch (curElem.getToken()) {
                case MULT:
                    res = new CheckedMultiply(res, unary());
                    break;
                case DIV:
                    res = new CheckedDivide(res, unary());
                    break;
                default:
                    return res;
            }
        }
    }

    private TripleExpression unary() {
        TripleExpression res = null;
        curElem = getNextElem();
        switch (curElem.getToken()) {
            case CLOSING_BRACE:
                curElem = getNextElem();
                break;
            case OPENING_BRACE:
                res = addSub();
                curElem = getNextElem();
                break;
            case VAR:
                res = new Variable(curElem.getName());
                curElem = getNextElem();
                break;
            case NUMBER:
                res = new Const(curElem.getValue());
                curElem = getNextElem();
                break;
            case NEG:
                return new CheckedNegate(unary());
            case HIGH:
                return new CheckedHigh(unary());
            case LOW:
                return new CheckedLow(unary());
            default:
                return new Const(0);
        }
        return res;
    }
}
