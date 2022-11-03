package src.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import src.app.*;

import java.util.List;
import java.util.Objects;

public class MeasureWindowController {

    //TODO: Pass data bettween controllers (save data in User Class).
    //TODO: Trigger Initialisation functions whene loading a scene (JavaFx event like Onload).
    //TODO: Create a MainWindow.fxml and add componenets to it.
    //TODO: Fix MainWindowController.
    //TODO: Set Controllers to each view.
    //TODO: Add Navigation Buttons to views.
    //TODO: Clean Controllers.





    private Scene scene;
    private Stage stage;
    private Parent root;

    Data data = new Data();
    Statistics stats = new Statistics();
    HistogrammsPlot histogram = new HistogrammsPlot();
    WhiskersPlot whiskers = new WhiskersPlot();
    HistogrammsPlot histogram1 = new HistogrammsPlot();
    WhiskersPlot whiskers1 = new WhiskersPlot();
    ScatterPlot scatter = new ScatterPlot();
    public ComboBox<String> attributBox = new ComboBox<>();
    @FXML
    private ComboBox<String> attributCombo = new ComboBox<>();

    @FXML
    private ComboBox<String> attributWithOutliersCombo = new ComboBox<>();
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
    public Label varianceLabel;
    public Label ecartTypeLabel;
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
    public Label varianceLabel1;
    public Label ecartTypeLabel1;


    @FXML
        // Affiche les mesures de l'attribut 1
    void calculerMesures(ActionEvent event) {
        int index = data.attributnames.indexOf(attributBox.getValue());
        if (stats.checktype(data.attributlist.get(index))) {
            stats.calculMesures(data.attributlist.get(index));
            iqrLabel.setText(stats.mesures.get("iqr"));
            nbOutlierLabel.setText(stats.mesures.get("nbOutliers"));
            moyenneLabel.setText(stats.mesures.get("moyenne"));
            maxLabel.setText(stats.mesures.get("max"));
            minLabel.setText(stats.mesures.get("min"));
            ecartTypeLabel.setText(stats.mesures.get("ecartType"));
            varianceLabel.setText(stats.mesures.get("variance"));
            medianeLabel.setText(stats.mesures.get("mediane"));
            modeLabel.setText(stats.mesures.get("mode"));
            q1Label.setText(stats.mesures.get("q1"));
            q2Label.setText(stats.mesures.get("q2"));
            q3Label.setText(stats.mesures.get("q3"));
            histogram.generatehistogramplot(data.attributlist.get(index), data.attributnames.get(index));
            whiskers.generatewhiskerplot(data.attributlist.get(index), data.attributnames.get(index));

        } else {
            //System.out.println("/");
            iqrLabel.setText(" ");
            nbOutlierLabel.setText(" ");
            moyenneLabel.setText(" ");
            maxLabel.setText(" ");
            minLabel.setText(" ");
            medianeLabel.setText(" ");
            varianceLabel.setText(" ");
            ecartTypeLabel.setText(" ");
            modeLabel.setText(stats.calculMode(data.attributlist.get(index)) + "");
            List<String> quartiles = stats.quartile(data.attributlist.get(index));
            q1Label.setText(quartiles.get(0));
            q2Label.setText(quartiles.get(1));
            q3Label.setText(quartiles.get(2));
        }
    }

    @FXML
        // affiche les mesures de l'attribut 2
    void calculerMesures2(ActionEvent event) {
        int index1 = data.attributnames.indexOf(attributBox1.getValue());
        if (stats.checktype(data.attributlist.get(index1))) {
            stats.calculMesures(data.attributlist.get(index1));
            iqrLabel1.setText(stats.mesures.get("iqr"));
            nbOutlierLabel1.setText(stats.mesures.get("nbOutliers"));
            moyenneLabel1.setText(stats.mesures.get("moyenne"));
            maxLabel1.setText(stats.mesures.get("max"));
            minLabel1.setText(stats.mesures.get("min"));
            ecartTypeLabel1.setText(stats.mesures.get("ecartType"));
            varianceLabel1.setText(stats.mesures.get("variance"));
            medianeLabel1.setText(stats.mesures.get("mediane"));
            modeLabel1.setText(stats.mesures.get("mode"));
            q1Label1.setText(stats.mesures.get("q1"));
            q2Label1.setText(stats.mesures.get("q2"));
            q3Label1.setText(stats.mesures.get("q3"));
            histogram1.generatehistogramplot(data.attributlist.get(index1), data.attributnames.get(index1));
            whiskers1.generatewhiskerplot(data.attributlist.get(index1), data.attributnames.get(index1));
        } else {
            iqrLabel1.setText(" ");
            nbOutlierLabel1.setText(" ");
            moyenneLabel1.setText(" ");
            maxLabel1.setText(" ");
            minLabel1.setText(" ");
            medianeLabel1.setText(" ");
            varianceLabel1.setText(" ");
            ecartTypeLabel1.setText(" ");
            modeLabel1.setText(stats.calculMode(data.attributlist.get(index1)) + "");
            List<String> quartiles = stats.quartile(data.attributlist.get(index1));
            q1Label1.setText(quartiles.get(0));
            q2Label1.setText(quartiles.get(1));
            q3Label1.setText(quartiles.get(2));
        }
    }

    @FXML
        // scatter plot pour les deux attributs
    void compareAttributes(ActionEvent event) {
        int index1 = data.attributnames.indexOf(attributBox1.getValue());
        int index = data.attributnames.indexOf(attributBox.getValue());
        scatter.generateScatterPlot(data.unsortedAttributes.get(index),
                data.unsortedAttributes.get(index1), data.attributnames.get(index), data.attributnames.get(index1));

    }


    public void switchToMainInterface(ActionEvent event) throws Exception{
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/src/views/Maininterface.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToPreProcessingWindow(ActionEvent event) throws Exception{
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/src/views/preProcessingWindow.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
