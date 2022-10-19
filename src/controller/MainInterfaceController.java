package src.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.tools.ant.taskdefs.Java;
import src.app.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;

public class MainInterfaceController {
    Data data = new Data();
    Statistics stats = new Statistics();
    HistogrammsPlot histogram = new HistogrammsPlot();
    WhiskersPlot  whiskers = new WhiskersPlot();
    HistogrammsPlot histogram1 = new HistogrammsPlot();
    WhiskersPlot  whiskers1 = new WhiskersPlot();
    ScatterPlot  scatter = new ScatterPlot();

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
    public Label moyenneLabel;
    public Label medianeLabel;
    public Label modeLabel;
    public Label maxLabel;
    public Label q1Label;
    public Label q2Label;
    public Label q3Label;
    public Label minLabel;
    public Label iqrLabel;
    public Label nbOutlierLabel;

    public Button calculerButton;

    public Button calculerButton1;
    public ComboBox<String> attributBox1 = new ComboBox<>();
    public Label moyenneLabel1;
    public Label medianeLabel1;
    public Label modeLabel1;
    public Label maxLabel1;
    public Label q1Label1;
    public Label q2Label1;
    public Label q3Label1;
    public Label minLabel1;
    public Label iqrLabel1;
    public Label nbOutlierLabel1;
    public Button compareButton;




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
        attributBox1.setItems(x);

    }

    @FXML
    void calculerMesures(ActionEvent event){
        int index = data.attributnames.indexOf(attributBox.getValue());
        stats.calculMesures(data.attributlist.get(index));

        moyenneLabel.setText(stats.mesures.get("moyenne"));
        medianeLabel.setText(stats.mesures.get("mediane"));
        modeLabel.setText(stats.mesures.get("mode"));
        maxLabel.setText(stats.mesures.get("max"));
        minLabel.setText(stats.mesures.get("min"));
        q1Label.setText(stats.mesures.get("q1"));
        q2Label.setText(stats.mesures.get("q2"));
        q3Label.setText(stats.mesures.get("q3"));
        iqrLabel.setText(stats.mesures.get("iqr"));
        nbOutlierLabel.setText(stats.mesures.get("nbOutliers"));

        histogram.generatehistogramplot(data.attributlist.get(index), data.attributnames.get(index));
        whiskers.generatewhiskerplot(data.attributlist.get(index), data.attributnames.get(index));


    }

    @FXML
    void calculerMesures2(ActionEvent event){
        int index1 = data.attributnames.indexOf(attributBox1.getValue());
        stats.calculMesures(data.attributlist.get(index1));

        moyenneLabel1.setText(stats.mesures.get("moyenne"));
        medianeLabel1.setText(stats.mesures.get("mediane"));
        modeLabel1.setText(stats.mesures.get("mode"));
        maxLabel1.setText(stats.mesures.get("max"));
        minLabel1.setText(stats.mesures.get("min"));
        q1Label1.setText(stats.mesures.get("q1"));
        q2Label1.setText(stats.mesures.get("q2"));
        q3Label1.setText(stats.mesures.get("q3"));
        iqrLabel1.setText(stats.mesures.get("iqr"));
        nbOutlierLabel1.setText(stats.mesures.get("nbOutliers"));

        histogram1.generatehistogramplot(data.attributlist.get(index1), data.attributnames.get(index1));
        whiskers1.generatewhiskerplot(data.attributlist.get(index1), data.attributnames.get(index1));


    }
    @FXML
    void compareAttributes(ActionEvent event){
        int index1 = data.attributnames.indexOf(attributBox1.getValue());
        int index = data.attributnames.indexOf(attributBox.getValue());

        scatter.generateScatterPlot(data.attributlist.get(index),
                data.attributlist.get(index1), data.attributnames.get(index), data.attributnames.get(index1));


    }


}
