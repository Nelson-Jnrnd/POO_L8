package engine.game.chess;

import engine.game.board.Board;
import engine.game.board.GameCondition;
import engine.game.board.Vector;

import java.util.Objects;

/**
 * Condition to check if there is no check
 * @author Alen Bijelic
 * @author Nelson Jeanrenaud
 */
public class MustNotCheck implements GameCondition<Chess> {
    /**
     * Check if during a movement there is a check
     * @param start Vector from where the action starts
     * @param destination Vector to where the action ends
     * @param chess Concerned ChessBoard
     * @return Either the condition is valid or not
     */
    @Override
    public boolean checkCondition(Vector start, Vector destination, Board<Chess> chess) {
        Objects.requireNonNull(start, "start vector must be non null");
        Objects.requireNonNull(destination, "destination vector must be non null");
        Objects.requireNonNull(chess, "chess board must be non null");
        Vector movementVector = new Vector(destination.getI() - start.getI(), destination.getJ() - start.getJ());
        for (Vector squareMovedThrough: movementVector.includedVectors()) {
            if(chess.self().isAttacked(chess.self().getPieceAtPosition(start).getColor(), start.add(squareMovedThrough))){
                return false;
            }
        }
        return !chess.self().isAttacked(chess.self().getPieceAtPosition(start).getColor(), destination);
    }
}
