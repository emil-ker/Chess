package ViewControl; // denna borde flyttas till paketet GameBoard /E

import GamePieces.GamePiece;

import javax.swing.*;
import java.awt.*;

public class BoardTile extends JButton { // want package private or proteced access level
    /**
     A class representing a tile on an IRL BoardGame. Is both visible as a JButton and invisible at the same time as
     it has attributes that the user playing the game doesn't see (for instance the contained piece).
     */
    private final int i;
    private final int j;
    private GamePiece piece;
    private Color usualColor; // color when not clicked/marked by user.
    private Color activeColor;

    BoardTile(int i, int j) {
        super();
        this.i = i;
        this.j = j;
    }

    void resetToUsualColor(){
        super.setBackground(usualColor);
    }

    void setToActiveColor(){
        super.setBackground(activeColor);
    }

    void setBackground(Color usualColor, Color activeColor){ // Jbutton setbackground() should not be used!!!
        this.usualColor = usualColor;
        this.activeColor = activeColor;
        super.setBackground(usualColor);
    }

    int[] getPosition() { return new int[] {i, j}; }

    GamePiece getPiece() { return piece; }

    boolean hasPiece() { return piece != null; }

    void setPiece(GamePiece newPiece) {
        piece = newPiece;
        this.setIcon(newPiece.getIcon());
    }

    void removePiece() {
        piece = null;
        this.setIcon(null);
    }

}
