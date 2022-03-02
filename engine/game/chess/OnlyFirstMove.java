package engine.game.chess;

import engine.game.board.Board;
import engine.game.board.GameCondition;
import engine.game.board.Vector;

import java.util.Objects;

/**
 * Condition for one first moves
 * @author Alen Bijelic
 * @author Nelson Jeanrenaud
 */
public class OnlyFirstMove implements GameCondition<Chess> {
    /**
     * Check if the piece has not moved before
     * @param start Vector from where the action starts
     * @param destination Vector to where the action ends
     * @param board Concerned Board on which the action will be performed
     * @return Either the condition is valid or not
     */
    @Override
    public boolean checkCondition(Vector start, Vector destination, Board<Chess> board) {
        Objects.requireNonNull(start, "start vector must be non null");
        Objects.requireNonNull(destination, "destination vector must be non null");
        Objects.requireNonNull(board, "chess board must be non null");
        return !board.hasMoved(board.getPieceAtPosition(start));
    }
}
