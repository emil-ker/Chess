package GamePieces;

import javax.swing.*;

public class King extends GamePiece {

    public King(String color) {
        super(color, new ImageIcon("Projekt/src/resources/" + color + "_king.png"));
    }

    public String toString() {
        return "this is a " + this.getClass().getSimpleName() + " " + super.toString();
    }
}
