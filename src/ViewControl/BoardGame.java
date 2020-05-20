package ViewControl;

import java.awt.*;

public interface BoardGame {
    /**
     Interface representing a standard tilebased boardgame where X players take turns to move pieces on a board.
     */
    boolean move(int i, int j);
    boolean[][] getValidMoves();
    String getStatus(int i, int j);
    String getMessage();
    String getTurn();
    Color[] getTileColors();
    Color getActiveTilesColor();
    int[][] getTilesToUpdate();
}
