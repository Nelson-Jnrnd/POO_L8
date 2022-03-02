package engine.game.board;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Board modelise a board of Squares in which different action can be made
 * @author Alen Bijelic
 * @author Nelson Jeanrenaud
 * @param <T> Type of Board
 */
public abstract class Board<T extends Board<T>> {
    private final int LENGTH;
    private final int HEIGHT;
    private final ArrayList<Square<T>> boardArray;

    protected final Historic<T> historicMoves;

    /**
     * Board Constructor
     * @param length Boards length
     * @param height Boards height
     */
    protected Board(int length, int height){
        if(length <= 0|| height <= 0)
            throw new IllegalArgumentException("Size must be above 0");
        LENGTH = length;
        HEIGHT = height;
        boardArray = new ArrayList<>();
        historicMoves = new Historic<>();
        // populate board
        for (int j = 0; j < HEIGHT; j++) {
            for (int i = 0; i < LENGTH; i++) {
                boardArray.add(new Square<>(i, j));
            }
        }
    }

    /**
     * Start a new game
     */
    protected void startGame(){
        initPieces();
    }

    /**
     * Init pieces on the board
     */
    protected void initPieces(){
        emptyBoard();
    }

    /**
     * Move request
     * @param from Vector from which the request move is made
     * @param to Vector to which the request move is made
     * @return Either the move can be made or not
     */
    public boolean move(Vector from, Vector to) {
        Objects.requireNonNull(from, "from vector must be non null");
        Objects.requireNonNull(from, "to vector must be non null");

        Square<T> fromSquare = getSquareAtPosition(from);

        // Si y a bien une pièce
        Piece<T> selectedPiece = fromSquare.getPiece();
        if(selectedPiece != null){
            return selectedPiece.move(from, to, true);
        }
        return false;
    }

    /**
     * Get the length of the board
     * @return The length of the board
     */
    public int getLENGTH() {
        return LENGTH;
    }

    /**
     * Get the height of the board
     * @return The height of the board
     */
    public int getHEIGHT() {
        return HEIGHT;
    }

    /**
     * Set piece at a given position
     * @param piece Piece to set at given position
     * @param position Position to set the piece on
     * @return The piece on the given position
     */
    public Piece<T> setPieceAtPosition(Piece<T> piece, Vector position){
        Objects.requireNonNull(piece, "piece must be non null");
        Objects.requireNonNull(position, "position must be non null");
        if (position.getI() < 0 || position.getJ() < 0 || position.getI() >= HEIGHT || position.getJ() >= LENGTH) {
            throw new IllegalArgumentException("Position is out of bounds");
        }
        else {
            Square<T> c = getSquareAtPosition(position);
            c.setPiece(piece);
            return piece;
        }
    }

    /**
     * Set piece at a given position by indexes
     * @param piece Piece to set at given indexes
     * @param i Index i to set the piece on
     * @param j Index j to set the piece on
     * @return The piece on the given position
     */
    public Piece<T> setPieceAtPosition(Piece<T> piece, int i, int j) {
        return setPieceAtPosition(piece, new Vector(i, j));
    }

    /**
     * Get the piece at a given position
     * @param position The position to get the piece
     * @return The piece at the given position
     */
    public Piece<T> getPieceAtPosition(Vector position){
        return getSquareAtPosition(Objects.requireNonNull(position, "position must be non null")).getPiece();
    }

    /**
     * Remove piece at a given position
     * @param position Position to remove the piece
     * @return The removed piece
     */
    public Piece<T> removePieceAtPosition(Vector position){
        return getSquareAtPosition(Objects.requireNonNull(position, "position must be non null")).removePiece();
    }

    /**
     * Move the position at a given position
     * @param from Vector from where to move the piece
     * @param to Vector to where to move the piece
     * @return The piece at the new position
     */
    public Piece<T> movePieceAtPosition(Vector from, Vector to){
        return setPieceAtPosition(removePieceAtPosition(Objects.requireNonNull(from, "from vector must be non null")), Objects.requireNonNull(to, "to vector must be non null"));
    }

    /**
     * Get square at a given position
     * @param position Position to get the square
     * @return The square at the given position
     */
    private Square<T> getSquareAtPosition(Vector position){
        Objects.requireNonNull(position);
        if(position.getI() >= 0 && position.getJ() >= 0 && position.getI() < HEIGHT && position.getJ() < LENGTH) {
            return boardArray.get(position.getI() + position.getJ() * LENGTH);
        }
        throw new IllegalArgumentException("Position is out of bounds");
    }

    /**
     * Remove all pieces from the board
     */
    public void emptyBoard(){
        for (Square<T> c : boardArray) {
            c.removePiece();
        }
    }

    /**
     * Search for a piece in the board
     * @param pieceToSearch Piece to be searched
     * @return The list off all pieces found
     */
    public ArrayList<Vector> searchPieces(Piece<T> pieceToSearch){
        ArrayList<Vector> foundPieces = new ArrayList<>();
        if(pieceToSearch == null){
            return foundPieces;
        }
        for (Square<T> c : boardArray) {
            Piece<T> pieceOnPosition = c.getPiece();
            if(pieceToSearch.equals(pieceOnPosition)){
                foundPieces.add(c.getPosition());
            }
        }
        return foundPieces;
    }

    /**
     * Intern square class
     * @param <T> Type of Square
     */
    private static class Square<T extends Board<T>> {
        private Piece<T> piece;
        private final int X;
        private final int Y;

        /**
         * Square constructor
         * @param x Position x of the square
         * @param y Position y of the square
         */
        private Square(int x, int y) {
            if(x < 0 || y < 0)
                throw new IllegalArgumentException("Square position can't be under 0");
            X = x;
            Y = y;
        }

        /**
         * Get the piece on the square
         * @return
         */
        private Piece<T> getPiece() {
            return piece;
        }

        /**
         * Set a piece on the square
         * @param piece
         */
        private void setPiece(Piece<T> piece) {
            Objects.requireNonNull(piece);
            this.piece = piece;
        }

        /**
         * Get the position of a square
         * @return
         */
        private Vector getPosition(){
            return new Vector(X, Y);
        }

        /**
         * Remove piece from the board
         * @return The removed piece
         */
        private Piece<T> removePiece(){
            Piece<T> returnPiece = piece;
            piece = null;
            return returnPiece;
        }
    }

    /**
     * Check if the piece has moved on the board
     * @param piece Piece to be checked
     * @return Either the piece has moved or not
     */
    public boolean hasMoved(Piece<T> piece){
        return historicMoves.isPieceContained(piece);
    }

    /**
     * Check if the move a move is the last one executed
     * @param move move we are looking for
     * @return true if the move given is the last one executed
     */
    public boolean isLastAction(Move<T> move){
        return historicMoves.isLastAction(move);
    }

    /**
     * Returns the last piece moved in the historic
     * @return
     */
    public Piece<T> lastPieceMoved(){
        return historicMoves.lastPieceMoved();
    }

    /**
     * Return itself from the most specific viewpoint
     * Exemple : A board of chess would return itself as a Chess class
     * @return
     */
    public abstract T self();
}
