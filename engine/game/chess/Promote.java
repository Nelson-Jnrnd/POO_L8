package engine.game.chess;

import engine.game.board.Board;
import engine.game.board.GameAction;
import engine.game.board.Piece;
import engine.game.board.Vector;

import java.util.Objects;

/**
 * Action for promotion
 * @author Alen Bijelic
 * @author Nelson Jeanrenaud
 */
public class Promote implements GameAction<Chess> {

    /**
     * Perform promotion
     * @param start Vector from where the action starts
     * @param destination Vector to where the action ends
     * @param chess Concerned Board on which the action will be performed
     * @return Affected piece
     */
    @Override
    public Piece<Chess> doAction(Vector start, Vector destination, Board<Chess> chess) {
        Objects.requireNonNull(start, "start vector must be non null");
        Objects.requireNonNull(destination, "destination vector must be non null");
        Objects.requireNonNull(chess, "chess board must be non null");

        Chess.ChessPiece candidatePiece = chess.self().getPieceAtPosition(destination);
        Piece<Chess> affectedPiece = null;
        if(destination.getJ() == candidatePiece.getColor().getPromotionRow()) {
            Chess.ChessPiece piece = chess.self().getPromotedPiece();
            if(piece != null){
                affectedPiece = chess.self().removePieceAtPosition(destination);
                chess.setPieceAtPosition(piece, destination);
            }
        }
        return affectedPiece;
    }

    /**
     * Revert promotion
     * @param start Vector from where the action started
     * @param destination Vector to where the action ended
     * @param affectedPiece Affected pieces by the action
     * @param chess Concerned Board on which the action will be performed
     */
    @Override
    public void revertAction(Vector start, Vector destination, Piece<Chess> affectedPiece, Board<Chess> chess) {
        Objects.requireNonNull(start, "start vector must be non null");
        Objects.requireNonNull(destination, "destination vector must be non null");
        Objects.requireNonNull(chess, "chess board must be non null");

        if(affectedPiece != null){
            chess.setPieceAtPosition(affectedPiece, destination);
        }
    }
}