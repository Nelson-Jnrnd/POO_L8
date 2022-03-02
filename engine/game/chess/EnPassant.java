package engine.game.chess;

import engine.game.board.*;
import java.util.Objects;

/**
 * Action and condition for En passant special move
 * @author Alen Bijelic
 * @author Nelson Jeanrenaud
 */
public class EnPassant implements GameAction<Chess>, GameCondition<Chess> {

    /**
     * Check condition for En passant
     * @param start Vector from where the action starts
     * @param destination Vector to where the action ends
     * @param chess Concerned Chessboard
     * @return Either the condition for En passant is accepted
     */
    @Override
    public boolean checkCondition(Vector start, Vector destination, Board<Chess> chess) {
        Objects.requireNonNull(start, "start vector must be non null");
        Objects.requireNonNull(destination, "destination vector must be non null");
        Objects.requireNonNull(chess, "chess board must be non null");
        Chess.ChessPiece pieceToEat = chess.self().getPieceAtPosition(eatPosition(start, destination));
        return (pieceToEat instanceof Chess.Pawn
            && chess.lastPieceMoved().equals(pieceToEat)
            && (chess.isLastAction(chess.self().getPawnStraight2Up()) || (chess.isLastAction(chess.self().getPawnStraight2Down()))));
    }

    /**
     * Get the victim of the En passant
     * @param start Start position
     * @param destination Destination position
     * @return Position of the victime piece
     */
    protected Vector eatPosition(Vector start, Vector destination){
        return new Vector(destination.getI(), start.getJ());
    }

    /**
     * Perform En passant action
     * @param start Vector from where the action starts
     * @param destination Vector to where the action ends
     * @param chess Concerned ChessBoard
     * @return Eaten piece
     */
    @Override
    public Piece<Chess> doAction(Vector start, Vector destination, Board<Chess> chess) {
        Objects.requireNonNull(start, "start vector must be non null");
        Objects.requireNonNull(destination, "destination vector must be non null");
        Objects.requireNonNull(chess, "chess board must be non null");
        return chess.removePieceAtPosition(eatPosition(start, destination));
    }

    /**
     * Revert En passant action
     * @param start Vector from where the action started
     * @param destination Vector to where the action ended
     * @param affectedPiece Affected pieces by the action
     * @param chess Concerned ChessBoard
     */
    @Override
    public void revertAction(Vector start, Vector destination, Piece<Chess> affectedPiece, Board<Chess> chess) {
        Objects.requireNonNull(start, "start vector must be non null");
        Objects.requireNonNull(destination, "destination vector must be non null");
        Objects.requireNonNull(chess, "chess board must be non null");
        if(affectedPiece != null)
            chess.setPieceAtPosition(affectedPiece, eatPosition(start, destination));
    }
}
