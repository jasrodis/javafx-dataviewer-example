package org.charts.dataviewer.javafx.example;

import org.charts.dataviewer.javafx.example.factory.JavaFxDataViewerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class JavaFxDataViewerExample extends Application {

	private static final Logger log = LoggerFactory.getLogger(JavaFxDataViewerExample.class);

	private final String PROJECT_TITLE = "DataViewer Example";
	private final int WINDOW_WIDTH = 1024;
	private final int WINDOW_HEIGHT = 768;

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		log.info("Dataviewer Example Started");

		/**********************************************
		 * Change the test
		 **********************************************/
		HBox testHBox = test1();
		// HBox testHBox = test2();
		// HBox testHBox = test3();
		/**********************************************/

		Scene mainScene = new Scene(testHBox);
		stage.setScene(mainScene);
		stage.setTitle(PROJECT_TITLE);
		stage.setHeight(WINDOW_HEIGHT);
		stage.setWidth(WINDOW_WIDTH);
		stage.setOnCloseRequest(e -> closeApplication());
		stage.show();
	}

	private void closeApplication() {
		log.info("Exiting...");
		Platform.exit();
		System.exit(0);
	}

	@SuppressWarnings("unused")
	private HBox test1() {
		VBox vboxLeft = new VBox();
		vboxLeft.getChildren().add(JavaFxDataViewerFactory.createDataViewerExample1());
		vboxLeft.getChildren().add(JavaFxDataViewerFactory.createDataViewerExample2());
		VBox vboxMiddle = new VBox();
		vboxMiddle.getChildren().add(JavaFxDataViewerFactory.createDataViewerExample3());
		vboxMiddle.getChildren().add(JavaFxDataViewerFactory.createDataViewerExample4());
		VBox vboxRight = new VBox();
		vboxRight.getChildren().add(JavaFxDataViewerFactory.createDataViewerExample5());
		vboxRight.getChildren().add(JavaFxDataViewerFactory.createDataViewerExample6());

		HBox hbox = new HBox();
		hbox.getChildren().addAll(vboxLeft, vboxMiddle, vboxRight);
		return hbox;
	}

	@SuppressWarnings("unused")
	private HBox test2() {
		VBox vboxLeft = new VBox();
		vboxLeft.getChildren().add(JavaFxDataViewerFactory.createDataViewerTuneExample(1));
		vboxLeft.getChildren().add(JavaFxDataViewerFactory.createDataViewerTuneExample(2));
		VBox vboxRight = new VBox();
		vboxRight.getChildren().add(JavaFxDataViewerFactory.createDataViewerTuneExample(3));
		vboxRight.getChildren().add(JavaFxDataViewerFactory.createDataViewerTuneExample(4));

		HBox hbox = new HBox();
		hbox.getChildren().addAll(vboxLeft, vboxRight);
		return hbox;
	}

	@SuppressWarnings("unused")
	private HBox test3() {
		VBox vboxLeft = new VBox();
		vboxLeft.getChildren().add(JavaFxDataViewerFactory.createDataViewerExample7());
		VBox vboxMiddle = new VBox();
		vboxMiddle.getChildren().add(JavaFxDataViewerFactory.createDataViewerExample8());
		VBox vboxRight = new VBox();
		vboxRight.getChildren().add(JavaFxDataViewerFactory.createDataViewerExample9());

		HBox hbox = new HBox();
		hbox.getChildren().addAll(vboxLeft, vboxMiddle, vboxRight);
		return hbox;
	}

}
