package GameBoard;

import java.util.ArrayList;

public class MoveOk {

    static String[] colors = new String[] {"black", "white"};
    static String other_color;
    static String own_color;
    static boolean[][] good_moves;

    private static ArrayList[] blackPawn(int pos_i, int pos_j, String[][] board) {
        ArrayList<Integer> ok_i = new ArrayList<>();
        ArrayList<Integer> ok_j = new ArrayList<>();
        if (board[pos_i + 1][pos_j].equals("-")) {
            ok_i.add(pos_i + 1);
            ok_j.add(pos_j);
            if (pos_i == 1 && board[pos_i + 2][pos_j].equals("-")) {
                ok_i.add(pos_i + 2);
            }
        }
        try {
            if (board[pos_i + 1][pos_j + 1].matches("white.*")) {
                ok_i.add(pos_i + 1);
                ok_j.add(pos_j + 1);
            }
        } catch(IndexOutOfBoundsException indexOutOfBoundsException) {
            System.err.println("Tile " + (pos_i + 1) + ", " + (pos_j + 1)  + " not on board");
        } try {
            if (board[pos_i + 1][pos_j - 1].matches("white.*")) {
                if (!ok_i.contains(pos_i)) ok_i.add(pos_i + 1);
                ok_j.add(pos_j - 1);
            }
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            System.err.println("Tile " + (pos_i + 1) + ", " + (pos_j - 1)  + " not on board");
        }
        return new ArrayList[] {ok_i, ok_j};
    }

    private static ArrayList[] whitePawn(int pos_i, int pos_j, String[][] board) {
        ArrayList<Integer> ok_i = new ArrayList<>();
        ArrayList<Integer> ok_j = new ArrayList<>();
        if (board[pos_i - 1][pos_j].equals("-")) {
            ok_i.add(pos_i - 1);
            ok_j.add(pos_j);
            if (pos_i == 6 && board[pos_i - 2][pos_j].equals("-")) {
                ok_i.add(pos_i - 2);
            }
        }
        try {
            if (board[pos_i - 1][pos_j + 1].matches("black.*")) {
                ok_i.add(pos_i - 1);
                ok_j.add(pos_j + 1);
            }
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            System.err.println("Tile " + (pos_i - 1) + ", " + (pos_j + 1) + " not on board");
        } try {
            if (board[pos_i - 1][pos_j - 1].matches("black.*")) {
                if (!ok_i.contains(pos_i)) ok_i.add(pos_i - 1);
                ok_j.add(pos_j - 1);
            }
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            System.err.println("Tile " + (pos_i - 1) + ", " + (pos_j - 1)  + " not on board");
        }
        return new ArrayList[] {ok_i, ok_j};
    }

    public static boolean[][] pawn(int[] position, String color, String[][] board) {
        good_moves = new boolean[8][8];
        int pos_i = position[0];
        int pos_j = position[1];
        ArrayList<Integer>[] ok_tiles = color.equals(colors[0]) ? MoveOk.blackPawn(pos_i, pos_j, board) : MoveOk.whitePawn(pos_i, pos_j, board);
        ArrayList<Integer> ok_i = ok_tiles[0]; ArrayList<Integer> ok_j = ok_tiles[1];
        if (ok_j.contains(pos_j)) {
            for (Integer row : ok_i) {
                good_moves[row][pos_j] = true;
            }
        }
        for (Integer col: ok_j) {
            if (!ok_i.isEmpty()) good_moves[ok_i.get(0)][col] = true;
        }
        return good_moves;
    }

    public static boolean[][] rook(int[] position, String color, String[][] board) {
        good_moves = new boolean[8][8];
        other_color = color.equals(colors[0]) ? colors[1] : colors[0];
        own_color = color;
        checkHorizontal(position[0], position[1], board);
        checkVertical(position[0], position[1], board);
        return good_moves;
    }

    private static void checkDiagonal(int i, int j, String[][] board) {
        checkDirection(new int[] {1, 1}, i, j, board);
        checkDirection(new int[] {-1, 1}, i, j, board);
        checkDirection(new int[] {1, -1}, i, j, board);
        checkDirection(new int[] {-1, -1}, i, j, board);
    }

    private static void checkVertical(int i, int j, String[][] board) {
        checkDirection(new int[] {1, 0}, i, j, board);
        checkDirection(new int[] {-1, 0}, i, j, board);
    }

