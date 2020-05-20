package GamePieces;

import javax.swing.*;

public class Pawn extends GamePiece {

    public Pawn(String color) {
        super(color, new ImageIcon("Projekt/src/resources/" + color + "_pawn.png"));
    }

    public String toString() {
        return "this is a " + this.getClass().getSimpleName() + " " + super.toString();
    }
}
