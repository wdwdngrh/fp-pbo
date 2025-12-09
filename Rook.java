import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
/**
 * Write a description of class Rook here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
class Rook extends Piece {
    Rook(Player p) { super(p, p == Player.WHITE ? "\u2656" : "\u265C"); }
    
    ArrayList<Position> getMoves(Position from, Board b) {
        return b.getLineMoves(from, new int[][]{{1,0},{-1,0},{0,1},{0,-1}});
    }
}