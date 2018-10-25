package grapher.ui;

import grapher.fc.Function;
import grapher.fc.FunctionFactory;
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
		BorderPane rootborderPane = new BorderPane();
                ObservableList<Function> functList = FXCollections.observableArrayList();
                //Recuperer les parametres passer au programme
                getParameters().getRaw().forEach((param) -> {
                    functList.add(FunctionFactory.createFunction(param));
                });
                
                ListView<Function> listView = new ListView<>(functList);
                listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                ObservableList<Function> selectionCourrante = listView.getSelectionModel().getSelectedItems();//se mets automatiquement a jour
                GrapherCanvas grapher = new GrapherCanvas(functList,selectionCourrante);
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
                        functList.add(FunctionFactory.createFunction(functName));
                        grapher.redraw();
                    });
                });
                Button deleteBtn = new Button("-");
                deleteBtn.setPrefSize(40, 40);
                deleteBtn.setOnAction((ActionEvent event) -> {
                    System.out.println("Delete Button");
                    while(!selectionCourrante.isEmpty()){
                        functList.remove(selectionCourrante.get(0));
                        grapher.redraw();
                    }
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
                        functList.add(FunctionFactory.createFunction(functName));
                        grapher.redraw();
                    });
                });
                MenuItem deleteMenu = new MenuItem("Supprimer");
                deleteMenu.setAccelerator(new KeyCodeCombination(KeyCode.BACK_SPACE, KeyCombination.CONTROL_DOWN));
                deleteMenu.setOnAction((ActionEvent e) -> {
                    System.out.println("Delete Menu");
                    while(!selectionCourrante.isEmpty()){
                        functList.remove(selectionCourrante.get(0));
                        grapher.redraw();
                    }
                });
                final Menu menu = new Menu("Expression");
                menu.getItems().addAll(addMenu,new SeparatorMenuItem(),deleteMenu);
                MenuBar menuBar = new MenuBar();
                menuBar.getMenus().addAll(menu);
                rootborderPane.setTop(menuBar);
                VBox vbox = new VBox(20);
                vbox.getChildren().addAll(listView,toolBar);
		SplitPane rootSplitPane = new SplitPane();
                rootSplitPane.getItems().addAll(new Group(vbox),grapher);
                rootborderPane.setCenter(rootSplitPane);
		stage.setTitle("grapher");
		stage.setScene(new Scene(rootborderPane));
		stage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}
}