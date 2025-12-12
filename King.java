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

        if (!moved && !b.isInCheck(player)) {
            addCastlingMoves(from, b, moves);
        }

        return moves;
    }

    private void addCastlingMoves(Position from, Board b, ArrayList<Position> moves) {
        int r = from.r;

        if (canCastle(b, from, new Position(r, 7), new int[]{5, 6}))
            moves.add(new Position(r, 6));

        if (canCastle(b, from, new Position(r, 0), new int[]{1, 2, 3}))
            moves.add(new Position(r, 2));
    }

    private boolean canCastle(Board b, Position king, Position rookPos, int[] clearCols) {
        Piece rook = b.getPiece(rookPos);
        if (!(rook instanceof Rook) || rook.moved) return false;
    
        for (int c : clearCols) {
            if (b.getPiece(new Position(king.r, c)) != null) return false;
        }
    
        int[] dangerSquares = (rookPos.c == 7) 
            ? new int[]{5, 6} 
            : new int[]{3, 2};  
    
        for (int c : dangerSquares) {
            if (b.wouldBeInCheck(king, new Position(king.r, c), player)) return false;
        }
    
        return true;
    }

}
