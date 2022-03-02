package engine.game.chess;

import engine.game.board.Board;
import engine.game.board.Move;
import engine.game.board.Piece;
import engine.game.board.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Chessboard
 * @author Alen Bijelic
 * @author Nelson Jeanrenaud
 */
public class Chess extends Board<Chess> {

    /**
     * All types of chess pieces
     */
    public enum ChessPieceType {
        PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING
    }

    /**
     * All directions available
     */
    public enum Direction {
        DOWN{
            /**
             * Starting at edge
             * @return Edge index
             */
            @Override
            protected int startingEdge() {
                return SIZE - 1;
            }

            /**
             * Starting at edge
             * @param value Value from edge
             * @return Edge without value index
             */
            @Override
            protected int startingEdge(int value) {
                return startingEdge() - value;
            }

            /**
             * Get oppposite Direction
             * @return Opposite Direction
             */
            @Override
            protected Direction opposite() {
                return UP;
            }

            /**
             * Get adjacent Directions
             * @return Array of adjacent Direction
             */
            @Override
            protected Direction[] adjacent() {
                return new Direction[] {LEFT, RIGHT};
            }
        }, LEFT{
            /**
             * Starting at edge
             * @return Edge index
             */
            @Override
            protected int startingEdge() {
                return 0;
            }

            /**
             * Starting at edge
             * @param value Value from edge
             * @return Edge with value index
             */
            @Override
            protected int startingEdge(int value) {
                return startingEdge() + value;
            }

            /**
             * Get the opposite Direction
             * @return Opposite direction
             */
            @Override
            protected Direction opposite() {
                return RIGHT;
            }

            /**
             * Get adjacent directions
             * @return Array of adjacent directions
             */
            @Override
            protected Direction[] adjacent() {
                return new Direction[]{UP, DOWN};
            }
        }, UP{
            /**
             * Starting at edge
             * @return Edge index
             */
            @Override
            protected int startingEdge() {
                return 0;
            }

            /**
             * Starting at edge
             * @param value Value from edge
             * @return Edge with value index
             */
            @Override
            protected int startingEdge(int value) {
                return startingEdge() + value;
            }

            /**
             * Get the opposite Direction
             * @return Opposite direction
             */
            @Override
            protected Direction opposite() {
                return DOWN;
            }

            @Override
            protected Direction[] adjacent() {
                return new Direction[] {LEFT, RIGHT};
            }
        }, RIGHT{
            /**
             * Starting at edge
             * @return Edge index
             */
            @Override
            protected int startingEdge() {
                return SIZE - 1;
            }

            /**
             * Starting at edge
             * @param value Value from edge
             * @return Edge without value index
             */
            @Override
            protected int startingEdge(int value) {
                return startingEdge() - value;
            }

            /**
             * Get the opposite Direction
             * @return Opposite direction
             */
            @Override
            protected Direction opposite() {
                return LEFT;
            }

            /**
             * Get adjacent directions
             * @return Array of adjacent directions
             */
            @Override
            protected Direction[] adjacent() {
                return new Direction[] {UP, DOWN};
            }
        };

        /**
         * Get index of the starting edge
         * @return Index of the starting edge
         */
        protected abstract int startingEdge();

        /**
         * Get index of the starting edge including value
         * @param value Value from edge
         * @return Edge with value index
         */
        protected abstract int startingEdge(int value);

        /**
         * Get the opposite Direction
         * @return Opposite direction
         */
        protected abstract Direction opposite();

        /**
         * Get adjacent directions
         * @return Array of adjacent directions
         */
        protected abstract Direction[] adjacent();
    }

    private static final int SIZE = 8;
    private final ChessColor FIRST_COLOR = ChessColor.WHITE;

    private boolean isStarted;
    private ChessColor turn;

    Move<Chess> getPawnStraight2Up() {
        return pawnStraight2Up;
    }
    Move<Chess> getPawnStraight2Down() {
        return pawnStraight2Down;
    }

    // Rules...
    private Promote promote;
    private Roque roque;
    private EnPassant pawnEnPassant;
    private EatPiece eatAction;
    private MoveChessPiece moveChessPiece;
    private MustEat mustEat;
    private CanNotEat cannotEat;
    private OnlyFirstMove onlyFirstMove;
    private MustNotCollide noCollision;

    /**
     * Get turn color
     * @return Turn color
     */
    public ChessColor getTurn() {
        return turn;
    }


