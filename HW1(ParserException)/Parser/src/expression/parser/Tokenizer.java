package expression.parser;

import expression.exceptions.*;

import java.util.ArrayList;
import java.util.List;

class Tokenizer {
    public enum Token {OPENING_BRACE, CLOSING_BRACE, VAR, NUMBER, ADD, SUB, MULT, DIV, NEG, HIGH, LOW, ERROR, END}

    private String expression;
    private int ind;
    private Token curToken;
    private ArrayList<ExpressionElement> result;

    Tokenizer(String exp) {
        ind = 0;
        expression = exp;
        curToken = Token.ERROR;
    }

    private void skipSpaces() {
        while (ind < expression.length() && Character.isWhitespace(expression.charAt(ind))) {
            ind++;
        }
    }

    private void checkFunction() throws UnidentifiedSymbolException {
        if (ind != expression.length() &&
                (Character.isDigit(expression.charAt(ind))) || Character.isLetter(expression.charAt(ind))) {
            throw new UnidentifiedSymbolException(makeLineWithCursor(ind));
        }
    }

    private ExpressionElement getNextElement() throws ParsingException {
        skipSpaces();
        if (ind >= expression.length()) {
            ind++;
            return new ExpressionElement(Token.END, ind - 1);
        }
        char curChar = expression.charAt(ind);
        switch (curChar) {
            case '+':
                ind++;
                return new ExpressionElement(Token.ADD, ind - 1);
            case '-':
                ind++;
                if (curToken == Token.CLOSING_BRACE || curToken == Token.VAR ||
                        curToken == Token.NUMBER) {
                    return new ExpressionElement(Token.SUB, ind - 1);
                } else {
                    if (ind + 10 <= expression.length() &&
                            expression.substring(ind - 1, ind + 10).equals(Integer.toString(Integer.MIN_VALUE))) {
                        ind += 10;
                        return new ExpressionElement(Token.NUMBER, ind - 11, Integer.MIN_VALUE);
                    } else {
                        return new ExpressionElement(Token.NEG, ind - 1);
                    }
                }
            case '*':
                ind++;
                return new ExpressionElement(Token.MULT, ind - 1);
            case '/':
                ind++;
                return new ExpressionElement(Token.DIV, ind - 1);
            case '(':
                ind++;
                return new ExpressionElement(Token.OPENING_BRACE, ind - 1);
            case ')':
                ind++;
                return new ExpressionElement(Token.CLOSING_BRACE, ind - 1);
            default:
                if (curChar == 'x' || curChar == 'y' || curChar == 'z') {
                    ind++;
                    return new ExpressionElement(Token.VAR, ind - 1, "" + curChar);
                } else if (ind + 4 <= expression.length() && expression.substring(ind, ind + 4).equals("high")) {
                    ind += 4;
                    checkFunction();
                    return new ExpressionElement(Token.HIGH, ind - 4);
                } else if (ind + 3 <= expression.length() && expression.substring(ind, ind + 3).equals("low")) {
                    ind += 3;
                    checkFunction();
                    return new ExpressionElement(Token.LOW, ind - 3);
                } else if (Character.isDigit(curChar)) {
                    int l = ind;
                    while (ind < expression.length() && Character.isDigit(expression.charAt(ind))) {
                        ind++;
                    }
                    try {
                        return new ExpressionElement(Token.NUMBER, l, Integer.parseInt(expression.substring(l, ind)));
                    } catch (Exception e) {
                        throw new InvalidConstantException(makeLineWithCursor(l));
                    }

                } else {
                    ind++;
                    return new ExpressionElement(Token.ERROR, ind - 1);
                }
        }
    }

    private String makeLineWithCursor(int cursorPlace) {
        String str = expression.substring(0, cursorPlace) + " ERROR->";
        if (cursorPlace < expression.length())
            str += expression.charAt(cursorPlace);
        return (str);
    }

    private void isGood() throws ParsingException {
        int bal = 0, cnt = 0;
        for (int i = 0; i < result.size() - 1; ++i) {
            ExpressionElement elem = result.get(i), nextElem = result.get(i + 1);
            if (i == 0 && isBinary(elem.getToken())) {
                throw new NotEnoughArgumentsException(makeLineWithCursor(elem.getPosition()));
            } else if (elem.getToken() == Tokenizer.Token.ERROR) {
                throw new UnidentifiedSymbolException(makeLineWithCursor(elem.getPosition()));
            } else if (elem.getToken() == Tokenizer.Token.OPENING_BRACE) {
                if (isBinary(nextElem.getToken())) {
                    throw new NotEnoughArgumentsException(makeLineWithCursor(nextElem.getPosition()));
                }
                bal++;
            } else if (elem.getToken() == Tokenizer.Token.CLOSING_BRACE) {
                bal--;
                if (bal < 0) {
                    throw new OddClosingBracketException(makeLineWithCursor(elem.getPosition()));
                }
            }
            if (isBinary(elem.getToken())) {
                if (isBinary(nextElem.getToken()) || nextElem.getToken() == Token.CLOSING_BRACE ||
                        nextElem.getToken() == Token.END) {
                    throw new NotEnoughArgumentsException(makeLineWithCursor(elem.getPosition()));
                }
                continue;
            }
            if (isUnary(elem.getToken())) {
                if (isBinary(nextElem.getToken()) || nextElem.getToken() == Token.CLOSING_BRACE
                        || nextElem.getToken() == Token.END) {
                    throw new NotEnoughArgumentsException(makeLineWithCursor(elem.getPosition()));
                }
                continue;
            }
            if (isConstOrVar(elem.getToken())) {
                if (nextElem.getToken() == Token.OPENING_BRACE || isConstOrVar(nextElem.getToken())) {
                    throw new MissingOperatorException(makeLineWithCursor(nextElem.getPosition()));
                }
                cnt++;
            }

        }
        if (bal > 0) throw new NoClosingBracketException(makeLineWithCursor(expression.length()));
        if (cnt == 0) {
            throw new ImpossibleToGetResultException(expression);
        }
    }

    private boolean isUnary(Token t) {
        return t == Token.NEG || t == Token.HIGH || t == Token.LOW;
    }

    private boolean isBinary(Token t) {
        return t == Token.ADD || t == Token.SUB || t == Token.MULT || t == Token.DIV;
    }

    private boolean isConstOrVar(Token t) {
        return t == Token.NUMBER || t == Token.VAR;
    }


    List<ExpressionElement> getElements() throws ParsingException {
        result = new ArrayList<>();
        while (ind <= expression.length()) {
            ExpressionElement curElem = getNextElement();
            curToken = curElem.getToken();
            result.add(curElem);
        }
        isGood();
        return result;
    }
}
