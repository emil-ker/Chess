package GameBoard;

import ViewControl.BoardGame;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class ChessBoard implements BoardGame {
    /**
     This is the underlying ChessBoard with the logic in the game in it. As such it handles initialisation of the board
     and all the following moves, checking for chess, checking for promotion etc.
     */

    private final String[][] board = new String[8][8]; // represents chess board
    private Color[] tileColors = {new Color(240, 240,240), Color.lightGray}; // to display for ViewControl
    private Color activeTilesColor = Color.yellow; //new Color(255, 255, 0, Math.round(0.7f*255));
    private final String[] colors = {"black", "white"}; // represent players, should maybe be changed to generalize
    private String activeColor = colors[1];
    private String dormantColor = colors[0];
    private String msg = activeColor.replace(activeColor.charAt(0), Character.toUpperCase(activeColor.charAt(0))) + "'s turn, choose a piece to move";
    private String activePiece = "";
    private int[] activePosition;
    private final int maxIndex = 7;
    private boolean chessFlag = false; // used to flag that a chessmove has been made for the "switchTurn()" function
    public ArrayList<String> whitePiecesTaken = new ArrayList<>();
    public ArrayList<String> blackPiecesTaken = new ArrayList<>();
    private int[] promotionTile; // has values if we at the end of turn need to promote pawn to queen
    private boolean[][] moveOk = new boolean[8][8]; // represents valid moves for activePiece (aka marked piece)

    public ChessBoard() {
        init_middle();
        init_pawns();
        for (int i = 0; i < 4; i++) { // this loop construct is used just to setup symmetrical pieces
            if (i == 0) {
                init_rooks(i);
            } else if (i == 1) {
                init_knights(i);
            } else if (i == 2) {
                init_bishops(i);
            } else {
                init_queens(i);
                init_kings(maxIndex-i);
            }
        }
    }

    private void init_rooks(int i) {
        board[0][i] = "black_rook";
        board[0][maxIndex - i] = "black_rook";
        board[7][i] = "white_rook";
        board[7][maxIndex - i] = "white_rook";
    }

    private void init_knights(int i) {
        board[0][i] = "black_knight";
        board[0][maxIndex - i] = "black_knight";
        board[7][i] = "white_knight";
        board[7][maxIndex - i] = "white_knight";
    }

    private void init_bishops(int i) {
        board[0][i] = "black_bishop";
        board[0][maxIndex - i] = "black_bishop";
        board[7][i] = "white_bishop";
        board[7][maxIndex - i] = "white_bishop";
    }

    private void init_queens(int i) {
        board[0][i] = "black_queen";
        board[7][i] = "white_queen";
    }

    private void init_kings(int i) {
        board[0][i] = "black_king";
        board[7][i] = "white_king";
    }

    private void init_pawns() {
        for (int i = 0; i < board[0].length; i++) {
            board[1][i] = "black_pawn";
            board[6][i] = "white_pawn";
        }
    }

    private void init_middle() {
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = "-";
            }
        }
    }

    public void switch_turn() {
        activeColor = activeColor.equals(colors[1]) ? colors[0]: colors[1];
        dormantColor = dormantColor.equals(colors[0]) ? colors[1]: colors[0];
        if(chessFlag){
            chessFlag = false; // if player moved into chess we don't want to do the msg change
            return;
        }
        msg = activeColor.replace(activeColor.charAt(0), Character.toUpperCase(activeColor.charAt(0))) + "'s turn, choose a piece to move";
    }

    public static void main(String[] args) {
        ChessBoard gb = new ChessBoard();
        for (String[] row: gb.board) {
            for (String tile: row) {
                System.out.print(String.format("%-12s\t", tile));
            }
            System.out.println();
        }
    }

    private void moveToEmpty(int i, int j) {
        String tmp = board[i][j];
        board[i][j] = activePiece;
        board[activePosition[0]][activePosition[1]] = tmp;
        checkForPromotion(i, j);
        checkForChess(i,j);
        activePiece = "";
        switch_turn();
    }

    private void moveToKill(int i, int j) {
        if (activeColor.equals("black")) whitePiecesTaken.add(board[i][j]);
        else blackPiecesTaken.add(board[i][j]);
        board[i][j] = activePiece;
        board[activePosition[0]][activePosition[1]] = "-";
        checkForPromotion(i, j);
        checkForChess(i,j);
        activePiece = "";
        switch_turn();
    }

    private void checkForChess(int played_i, int played_j) {
        System.out.println("Check for chess called");
        HashSet<int[]> threatPositions = new HashSet<int[]>();
        int[] opposingKingsPosition = new int[]{-37,-37}; //just to initialize to something

        //loopa igenom hela brädet, för varje pjäs av rätt färg få möjliga drag och se om något av de går till den andres kung.
        int counter = 1;
        for(int i=0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                String pieceName = board[i][j];
                if (pieceName.length()<5){
                    continue; //ignore empty Tiles
                }
                if(pieceName.substring(6).equals("king") && pieceName.substring(0, 5).equals(dormantColor)){
                    opposingKingsPosition = new int[]{i,j}; // get opposing players king's position
                }

                // get all positions activeColor threatens after a successful move
                if(pieceName.substring(0, 5).equals(activeColor)){
                    System.out.println(counter);
                    System.out.println(pieceName);
                    counter++;
                    activePiece = pieceName; // needed for calculateOkMoves() to work
                    calculateOkMoves(new int[]{i, j});

                    if(pieceName.equals("white_queen")){
                        System.out.println(board[0][4]);
                        System.out.println(moveOk[0][4]);
                    }

                     for(int inner_i=0; inner_i<8; inner_i++){
                        for(int inner_j=0; inner_j<8; inner_j++){
                            if (moveOk[inner_i][inner_j]){
                                System.out.println("threat");
                                threatPositions.add(new int[]{inner_i, inner_j});
                            }
                        }
                    }
                }
            }
        }

        System.out.println(threatPositions);
        for(int[] threatPosition: threatPositions) {
            if(Arrays.equals(threatPosition, opposingKingsPosition)){ //check if dormant color is in checked position
                System.out.println("CHECK!!!");
                chessFlag = true;
                msg = dormantColor.replace(dormantColor.charAt(0), Character.toUpperCase(dormantColor.charAt(0)))
                        + " is Checked, try to escape!";
            }

        }
    }

    private void checkForPromotion(int i, int j){
        if (activePiece.substring(6).equals("pawn") && i==0 && activeColor.equals("white")){
            promotePawn(i, j);
        } else if(activePiece.substring(6).equals("pawn") && i == 7 && activeColor.equals("black")){
            promotePawn(i, j);
        }
    }

    private void promotePawn(int i, int j){
        board[i][j] = activeColor + "_queen";
        promotionTile = new int[] {i, j};
        System.out.println("In Promote:");
        System.out.println(Integer.toString(i) + Integer.toString(j));
        System.out.println(board[i][j]);
    }

    public String[][] getBoard() {
        return board;
    }
    private void calculateOkMoves(int[] activePosition){
        System.out.println("Calc ok moves called");
        if (activePiece.substring(6).equals("pawn")) moveOk = MoveOk.pawn(activePosition, activeColor, board);
        else if (activePiece.substring(6).equals("rook")) moveOk = MoveOk.rook(activePosition, activeColor, board);
        else if (activePiece.substring(6).equals("knight")) moveOk = MoveOk.knight(activePosition, activeColor, board);
        else if (activePiece.substring(6).equals("bishop")) moveOk = MoveOk.bishop(activePosition, activeColor, board);
        else if (activePiece.substring(6).equals("queen")) moveOk = MoveOk.queen(activePosition, activeColor, board);
        else if (activePiece.substring(6).equals("king")) moveOk = MoveOk.king(activePosition, activeColor, board);
        else throw new IllegalStateException("Unknown piece");
    }

    @Override
    public boolean move(int i, int j) {
        //System.out.println(Arrays.toString(board));
        System.out.println("----");
        System.out.println(board[i][j]);
        System.out.println("call to move");
        System.out.println("i: " + i + " j: " + j);
        System.out.println("activePiece: " + activePiece);
        System.out.println("activePosition: " + Arrays.toString(activePosition));


        if (activePiece.equals("")) { // player is to mark?
            String tile = board[i][j];
            try {
                if (tile.substring(0, 5).equals(activeColor)) {
                    activePiece = tile;
                    activePosition = new int[] {i, j};
                    calculateOkMoves(activePosition);

                    msg = activeColor.replace(activeColor.charAt(0), Character.toUpperCase(activeColor.charAt(0)))
                            + "'s turn, pick a destination tile";
                } else throw new IndexOutOfBoundsException("Wrong color!");
            } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                msg = "Choose a " + activeColor + " piece";
            }

            System.out.println("---");
            System.out.println("Black pieces taken: " + blackPiecesTaken);
            System.out.println("White pieces taken: " + whitePiecesTaken + "\n\n");
            return false;
        }
        else if (i == activePosition[0] && j == activePosition[1]) { // player presses the same tile as already marked!?
            activePiece = "";
            System.out.println("---JABBA!---");
            msg = activeColor.replace(activeColor.charAt(0), Character.toUpperCase(activeColor.charAt(0))) + "'s turn, choose a piece to move";
            System.out.println("Black pieces taken: " + blackPiecesTaken);
            System.out.println("White pieces taken: " + whitePiecesTaken + "\n\n");
            return false;
        }
        //else if()

        if (moveOk[i][j]) {
            if (board[i][j].equals("-")) {
                moveToEmpty(i, j);
                System.out.println("Black pieces taken: " + blackPiecesTaken);
                System.out.println("White pieces taken: " + whitePiecesTaken + "\n\n");
                return true;
            } else if (board[i][j].substring(0, 5).equals(dormantColor)) {
                moveToKill(i, j);
                System.out.println("Black pieces taken: " + blackPiecesTaken);
                System.out.println("White pieces taken: " + whitePiecesTaken + "\n\n");
                return true;
            } else if (board[i][j].substring(0, 5).equals(activeColor)) { // THIS NEVER HAPPENS ANYMORE? /E
                msg = activeColor.replace(activeColor.charAt(0), Character.toUpperCase(activeColor.charAt(0)))
                        + "'s turn, pick a tile that is not inhabited by your own piece";
            }
            System.out.println("HHHHHHHHHHHH");
            System.out.println(i==0);
            System.out.println(activePiece.substring(6).equals("pawn"));
            System.out.println(activeColor.equals("white"));

        } else {
            System.out.println(board[i][j]);
            if (!board[i][j].equals("-") && board[i][j].substring(0, 5).equals(activeColor)){ // this is a remark, borde flyttas upp senare
                activePosition = new int[] {i, j};
                msg = activeColor.replace(activeColor.charAt(0), Character.toUpperCase(activeColor.charAt(0)))
                        + "'s turn, good remark";
                String tile = board[i][j];
                activePiece = tile;
                calculateOkMoves(activePosition);

                //need more HERE`???
            }else{
                msg = "Forbidden move, choose a correct destination tile";
            }
        }

        System.out.println("Black pieces taken: " + blackPiecesTaken);
        System.out.println("White pieces taken: " + whitePiecesTaken + "\n\n");
        return false;

    }

    @Override
    public int[][] getTilesToUpdate(){
        int[] tmp = promotionTile;
        promotionTile = null;
        return new int[][] {tmp};
    }

    @Override
    public boolean[][] getValidMoves() {
        return moveOk;
    }

    @Override
    public Color[] getTileColors(){
        return tileColors;
    }

    @Override
    public Color getActiveTilesColor(){
        return activeTilesColor;
    }

    @Override
    public String getStatus(int i, int j) {
        return board[i][j];
    }

    @Override
    public String getMessage() {
        return this.msg;
    }

    @Override
    public String getTurn() {
        return activeColor;
    }
}