package GamePieces;

import javax.swing.*;

public class Knight extends GamePiece {

    public Knight(String color) {
        super(color, new ImageIcon("Projekt/src/resources/" + color + "_knight.png"));
    }

    public String toString() {
        return "this is a " + this.getClass().getSimpleName() + " " + super.toString();
    }
}
