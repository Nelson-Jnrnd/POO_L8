package engine.game.board;

import java.util.List;
import java.util.Objects;
import java.util.Stack;

/**
 * Historic of all moves
 * @author Alen Bijelic
 * @author Nelson Jeanrenaud
 * @param <T> Type of Board
 */
public class Historic<T extends Board<T>> {
    private final Stack<Action<T>> historicMoves;

    /**
     * Historic constructor
     */
    Historic(){
        this.historicMoves = new Stack<>();
    }

    /**
     * Add an action to the stack of historic moves
     * @param piece Concerned piece
     * @param move Move made by the piece
     * @param depart Begin vector
     * @param arrivee Destination vector
     * @param affectedPieces List of affected pieces
     */
    void add(Piece<T> piece, Move<T> move, Vector depart, Vector arrivee, List<Piece<T>> affectedPieces){
        historicMoves.push(new Action<>(piece, move, depart, arrivee, affectedPieces));
    }

    /**
     * Check if the piece is contained in stack of historic moves
     * @param piece The piece to be checked
     * @return Either the piece is contained in the historic moves or not
     */
    public boolean isPieceContained(Piece<T> piece){
        if(piece == null)
            return false;
        for(Action<T> action : historicMoves){
            if(piece == action.piece)
                return true;
        }
        return false;
    }

    /**
     * Cancel last move
     */
    public void revertLastMove(){
        if(historicMoves.empty())
            throw new RuntimeException("No move have been done. Can't revert");
        historicMoves.pop().revert();
    }

    /**
     * Check if the move a move is the last one executed
     * @param move move we are looking for
     * @return true if the move given is the last one executed
     */
    boolean isLastAction(Move<T> move){
        return getLastAction().isMove(move);
    }

    /**
     * Returns the last piece moved in the historic
     * @return
     */
    Piece<T> lastPieceMoved(){
        return getLastAction().piece;
    }


    /**
     * Get the last action recorded
     * @return Last added action in historic list
     */
    private Action<T> getLastAction(){
        if(historicMoves.empty())
            throw new RuntimeException("No move have been done. Can't get last action");
        return historicMoves.peek();
    }

    /**
     * Action tracks the move of a Piece
     * @author Alen Bijelic
     * @author Nelson Jeanrenaud
     * @param <T> Type of Board
     */
    private static class Action<T extends Board<T>> {
        private final Piece<T> piece;
        private final Move<T> move;
        private final Vector depart;
        private final Vector arrivee;

        // List of affected pieces i.e. pieces eaten by the piece
        private final List<Piece<T>> affectedPieces;

        /**
         * Action constructor
         * @param piece Piece that made the action
         * @param move The move made by the piece
         * @param depart Vector from where the piece is moved
         * @param arrivee Vector to where the piece is moved
         * @param affectedPieces Affected pieces by the action
         */
        private Action(Piece<T> piece, Move<T> move, Vector depart, Vector arrivee, List<Piece<T>> affectedPieces){
            Objects.requireNonNull(piece);
            Objects.requireNonNull(move);
            Objects.requireNonNull(depart);
            Objects.requireNonNull(arrivee);
            this.piece = piece;
            this.move = move;
            this.depart = depart;
            this.arrivee = arrivee;
            this.affectedPieces = affectedPieces;
        }

        /**
         * is the move the one done at this action
         * @param move move we are comparing it to
         * @return true if it's the case
         */
        private boolean isMove(Move<T> move){
            return move.equals(this.move);
        }

        /**
         * Brings back to the original state
         */
        private void revert(){
            move.revertMove(depart, arrivee, affectedPieces, piece.getBoard());
        }

    }

}