    private static void checkHorizontal(int i, int j, String[][] board) {
        checkDirection(new int[] {0, 1}, i, j, board);
        checkDirection(new int[] {0, -1}, i, j, board);
    }

    private static void checkDirection(int[] direction, int i, int j, String[][] board) {
        int it_i = i + direction[0];
        int it_j = j + direction[1];
        while (it_i >= 0 && it_j >= 0 && it_i < 8 && it_j < 8 && board[it_i][it_j].equals("-")) {
            good_moves[it_i][it_j] = true;
            it_i += direction[0];
            it_j += direction[1];
        }
        if (it_i < 8 && it_j < 8 && it_i >= 0 && it_j >= 0)
            good_moves[it_i][it_j] = board[it_i][it_j].substring(0, 5).equals(other_color);
    }

    public static boolean[][] knight(int[] position, String color, String[][] board) {
        good_moves = new boolean[8][8];
        int i = position[0];
        int j = position[1];
        other_color = color.matches(colors[0]) ? colors[1] : colors[0];
        own_color = color;
        if (i > 1 && i < 6 && j > 1 && j < 6) {
            // check two rows up & one col each side 2 calls
            // check one row up & two cols each side 2 calls
            // check one row down & two cols each side 2 calls
            // check two rows down & one col each side 2 calls
            // total 8 calls
            upLeftLeftKnight(i, j, board);
            upUpLeftKnight(i, j, board);
            upUpRightKnight(i, j, board);
            upRightRightKnight(i, j, board);
            downRightRightKnight(i, j, board);
            downDownRightKnight(i, j, board);
            downDownLeftKnight(i, j, board);
            downLeftLeftKnight(i, j, board);
        }
        if (i == 0) {
            if (j == 1) {
                // check two rows down & one col each side 2 calls
                // check one row down & two cols right 1 call
                // total 3 calls
                downDownLeftKnight(i, j, board);
                downDownRightKnight(i, j, board);
                downRightRightKnight(i, j, board);
            } else if (j == 6) {
                // check two rows down & one col each side
                // check one row down & two cols left
                downDownRightKnight(i, j, board);
                downDownLeftKnight(i, j, board);
                downLeftLeftKnight(i, j, board);
            } else if (j == 0) {
                // check two rows down & one col right
                // check one row down & two cols right
                downDownRightKnight(i, j, board);
                downRightRightKnight(i, j, board);
            } else if (j == 7) {
                // check two rows down & one col left
                // check one row down & two cols left
                downDownLeftKnight(i, j, board);
                downLeftLeftKnight(i, j, board);
            } else {
                // check one row down & two cols each side
                // check two rows down & one col each side
                downRightRightKnight(i, j, board);
                downDownRightKnight(i, j, board);
                downDownRightKnight(i,  j, board);
                downLeftLeftKnight(i, j, board);
            }
        } else if (i == 1) {
            if (j == 1) {
                // check two rows down & one col each side 2 calls
                // check one row down & two cols right 1 call
                // total 3 calls
                upRightRightKnight(i, j, board);
                downRightRightKnight(i, j, board);
                downDownRightKnight(i, j, board);
                downDownLeftKnight(i, j, board);
            } else if (j == 6) {
                // check two rows down & one col each side
                // check one row down & two cols left
                downDownRightKnight(i, j, board);
                downDownLeftKnight(i, j, board);
                downLeftLeftKnight(i, j, board);
                upLeftLeftKnight(i, j, board);
            } else if (j == 0) {
                // check two rows down & one col right
                // check one row down & two cols right
                upRightRightKnight(i, j, board);
                downDownRightKnight(i, j, board);
                downRightRightKnight(i, j, board);
            } else if (j == 7) {
                // check two rows down & one col left
                // check one row down & two cols left
                downDownLeftKnight(i, j, board);
                downLeftLeftKnight(i, j, board);
                upLeftLeftKnight(i, j, board);
            } else {
                // check one row down & two cols each side
                // check two rows down & one col each side
                upRightRightKnight(i, j, board);
                downRightRightKnight(i, j, board);
                downDownRightKnight(i, j, board);
                downDownRightKnight(i,  j, board);
                downLeftLeftKnight(i, j, board);
                upLeftLeftKnight(i, j, board);
            }
        } else if (i == 6) {
            if (j == 1) {
                // check two rows up & one col each side
                // check one row up & two cols right
                upUpRightKnight(i, j, board);
                upRightRightKnight(i, j, board);
                downLeftLeftKnight(i, j, board);
                upUpLeftKnight(i, j, board);
            } else if (j == 6) {
                // check two rows up & one col each side
                // check one row up & two cols left
                upUpRightKnight(i, j, board);
                downLeftLeftKnight(i, j, board);
                upLeftLeftKnight(i, j, board);
                upUpLeftKnight(i, j, board);
            } else if (j == 0) {
                // check two rows up & one col right
                // check one row up & two cols right
                upUpRightKnight(i, j, board);
                upRightRightKnight(i, j, board);
                downRightRightKnight(i, j, board);
            } else if (j == 7) {
                // check two rows up & one col left
                // check one row up & two cols left
                downLeftLeftKnight(i, j, board);
                upLeftLeftKnight(i, j, board);
                upUpLeftKnight(i, j, board);
            } else {
                // check one row down & two cols each side
                // check two rows down & one col each side
                upUpRightKnight(i, j, board);
                upRightRightKnight(i, j, board);
                downRightRightKnight(i, j, board);
                downLeftLeftKnight(i, j, board);
                upLeftLeftKnight(i, j, board);
                upUpLeftKnight(i, j, board);
            }
        } else if (i == 7) {
            if (j == 1) {
                // check two rows up & one col each side
                // check one row up & two cols right
                upUpRightKnight(i, j, board);
                upRightRightKnight(i, j, board);
                upUpLeftKnight(i, j, board);
            } else if (j == 6) {
                // check two rows up & one col each side
                // check one row up & two cols left
                upUpRightKnight(i, j, board);
                upLeftLeftKnight(i, j, board);
                upUpLeftKnight(i, j, board);
            } else if (j == 0) {
                // check two rows up & one col right
                // check one row up & two cols right
                upUpRightKnight(i, j, board);
                upRightRightKnight(i, j, board);
            } else if (j == 7) {
                // check two rows up & one col left
                // check one row up & two cols left
                upUpLeftKnight(i, j, board);
                upLeftLeftKnight(i, j, board);
            } else {
                // check one row down & two cols each side
                // check two rows down & one col each side
                upUpRightKnight(i, j, board);
                upRightRightKnight(i, j, board);
                upLeftLeftKnight(i, j, board);
                upUpLeftKnight(i, j, board);
            }
        } else if (j < 2) {
            upUpRightKnight(i, j, board);
            upRightRightKnight(i, j, board);
            downRightRightKnight(i, j, board);
            downDownRightKnight(i, j, board);
            if (j == 1) {
                downDownLeftKnight(i, j, board);
                upUpLeftKnight(i, j, board);
            }
        } else if (5 < j) {
            downDownLeftKnight(i, j, board);
            downLeftLeftKnight(i, j, board);
            upLeftLeftKnight(i, j, board);
            upUpLeftKnight(i, j, board);
            if (j == 6) {
                upUpRightKnight(i, j, board);
                downDownRightKnight(i, j, board);
            }
        }
        return good_moves;
    }

