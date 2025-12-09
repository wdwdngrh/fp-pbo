import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
/**
 * Write a description of class Position here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
class Position {
    int r, c;
    Position(int r, int c) { this.r = r; this.c = c; }
    boolean valid() { return r >= 0 && r < 8 && c >= 0 && c < 8; }
    public boolean equals(Object o) {
        if (!(o instanceof Position)) return false;
        Position p = (Position) o;
        return r == p.r && c == p.c;
    }
}