package grapher.ui;

import java.util.Optional;
import javafx.application.Application;
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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;



public class Main extends Application {
        @Override
	public void start(Stage stage) {
		BorderPane root = new BorderPane();
                ObservableList<String> functNames = FXCollections.observableArrayList();
                //Recuperer les parametres passer au programme
                getParameters().getRaw().forEach((param) -> {
                    functNames.add(param);
                });
                
                ListView<String> listView = new ListView<>(functNames);
                listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                ObservableList<String> selectionCourrante = listView.getSelectionModel().getSelectedItems();//se mets automatiquement a jour
                GrapherCanvas grapher = new GrapherCanvas(functNames,selectionCourrante);
                selectionCourrante.addListener((Observable o) -> {
                    grapher.redraw();
                    System.out.println(listView.getSelectionModel().getSelectedItems());
                });
                
                Button addBtn = new Button("+");
                addBtn.setPrefSize(40, 40);
                addBtn.setOnAction((ActionEvent event) -> {
                    System.out.println("Add Button");
                    TextInputDialog inputDialog = new TextInputDialog("Expression");
                    inputDialog.setTitle("Expression");
                    inputDialog.setHeaderText("Nouvelle expression : ");
                    inputDialog.setContentText("expression(x)");

                    Optional<String> resultat = inputDialog.showAndWait();

                    resultat.ifPresent(functName -> {
                        functNames.add(functName);
                        grapher.setFunctions(functNames);//update grapher with new funct list
                    });
                });
                Button deleteBtn = new Button("-");
                deleteBtn.setPrefSize(40, 40);
                deleteBtn.setOnAction((ActionEvent event) -> {
                    System.out.println("Delete Button");
                    selectionCourrante.forEach((functName) -> {
                        functNames.remove(functName);
                    });  
                    grapher.setFunctions(functNames);
                });
                
                HBox hbox = new HBox(50);
                hbox.setPadding(new Insets(0, 0, 0, 50));//permet d'ajouter du padding (dans ce cas 50 a gauche)
                hbox.getChildren().addAll(addBtn,deleteBtn);
                ToolBar toolBar = new ToolBar(hbox);
                MenuItem addMenu = new MenuItem("Ajouter");
                addMenu.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
                addMenu.setOnAction((ActionEvent e) -> {
                    System.out.println("Add Menu");
                    TextInputDialog inputDialog = new TextInputDialog("Expression");
                    inputDialog.setTitle("Expression");
                    inputDialog.setHeaderText("Nouvelle expression : ");
                    inputDialog.setContentText("expression(x)");

                    Optional<String> resultat = inputDialog.showAndWait();

                    resultat.ifPresent(functName -> {
                        functNames.add(functName);
                        grapher.setFunctions(functNames);//update grapher with new funct list
                    });
                });
                MenuItem deleteMenu = new MenuItem("Supprimer");
                deleteMenu.setAccelerator(new KeyCodeCombination(KeyCode.BACK_SPACE, KeyCombination.CONTROL_DOWN));
                deleteMenu.setOnAction((ActionEvent e) -> {
                    System.out.println("Delete Menu");
                    selectionCourrante.forEach((functName) -> {
                        functNames.remove(functName);
                    });  
                    grapher.setFunctions(functNames);
                });
                final Menu menu = new Menu("Expression");
                menu.getItems().addAll(addMenu,new SeparatorMenuItem(),deleteMenu);
                MenuBar menuBar = new MenuBar();
                menuBar.getMenus().addAll(menu);
                root.setTop(menuBar);
                VBox vbox = new VBox(20);
                vbox.getChildren().addAll(listView,toolBar);
		SplitPane rootSplitPane = new SplitPane();
                rootSplitPane.getItems().addAll(new Group(vbox),grapher);
                root.setCenter(rootSplitPane);
		stage.setTitle("grapher");
		stage.setScene(new Scene(root));
		stage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}
}