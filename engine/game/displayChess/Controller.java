package engine.game.displayChess;

import chess.ChessController;
import chess.ChessView;
import chess.views.console.ConsoleView;
import chess.views.gui.GUIView;

import java.util.Objects;

/**
 * Controller between engine and GUI
 * Entrypoint of the program
 * @author Alen Bijelic
 * @author Nelson Jeanrenaud
 */
public class Controller implements ChessController {
    private final DisplayChess chess;
    private ChessView view;

    /**
     * Controller constructor
     */
    public Controller() {
        chess = new DisplayChess(this);
    }

    /**
     * Get the view type
     * @return View type
     */
    public ChessView getView() {
        return view;
    }

    /**
     * Start a new game
     * @param view View in which to start
     */
    public void start(ChessView view) {
        this.view = Objects.requireNonNull(view, "Chessview must be non null");
        view.startView();
    }

    /**
     * Move a piece
     * @param fromX Start X value
     * @param fromY  Start Y value
     * @param toX Destination X value
     * @param toY Destination Y value
     * @return Either the piece can move or not
     */
    public boolean move(int fromX, int fromY, int toX, int toY) {
        // Déplacement à la même position impossible
        if(fromX != toX || fromY != toY){
            return chess.move(fromX, fromY, toX, toY);
        }
        return false;
    }

    /**
     * Start a new game
     */
    @Override
    public void newGame() {
        chess.startGame();
    }

    /**
     * Main programm
     * @param args Programm arguments
     */
    public static void main(String[] args) {
        if(args.length == 1){
            ChessController c = new Controller();
            switch (args[0]) {
                case "0":
                    c.start(new ConsoleView(c));
                    break;
                case "1":
                    c.start(new GUIView(c));
                    c.newGame();
                    break;
                default:
                    System.out.println("Invalid Gamemode : 0 -> Console | 1 -> Graphics");
                    System.exit(-1);
            }
        } else{
            System.out.println("Invalid Number of args");
            System.exit(-1);
        }
    }
}