    private static void downRightRightKnight(int i, int j, String[][] board) {
        good_moves[i + 1][j + 2] = board[i + 1][j + 2].equals("-") || board[i + 1][j + 2].substring(0, 5).equals(other_color);
    }

    private static void downDownRightKnight(int i, int j, String[][] board) {
        good_moves[i + 2][j + 1] = board[i + 2][j + 1].equals("-") || board[i + 2][j + 1].substring(0, 5).equals(other_color);
    }

    private static void downDownLeftKnight(int i, int j, String[][] board) {
        good_moves[i + 2][j - 1] = board[i + 2][j - 1].equals("-") || board[i + 2][j - 1].substring(0, 5).equals(other_color);
    }

    private static void downLeftLeftKnight(int i, int j, String[][] board) {
        good_moves[i + 1][j - 2] = board[i + 1][j - 2].equals("-") || board[i + 1][j - 2].substring(0, 5).equals(other_color);
    }

    private static void upLeftLeftKnight(int i, int j, String[][] board) {
        good_moves[i - 1][j - 2] = board[i - 1][j - 2].equals("-") || board[i - 1][j - 2].substring(0, 5).equals(other_color);
    }

    private static void upUpLeftKnight(int i, int j, String[][] board) {
        good_moves[i - 2][j - 1] = board[i - 2][j - 1].equals("-") || board[i - 2][j - 1].substring(0, 5).equals(other_color);
    }

