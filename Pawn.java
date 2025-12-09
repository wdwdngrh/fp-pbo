import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
/**
 * Write a description of class Pawn here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
class Pawn extends Piece {
    Pawn(Player p) { super(p, p == Player.WHITE ? "\u2659" : "\u265F"); }
    
    ArrayList<Position> getMoves(Position from, Board b) {
        ArrayList<Position> moves = new ArrayList<>();
        int dir = player == Player.WHITE ? -1 : 1;
        
        Position fwd = new Position(from.r + dir, from.c);
        if (fwd.valid() && b.getPiece(fwd) == null) {
            moves.add(fwd);
            if (!moved) {
                Position fwd2 = new Position(from.r + 2 * dir, from.c);
                if (b.getPiece(fwd2) == null) moves.add(fwd2);
            }
        }
        
        for (int dc : new int[]{-1, 1}) {
            Position cap = new Position(from.r + dir, from.c + dc);
            if (cap.valid() && b.getPiece(cap) != null && b.getPiece(cap).player != player)
                moves.add(cap);
        }
        return moves;
    }
}