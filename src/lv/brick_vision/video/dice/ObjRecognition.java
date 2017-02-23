package lv.brick_vision.video.dice;

import org.opencv.core.Core;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

/**
* The main class for a JavaFX application. It creates and handles the main
* window with its resources (style, graphics, etc.).
* This application counts pips on dice using "findCircles" method.
* Use sliders to adjust the image or video for better results. Found circles are outlined and pip
* count displayed.
*
* @author Original program author <a href="mailto:luigi.derussis@polito.it">Luigi De Russis</a>; Modified by 
* Kaspars Bulindzs, Janis Karklins, Dmitrijs Ozerskis, Andrejs Paramonovs, Andrejs Derevjanko, Antons Kalcevs.  
* @version 1.0 (2017-02-24)
* @since 1.0 (2017-02-24)
*
*/
public class ObjRecognition extends Application {
	/**
	* The main entry point for all JavaFX applications.
	*/
	@Override
	public void start(Stage primaryStage) {
		try {
			// load the FXML resource
			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("ObjRecognition.fxml"));
			// set a whitesmoke background
			root.setStyle("-fx-background-color: whitesmoke;");
			// create and style a scene
			Scene scene = new Scene(root, 800, 800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			// create the stage with the given title and the previously created scene
			primaryStage.setTitle("Dice Counting");
			primaryStage.setScene(scene);
			// show the GUI
			primaryStage.show();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
     * Start of the program
     * @param args Argument from main method
     */
	public static void main(String[] args) {
		// load the native OpenCV library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		launch(args);
	}
}
