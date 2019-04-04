package expression.parser.Operations;

import expression.exceptions.InvalidConstantException;
import expression.exceptions.ParsingException;

import static java.lang.Double.NaN;

public class DoubleMode implements Operation<Double> {

    public Double add(Double x, Double y) {
        return x + y;
    }

    public Double substract(Double x, Double y) {
        return x - y;
    }

    public Double multiply(Double x, Double y) {
        return x * y;
    }

    public Double divide(Double x, Double y) {
        return x / y;
    }

    public Double negate(Double x) {
        return -x;
    }

    public Double getNumber(String str) throws ParsingException {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            throw new InvalidConstantException();
        }
    }

    public Double abs(Double x) {
        if (x >= 0) {
            return x;
        } else {
            return -x;
        }
    }

    public Double mod(Double x, Double y) {
        if (y == 0) {
            return NaN;
        }
        return x % y;
    }

    public Double square(Double x) {
        return multiply(x, x);
    }
}
