import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
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
    private XYChart.Series<String,Number> res = new XYChart.Series<>();
    private TableColumn firstNameCol = new TableColumn("Date");
    private TableColumn lastNameCol = new TableColumn("Value");
    @Override
    public void start(Stage stage) {
        final VBox vbox = new VBox();
        final HBox hbox = new HBox();
        final VBox vboxMain = new VBox();
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Sample");
        stage.setWidth(800);
        stage.setHeight(510);

        final Label label = new Label("Exchange Rates");
        label.setFont(new Font("Arial", 20));

        lastNameCol.setMinWidth(125);
        firstNameCol.setMinWidth(125);
        vbox.setPadding(new Insets(10, 0, 10, 10));
        table.getColumns().addAll(firstNameCol, lastNameCol);

        //график
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Value");

        LineChart<String,Number > lineChart = new LineChart<String, Number>(xAxis,yAxis);
        res.setName("USD");


        myConnect();
        lineChart.getData().add(res);


        final Button myButton = new Button("Refresh");
        myButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                myList.clear();
                //res.getData().add(new XYChart.Data<String,String>("xX","xe"));
                res.getData().clear();
                myConnect();
            }
        });
        vbox.getChildren().addAll(label, table);
        hbox.getChildren().addAll(vbox, lineChart);
        vboxMain.getChildren().addAll(hbox, myButton);
        ((Group) scene.getRoot()).getChildren().addAll(vboxMain);
        stage.setScene(scene);

        stage.show();
    }
    private void myConnect(){
        //Таблица
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

        //График
        for(Parser.Item Dop : myList){
            res.getData().add(new XYChart.Data<String,Number>(Dop.getDate(),Float.valueOf(Dop.getValue().replace(",","."))));

        }
    }
}
