package engine.game.chess;

import engine.game.board.*;
import engine.game.board.Vector;

import java.util.*;

/**
 * Action and condition for roque
 * @author Alen Bijelic
 * @author Nelson Jeanrenaud
 */
public class Roque extends MustNotCheck implements GameAction<Chess>, GameCondition<Chess> {

    private static final Set<Chess.ChessPieceType> CAN_ROQUE_WITH = EnumSet.of(
            Chess.ChessPieceType.ROOK);

    /**
     * Perform roque
     * @param start Vector from where the action starts
     * @param destination Vector to where the action ends
     * @param chess Concerned Board on which the action will be performed
     * @return Affected Pieces
     */
    @Override
    public Piece<Chess> doAction(Vector start, Vector destination, Board<Chess> chess) {
        Objects.requireNonNull(start, "start vector must be non null");
        Objects.requireNonNull(destination, "destination vector must be non null");
        Objects.requireNonNull(chess, "chess board must be non null");
        movePiece(start, getPieceRoqueDestination(start, destination), chess);
        movePiece(getPieceRoqueWithPosition(start, destination, chess), getPieceRoqueWithDestination(start, destination, chess), chess);
        return null;
    }

    /**
     * Revert roque
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
        movePiece(getPieceRoqueDestination(start, destination), start, chess);
        movePiece(getPieceRoqueWithDestination(start, destination, chess), getPieceRoqueWithPosition(start, destination, chess), chess);
    }

    /**
     * Get the Rook with which roque
     * @param start Vector from where the action started
     * @param destination Vector to where the action ended
     * @param chess Concerned Board on which the action will be performed
     * @return Roquing with Rook position
     */
    private Vector getPieceRoqueWithPosition(Vector start, Vector destination, Board<Chess> chess){
        return (destination.getI() < start.getI())
                ? new Vector(0, start.getJ())
                : new Vector(chess.self().getLENGTH() - 1, start.getJ());
    }

    /**
     * Get Rook destination
     * @return Rook destination position
     */
    private Vector getPieceRoqueDestination(Vector start, Vector destination) {
        return start.add(new Vector(destination.getI() - start.getI(), destination.getJ() - start.getJ()));
    }

    /**
     * Get Rook destination
     * @param start Vector from where the action started
     * @param destination Vector to where the action ended
     * @param chess Concerned Board on which the action will be performed
     * @return Rook destination position
     */
    private Vector getPieceRoqueWithDestination(Vector start, Vector destination, Board<Chess> chess) {
        Vector pieceRoqueVector = getPieceRoqueDestination(start, destination);
        Vector pieceRoqueWithVector = getPieceRoqueWithPosition(start, destination, chess);

        return  pieceRoqueWithVector.getI() < pieceRoqueVector.getI()
                ? new Vector(pieceRoqueVector.getI() + 1, pieceRoqueVector.getJ())
                : new Vector(pieceRoqueVector.getI() - 1, pieceRoqueVector.getJ());
    }

    /**
     * Move pieces concerned by Roque
     * @param start Vector from where the action started
     * @param destination Vector to where the action ended
     * @param chess Concerned Board on which the action will be performed
     */
    protected void movePiece(Vector start, Vector destination, Board<Chess> chess) {
        Piece<Chess> piece = chess.self().removePieceAtPosition(start);
        chess.self().setPieceAtPosition(piece, destination);
    }

    /**
     * Check if all conditions for a Roque are valid
     * @param start Vector from where the action starts
     * @param destination Vector to where the action ends
     * @param chess Concerned ChessBoard
     * @return Either the condition is valid or not
     */
    @Override
    public boolean checkCondition(Vector start, Vector destination, Board<Chess> chess) {
        Objects.requireNonNull(start, "start vector must be non null");
        Objects.requireNonNull(destination, "destination vector must be non null");
        Objects.requireNonNull(chess, "chess board must be non null");

        Chess.ChessPiece pieceOnStart = chess.self().getPieceAtPosition(start);
        if(pieceOnStart == null)
            return false;

        Chess.ChessPiece pieceOnDestination = chess.self().getPieceAtPosition(getPieceRoqueWithPosition(start, destination, chess));

        Vector legitVector = new Vector(2, 0);
        Vector movementVector = new Vector(destination.getI() - start.getI(), destination.getJ() - start.getJ());

        int begin = movementVector.getI() < 0 ? 1 : start.getI() + 1;
        int end = movementVector.getI() < 0 ? start.getI() - 1 : chess.self().getLENGTH() - 1;

        // Check for Piece between the piece who Roque and the piece we're roquing with
        for(int i = begin; i < end; ++i) {
            if(chess.self().getPieceAtPosition(new Vector(i, start.getJ())) != null){
                return false;
            }
        }

        return  pieceOnDestination != null
                && CAN_ROQUE_WITH.contains(pieceOnDestination.getPieceType())
                && legitVector.areCollinear(movementVector)
                && legitVector.norm() == movementVector.norm()
                && !chess.self().hasMoved(pieceOnStart)
                && !chess.self().hasMoved(pieceOnDestination)
                && super.checkCondition(start, destination, chess);
    }
}