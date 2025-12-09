import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
/**
 * Write a description of class Knight here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
class Knight extends Piece {
    Knight(Player p) { super(p, p == Player.WHITE ? "\u2658" : "\u265E"); }
    
    ArrayList<Position> getMoves(Position from, Board b) {
        ArrayList<Position> moves = new ArrayList<>();
        int[][] offsets = {{2,1},{2,-1},{-2,1},{-2,-1},{1,2},{1,-2},{-1,2},{-1,-2}};
        for (int[] o : offsets) {
            Position to = new Position(from.r + o[0], from.c + o[1]);
            if (to.valid() && (b.getPiece(to) == null || b.getPiece(to).player != player))
                moves.add(to);
        }
        return moves;
    }
}