    // Moves...
    private Move<Chess> kingHorizontalStraights;
    private Move<Chess> kingVerticalStraights;
    private Move<Chess> kingDiagonals;
    private Move<Chess> kingGrandRoque;
    private Move<Chess> kingPetitRoque;

    private Move<Chess> pawnStraight1Up;
    private Move<Chess> pawnStraight2Up;
    private Move<Chess> pawnEat1Up;
    private Move<Chess> pawnEnPassantUp;
    private Move<Chess> pawnStraight1Down;
    private Move<Chess> pawnStraight2Down;
    private Move<Chess> pawnEat1Down;
    private Move<Chess> pawnEnPassantDown;

    private Move<Chess> knightL;
    private Move<Chess> knightL2;

    private Move<Chess> horizontalStraights;
    private Move<Chess> verticalStraights;
    private Move<Chess> diagonals;

    /**
     * Chess constructor
     */
    public Chess(){
        super(SIZE, SIZE);
        isStarted = false;
        initRules();
        initMoves();
    }

    /**
     * Start a game
     */
    @Override
    public void startGame() {
        turn = FIRST_COLOR;
        isStarted = true;
        super.startGame();
    }

    /**
     * Init legit moves in chess
     */
    protected void initMoves() {
        horizontalStraights = new Move<>(new Vector(super.getLENGTH(), 0), false, true, List.of(noCollision), List.of(eatAction));
        verticalStraights = new Move<>(new Vector(0, super.getHEIGHT()), true, false, List.of(noCollision), List.of(eatAction));
        diagonals = new Move<>(new Vector(super.getLENGTH(), super.getHEIGHT()), true, true, List.of(noCollision), List.of(eatAction));

        kingGrandRoque = new Move<>(new Vector(-4, 0), false, false, List.of(onlyFirstMove, noCollision, roque), List.of(roque));
        kingPetitRoque = new Move<>(new Vector(3, 0), false, false, List.of(onlyFirstMove,  noCollision, roque), List.of(roque));
        kingHorizontalStraights = new Move<>(new Vector(1, 0), true, true, List.of(noCollision), List.of(eatAction));
        kingVerticalStraights = new Move<>(new Vector(0, 1), true, false, List.of(noCollision), List.of(eatAction));
        kingDiagonals = new Move<>(new Vector(1, 1), true, true, List.of(noCollision), List.of(eatAction));

        pawnStraight1Up = new Move<>(new Vector(0, 1), false, false, List.of(noCollision, cannotEat), List.of(moveChessPiece, promote));
        pawnEat1Up = new Move<>(new Vector(1, 1), false, true, List.of(noCollision, mustEat), List.of(eatAction, promote));
        pawnStraight2Up = new Move<>(new Vector(0,2), false, false, List.of(noCollision, cannotEat, onlyFirstMove), List.of(moveChessPiece));
        pawnEnPassantUp = new Move<>(new Vector(1, 1), false, true, List.of(noCollision, pawnEnPassant), List.of(pawnEnPassant, moveChessPiece));

        pawnStraight1Down = new Move<>(new Vector(0, -1), false, false, List.of(noCollision, cannotEat), List.of(moveChessPiece, promote));
        pawnEat1Down = new Move<>(new Vector(-1, -1), false, true, List.of(noCollision, mustEat), List.of(eatAction, promote));
        pawnStraight2Down = new Move<>(new Vector(0,-2), false, false, List.of(noCollision, cannotEat, onlyFirstMove), List.of(moveChessPiece));
        pawnEnPassantDown = new Move<>(new Vector(-1, -1), false, true, List.of(noCollision, pawnEnPassant), List.of(pawnEnPassant, moveChessPiece));

        knightL = new Move<>(new Vector(2, 1), true, true, null, List.of(eatAction));
        knightL2 = new Move<>(new Vector(1, 2), true, true, null, List.of(eatAction));
    }

    /**
     * Init all chess rules
     */
    protected void initRules(){
        promote = new Promote();
        roque = new Roque();
        pawnEnPassant = new EnPassant();

        eatAction = new EatPiece();
        moveChessPiece = new MoveChessPiece();
        mustEat = new MustEat();
        cannotEat = new CanNotEat();
        onlyFirstMove = new OnlyFirstMove();
        noCollision = new MustNotCollide();
    }

