package expression.parser.Operations;

import expression.exceptions.*;

import java.math.BigInteger;

public class BigIntegerMode implements Operation<BigInteger> {

    public BigInteger add(BigInteger x, BigInteger y) {
        return x.add(y);
    }

    public BigInteger substract(BigInteger x, BigInteger y) {
        return x.subtract(y);
    }

    public BigInteger multiply(BigInteger x, BigInteger y) {
        return x.multiply(y);
    }

    public BigInteger divide(BigInteger x, BigInteger y) throws OperationException {
        if (y.equals(BigInteger.ZERO)) {
            throw new DivisionByZeroException();
        }
        return x.divide(y);
    }

    public BigInteger negate(BigInteger x) {
        return x.negate();
    }

    public BigInteger getNumber(String str) throws ParsingException {
        try {
            return new BigInteger(str);
        } catch (NumberFormatException e) {
            throw new InvalidConstantException();
        }
    }

    public BigInteger square(BigInteger x) {
        return multiply(x, x);
    }

    @Override
    public BigInteger abs(BigInteger x) {
        if (x.compareTo(BigInteger.ZERO) < 0) {
            return multiply(x, new BigInteger("-1"));
        } else {
            return x;
        }
    }

    @Override
    public BigInteger mod(BigInteger x, BigInteger y) throws OperationException {
        if (y.compareTo(BigInteger.ZERO) <= 0) {
            throw new DivisionByZeroException();
        }
        return x.mod(y);
    }
}
