package GamePieces;

import javax.swing.*;

public class Bishop extends GamePiece {

    public Bishop(String color) {
        super(color, new ImageIcon("Projekt/src/resources/" + color + "_bishop.png"));
    }

    public String toString() {
        return "this is a " + this.getClass().getSimpleName() + " " + super.toString();
    }
}
