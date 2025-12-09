import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
/**
 * Write a description of class Piece here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
abstract class Piece {
    Player player;
    String symbol;
    boolean moved = false;
    
    Piece(Player p, String s) { player = p; symbol = s; }
    abstract ArrayList<Position> getMoves(Position from, Board b);
}