package Controller;

import Model.BoardState;
import Model.Player;
import Model.Point;
import View.View;
import javafx.scene.control.Button;

public interface IController {

	public Point AI(int player);

	int checkEnd(int x, int y);

	BoardState getBoardState();

	public void setPlayer(Player player);

	void play(Button c, Button[][] a);

	boolean isEnd();

	void setView(View view);

	void setEnd(boolean end);

	void undo(Button[][] a);

	void reset(Button[][] a);
}
