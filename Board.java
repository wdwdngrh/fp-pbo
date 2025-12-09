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
    
    Board() {
        // Pawns
        for (int c = 0; c < 8; c++) {
            board[1][c] = new Pawn(Player.BLACK);
            board[6][c] = new Pawn(Player.WHITE);
        }
        
        // Other pieces
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
        board[to.r][to.c] = piece;
        board[from.r][from.c] = null;
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
                    if (p.getMoves(new Position(r, c), this).contains(king))
                        return true;
                }
            }
        }
        return false;
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
}