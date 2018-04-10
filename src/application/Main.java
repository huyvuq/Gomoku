/*
 * 
 * @author Huy Vu
 * @date 04/08/2018
 * Proposal project for AI course - Spring 2018
 * Stockton University
 *
 */
package application;
import View.*;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			View view = new View();
			view.start(primaryStage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void handle(WindowEvent event) {
		System.exit(0);
	}

}
