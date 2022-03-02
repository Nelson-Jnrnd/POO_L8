package engine.game.chess;

import engine.game.board.*;
import java.util.Objects;

/**
 * Action and condition for ChessPiece move
 * @author Alen Bijelic
 * @author Nelson Jeanrenaud
 */
public class MoveChessPiece implements GameCondition<Chess>, GameAction<Chess> {

    /**
     * Perform a ChessPiece move
     * @param start Vector from where the action starts
     * @param destination Vector to where the action ends
     * @param board Concerned ChessBoard
     * @return Moved piece
     */
    @Override
    public Piece<Chess> doAction(Vector start, Vector destination, Board<Chess> board) {
        Objects.requireNonNull(start, "start vector must be non null");
        Objects.requireNonNull(destination, "destination vector must be non null");
        Objects.requireNonNull(board, "chess board must be non null");
        board.movePieceAtPosition(start, destination);
        return null;
    }

    /**
     * Revert ChessPiece move
     * @param start Vector from where the action started
     * @param destination Vector to where the action ended
     * @param affectedPiece Affected pieces by the action
     * @param board Concerned ChessBoard
     */
    @Override
    public void revertAction(Vector start, Vector destination, Piece<Chess> affectedPiece, Board<Chess> board) {
        Objects.requireNonNull(start, "start vector must be non null");
        Objects.requireNonNull(destination, "destination vector must be non null");
        Objects.requireNonNull(board, "chess board must be non null");
        board.movePieceAtPosition(destination, start);
    }

    /**
     * Check if a ChessPiece move is valid
     * @param start Vector from where the action starts
     * @param destination Vector to where the action ends
     * @param board Concerned ChessBoard
     * @return Either the move is valid or not
     */
    @Override
    public boolean checkCondition(Vector start, Vector destination, Board<Chess> board) {
        Objects.requireNonNull(start, "start vector must be non null");
        Objects.requireNonNull(destination, "destination vector must be non null");
        Objects.requireNonNull(board, "chess board must be non null");
        Chess.ChessPiece piece = board.self().getPieceAtPosition(destination);
        if(piece != null)
            return piece.getColor() != board.self().getPieceAtPosition(start).getColor();
        return true;
    }
}
