package expression.parser.Operations;

import expression.exceptions.*;

public class ByteMode implements Operation<Byte> {
    private boolean checkingMode;

    public ByteMode(boolean withChecking) {
        checkingMode = withChecking;
    }

    public Byte add(Byte x, Byte y) throws OperationException {
        if (checkingMode) {
            if ((x > 0 && Byte.MAX_VALUE - x < y) || (x < 0 && Byte.MIN_VALUE - x > y)) {
                throw new OverflowException();
            }
        }
        return (byte)(x + y);
    }

    public Byte substract(Byte x, Byte y) throws OperationException {
        if (checkingMode) {
            if ((x >= 0 && y < 0 && x - Byte.MAX_VALUE > y) || (x <= 0 && y > 0 && Byte.MIN_VALUE - x > -y)) {
                throw new OverflowException();
            }
        }
        return (byte)(x - y);
    }

    public Byte multiply(Byte x, Byte y) throws OperationException {
        if (checkingMode) {
            if ((x > 0 && y > 0 && Byte.MAX_VALUE / x < y) || (x < 0 && y < 0 && Byte.MAX_VALUE / x > y)) {
                throw new OverflowException();
            }
            if ((x > 0 && y < 0 && Byte.MIN_VALUE / x > y) || (x < 0 && y > 0 && Byte.MIN_VALUE / y > x)) {
                throw new OverflowException();
            }
        }
        return (byte)(x * y);
    }

    public Byte divide(Byte x, Byte y) throws OperationException {
        if (checkingMode) {
            if (y == -1 && x == Byte.MIN_VALUE) {
                throw new OverflowException();
            }
        }
        if (y == 0) {
            throw new DivisionByZeroException();
        }
        return (byte)(x / y);
    }

    public Byte negate(Byte x) throws OperationException {
        if (checkingMode) {
            if (x == Byte.MIN_VALUE) {
                throw new OverflowException();
            }
        }
        return (byte)(-x);
    }

    public Byte getNumber(String str) throws ParsingException {
        try {
            return (byte)Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new InvalidConstantException();
        }
    }

    public Byte square(Byte x) throws OperationException {
        return multiply(x, x);
    }

    @Override
    public Byte abs(Byte x) throws OperationException {
        if (x >= 0) {
            return x;
        } else {
            return substract((byte)0, x);
        }
    }

    @Override
    public Byte mod(Byte x, Byte y) throws OperationException {
        if (y == 0) {
            throw new DivisionByZeroException();
        }
        return (byte)(x % y);
    }
}