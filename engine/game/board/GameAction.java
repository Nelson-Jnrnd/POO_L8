package engine.game.board;

/**
 * Interface for game actions
 * @author Alen Bijelic
 * @author Nelson Jeanrenaud
 * @param <T> Type of Board
 */
public interface GameAction<T extends Board<T>> {
    /**
     * Perform an action
     * @param start Vector from where the action starts
     * @param destination Vector to where the action ends
     * @param t Concerned Board on which the action will be performed
     * @return The piece on the destination
     */
    Piece<T> doAction(Vector start, Vector destination, Board<T> t);

    /**
     * Revert an action
     * @param start Vector from where the action started
     * @param destination Vector to where the action ended
     * @param affectedPiece Affected pieces by the action
     * @param t Concerned Board on which the action was perform
     */
    void revertAction(Vector start, Vector destination, Piece<T> affectedPiece, Board<T> t);
}