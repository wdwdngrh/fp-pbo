import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
/**
 * Write a description of class Board here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
class Board {
    private Piece[][] board = new Piece[8][8];
    Position enPassantTarget = null;
    
    Board() {
 
        for (int c = 0; c < 8; c++) {
            board[1][c] = new Pawn(Player.BLACK);
            board[6][c] = new Pawn(Player.WHITE);
        }
        
        Player[] players = {Player.BLACK, Player.WHITE};
        int[] rows = {0, 7};
        for (int i = 0; i < 2; i++) {
            int r = rows[i];
            Player p = players[i];
            board[r][0] = new Rook(p);
            board[r][7] = new Rook(p);
            board[r][1] = new Knight(p);
            board[r][6] = new Knight(p);
            board[r][2] = new Bishop(p);
            board[r][5] = new Bishop(p);
            board[r][3] = new Queen(p);
            board[r][4] = new King(p);
        }
    }
    
    Piece getPiece(Position p) {
        return board[p.r][p.c];
    }
    
    boolean movePiece(Position from, Position to, Player player) {
        if (!getValidMoves(from, player).contains(to)) return false;
    
        Piece piece = board[from.r][from.c];
    
        if (piece instanceof Pawn && enPassantTarget != null && to.equals(enPassantTarget)) {
            int dir = (piece.player == Player.WHITE ? -1 : 1); 
            Position capturedPawnPos = new Position(to.r - dir, to.c);
            board[capturedPawnPos.r][capturedPawnPos.c] = null;
        }
    
        if (piece instanceof King && Math.abs(to.c - from.c) == 2) {
            if (to.c == 6) {
                Position rookFrom = new Position(from.r, 7);
                Position rookTo = new Position(from.r, 5);
                board[rookTo.r][rookTo.c] = board[rookFrom.r][rookFrom.c];
                board[rookFrom.r][rookFrom.c] = null;
                board[rookTo.r][rookTo.c].moved = true;
            } else if (to.c == 2) {
                Position rookFrom = new Position(from.r, 0);
                Position rookTo = new Position(from.r, 3);
                board[rookTo.r][rookTo.c] = board[rookFrom.r][rookFrom.c];
                board[rookFrom.r][rookFrom.c] = null;
                board[rookTo.r][rookTo.c].moved = true;
            }
        }
    
        board[to.r][to.c] = piece;
        board[from.r][from.c] = null;
    
        enPassantTarget = null;
    
        if (piece instanceof Pawn && Math.abs(to.r - from.r) == 2) {
            enPassantTarget = new Position((from.r + to.r) / 2, from.c);
        }
        
        if (piece instanceof Pawn) {
            if ((piece.player == Player.WHITE && to.r == 0) ||
                (piece.player == Player.BLACK && to.r == 7)) {
                return handlePromotion(to, piece.player);
            }
        }
        
        piece.moved = true;
        return true;
    }

    
    
    ArrayList<Position> getValidMoves(Position from, Player player) {
        Piece p = getPiece(from);
        if (p == null || p.player != player) return new ArrayList<>();
        
        ArrayList<Position> moves = p.getMoves(from, this);
        moves.removeIf(to -> wouldBeInCheck(from, to, player));
        return moves;
    }
    
    boolean wouldBeInCheck(Position from, Position to, Player player) {
        Piece piece = board[from.r][from.c];
        Piece captured = board[to.r][to.c];
        
        board[to.r][to.c] = piece;
        board[from.r][from.c] = null;
        
        boolean check = isInCheck(player);
        
        board[from.r][from.c] = piece;
        board[to.r][to.c] = captured;
        
        return check;
    }
    
    boolean isInCheck(Player player) {
        Position king = findKing(player);
        if (king == null) return false;
        
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];
                if (p != null && p.player != player) {
                    ArrayList<Position> attacks = getAttackSquares(new Position(r, c));
                    if (attacks.contains(king)) return true;
                }
            }
        }
        return false;
    }
    
    ArrayList<Position> getAttackSquares(Position from) {
        ArrayList<Position> attacks = new ArrayList<>();
        Piece p = getPiece(from);
        if (p == null) return attacks;
        
        if (p instanceof Pawn) {
            int dir = (p.player == Player.WHITE ? -1 : 1);
            for (int dc : new int[]{-1, 1}) {
                Position cap = new Position(from.r + dir, from.c + dc);
                if (cap.valid()) attacks.add(cap);
            }
            return attacks;
        }
        
        if (p instanceof King) {
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr == 0 && dc == 0) continue;
                    Position to = new Position(from.r + dr, from.c + dc);
                    if (to.valid()) attacks.add(to);
                }
            }
            return attacks;
        }
        
        if (p instanceof Knight) {
            int[][] jumps = {{-2,-1},{-2,1},{-1,-2},{-1,2},{1,-2},{1,2},{2,-1},{2,1}};
            for (int[] j : jumps) {
                Position to = new Position(from.r + j[0], from.c + j[1]);
                if (to.valid()) attacks.add(to);
            }
            return attacks;
        }
        
        if (p instanceof Rook) {
            attacks.addAll(getLineAttacks(from, new int[][]{{1,0},{-1,0},{0,1},{0,-1}}));
            return attacks;
        }
        if (p instanceof Bishop) {
            attacks.addAll(getLineAttacks(from, new int[][]{{1,1},{1,-1},{-1,1},{-1,-1}}));
            return attacks;
        }
        if (p instanceof Queen) {
            attacks.addAll(getLineAttacks(from, new int[][]{{1,0},{-1,0},{0,1},{0,-1},{1,1},{1,-1},{-1,1},{-1,-1}}));
            return attacks;
        }
        
        return attacks;
    }
    
    ArrayList<Position> getLineAttacks(Position from, int[][] dirs) {
        ArrayList<Position> moves = new ArrayList<>();
        for (int[] d : dirs) {
            for (int i = 1; i < 8; i++) {
                Position to = new Position(from.r + d[0] * i, from.c + d[1] * i);
                if (!to.valid()) break;
                moves.add(to);
                Piece p = getPiece(to);
                if (p != null) break;
            }
        }
        return moves;
    }
    
    Position findKing(Player player) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];
                if (p instanceof King && p.player == player)
                    return new Position(r, c);
            }
        }
        return null;
    }
    
    boolean isCheckmate(Player player) {
        if (!isInCheck(player)) return false;
        
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (!getValidMoves(new Position(r, c), player).isEmpty())
                    return false;
            }
        }
        return true;
    }
    
    ArrayList<Position> getLineMoves(Position from, int[][] dirs) {
        ArrayList<Position> moves = new ArrayList<>();
        for (int[] d : dirs) {
            for (int i = 1; i < 8; i++) {
                Position to = new Position(from.r + d[0] * i, from.c + d[1] * i);
                if (!to.valid()) break;
                Piece p = getPiece(to);
                if (p == null) {
                    moves.add(to);
                } else {
                    if (p.player != getPiece(from).player) moves.add(to);
                    break;
                }
            }
        }
        return moves;
    }

    private boolean handlePromotion(Position pos, Player player) {
        String[] options = {"Queen", "Rook", "Bishop", "Knight"};
        String choice = (String) JOptionPane.showInputDialog(
            null,
            "Promote Pawn To:",
            "Pawn Promotion",
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            "Queen"
        );
    
        if (choice == null) choice = "Queen";
    
        switch (choice) {
            case "Rook":
                board[pos.r][pos.c] = new Rook(player);
                break;
            case "Bishop":
                board[pos.r][pos.c] = new Bishop(player);
                break;
            case "Knight":
                board[pos.r][pos.c] = new Knight(player);
                break;
            default:
                board[pos.r][pos.c] = new Queen(player);
        }
    
        board[pos.r][pos.c].moved = true;
        return true;
    }

}