    private static void upUpRightKnight(int i, int j, String[][] board) {
        good_moves[i - 2][j + 1] = board[i - 2][j + 1].equals("-") || board[i - 2][j + 1].substring(0, 5).equals(other_color);
    }

    private static void upRightRightKnight(int i, int j, String[][] board) {
        good_moves[i - 1][j + 2] = board[i - 1][j + 2].equals("-") || board[i - 1][j + 2].substring(0, 5).equals(other_color);
    }

    public static boolean[][] bishop(int[] position, String color, String[][] board) {
        good_moves = new boolean[8][8];
        other_color = color.equals(colors[0]) ? colors[1] : colors[0];
        own_color = color;
        checkDiagonal(position[0], position[1], board);
        return good_moves;
    }

    public static boolean[][] queen(int[] position, String color, String[][] board) {
        good_moves = new boolean[8][8];
        other_color = color.equals(colors[0]) ? colors[1] : colors[0];
        own_color = color;
        checkVertical(position[0], position[1], board);
        checkHorizontal(position[0], position[1], board);
        checkDiagonal(position[0], position[1], board);
        return good_moves;
    }

    public static boolean[][] king(int[] position, String color, String[][] board) {
        good_moves = new boolean[8][8];
        other_color = color.equals(colors[0]) ? colors[1] : colors[0];
        own_color = color;
        int i = position[0];
        int j = position[1];
        if (i == 0) {
            checkDirectionKing(new int[] {1, 0}, i, j, board);
            if (0 < j && j < 7) {
                checkDirectionKing(new int[] {1, 1}, i, j, board);
                checkDirectionKing(new int[] {0, 1}, i, j, board);
                checkDirectionKing(new int[] {0, -1}, i, j, board);
                checkDirectionKing(new int[] {1, -1}, i, j, board);
            } else if (j == 0) {
                checkDirectionKing(new int[] {0, 1}, i, j, board);
                checkDirectionKing(new int[] {1, 1}, i, j, board);
            } else if (j == 7) {
                checkDirectionKing(new int[] {0, -1}, i, j, board);
                checkDirectionKing(new int[] {1, -1}, i, j, board);
            }
        } else if (i == 7) {
            checkDirectionKing(new int[] {-1, 0}, i, j, board);
            if (0 < j && j < 7) {
                checkDirectionKing(new int[] {0, -1}, i, j, board);
                checkDirectionKing(new int[] {-1, -1}, i, j, board);
                checkDirectionKing(new int[] {-1, 1}, i, j, board);
                checkDirectionKing(new int[] {0, 1}, i, j, board);
            } else if (j == 0) {
                checkDirectionKing(new int[] {0, 1}, i, j, board);
                checkDirectionKing(new int[] {1, 1}, i, j, board);
            } else if (j == 7) {
                checkDirectionKing(new int[] {0, -1}, i, j, board);
                checkDirectionKing(new int[] {-1, -1}, i, j, board);
            }
        } else if (j == 0) {
            checkDirectionKing(new int[] {-1, 0}, i, j, board);
            checkDirectionKing(new int[] {-1, 1}, i, j, board);
            checkDirectionKing(new int[] {0, 1}, i, j, board);
            checkDirectionKing(new int[] {1, 1}, i, j, board);
            checkDirectionKing(new int[] {1, 0}, i, j, board);
        } else if (j == 7) {
            checkDirectionKing(new int[] {-1, 0}, i, j, board);
            checkDirectionKing(new int[] {-1, -1}, i, j, board);
            checkDirectionKing(new int[] {0, -1}, i, j, board);
            checkDirectionKing(new int[] {1, -1}, i, j, board);
            checkDirectionKing(new int[] {1, 0}, i, j, board);
        } else {
            for (int k: new int[] {1, -1}) {
                int[][] directions = new int[][] {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
                for (int[] direction : directions) {
                    direction[0] *= k;
                    direction[1] *= k;
                    checkDirectionKing(direction, i, j, board);
                }
            }
        }
        return good_moves;
    }

    private static void checkDirectionKing(int[] direction, int i, int j, String[][] board) {
        int pos_i = i + direction[0];
        int pos_j = j + direction[1];
        good_moves[pos_i][pos_j] = board[pos_i][pos_j].equals("-") || board[pos_i][pos_j].substring(0, 5).equals(other_color);
    }
}