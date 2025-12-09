
/**
 * Write a description of class ChessGame here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class ChessGame extends JFrame {
    private Board board;
    private JButton[][] squares;
    private Position selected;
    private Player currentPlayer;
    
    public ChessGame() {
        setTitle("Chess Game");
        setSize(640, 640);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 8));
        
        board = new Board();
        squares = new JButton[8][8];
        selected = null;
        currentPlayer = Player.WHITE;
        
        initBoard();
        setVisible(true);
    }
    
    private void initBoard() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                JButton btn = new JButton();
                btn.setFont(new Font("Dialog", Font.PLAIN, 48));
                btn.setFocusPainted(false);
                btn.setBackground((r + c) % 2 == 0 ? Color.WHITE : Color.GRAY);
                
                final int row = r, col = c;
                btn.addActionListener(e -> handleClick(row, col));
                
                squares[r][c] = btn;
                add(btn);
            }
        }
        updateDisplay();
    }
    
    private void handleClick(int r, int c) {
        Position pos = new Position(r, c);
        
        if (selected == null) {
            Piece p = board.getPiece(pos);
            if (p != null && p.player == currentPlayer) {
                selected = pos;
                highlightMoves();
            }
        } else {
            if (board.movePiece(selected, pos, currentPlayer)) {
                currentPlayer = (currentPlayer == Player.WHITE) ? Player.BLACK : Player.WHITE;
                selected = null;
                updateDisplay();
                
                if (board.isCheckmate(currentPlayer)) {
                    JOptionPane.showMessageDialog(this, 
                        (currentPlayer == Player.WHITE ? "Black" : "White") + " wins!");
                }
            } else {
                selected = null;
                updateDisplay();
            }
        }
    }
    
    private void highlightMoves() {
        updateDisplay();
        squares[selected.r][selected.c].setBackground(Color.YELLOW);
        for (Position p : board.getValidMoves(selected, currentPlayer)) {
            Color orig = (p.r + p.c) % 2 == 0 ? Color.WHITE : Color.GRAY;
            squares[p.r][p.c].setBackground(orig.darker());
        }
    }
    
    private void updateDisplay() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board.getPiece(new Position(r, c));
                squares[r][c].setText(p != null ? p.symbol : "");
                squares[r][c].setBackground((r + c) % 2 == 0 ? Color.WHITE : Color.GRAY);
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChessGame::new);
    }
}
