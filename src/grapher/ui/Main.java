package grapher.ui;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;



public class Main extends Application {
	public void start(Stage stage) {
		BorderPane root = new BorderPane();
                ObservableList<String> functNames = FXCollections.observableArrayList();
                //recuperer les parametres passer au programme 
                for(String param: getParameters().getRaw()) {
                    functNames.add(param);
                }
                
                ListView<String> listView = new ListView<>(functNames);
                listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                ObservableList<String> selectionCourrante = listView.getSelectionModel().getSelectedItems();//se mets automatiquement a jour
                GrapherCanvas grapher = new GrapherCanvas(functNames,selectionCourrante);
                root.setCenter(grapher);
                selectionCourrante.addListener((Observable o) -> {
                    grapher.redraw();// ou bien recuperer le GraphicsContext est faire setLineWidth
                    System.out.println(listView.getSelectionModel().getSelectedItems());
                });
                
                Button addBtn = new Button("+");
                addBtn.setPrefSize(40, 40);
                addBtn.setOnAction((ActionEvent event) -> {
                    System.out.println("Add");
                });
                Button deleteBtn = new Button("-");
                deleteBtn.setPrefSize(40, 40);
                deleteBtn.setOnAction((ActionEvent event) -> {
                    System.out.println("Delete");
                });
                
                HBox hbox = new HBox(50);
                hbox.setPadding(new Insets(0, 0, 0, 50));//permet d'ajouter du padding (dans ce cas 50 a gauche)
                hbox.getChildren().addAll(addBtn,deleteBtn);
                ToolBar toolBar = new ToolBar(hbox);
                
                VBox vbox = new VBox(20);
                vbox.getChildren().addAll(listView,toolBar);
		SplitPane rootSplitPane = new SplitPane();
                rootSplitPane.getItems().addAll(new Group(vbox),root);
		stage.setTitle("grapher");
		stage.setScene(new Scene(rootSplitPane));
		stage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}
}