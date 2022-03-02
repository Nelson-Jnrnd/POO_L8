package engine.game.chess;

import engine.game.board.Board;
import engine.game.board.GameCondition;
import engine.game.board.Vector;
import java.util.Objects;

/**
 * Condition to check if there is no collision between pieces
 * @author Alen Bijelic
 * @author Nelson Jeanrenaud
 */
public class MustNotCollide implements GameCondition<Chess> {

    /**
     * Check if there is no collision between pieces
     * @param start Vector from where the action starts
     * @param destination Vector to where the action ends
     * @param board Concerned ChessBoard
     * @return  Either the condition is valid or not
     */
    @Override
    public boolean checkCondition(Vector start, Vector destination, Board<Chess> board) {
        Objects.requireNonNull(start, "start vector must be non null");
        Objects.requireNonNull(destination, "destination vector must be non null");
        Objects.requireNonNull(board, "chess board must be non null");
        for (Vector positionOffset: destination.sub(start).includedVectors()) {
            Vector coordinates = start.add(positionOffset);
            if(board.getPieceAtPosition(coordinates) != null){
                return false;
            }
        }
        return true;
    }
}
