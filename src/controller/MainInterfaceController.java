package src.controller;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import src.app.*;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainInterfaceController {
    Data data = new Data();
    //dataset ds;
    Statistics stats = new Statistics();
    HistogrammsPlot histogram = new HistogrammsPlot();
    WhiskersPlot whiskers = new WhiskersPlot();
    HistogrammsPlot histogram1 = new HistogrammsPlot();
    WhiskersPlot whiskers1 = new WhiskersPlot();
    ScatterPlot scatter = new ScatterPlot();

    //Window fields
    private Stage stage;
    //controller attributes
    public static String filePath = "";
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
    public Button exportButton;

    @FXML
    public TextArea descriptionAttributs;

    @FXML
    public TextArea descriptionGlobal;


    @FXML
    void loadData(ActionEvent event) {
        clearFields();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Ressource File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XLSX files (*.XLSX, *.xlsx)", "*.XLSX", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        File selectedFile = fileChooser.showOpenDialog(stage);

        data =new Data();
        if (selectedFile != null) {
            String path = selectedFile.getAbsolutePath();
            fileExtension = path.split("\\.")[1];
            filePath = path;
            data.reaData(path);
            StringBuilder s = new StringBuilder();
            ObservableList<String> x = FXCollections.observableArrayList();
            x.addAll(data.attributnames);
            attributBox.setItems(x);
            attributBox1.setItems(x);

            init();
            printDescription(path);
        }
    }

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

//////////////////////////////////////////////////////////////////////////////////////////////////////////
// partie à séparer

    @FXML
    private TableView<ObservableList<String>> tableView;
    //Contient les noms des attributs
    public List<String> attributnames = new ArrayList<>();

    //HomeTabController
    public void init() { //ActionEvent actionEvent
        // this.tableView = new TableView();
        final List<List<String>> excelData = data.dataSet;

        ObservableList<ObservableList<String>> data_ = FXCollections.observableArrayList();
        //Add excel data to an observable list
        for (int i = 0; i < excelData.size(); i++) {
            data_.add(FXCollections.observableArrayList(excelData.get(i)));
        }

        tableView.setItems(data_);
        tableView.setEditable(true);

        //Create the table columns, set the cell value factory and add the column to the tableview.
        for (int i = 0; i < excelData.get(0).size(); i++) {
            final int curCol = i;
            final TableColumn<ObservableList<String>, String> column = new TableColumn<>(
                    data.attributnames.get(curCol)
            );
            column.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().get(curCol))
            );

            //make column editable
            column.setCellFactory(TextFieldTableCell.forTableColumn());
            /**
             * This code will allow the user to double click on a cell and update it
             */


            column.setOnEditCommit(event -> {
                ObservableList<String> row = event.getRowValue();
                row.set(tableView.focusModelProperty().get().focusedCellProperty().get().getColumn(), event.getNewValue());
            });

            tableView.getColumns().add(column);

        }
    }


    public void deleteRow(ActionEvent actionEvent) {
        int selected = tableView.getSelectionModel().getSelectedIndex();
        tableView.getItems().remove(selected);
    }

    @FXML
    public void save(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            try {

                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet spreadsheet = workbook.createSheet("sample");

                XSSFRow row = null;
                XSSFCell cell = null;

                row = spreadsheet.createRow(0);
                for (int j = 0; j < data.noOfCols; j++) {
                    cell = row.createCell(j);
                    cell.setCellValue(Data.attributnames.get(j));
                }

                for (int i = 0; i < tableView.getItems().size(); i++) {
                    row = spreadsheet.createRow(i+1);
                    for (int j = 0; j < tableView.getColumns().size(); j++) {
                        cell = row.createCell(j);
                        if (tableView.getColumns().get(j).getCellData(i) != null && !tableView.getColumns().get(j).getCellData(i).toString().equals(" ") && !tableView.getColumns().get(j).getCellData(i).toString().isEmpty()) {
                            if(Data.attribute_type.get(j) == 0){

                                cell.setCellValue(Float.parseFloat(tableView.getColumns().get(j).getCellData(i).toString()));
                                cell.setCellType(CellType.NUMERIC);
                            }
                            else {
                                cell.setCellValue(tableView.getColumns().get(j).getCellData(i).toString());
                                cell.setCellType(CellType.STRING);
                            }

                        } else {
                            cell.setCellValue("");
                        }
                    }
                }

                FileOutputStream out = new FileOutputStream(file + ".xlsx");
                workbook.write(out);
                out.close();
                System.out.println("Data is written Successfully");

            } catch (Exception e) {
                System.out.println(e);

            }

        }
    }

    public void printDescription(String filepath) {

        //Description attribut
        String s = "";
        for(int i=0; i<data.noOfCols; i++){
            s = s.concat("\n\n# attribut : " + data.attributnames.get(i) + "\n  type attribut      : " + data.getTypeAtrribute(data.attribute_type.get(i)) + "\n  plage de valeurs : "+ data.getPlageValeur(i) + "\n  valeurs manquantes : " + data.noMissing_attribute.get(i));

        }

        descriptionAttributs.appendText(s);

        //Description Global
        s = "";
        s = s.concat(" Nombre de lignes : " + data.noOfRows + "\n Nombre de colonnes : " + data.noOfCols + "\n Nombre total de valeurs manquantes : " + data.noMissing_total);
        s = s.concat("");

        descriptionGlobal.appendText(s);
    }

    private void clearFields(){
        tableView.getItems().clear();
        descriptionAttributs.setText("");
        descriptionGlobal.setText("");
    }
}
