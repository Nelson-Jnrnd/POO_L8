package engine.game.board;

import chess.ChessView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Manage pieces move
 * @author Alen Bijelic
 * @author Nelson Jeanrenaud
 * @param <T> Type of Board
 */
public abstract class Piece<T extends Board<T>> implements ChessView.UserChoice {
    private final T board;
    private final List<Move<T>> movements;

    /**
     * Piece constructor
     * @param board The board type
     * @param moves List of legit moves
     */
    public Piece(T board, List<Move<T>> moves) {
        this.board = Objects.requireNonNull(board, "board must be non null");
        this.movements = moves;
    }

    /**
     * Get the board type
     * @return the board type
     */
    public T getBoard() {
        return board;
    }

    /**
     * Move a piece
     * @param fromX Start X value
     * @param fromY  Start Y value
     * @param toX Destination X value
     * @param toY Destination Y value
     * @param doMove Perform or simulate the move
     * @return Either the piece can move or not
     */
    public boolean move(int fromX, int fromY, int toX, int toY, boolean doMove){
        for (Move<T> moveType: movements) {
            Vector start = new Vector(fromX, fromY), destination = new Vector(toX, toY);
            if(canMove(start, destination, moveType)){
                if(doMove) {
                    doMove(start, destination, moveType);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a move can be made from start to destination
     * @param start Vector from where the action starts
     * @param destination Vector to where the action ends
     * @param moveType Move type
     * @return Either the move is legit or not
     */
    protected boolean canMove(Vector start, Vector destination, Move<T> moveType){
        Objects.requireNonNull(moveType, "the move type must be non null");
        return moveType.canMove(Objects.requireNonNull(start, "start vector must be non null"),
                Objects.requireNonNull(destination, "destination vector must be non null"), getBoard());
    }

    /**
     * Perform a move
     * @param start Vector from where the move starts
     * @param destination Vector to where the move ends
     * @param moveType Move type
     */
    protected void doMove(Vector start, Vector destination, Move<T> moveType){
        Objects.requireNonNull(start, "start vector must be non null");
        Objects.requireNonNull(destination, "destination vector must be non null");
        Objects.requireNonNull(moveType, "moveType must be non null");
        getBoard().historicMoves.add(this, moveType, start, destination, moveType.doMove(start, destination, getBoard()));
    }

    /**
     * Move a piece
     * @param start Vector from where the action starts
     * @param destination Vector to where the action ends
     * @param doMove Perform or simulate the move
     * @return Either the piece can move or not
     */
    public boolean move(Vector start, Vector destination, boolean doMove){
        Objects.requireNonNull(start, "start vector must be non null");
        Objects.requireNonNull(destination, "destination vector must be non null");
        return move(start.getI(), start.getJ(), destination.getI(), destination.getJ(), doMove);
    }

    /**
     * Lists all possible moves of a piece
     * @param start Start position of the piece
     * @return List of all possible moves
     */
    public List<Vector> possibleMoves(Vector start){
        Objects.requireNonNull(start, "start vector must be non null");
        List<Vector> possibleMoves = new ArrayList<>();
        for (Move<T> moveType: movements) {
            for (int i = 0; i < getBoard().getLENGTH(); i++) {
                for (int j = 0; j < getBoard().getHEIGHT() ; j++) {
                    Vector destination = new Vector(i, j);
                    if(canMove(start, destination, moveType)){
                        possibleMoves.add(destination);
                    }
                }
            }
        }
        return possibleMoves;
    }


    /**
     * Get Piece to a string formatted value
     * @return String formatted value of the class
     */
    @Override
    public String toString() {
        return textValue();
    }

    /**
     * Get the class name
     * @return Class name
     */
    public String textValue(){
        return getClass().getSimpleName();
    }
}
