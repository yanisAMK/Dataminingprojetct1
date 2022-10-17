package src.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.tools.ant.taskdefs.Java;
import src.app.Data;
import src.app.HistogrammsPlot;
import src.app.Statistics;
import src.app.WhiskersPlot;

import java.util.ArrayList;
import java.util.Collections;

public class MainInterfaceController {
    Data data = new Data();
    Statistics stats = new Statistics();
    HistogrammsPlot histogram = new HistogrammsPlot();
    WhiskersPlot  whiskers = new WhiskersPlot();

    //Window fields
    private Stage stage;
    private Scene scene;
    private Parent root;
    //controller attributes
    public static String filePath = "";
    private static ActionEvent tempEvent;
    public static String fileExtension = "txt";

    @FXML
    private Button loadbutton;
    public ComboBox<String> attributBox = new ComboBox<>();

    @FXML
    void loadData(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Ressource File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XLSX files (*.XLSX, *.xlsx)", "*.XLSX", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        String path = fileChooser.showOpenDialog(stage).getAbsolutePath();
        fileExtension = path.split("\\.")[1];
        filePath = path;
        data.reaData(path);
        StringBuilder s = new StringBuilder();


        ObservableList<String> x =  FXCollections.observableArrayList();
        x.addAll(data.attributnames);
        attributBox.setItems(x);

    }

}
