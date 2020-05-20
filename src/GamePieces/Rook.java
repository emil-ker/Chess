package GamePieces;

import javax.swing.*;

public class Rook extends GamePiece {

    public Rook(String color) {
        super(color, new ImageIcon("Projekt/src/resources/" + color + "_rook.png"));
    }

    public String toString() {
        return "this is a " + this.getClass().getSimpleName() + " " + super.toString();
    }
}