    /**
     * Init all pieces available in chess
     */
    @Override
    protected void initPieces() {
        super.initPieces();
        for (ChessColor color: ChessColor.values()) {
            // Pawns
            for (int i = 0; i < getLENGTH(); i++) {
                setPieceAtPosition(new Pawn(color, this), color.getDirection().adjacent()[0].startingEdge(i),
                        color.getDirection().startingEdge(Pawn.STARTING_ROW_FROM_EDGE));
            }
            setPieceAtPosition(new Queen(color, this), color.getDirection().adjacent()[0].startingEdge(Queen.STARTING_COLUMN_FROM_EDGE),
                    color.getDirection().startingEdge(Queen.STARTING_ROW_FROM_EDGE));
            setPieceAtPosition(new King(color, this), color.getDirection().adjacent()[0].startingEdge(King.STARTING_COLUMN_FROM_EDGE),
                    color.getDirection().startingEdge(King.STARTING_ROW_FROM_EDGE));

            // for symetric pairs
            for (Direction d: color.getDirection().adjacent()) {
                setPieceAtPosition(new Rook(color, this), d.startingEdge(Rook.STARTING_COLUMN_FROM_EDGE),
                        color.getDirection().startingEdge(Rook.STARTING_ROW_FROM_EDGE));

                setPieceAtPosition(new Knight(color, this), d.startingEdge(Knight.STARTING_COLUMN_FROM_EDGE),
                        color.getDirection().startingEdge(Knight.STARTING_ROW_FROM_EDGE));

                setPieceAtPosition(new Bishop(color, this), d.startingEdge(Bishop.STARTING_COLUMN_FROM_EDGE),
                        color.getDirection().startingEdge(Bishop.STARTING_ROW_FROM_EDGE));
            }

        }
    }

    /**
     * Move a piece
     * @param from Vector from which the request move is made
     * @param to Vector to which the request move is made
     * @return Either the move is made or not
     */
    public boolean move(Vector from, Vector to){
        Objects.requireNonNull(from, "from vector must be non null");
        Objects.requireNonNull(to, "to vector must be non null");
        if(!isStarted)
            return false;

        boolean status = false;
        ChessPiece movedPiece = getPieceAtPosition(from);
        if(movedPiece != null
                && movedPiece.getColor() == turn
                && super.move(from, to)){

            turn = turn.next();
            if(checkmate(turn)) {
                endGame(turn.next());
            }
            status = true;

        }

        return status;
    }

    /**
     * End the current game
     * @param winner Winner color
     */
    protected void endGame(ChessColor winner){
        Objects.requireNonNull(winner, "winner must be non null");
        isStarted = false;
    }

    /**
     * Get the current chess
     * @return Current chess
     */
    @Override
    public Chess self() {
        return this;
    }

    /**
     * Check for King in check
     * @param defendingColor Defending color
     * @return Either the King is in check or not
     */
    public boolean check(ChessColor defendingColor){
        Objects.requireNonNull(defendingColor, "defending color must be non null");
        // Search for the King
        for (Vector positionKing : searchPieces(new King(defendingColor, this))) {
            if(isAttacked(defendingColor, positionKing))
                return true;
        }
        return false;
    }

