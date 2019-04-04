package expression.parser;

import expression.exceptions.*;
import expression.parser.Operations.Operation;
import expression.parser.Operators.*;

public class ExpressionParser<T> implements Parser<T> {
    private String expression;
    private String func;
    private char varName;
    private Token currToken;
    private int ind, opInd;
    private T value;
    private int leftInd;
    private Operation<T> operation;

    public ExpressionParser(Operation<T> op) {
        operation = op;
    }

    public TripleExpression<T> parse(String in) throws ParsingException {
        expression = in;
        currToken = Token.BEGIN;
        ind = 0;
        TripleExpression<T> res = addSub();
        if (currToken != Token.END) {
//            if (currToken == Token.CLOSING_BRACE) {
//                throw new OddClosingBracketException("Odd closing brace, position: " + ind);
//            }
//            else if (currToken == Token.NUMBER || currToken == Token.VAR) {
//                throw new MissingOperatorException("Missed operator, position: " + ind);
//            } else {
                throw new EndException("Expected end of expression, found ' ': " + ind);
//            }
        } else {
            return res;
        }

    }

    private TripleExpression<T> addSub() throws ParsingException {
        TripleExpression<T> res = mulDiv();
        while (true) {
            switch (currToken) {
                case ADD:
                    res = new CheckedAdd<>(res, mulDiv(), operation);
                    break;
                case SUB:
                    res = new CheckedSubtract<>(res, mulDiv(), operation);
                    break;
                default:
                    return res;
            }
        }
    }

    private TripleExpression<T> mulDiv() throws ParsingException {
        TripleExpression<T> res = unary();
        while (true) {
            switch (currToken) {
                case MULT:
                    res = new CheckedMultiply<>(res, unary(), operation);
                    break;
                case DIV:
                    res = new CheckedDivide<>(res, unary(), operation);
                    break;
                case MOD:
                    res = new CheckedMod<>(res, unary(), operation);
                    break;
                default:
                    return res;
            }
        }
    }

    private TripleExpression<T> unary() throws ParsingException {
        TripleExpression<T> res;
        switch (getToken()) {
            case OPENING_BRACE:
                res = addSub();
                if (currToken != Token.CLOSING_BRACE) {
                    throw new NoClosingBracketException("No closing bracket, position: " + ind);
                }
                getToken();
                break;
            case VAR:
                res = new Variable<>(varName);
                getToken();
                break;
            case NUMBER:
                getNumber();
                res = new Const<>(value);
                getToken();
                break;
            case NEG:
                return new CheckedNegate<>(unary(), operation);
            case ABS:
                return new CheckedAbs<>(unary(), operation);
            case SQUARE:
                return new CheckedSquare<>(unary(), operation);
            default:
                throw new NotEnoughArgumentsException("Missed operand, position: " + opInd);
        }
        return res;
    }

    private void getOperation() {
        leftInd = ind - 1;
        while (ind < expression.length() && Character.isLetter(expression.charAt(ind))) {
            ind++;
        }
        func = expression.substring(leftInd, ind);
    }

    private void getNumber() throws ParsingException {
        leftInd = ind - 1;
        while (ind < expression.length() && (Character.isDigit(expression.charAt(ind)) || expression.charAt(ind) == '.')) {
            ind++;
        }
        value = operation.getNumber(expression.substring(leftInd, ind));
    }

    private Token getToken() throws ParsingException {
        Token oldToken = currToken;
        skipSpaces();
        if (ind >= expression.length()) {
            currToken = Token.END;
            return currToken;
        }
        char currSymb = expression.charAt(ind);
        switch (currSymb) {
            case '+':
                opInd = ind;
                currToken = Token.ADD;
                break;
            case '-':
                if (oldToken == Token.VAR || oldToken == Token.NUMBER || oldToken == Token.CLOSING_BRACE) {
                    currToken = Token.SUB;
                } else if (expression.length() > ind + 1 && Character.isDigit(expression.charAt(ind + 1))) {
                    currToken = Token.NUMBER;
                } else {
                    currToken = Token.NEG;
                }
                break;
            case '*':
                opInd = ind;
                currToken = Token.MULT;
                break;
            case '/':
                opInd = ind;
                currToken = Token.DIV;
                break;
            case '(':
                currToken = Token.OPENING_BRACE;
                break;
            case ')':
                opInd = ind;
                currToken = Token.CLOSING_BRACE;
                break;
            case 'x':
            case 'y':
            case 'z':
                varName = currSymb;
                currToken = Token.VAR;
                break;
            default:
                if (Character.isDigit(currSymb)) {
                    currToken = Token.NUMBER;
                } else {
                    ind++;
                    getOperation();
                    switch (func) {
                        case "mod":
                            currToken = Token.MOD;
                            break;
                        case "abs":
                            currToken = Token.ABS;
                            break;
                        case "square":
                            currToken = Token.SQUARE;
                            break;
                        default:
                            throw new WrongOperationException("Unsupported operation " + func + ", position: " + leftInd);
                    }
                    ind--;
                }
        }
        ind++;
        return currToken;
    }

    private void skipSpaces() {
        while (ind < expression.length() && Character.isWhitespace(expression.charAt(ind))) {
            ind++;
        }
    }
}


enum Token {OPENING_BRACE, CLOSING_BRACE, VAR, NUMBER, ADD, SUB, MULT, DIV, NEG, BEGIN, MOD, END, ABS, SQUARE}
