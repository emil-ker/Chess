package GamePieces;

import javax.swing.*;

public class GamePiece {

    private String col;
    private final Icon pieceIcon;

    GamePiece(String color, ImageIcon pic) {
        col = color;
        this.pieceIcon = pic;
    }

    public String getCol() { return col; }

    public Icon getIcon() { return pieceIcon; }

}
