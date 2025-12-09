import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
/**
 * Write a description of class King here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
class King extends Piece {
    King(Player p) { super(p, p == Player.WHITE ? "\u2654" : "\u265A"); }
    
    ArrayList<Position> getMoves(Position from, Board b) {
        ArrayList<Position> moves = new ArrayList<>();
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                Position to = new Position(from.r + dr, from.c + dc);
                if (to.valid() && (b.getPiece(to) == null || b.getPiece(to).player != player))
                    moves.add(to);
            }
        }
        return moves;
    }
}