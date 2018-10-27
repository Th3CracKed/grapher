package grapher.ui;

import grapher.fc.FunctionFactory;
import java.util.Optional;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TableView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;



public class Main extends Application {
        private ObservableList<rowModel> functList;
        private ObservableList<rowModel> selectionCourrante;
        private GrapherCanvas grapher;
        
        @Override
	public void start(Stage stage) {
                
                //Recuperer les parametres passer au programme
                functList = FXCollections.observableArrayList();
                getParameters().getRaw().forEach((param) -> {
                    ColorPicker color = new ColorPicker(Color.BLACK);//couleur par default
                    //si la couleur change -> redessiner le graphe
                    color.setOnAction((ActionEvent event) -> {
                        grapher.redraw();
                    });
                    functList.add(new rowModel(FunctionFactory.createFunction(param),color));
                });
                
                //creation & configuration du tableView (editable & selection Multiple & resize)
                TableView<rowModel> tableView = new TableView<>(functList);
                tableView.setEditable(true);
                tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                //liste de ligne selectionner, se mettent automatiquement a jour
                selectionCourrante = tableView.getSelectionModel().getSelectedItems();
                //mettre a jour le graphe si la selection a changer, permet de redessiner les functions selectionné en gras
                selectionCourrante.addListener((Observable o) -> {
                    grapher.redraw();
                });
                
                TableColumn<rowModel,String> expColumn = new TableColumn<>("Expression");
                expColumn.setCellValueFactory((CellDataFeatures<rowModel, String> row) -> row.getValue().getFunctionProperty());
                
                //Permettre la modification grace a un textfieldTable
                expColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		expColumn.setOnEditCommit((event) -> {
                    System.out.println("Modification Confirmer");
                    ColorPicker currentColor = functList.get(event.getTablePosition().getRow()).getColor();//recuperer la couleur 
                    //creation d'une nouvelle ligne avec la nouvelle valeur de fonction
                    rowModel newRow = new rowModel(FunctionFactory.createFunction(event.getNewValue()),currentColor);
                    functList.set(event.getTablePosition().getRow(), newRow);//remplacer l'ancien ligne avec la nouvelle
                });
                
                TableColumn<rowModel,ColorPicker> colorColumn = new TableColumn<>("Color");
                colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
                
                tableView.getColumns().setAll(expColumn,colorColumn);//Ajouter les deux colonnes au tableView
                
                BorderPane rootborderPane = new BorderPane();
                    SplitPane rootSplitPane = new SplitPane();
                        VBox vbox = new VBox();
                        vbox.getChildren().addAll(tableView,new ToolBar(getAddBtn(),getDeleteBtn()));
                        grapher = new GrapherCanvas(functList,selectionCourrante);
                    rootSplitPane.getItems().addAll(vbox,grapher);
                rootborderPane.setCenter(rootSplitPane);
                rootborderPane.setTop(getMenuBar());
		stage.setScene(new Scene(rootborderPane));
                stage.setTitle("grapher");
		stage.show();
	}
        
	public static void main(String[] args) {
		launch(args);
	}

        private void addAction() {
            TextInputDialog inputDialog = new TextInputDialog("Expression");
            inputDialog.setTitle("Expression");
            inputDialog.setHeaderText("Nouvelle expression : ");
            inputDialog.setContentText("");

            Optional<String> resultat = inputDialog.showAndWait();

            resultat.ifPresent(functName -> {
                functList.add(new rowModel(FunctionFactory.createFunction(functName),new ColorPicker(Color.BLACK)));
                grapher.redraw();
            });
        }
        
        private void deleteAction() {
            functList.removeAll(selectionCourrante);
            grapher.redraw();
        }

        private MenuBar getMenuBar() {
            MenuItem addMenu = new MenuItem("Ajouter");
            addMenu.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
            addMenu.setOnAction((ActionEvent e) -> {
            System.out.println("Add Menu");
            addAction();
            });
            MenuItem deleteMenu = new MenuItem("Supprimer");
            deleteMenu.setAccelerator(new KeyCodeCombination(KeyCode.BACK_SPACE, KeyCombination.CONTROL_DOWN));
            deleteMenu.setOnAction((ActionEvent e) -> {
                System.out.println("Delete Menu");
                deleteAction();
            });
            final Menu menu = new Menu("Expression");
            menu.getItems().addAll(addMenu,new SeparatorMenuItem(),deleteMenu);
            MenuBar menuBar = new MenuBar();
            menuBar.getMenus().addAll(menu);
            return menuBar;
        }

        private Button getAddBtn() {
            Button addBtn = new Button("+");
            addBtn.setPrefSize(40, 40);
            addBtn.setOnAction((ActionEvent event) -> {
                System.out.println("Add Button");
                addAction();
            });
            return addBtn;
        }

        private Button getDeleteBtn() {
            Button deleteBtn = new Button("-");
            deleteBtn.setPrefSize(40, 40);
            deleteBtn.setOnAction((ActionEvent event) -> {
                System.out.println("Delete Button");
                deleteAction();
            });
            return deleteBtn;
        }
}