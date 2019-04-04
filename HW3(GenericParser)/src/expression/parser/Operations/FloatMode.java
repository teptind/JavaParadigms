package expression.parser.Operations;

import expression.exceptions.InvalidConstantException;
import expression.exceptions.ParsingException;

import static java.lang.Double.NaN;

public class FloatMode implements Operation<Float> {

    public Float add(Float x, Float y) {
        return x + y;
    }

    public Float substract(Float x, Float y) {
        return x - y;
    }

    public Float multiply(Float x, Float y) {
        return x * y;
    }

    public Float divide(Float x, Float y) {
        return x / y;
    }

    public Float negate(Float x) {
        return -x;
    }

    public Float getNumber(String str) throws ParsingException {
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            throw new InvalidConstantException();
        }
    }

    public Float square(Float x) {
        return multiply(x, x);
    }

    public Float abs(Float x) {
        if (x >= 0) {
            return x;
        } else {
            return -x;
        }
    }

    public Float mod(Float x, Float y) {
        if (y == 0) {
            return (float)NaN;
        }
        return x % y;
    }
}
