package engine.game.chess;

/**
 * Chess player color
 * @author Alen Bijelic
 * @author Nelson Jeanrenaud
 */
public enum ChessColor {
    WHITE("White"){
        /**
         * Get White Direction
         * @return
         */
        @Override
        Chess.Direction getDirection() {
            return Chess.Direction.UP;
        }

        /**
         * Get next turn Color
         * @return
         */
        @Override
        ChessColor next() {
            return BLACK;
        }
    },
    BLACK("Black"){
        /**
         * Get Black Direction
         * @return
         */
        @Override
        Chess.Direction getDirection() {
            return Chess.Direction.DOWN;
        }

        /**
         * Get next turn Color
         * @return
         */
        @Override
        ChessColor next() {
            return WHITE;
        }
    };

    @Override
    public String toString() {
        return text;
    }

    private final String text;

    ChessColor(String text) {
        this.text = text;
    }

    /**
     * Get Direction
     * @return
     */
    abstract Chess.Direction getDirection();

    /**
     * Get next turn Color
     * @return
     */
    abstract ChessColor next();

    /**
     * Get promotion row
     * @return Promotion row index
     */
    int getPromotionRow(){
        return getDirection().opposite().startingEdge();
    }
}
