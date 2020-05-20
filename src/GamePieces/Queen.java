package GamePieces;

import javax.swing.*;

public class Queen extends GamePiece {

    public Queen(String color) {
        super(color, new ImageIcon("Projekt/src/resources/" + color + "_queen.png"));
    }

    public String toString() {
        return "this is a " + this.getClass().getSimpleName() + " " + super.toString();
    }
}
