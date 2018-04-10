package Controller;

import javax.swing.JButton;

import Model.BoardState;
import Model.Player;
import Model.Point;
import View.Swing;
import View.View;
import javafx.scene.control.Button;

public interface IController {

	public void setPlayerFlag(int playerFlag);

	public int getPlayerFlag();

	public Point AI(int player);

	int checkEnd(int x, int y);

	BoardState getBoardState();

	void play(JButton c, JButton[][] a);

	void setSwing(Swing swing);

	public void setPlayer(Player player);

	void play(Button c, Button[][] a);

	boolean isEnd();

	void setView(View view);

	void setEnd(boolean end);

	void undo(Button[][] a);

	void reset(Button[][] a);
}
