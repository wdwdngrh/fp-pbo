import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
/**
 * Write a description of class Bishop here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
class Bishop extends Piece {
    Bishop(Player p) { super(p, p == Player.WHITE ? "\u2657" : "\u265D"); }
    
    ArrayList<Position> getMoves(Position from, Board b) {
        return b.getLineMoves(from, new int[][]{{1,1},{1,-1},{-1,1},{-1,-1}});
    }
}