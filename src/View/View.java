package View;

import Controller.Controller;
import Controller.IController;
import Model.BoardState;
import Model.GomokuAI;
import Model.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class View implements EventHandler<ActionEvent> {
	static final int W = 20; //board size
	static final int H = 20;
	IController controller;
	public Button[][] a;
	public static Stage primaryStage;

	public View() {
	}

	public void start(Stage primaryStage) {
		try {
			View.primaryStage = primaryStage;
			a = new Button[W][H];
			BoardState boardState = new BoardState(W, H);
			controller = new Controller();
			controller.setView(this);
			Player computer = new GomokuAI(boardState);
			controller.setPlayer(computer);

			BorderPane borderPane = new BorderPane();
			BorderPane borderPane1 = new BorderPane();
			menu(borderPane1);
			GridPane root = new GridPane();

			Scene scene = new Scene(borderPane, 950, 800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			borderPane.setPadding(new Insets(20));
			borderPane.setCenter(root);
			borderPane.setRight(borderPane1);
			controller.setPlayerFlag(1);
			for (int i = 0; i < W; i++) {
				for (int j = 0; j < H; j++) {
					Button button = new Button();
					button.setPrefSize(40, 40);
					button.setAccessibleText(i + ";" + j);
					a[i][j] = button;
					root.add(button, j, i);
					button.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							if (!controller.isEnd()) {
								controller.play(button, a);

							}
						}
					});
				}
			}

			primaryStage.setScene(scene);
			primaryStage.setTitle("Gomoku");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	Button btnCom;
	Button btnEx;
	Button btnUndo;

	private void menu(BorderPane pane) {
		VBox box = new VBox();
		box.setSpacing(10);
		AnchorPane anchorPane1 = new AnchorPane();
		//
		AnchorPane anchorPane = new AnchorPane();

		// undo
		btnUndo = new Button("Undo");
		btnUndo.setId("btnNguoi");
		btnUndo.setOnAction(this);
		AnchorPane.setTopAnchor(btnUndo, 90.0);
		AnchorPane.setLeftAnchor(btnUndo, 30.0);
		AnchorPane.setRightAnchor(btnUndo, 30.0);

	
		// exit
		btnEx = new Button("Exit");
		btnEx.setId("btnNguoi");
		btnEx.setOnAction(this);
		AnchorPane.setTopAnchor(btnEx, 150.0);
		AnchorPane.setLeftAnchor(btnEx, 30.0);
		AnchorPane.setRightAnchor(btnEx, 30.0);

		anchorPane.getChildren().addAll(btnUndo, btnEx);
		box.getChildren().add(anchorPane1);
		box.getChildren().add(anchorPane);

		pane.setCenter(box);

	}

	Label namePlayer1;
	Labeled timePlayer1, timePlayer2;

	@Override
	public void handle(ActionEvent e) {
		if (e.getSource() == btnEx) {
			primaryStage.close();
		}

		if (e.getSource() == btnCom) {
			controller.setEnd(false);
			controller.setPlayer(new GomokuAI(new BoardState(W, H)));
			controller.setPlayerFlag(1);
			controller.reset(a);
		}
		if (e.getSource() == btnUndo) {
			controller.undo(a);
		}
	}

	public void replayComputer() {
		controller.setEnd(false);
		controller.setPlayer(new GomokuAI(new BoardState(W, H)));
		controller.setPlayerFlag(1);
		controller.reset(a);
	}

}
