package engine.game.chess;

import engine.game.board.Board;
import engine.game.board.Piece;
import engine.game.board.Vector;
import java.util.Objects;

/**
 * Action when eating a piece
 * @author Alen Bijelic
 * @author Nelson Jeanrenaud
 */
public class EatPiece extends MoveChessPiece {

    /**
     * Perform action when eating a piece
     * @param start Start position of the action
     * @param destination Desination of the action
     * @param board Concerned board
     * @return Removed piece
     */
    @Override
    public Piece<Chess> doAction(Vector start, Vector destination, Board<Chess> board) {
        Objects.requireNonNull(start, "start vector must be non null");
        Objects.requireNonNull(destination, "destination vector must be non null");
        Objects.requireNonNull(board, "chess board must be non null");
        Piece<Chess> removedPiece = board.removePieceAtPosition(destination);
        super.doAction(start, destination, board);
        return removedPiece;
    }

    /**
     * Revert action when a piece is eaten
     * @param start Start position of the action
     * @param destination Desination of the action
     * @param board Concerned board
     */
    @Override
    public void revertAction(Vector start, Vector destination, Piece<Chess> affectedPiece, Board<Chess> board) {
        Objects.requireNonNull(start, "start vector must be non null");
        Objects.requireNonNull(destination, "destination vector must be non null");
        Objects.requireNonNull(board, "chess board must be non null");
        super.revertAction(start, destination, affectedPiece, board);
        if(affectedPiece != null)
             board.setPieceAtPosition(affectedPiece, destination);
    }
}
