package Controller;

import java.io.InputStream;
import java.util.Optional;
import java.util.Queue;
import java.util.Stack;
import java.util.StringTokenizer;

import Model.BoardState;
import Model.GomokuAI;
import Model.Player;
import Model.Point;
import View.View;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
/**
 * Player default's symbol: X
 * PC default's symbol: O
 * @author Huy Vu
 *
 */
public class Controller implements IController {
	public View view;
	Player player;
	int x1 = 0, y1 = 0;
	Stack<Point> stack = new Stack<>();

	Class<?> clazz = this.getClass();
	InputStream o = clazz.getResourceAsStream("/Resources/o.png");
	InputStream x = clazz.getResourceAsStream("/Resources/x.png");
	Image imageO = new Image(o);
	Image imageX = new Image(x);
	boolean end = false;

	public Controller() {

	}

	@Override
	public Point AI(int player) {
		return this.player.movePoint(player);
	}


	@Override
	public BoardState getBoardState() {
		return player.getBoardState();
	}

	@Override
	public int checkEnd(int x, int y) {
		return player.getBoardState().checkEnd(x, y);
	}

	@Override
	public void play(Button c, Button[][] a) {
		StringTokenizer tokenizer = new StringTokenizer(c.getAccessibleText(), ";");
		int x = Integer.parseInt(tokenizer.nextToken());
		int y = Integer.parseInt(tokenizer.nextToken());
	
		if (getBoardState().getPosition(x, y) == 0) {
			setAMove(x, y, 1, a);
			// PC's turn
			if (!end) { 
				Point p = AI(2);
				setAMove(p.x, p.y, 2, a);
			}
		}
		
		//Check if the game ends
		if (end) {
			if (player instanceof GomokuAI && playerWin.equals("2")) {
				playerWin = "PC";
			}
			showDialog("Player " + playerWin + " won!");
			return;
		}
	}

	int totalMoves = 0;
	String playerWin = "";

	/**
	 * Set a move on the board
	 * @param x
	 * @param y
	 * @param player
	 * @param a
	 */
	private void setAMove(int x, int y, int player, Button[][] a) {
		getBoardState().setPosition(x, y, player);
		x1 = x;
		y1 = y;
		if (player == 1) {
			Point point = new Point(x, y);
			point.setPlayer(1);
			stack.push(point);
			a[x][y].setGraphic(new ImageView(imageX));
			totalMoves++;
		} else {
			Point point = new Point(x, y);
			point.setPlayer(2);
			stack.push(point);
			a[x][y].setGraphic(new ImageView(imageO));
			
			totalMoves++;
		}
		if (getBoardState().checkEnd(x, y) == player) {
			playerWin = player + "";
			end = true;
		}
		if (totalMoves == (getBoardState().height * getBoardState().width)) {
			playerWin = 2 + "";
			end = true;
		}

	}



	Queue<Point> queue;

	@Override
	public void undo(Button[][] a) {
		if (!stack.isEmpty()) {
			totalMoves--;
			Point point = stack.pop();
			getBoardState().boardArr[point.x][point.y] = 0;
			a[point.x][point.y].setGraphic(null);
			a[point.x][point.y].setStyle("-fx-background-color: rgb(220,220,220);");

		}
	}

	public void showDialog(String title) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Game over");
		alert.setHeaderText(title);
		alert.setContentText("Do you want to replay?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
				view.replayComputer();
		} 
	}

	@Override
	public void reset(Button[][] a) {
		totalMoves = 0;

		getBoardState().resetBoard();
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				a[i][j].setGraphic(null);
				a[i][j].setStyle("-fx-background-color: rgb(220,220,220);");
			}
		}
	}
	
	@Override
	public void setPlayer(Player player) {
		this.player = player;
	}

	public EventHandler<ActionEvent> action(String action) {
		return null;
	}

	EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent e) {
		}
	};
	
	@Override
	public boolean isEnd() {
		return end;
	}

	@Override
	public void setView(View view) {
		this.view = view;
	}

	@Override
	public void setEnd(boolean end) {
		this.end = end;
	}

	public Player getPlayer() {
		return player;
	}
}
