import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class myGUI extends Application {
    private TableView<Parser.Item> table = new TableView<>();

    private final ObservableList<Parser.Item> items = FXCollections.observableArrayList();

    private XYChart.Series<String,Number> res = new XYChart.Series<>();

    public static void main(String[] args) {   launch(args);   }
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new BorderPane());

        stage.setTitle("Table View Sample");
        stage.setWidth(800);
        stage.setHeight(510);

        final Label label = new Label("Exchange Rates");
        label.setFont(new Font("Arial", 20));

        TableColumn firstNameCol = new TableColumn("Date");
        TableColumn lastNameCol = new TableColumn("Value");
        lastNameCol.setMinWidth(125);
        firstNameCol.setMinWidth(125);
        table.getColumns().addAll(firstNameCol, lastNameCol);

        // График
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Value");

        LineChart<String,Number > lineChart = new LineChart<>(xAxis,yAxis);
        res.setName("USD");


        refresh();
        table.setItems(items);
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<Parser.Item, String>("Date"));
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<Parser.Item, String>("Value"));
        lineChart.getData().add(res);


        final Button myButton = new Button("Refresh");

        myButton.setOnAction(e -> refresh());

        final VBox vBox = new VBox();
        vBox.setPadding(new Insets(10, 0, 10, 10));
        vBox.getChildren().addAll(label, table);
        final HBox hBox = new HBox();
        hBox.getChildren().addAll(vBox, lineChart);
        final VBox vBoxMain = new VBox();
        vBoxMain.getChildren().addAll(hBox, myButton);

        ((BorderPane) scene.getRoot()).setCenter(vBoxMain);
        stage.setScene(scene);

        stage.show();
    }
    private void refresh(){
        items.clear();
        res.getData().clear();
        // Таблица
        try{
            DB.mySelectForTable(items);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // График
        for(Parser.Item item : items){
            Float value = Float.valueOf(item.getValue().replace(",","."));
            res.getData().add(new XYChart.Data<String,Number>(item.getDate(),value));

        }
    }
}
