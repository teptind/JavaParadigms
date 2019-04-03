package expression.parser;

import expression.exceptions.OperationApplyingException;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface TripleExpression {
    int evaluate(int x, int y, int z) throws OperationApplyingException;
}
