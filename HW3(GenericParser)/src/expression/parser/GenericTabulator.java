package expression.parser;

import expression.exceptions.OperationException;
import expression.exceptions.ParsingException;
import expression.exceptions.UnsupportedModeException;
import expression.parser.Operations.*;

import java.util.Map;

public class GenericTabulator implements Tabulator {
    private static final Map<String, Operation> OPS = Map.of(
            "i", new IntegerMode(true),
            "d", new DoubleMode(),
            "bi", new BigIntegerMode(),
            "u", new IntegerMode(false),
            "f", new FloatMode(),
            "b", new ByteMode(false)
    );

    public Object[][][] tabulate(String str, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws UnsupportedModeException, ParsingException {
        Operation<?> mode = OPS.get(str);
        if (mode == null) {
            throw new UnsupportedModeException(str);
        }
        return tabulateWith(mode, expression, x1, x2, y1, y2, z1, z2);
    }

    private <T>Object[][][] tabulateWith(Operation<T> operation, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws ParsingException {
        Object[][][] ans = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        Parser<T> parser = new ExpressionParser<>(operation);
        TripleExpression<T> resultExpression = parser.parse(expression);

        for (int x = x1; x <= x2; ++x) {
            for (int y = y1; y <= y2; ++y) {
                for (int z = z1; z <= z2; ++z) {
                    try {
                        ans[x - x1][y - y1][z   - z1] = resultExpression.evaluate(operation.getNumber(Integer.toString(x)),
                                operation.getNumber(Integer.toString(y)), operation.getNumber(Integer.toString(z)));
                    } catch (OperationException | ParsingException ignored) {
                    }
                }
            }
        }
        return ans;
    }
}
