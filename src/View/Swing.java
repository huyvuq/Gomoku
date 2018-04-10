package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Controller.Controller;
import Controller.IController;
import Model.BoardState;
import Model.GomokuAI;
import Model.Player;

public class Swing extends JFrame {
	JButton[][] a;
	int x, y;
	static final int W = 10;
	static final int H = 10;
	BoardState boardState;
	IController controller;

	public Swing() {
		setTitle("Gomoku");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		a = new JButton[W][H];
		boardState = new BoardState(W, H);
		controller = new Controller();
		Player computer = new GomokuAI(boardState);
		controller.setPlayer(computer);
		controller.setSwing(this);
		JPanel p = new JPanel(new GridLayout(W, H));
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a.length; j++) {
				JButton x = new JButton();
				x.setActionCommand(i + ";" + j);
				x.setBackground(Color.WHITE);
				a[i][j] = x;
				x.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						controller.play(x, a);
					}
				});
				p.add(x);
			}
		}
		a[0][2].setText("");
		controller.setPlayerFlag(1);
		add(p, BorderLayout.CENTER);
	}

	void print(int[][] a) {
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				System.out.print(a[i][j]);
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		Swing s = new Swing();
		s.setSize(600, 600);
		s.setVisible(true);

	}

}
