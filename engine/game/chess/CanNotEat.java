package engine.game.chess;

import engine.game.board.Board;
import engine.game.board.GameCondition;
import engine.game.board.Vector;

import java.util.Objects;

/**
 * Condition to not eat
 * @author Alen Bijelic
 * @author Nelson Jeanrenaud
 */
public class CanNotEat implements GameCondition<Chess> {

    /**
     * Check if there is no pieces at destination
     * @param start Vector from where the action starts
     * @param destination Vector to where the action ends
     * @param board Concerned Board on which the action will be performed
     * @return If there is no pieces at destination
     */
    @Override
    public boolean checkCondition(Vector start, Vector destination, Board<Chess> board) {
        Objects.requireNonNull(start, "start vector must be non null");
        Objects.requireNonNull(destination, "destination vector must be non null");
        Objects.requireNonNull(board, "chess board must be non null");
        return board.getPieceAtPosition(destination) == null;
    }
}
