package expression.parser.Operations;

import expression.exceptions.*;

public class IntegerMode implements Operation<Integer> {
    private boolean checkingMode;

    public IntegerMode(boolean withChecking) {
        checkingMode = withChecking;
    }

    public Integer add(Integer x, Integer y) throws OperationException {
        if (checkingMode) {
            if ((x > 0 && Integer.MAX_VALUE - x < y) || (x < 0 && Integer.MIN_VALUE - x > y)) {
                throw new OverflowException();
            }
        }
        return x + y;
    }

    public Integer substract(Integer x, Integer y) throws OperationException {
        if (checkingMode) {
            if ((x >= 0 && y < 0 && x - Integer.MAX_VALUE > y) || (x <= 0 && y > 0 && Integer.MIN_VALUE - x > -y)) {
                throw new OverflowException();
            }
        }
        return x - y;
    }

    public Integer multiply(Integer x, Integer y) throws OperationException {
        if (checkingMode) {
            if ((x > 0 && y > 0 && Integer.MAX_VALUE / x < y) || (x < 0 && y < 0 && Integer.MAX_VALUE / x > y)) {
                throw new OverflowException();
            }
            if ((x > 0 && y < 0 && Integer.MIN_VALUE / x > y) || (x < 0 && y > 0 && Integer.MIN_VALUE / y > x)) {
                throw new OverflowException();
            }
        }
        return x * y;
    }

    public Integer square(Integer x) throws OperationException {
        return (multiply(x, x));
    }

    public Integer abs(Integer x) throws OperationException {
        if (x >= 0) {
            return x;
        } else {
            return substract(0, x);
        }
    }

    public Integer mod(Integer x, Integer y) throws OperationException {
        if (y == 0) {
            throw new DivisionByZeroException();
        }
        return x % y;
    }

    public Integer divide(Integer x, Integer y) throws OperationException {
        if (checkingMode) {
            if (y == -1 && x == Integer.MIN_VALUE) {
                throw new OverflowException();
            }
        }
        if (y == 0) {
            throw new DivisionByZeroException();
        }
        return x / y;
    }

    public Integer negate(Integer x) throws OperationException {
        if (checkingMode) {
            if (x == Integer.MIN_VALUE) {
                throw new OverflowException();
            }
        }
        return -x;
    }

    public Integer getNumber(String str) throws ParsingException {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new InvalidConstantException();
        }
    }
}
