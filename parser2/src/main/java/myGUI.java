import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;


public class myGUI extends Application {
    private TableView<Parser.Item> table = new TableView<Parser.Item>();
    public static void main(String[] args) {
        launch(args);
    }
    private final ObservableList<Parser.Item> myList = FXCollections.observableArrayList();


    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Sample");
        stage.setWidth(300);
        stage.setHeight(500);

        final Label label = new Label("Exchange Rates");
        label.setFont(new Font("Arial", 20));

        TableColumn firstNameCol = new TableColumn("Date");
        TableColumn lastNameCol = new TableColumn("Value");
        lastNameCol.setMinWidth(125);
        firstNameCol.setMinWidth(125);
        final VBox vbox = new VBox();
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);

        table.getColumns().addAll(firstNameCol, lastNameCol);

        try{
            DB.mySelectForTable(myList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        table.setItems(myList);
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<Parser.Item, String>("Date"));
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<Parser.Item, String>("Value"));


        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);

        stage.show();
    }
}
