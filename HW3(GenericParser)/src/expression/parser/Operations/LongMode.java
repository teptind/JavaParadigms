package expression.parser.Operations;

import expression.exceptions.*;

public class LongMode implements Operation<Long> {
    private boolean checkingMode;

    public LongMode(boolean withChecking) {
        checkingMode = withChecking;
    }

    public Long add(Long x, Long y) throws OperationException {
        if (checkingMode) {
            if ((x > 0 && Long.MAX_VALUE - x < y) || (x < 0 && Long.MIN_VALUE - x > y)) {
                throw new OverflowException();
            }
        }
        return (x + y);
    }

    public Long substract(Long x, Long y) throws OperationException {
        if (checkingMode) {
            if ((x >= 0 && y < 0 && x - Long.MAX_VALUE > y) || (x <= 0 && y > 0 && Long.MIN_VALUE - x > -y)) {
                throw new OverflowException();
            }
        }
        return (x - y);
    }

    public Long multiply(Long x, Long y) throws OperationException {
        if (checkingMode) {
            if ((x > 0 && y > 0 && Long.MAX_VALUE / x < y) || (x < 0 && y < 0 && Long.MAX_VALUE / x > y)) {
                throw new OverflowException();
            }
            if ((x > 0 && y < 0 && Long.MIN_VALUE / x > y) || (x < 0 && y > 0 && Long.MIN_VALUE / y > x)) {
                throw new OverflowException();
            }
        }
        return (x * y);
    }

    public Long divide(Long x, Long y) throws OperationException {
        if (checkingMode) {
            if (y == -1 && x == Long.MIN_VALUE) {
                throw new OverflowException();
            }
        }
        if (y == 0) {
            throw new DivisionByZeroException();
        }
        return (x / y);
    }

    public Long negate(Long x) throws OperationException {
        if (checkingMode) {
            if (x == Long.MIN_VALUE) {
                throw new OverflowException();
            }
        }
        return (-x);
    }

    public Long square(Long x) throws OperationException {
        return (multiply(x, x));
    }

    public Long abs(Long x) throws OperationException {
        if (x >= 0) {
            return x;
        } else {
            return substract((long) 0, x);
        }
    }

    public Long mod(Long x, Long y) throws OperationException {
        if (y == 0) {
            throw new DivisionByZeroException();
        }
        return x % y;
    }

    public Long getNumber(String str) throws ParsingException {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            throw new InvalidConstantException();
        }
    }
}