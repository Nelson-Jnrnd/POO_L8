package engine.game.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Manage all moves that can be made in a board
 * @author Alen Bijelic
 * @author Nelson Jeanrenaud
 * @param <T> Type of Board
 */
public class Move<T extends Board<T>> {
    private final Vector vector;
    private final boolean isMirroredX;
    private final boolean isMirroredY;
    private final List<GameAction<T>>actions;
    private final List<GameCondition<T>> conditions;

    /**
     * Move constructor
     * @param vector Move vector made by the piece
     * @param isMirroredX Either the move is vertically mirrored or not
     * @param isMirroredY Either the move is horizontally mirrored or not
     * @param conditions List all conditions that applies to the move
     * @param actions List all actions that applied to the move
     */
    public Move(Vector vector, boolean isMirroredX, boolean isMirroredY, List<GameCondition<T>> conditions, List<GameAction<T>> actions) {
        this.vector = Objects.requireNonNull(vector, "movement vector must be non null");
        this.isMirroredX = isMirroredX;
        this.isMirroredY = isMirroredY;
        this.actions = actions;
        this.conditions = conditions;
    }

    /**
     * Move constructor whitout conditions and actions
     * @param vector  Move vector made by the piece
     * @param isMirroredX Either the move is vertically mirrored or not
     * @param isMirroredY Either the move is horizontally mirrored or not
     */
    public Move(Vector vector, boolean isMirroredX, boolean isMirroredY) {
        this(vector, isMirroredX, isMirroredY, null, null);
    }

    /**
     * Check conditions for moves
     * @param start Vector from where the action starts
     * @param destination Vector to where the action ends
     * @param t Concerned Board on which the action will be performed
     * @return Either the move is legit or not
     */
    protected boolean checkConditions(Vector start, Vector destination, Board<T> t) {
        Objects.requireNonNull(start, "start vector must be non null");
        Objects.requireNonNull(destination, "destination vector must be non null");
        Objects.requireNonNull(t, "board must be non null");

        if (conditions != null) {
            for (GameCondition<T> condition : conditions) {
                if (!condition.checkCondition(start, destination, t)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check if a move can be made from start to destination
     * @param start Vector from where the action starts
     * @param destination Vector to where the action ends
     * @param t Concerned Board on which the action will be performed
     * @return Either the move is legit or not
     */
    boolean canMove(Vector start, Vector destination, Board<T> t){
        Objects.requireNonNull(start, "start vector must be non null");
        Objects.requireNonNull(destination, "destination vector must be non null");
        Objects.requireNonNull(t, "board must be non null");

        Vector movementVector = new Vector(destination.getI() - start.getI(), destination.getJ() - start.getJ());

        if(vector.norm() < movementVector.norm()){
            return false;
        }

        // Check si dans la bonne direction
        return ((movementVector.areCollinearAndSameDirection(vector) ||
                (isMirroredX && movementVector.areCollinearAndSameDirection(vector.getMirrorXVector())) ||
                (isMirroredY && movementVector.areCollinearAndSameDirection(vector.getMirrorYVector())) ||
                (isMirroredX && isMirroredY && movementVector.areCollinearAndSameDirection(vector.getOpposedVector())))
                && checkConditions(start, destination, t));
    }

    /**
     * Perform a move
     * @param start Vector from where the move starts
     * @param destination Vector to where the move ends
     * @param t Concerned Board on which the move will be performed
     * @return List of affected pieces
     */
    ArrayList<Piece<T>> doMove(Vector start, Vector destination, T t){
        Objects.requireNonNull(start, "start vector must be non null");
        Objects.requireNonNull(destination, "destination vector must be non null");
        Objects.requireNonNull(t, "board must be non null");

        ArrayList<Piece<T>> affectedPieces = new ArrayList<>();
        if(actions != null) {
            for (GameAction<T> actionToDo : actions) {
                   affectedPieces.add(actionToDo.doAction(start, destination, t));
            }
        }
        return affectedPieces;
    }

    /**
     * Revert a move
     * @param start Vector from where the move started
     * @param destination Vector to where the move ended
     * @param affectedPieces List of affected pieces
     * @param t Concerned Board on which the move will be performed
     */
    void revertMove(Vector start, Vector destination, List<Piece<T>> affectedPieces, Board<T> t){
        Objects.requireNonNull(start, "start vector must be non null");
        Objects.requireNonNull(destination, "destination vector must be non null");
        Objects.requireNonNull(t, "board must be non null");

        if(actions != null) {
            for (int i = actions.size() - 1; i >= 0; i--) {
                actions.get(i).revertAction(start, destination, affectedPieces.get(i), t);
            }
        }
    }
}
