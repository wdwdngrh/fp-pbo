import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
/**
 * Write a description of class Queen here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
class Queen extends Piece {
    Queen(Player p) { super(p, p == Player.WHITE ? "\u2655" : "\u265B"); }
    
    ArrayList<Position> getMoves(Position from, Board b) {
        return b.getLineMoves(from, new int[][]{{1,0},{-1,0},{0,1},{0,-1},{1,1},{1,-1},{-1,1},{-1,-1}});
    }
}