    /**
     * Check for checkmate
     * @param defendingColor Defending color
     * @return Either the King is checkmated or not
     */
    public boolean checkmate(ChessColor defendingColor){
        Objects.requireNonNull(defendingColor, "defending color must be non null");
        if(!check(defendingColor)){
            return false;
        }
        // Search for the King
        for (Vector positionKing : searchPieces(new King(defendingColor, this))) {
            // for each pieces that player controls
            for (Vector positionPiece : searchPieces(defendingColor)) {
                if(getPieceAtPosition(positionPiece).possibleMoves(positionPiece).size() > 0){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Get piece at a given position
     * @param position The position to get the piece
     * @return Piece on given position
     */
    @Override
    public ChessPiece getPieceAtPosition(Vector position) {
        return (ChessPiece) super.getPieceAtPosition(position);
    }

    /**
     * Remove piece at a given position
     * @param position Position to remove the given piece
     * @return Removed piece
     */
    @Override
    public ChessPiece removePieceAtPosition(Vector position) {
        return (ChessPiece) super.removePieceAtPosition(position);
    }

    /**
     * Set piece at given position
     * @param piece Piece to set at given indexes
     * @param i Index i to set the piece on
     * @param j Index j to set the piece on
     * @return Set piece
     */
    @Override
    public ChessPiece setPieceAtPosition(Piece<Chess> piece, int i, int j) {
        return (ChessPiece) super.setPieceAtPosition(piece, i, j);
    }

    /**
     * Move a piece to a given position
     * @param from Vector from where to move the piece
     * @param to Vector to where to move the piece
     * @return Moved piece
     */
    @Override
    public ChessPiece movePieceAtPosition(Vector from, Vector to) {
        return (ChessPiece) super.movePieceAtPosition(from, to);
    }

    /**
     * Lists all pieces of the same color
     * @param colorToSearch Piece color to search
     * @return List of all position of pieces of the same color
     */
    public ArrayList<Vector> searchPieces(ChessColor colorToSearch){
        Objects.requireNonNull(colorToSearch, "color to search must be non null");
        ArrayList<Vector> foundPieces = new ArrayList<>();
        for (int i = 0; i < getLENGTH(); i++) {
            for (int j = 0; j < getHEIGHT(); j++) {
                Vector pos = new Vector(i, j);
                ChessPiece pieceOnPosition = getPieceAtPosition(pos);
                if (pieceOnPosition != null && pieceOnPosition.getColor() == colorToSearch) {
                    foundPieces.add(pos);
                }
            }
        }
        return foundPieces;
    }

    /**
     * Check if the piece is attacked
     * @param defendingColor Defending piece color
     * @param position Position to check if attacked
     * @return Either the pice at position is attacked or not
     */
    public boolean isAttacked(ChessColor defendingColor, Vector position) {
        Objects.requireNonNull(defendingColor, "defending color must be non null");
        Objects.requireNonNull(position, "position vector must be non null");

        for (int i = 0; i < getLENGTH(); i++) {
            for (int j = 0; j < getHEIGHT(); j++) {
                ChessPiece pieceOnPosition = getPieceAtPosition(new Vector(i, j));
                if (pieceOnPosition != null && !pieceOnPosition.getColor().equals(defendingColor)) {
                    if (pieceOnPosition.move(i, j, position.getI(), position.getJ(), false)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Check if a move is  produce a check
     * @param piece Piece to check on
     * @param start Starting position
     * @param destination Ending position
     * @param moveType Move type
     * @return Either the move is producing a check or not
     */
    public boolean doesMoveCheck(ChessPiece piece, Vector start, Vector destination, Move<Chess> moveType){
        Objects.requireNonNull(piece, "piece must be non null");
        Objects.requireNonNull(start, "start vector must be non null");
        Objects.requireNonNull(destination, "destination vector must be non null");
        Objects.requireNonNull(moveType, "moveType must be non null");
        piece.doMove(start, destination, moveType);
        boolean isCheck = check(turn);
        historicMoves.revertLastMove();
        return isCheck;
    }

    /**
     * Get promoted piece
     * @return Affected piece
     */
    public ChessPiece getPromotedPiece(){
        return new Queen(getTurn(), this);
    }

    /**
     * Available chess pieces
     */
    public abstract class ChessPiece extends  Piece<Chess>{
        private final ChessColor color;

        /**
         * ChessPiece constructor
         * @param color Piece color
         * @param board Concerned Board
         * @param moves List of all moves
         */
        public ChessPiece(ChessColor color, Chess board, List<Move<Chess>> moves) {
            super(Objects.requireNonNull(board, "board must be non null"), moves);
            this.color = Objects.requireNonNull(color, "color must be non null");
        }

        /**
         * Get the piece color
         * @return Piece color
         */
        public ChessColor getColor() {
            return color;
        }

        /**
         * Check if a move is legit
         * @param start Vector from where the action starts
         * @param destination Vector to where the action ends
         * @param moveType Move type
         * @return Either the move is legit or not
         */
        @Override
        protected boolean canMove(Vector start, Vector destination, Move<Chess> moveType) {
            Objects.requireNonNull(start, "start vector must be non null");
            Objects.requireNonNull(destination, "destination vector must be non null");
            Objects.requireNonNull(moveType, "moveType must be non null");
            ChessPiece pieceEaten = getPieceAtPosition(destination);
            if(pieceEaten != null && pieceEaten.getColor() == getColor()){
                return false;
            }
            if(super.canMove(start, destination, moveType)){
                return !doesMoveCheck(this, start, destination, moveType);
            }
            return false;
        }

        /**
         * Perform a move
         * @param start Vector from where the move starts
         * @param destination Vector to where the move ends
         * @param moveType Move type
         */
        @Override
        protected void doMove(Vector start, Vector destination, Move<Chess> moveType) {
            Objects.requireNonNull(start, "start vector must be non null");
            Objects.requireNonNull(destination, "destination vector must be non null");
            Objects.requireNonNull(moveType, "moveType must be non null");
            super.doMove(start, destination, moveType);
        }

        /**
         * Get the piece type
         * @return Piece type
         */
        public abstract ChessPieceType getPieceType();

        /**
         * Genereate Chess hash code
         * @return
         */
        @Override
        public int hashCode() {
            return Objects.hash(getPieceType().hashCode(), getColor());
        }

        /**
         * Check if the Chess is equal to an object
         * @param o Object to check with
         * @return Either the Chess is equal to the object or not
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ChessPiece that = (ChessPiece) o;
            return color == that.color;
        }
    }

    /**
     * Bishop piece for Chess
     */
    public class Bishop extends ChessPiece{
        private static final int STARTING_COLUMN_FROM_EDGE = 2;
        private static final int STARTING_ROW_FROM_EDGE = 0;

        /**
         * Bishop constructor
         * @param color Color of the Bishop
         * @param chess Concerned chess
         */
        public Bishop(ChessColor color, Chess chess) {
            super(color, chess, List.of(diagonals));
        }

        /**
         * Get Bishop type
         * @return Bishop type
         */
        @Override
        public ChessPieceType getPieceType() {
            return ChessPieceType.BISHOP;
        }

    }

    /**
     * Rook piece for Chess
     */
    public class Rook extends ChessPiece{
        private static final int STARTING_COLUMN_FROM_EDGE = 0;
        private static final int STARTING_ROW_FROM_EDGE = 0;

        /**
         * Rook constructor
         * @param color Color of the rook
         * @param chess Concerned color
         */
        public Rook(ChessColor color, Chess chess) {
            super(color, chess, List.of(verticalStraights, horizontalStraights));
        }

        /**
         * Get Rook type
         * @return Rook type
         */
        @Override
        public ChessPieceType getPieceType() {
            return ChessPieceType.ROOK;
        }
    }

    /**
     * Queen piece for Chess
     */
    public class Queen extends ChessPiece{
        private static final int STARTING_COLUMN_FROM_EDGE = 3;
        private static final int STARTING_ROW_FROM_EDGE = 0;

        /**
         * Queen constructor with King
         * @param color Queen color
         * @param chess Concerned chess
         */
        public Queen(ChessColor color, Chess chess){
            super(color, chess, List.of(verticalStraights, horizontalStraights, diagonals));
        }

        /**
         * Get Queen type
         * @return Queen type
         */
        @Override
        public ChessPieceType getPieceType() {
            return ChessPieceType.QUEEN;
        }
    }

    /**
     * King piece for Chess
     */
    public class King extends ChessPiece{
        private static final int STARTING_COLUMN_FROM_EDGE = 4;
        private static final int STARTING_ROW_FROM_EDGE = 0;

        /**
         * King constructor
         * @param color Color of the King
         * @param chess Concerned chess
         */
        public King(ChessColor color, Chess chess){
            super(color, chess, List.of(kingVerticalStraights, kingHorizontalStraights, kingDiagonals, kingGrandRoque, kingPetitRoque));
        }

        /**
         * Get King type
         * @return King type
         */
        @Override
        public ChessPieceType getPieceType() {
            return ChessPieceType.KING;
        }
    }

    /**
     * Knight piece for Chess
     */
    public class Knight extends ChessPiece{
        private static final int STARTING_COLUMN_FROM_EDGE = 1;
        private static final int STARTING_ROW_FROM_EDGE = 0;

        /**
         * Knight constructor
         * @param color Color of the knight
         * @param chess Concerned chess
         */
        public Knight(ChessColor color, Chess chess){
            super(color, chess, List.of(knightL, knightL2));
        }

        /**
         * Get King type
         * @return King type
         */
        @Override
        public ChessPieceType getPieceType() {
            return ChessPieceType.KNIGHT;
        }
    }

    /**
     * Pawn piece for Chess
     */
    public class Pawn extends ChessPiece{
        private static final int STARTING_ROW_FROM_EDGE = 1;

        /**
         * Pawn constructor
         * @param color
         * @param chess
         */
        public Pawn(ChessColor color, Chess chess){
            super(color,
                    chess, color.getDirection() == Direction.UP ?
                            List.of(pawnStraight1Up, pawnStraight2Up, pawnEat1Up, pawnEnPassantUp)
                            : List.of(pawnStraight1Down, pawnStraight2Down, pawnEat1Down, pawnEnPassantDown));
        }

        /**
         * Get Pawn type
         * @return Pawn type
         */
        @Override
        public ChessPieceType getPieceType() {
            return ChessPieceType.PAWN;
        }

    }

}