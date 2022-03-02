package engine.game.chess;

import engine.game.board.Board;
import engine.game.board.GameCondition;
import engine.game.board.Vector;

import java.util.Objects;

/**
 * Condition when a piece should eat
 * @author Alen Bijelic
 * @author Nelson Jeanrenaud
 */
public class MustEat implements GameCondition<Chess> {

    /**
     * Check if destination contains a piece to be eaten
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
        return board.getPieceAtPosition(destination) != null;
    }
}
