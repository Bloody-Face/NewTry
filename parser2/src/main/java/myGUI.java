import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


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
        stage.setWidth(800);
        stage.setHeight(500);

        final Label label = new Label("Exchange Rates");
        label.setFont(new Font("Arial", 20));

        TableColumn firstNameCol = new TableColumn("Date");
        TableColumn lastNameCol = new TableColumn("Value");
        lastNameCol.setMinWidth(125);
        firstNameCol.setMinWidth(125);
        final VBox vbox = new VBox();
        final HBox hbox = new HBox();
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




        //график
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");

        CategoryAxis yAxis = new CategoryAxis();
        yAxis.setLabel("Value");

        LineChart<String,String > lineChart = new LineChart<String,String>(xAxis,yAxis);
        XYChart.Series<String,String> res = new XYChart.Series<>();
        for(Parser.Item Dop : myList){
            res.getData().add(new XYChart.Data<String,String>(Dop.getDate(),Dop.getValue()));
        }

        lineChart.getData().add(res);

        hbox.getChildren().addAll(vbox, lineChart);
        ((Group) scene.getRoot()).getChildren().addAll(hbox);
        stage.setScene(scene);

        stage.show();
    }
}
