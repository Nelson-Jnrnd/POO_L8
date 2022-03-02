package engine.game.board;

/**
 * Interface for game conditions
 * @author Alen Bijelic
 * @author Nelson Jeanrenaud
 * @param <T> Type of Board
 */
public interface GameCondition<T extends Board<T>> {
    /**
     * Check a condition
     * @param start Vector from where the action starts
     * @param destination Vector to where the action ends
     * @param board Concerned Board on which the action will be performed
     * @return Either the condition is accepted or not
     */
    boolean checkCondition(Vector start, Vector destination, Board<T> board);
}