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
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static src.app.Data.*;
import static src.app.DataProcessor.*;

public class MainInterfaceController {
    Data data = new Data();
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
            Data.reaData(path);

            ObservableList<String> x = FXCollections.observableArrayList();
            x.addAll(Data.attributnames);
            attributBox.setItems(x);
            attributBox1.setItems(x);

            init();
            printDescription();
            initPretraitement();
            populateTable(Data.unsortedAttributes);


/*            dropCategoricalAttributes();
            //treatMissingValues
            for (int i = 0; i < dataSet.size(); i++) {
                for (int j = 0; j < Data.dataSet.get(0).size(); j++) {
                    if (dataSet.get(i).get(j).equals(" ") || dataSet.get(i).get(j).equals("NaN")) {
                        dataSet.remove(i);
                        break;
                    }
                }
            }
            updateStructures(transposer(dataSet));*/





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
// Home panel

    @FXML
    private TableView<ObservableList<String>> tableView;
    //Contient les noms des attributs
    //public List<String> attributnames = new ArrayList<>();

    //HomeTabController
    public void init() { //ActionEvent actionEvent

        final List<List<String>> excelData = data.dataSet;

        ObservableList<ObservableList<String>> data_ = FXCollections.observableArrayList();
        //Copy data to an observable list
        for (List<String> excelDatum : excelData) {
            data_.add(FXCollections.observableArrayList(excelDatum));
        }

        tableView.setItems(data_);
        tableView.setEditable(true);

        ArrayList<TableColumn<ObservableList<String>, String>> columns = new ArrayList<>();

        //Create the table columns, set the cell value factory and add the column to the tableview.
        for (int i = 0; i < excelData.get(0).size(); i++) {
            final int curCol = i;
            final TableColumn<ObservableList<String>, String> column = new TableColumn<>(
                    Data.attributnames.get(curCol)
            );
            column.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().get(curCol))
            );

            //make column editable
            column.setCellFactory(TextFieldTableCell.forTableColumn());

            // This code will allow the user to double click on a cell and update it

            column.setOnEditCommit(event -> {
                ObservableList<String> row = event.getRowValue();
                row.set(tableView.focusModelProperty().get().focusedCellProperty().get().getColumn(), event.getNewValue());
            });

            columns.add(column);
        }

        tableView.getColumns().setAll(columns);
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
                for (int j = 0; j < tableView.getColumns().size(); j++) { //data.noOfCols
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

    @FXML
    public void save_2(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            try {

                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet spreadsheet = workbook.createSheet("sample");

                XSSFRow row = null;
                XSSFCell cell = null;

                row = spreadsheet.createRow(0);
                for (int j = 0; j < tableView_2.getColumns().size(); j++) { //data.noOfCols
                    cell = row.createCell(j);
                    cell.setCellValue(Data.attributnames.get(j));
                }

                for (int i = 0; i < tableView_2.getItems().size(); i++) {
                    row = spreadsheet.createRow(i+1);
                    for (int j = 0; j < tableView_2.getColumns().size(); j++) {
                        cell = row.createCell(j);
                        if (tableView_2.getColumns().get(j).getCellData(i) != null && !tableView_2.getColumns().get(j).getCellData(i).toString().equals(" ") && !tableView_2.getColumns().get(j).getCellData(i).toString().isEmpty()) {
                            if(Data.attribute_type.get(j) == 0){

                                cell.setCellValue(Float.parseFloat(tableView_2.getColumns().get(j).getCellData(i).toString()));
                                cell.setCellType(CellType.NUMERIC);
                            }
                            else {
                                cell.setCellValue(tableView_2.getColumns().get(j).getCellData(i).toString());
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

    public void printDescription() {

        //Description attribut
        String s = "";
        for(int i=0; i<Data.noOfCols; i++){
            s = s.concat("\n\n# attribut : " + Data.attributnames.get(i) + "\n  type attribut      : " + data.getTypeAtrribute(data.attribute_type.get(i)) + "\n  plage de valeurs : "+ data.getPlageValeur(i) + "\n  valeurs manquantes : " + Data.noMissing_attribute.get(i));

        }

        descriptionAttributs.appendText(s);

        //Description Global
        s = "";
        s = s.concat(" Nombre de lignes : " + Data.noOfRows + "\n Nombre de colonnes : " + data.noOfCols + "\n Nombre total de valeurs manquantes : " + Data.noMissing_total);
        s = s.concat("");

        descriptionGlobal.appendText(s);
    }

    private void clearFields(){
        tableView.getItems().clear();
        tableView_2.getItems().clear();
        Data.reset();
        descriptionAttributs.setText("");
        descriptionGlobal.setText("");
    }

    ////////////////////////////////////////////////////////////////////////////////
    //PRETRAITEMENT

    @FXML
    private ComboBox<String> normalizationCombo;

    @FXML
    private ComboBox<String> attributDiscretisation;
    @FXML
    private ComboBox<String> discretizationCombo;

    @FXML
    private Button reductionButton;

    @FXML
    private Button discretizationButton;

    @FXML
    private TextField noOfBins;

    @FXML
    private TableView<ObservableList<String>> tableView_2;

    @FXML
    private Tab pretraitButton;

    @FXML
    private Button missingValuesButton;

    @FXML
    private ComboBox<String> missingValuesCombo;

    @FXML
    private Button outliersButton;

    @FXML
    private ComboBox<String> outliersCombo;

    public List<List<String>> AttributeList_norm;
    public List<List<String>> AttributeList_disc;

    @FXML
    void initPretraitement() {
        ObservableList<String> options = FXCollections.observableArrayList("Min Max", "z-score");
        normalizationCombo.setItems(options);

        DataProcessor.normalized = false;

        options = FXCollections.observableArrayList("En classes d'effectifs égaux ", "En classes d'amplitudes égales");
        discretizationCombo.setItems(options);

        options = FXCollections.observableArrayList("Suppression",
                "Remplacement par la moyenne",
                "Remplacement par le mode",
                "Remplacement par la médiane");
        missingValuesCombo.setItems(options);

        options = FXCollections.observableArrayList("Suppression",
                "Remplacement par la moyenne",
                "Remplacement par le mode",
                "Remplacement par la médiane");
        outliersCombo.setItems(options);


        options = FXCollections.observableArrayList();
        options.addAll(Data.attributnames);
        attributeToDeleteCombo.setItems(options);

        options = FXCollections.observableArrayList();
        options.addAll(Data.attributnames);
        attributDiscretisation.setItems(options);

        options = FXCollections.observableArrayList();
        options.add("All");
        options.addAll(Data.attributnames);
        attributCombo.setItems(options);

        options = FXCollections.observableArrayList();
        options.add("All");

        for(int i=0;i<Data.unsortedAttributes.size();i++){

            if(Data.attribute_type.get(i) == 0){
                stats.calculMesures(Data.attributlist.get(i));

                if(! stats.mesures.get("nbOutliers").equals("0"))
                    options.add(Data.attributnames.get(i));
            }
        }
        attributWithOutliersCombo.setItems(options);


    }
    @FXML
    void normalization(ActionEvent event) {

        String selected = normalizationCombo.getSelectionModel().getSelectedItem();

        if (selected != null && !selected.isEmpty()) {

            List<List<String>> AttributeList;

            if (normalizationCombo.getSelectionModel().getSelectedItem().equals("Min Max"))
                AttributeList = DataProcessor.minMaxNormalization(Data.unsortedAttributes);
            else
                AttributeList = DataProcessor.zscoresNormalization(Data.unsortedAttributes);

            this.AttributeList_norm = AttributeList;

            populateTable(AttributeList);
            updateStructures(AttributeList_norm);
        }
    }

    @FXML
    void discretisation(ActionEvent event) {
        dropCategoricalAttributes();
        String selected = discretizationCombo.getSelectionModel().getSelectedItem();
        String selected_attribute =attributDiscretisation.getSelectionModel().getSelectedItem();

        int k=1;
        if(! noOfBins.getText().isEmpty())
            k = Integer.parseInt(noOfBins.getText());
        else
            k = (int) Math.ceil(1 + (10/3) * Math.log10(Data.unsortedAttributes.get(0).size()));

        if (selected != null && !selected.isEmpty() && selected_attribute != null && !selected_attribute.isEmpty()) { // && ! noOfBins.getText().isEmpty()

            int indexAtt = -1;
            for(int i = 0; i < Data.attributnames.size(); i++)
                if(Data.attributnames.get(i).equals(selected_attribute))
                    indexAtt = i;

            List<List<String>> AttributeList = new ArrayList<>();
            AttributeList = unsortedAttributes;
            List<String> discretizedAttribute = new ArrayList<>();

            if (selected.equals("En classes d'effectifs égaux ")){

                List<List<String>> sortedDataset = new ArrayList<>();

                sortedDataset = linkedListData_to_List(Data.sortDataSetByValuesOfAttribut(indexAtt));
                discretizedAttribute = DataProcessor.discretisation_effectifs(k, transposer(sortedDataset).get(indexAtt));
                AttributeList.set(indexAtt, discretizedAttribute);
            }
            else{
                    if(Data.attribute_type.get(indexAtt) == 0) {
                        discretizedAttribute = DataProcessor.discretisation_amplitude(k, Data.unsortedAttributes.get(indexAtt));
                        AttributeList.set(indexAtt, discretizedAttribute);
                    }
                    else
                        AttributeList.set(indexAtt, unsortedAttributes.get(indexAtt));
            }

            this.AttributeList_disc = AttributeList;

            Data.updateStructures(AttributeList);
            populateTable(AttributeList);

        }
    }

    private void populateTable(List<List<String>> AttributeList){ //ActionEvent actionEvent

        if(tableView_2.getItems() != (null)){
            /*tableView_2.getItems().clear();
            tableView_2.setItems(null);*/
            tableView_2.getItems().removeAll();
            tableView_2.getColumns().removeAll();
        }

        ObservableList<ObservableList<String>> data_ = FXCollections.observableArrayList();

        //Copy data to an observable list

        List<List<String>> dataset =  transposer(AttributeList);

        for (List<String> row : dataset) {
            data_.add(FXCollections.observableArrayList(row));
        }

        tableView_2.setItems(data_);
        tableView_2.setEditable(false);

        tableView_2.setTableMenuButtonVisible(true);

        ArrayList<TableColumn<ObservableList<String>, String>> columns = new ArrayList<>();

        //Create the table columns, set the cell value factory and add the column to the tableView_2.
        for (int i = 0; i < AttributeList.size(); i++) {
            int curCol = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(
                    Data.attributnames.get(curCol)
            );
            column.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().get(curCol))
            );

            // A context menu for the table column cells:
            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteColumnItem = new MenuItem("Remove Column");
            deleteColumnItem.setOnAction(e -> tableView_2.getColumns().remove(column));
            contextMenu.getItems().add(deleteColumnItem);
            column.setContextMenu(contextMenu);


            columns.add(column);
        }

        tableView_2.getColumns().setAll(columns);
    }




    @FXML
    void dataReduction(ActionEvent event) {

        String info = eliminateRedundancy(Data.dataSet);
        System.out.println(info);
        //todo : print info in a dialog window

        populateTable(unsortedAttributes);

        //update attribute comboBox
        ObservableList<String> options = FXCollections.observableArrayList();
        options.add("All");
        options.addAll(Data.attributnames);
        attributCombo.setItems(options);

        options = FXCollections.observableArrayList();
        options.add("All");
        for(int i=0;i<Data.unsortedAttributes.size();i++){
            if(Data.attribute_type.get(i) == 0){
                stats.calculMesures(Data.attributlist.get(i));
                if(! stats.mesures.get("nbOutliers").equals("0"))
                    options.add(Data.attributnames.get(i));
            }
        }
        attributWithOutliersCombo.setItems(options);

    }

    @FXML
    public void treatMissingValues(ActionEvent event){

        String selected = missingValuesCombo.getSelectionModel().getSelectedItem();
        String selected_attribute = attributCombo.getSelectionModel().getSelectedItem();
        //int indexAtt = attributCombo.getSelectionModel().getSelectedIndex();

        if (selected != null && !selected.isEmpty() && selected_attribute != null && !selected_attribute.isEmpty()) {

            if (selected_attribute.equals("All")) {

                switch (missingValuesCombo.getSelectionModel().getSelectedItem()) {
                    case "Suppression":
                        for (int i = 0; i < dataSet.size(); i++) {
                            for (int j = 0; j < Data.dataSet.get(0).size(); j++) {
                                if (dataSet.get(i).get(j).equals(" ") || dataSet.get(i).get(j).equals("NaN")) {
                                    dataSet.remove(i);
                                    break;
                                }
                            }
                        }
                        updateStructures(transposer(dataSet));
                        break;
                    case "Remplacement par la moyenne":
                        for (int i = 0; i < Data.unsortedAttributes.size(); i++)
                            traitementValeursManquantes(Data.unsortedAttributes.get(i), Statistics.calculMoyenne(Data.unsortedAttributes.get(i)));
                        updateStructures(unsortedAttributes);
                        break;
                    case "Remplacement par le mode":
                        for (int i = 0; i < Data.unsortedAttributes.size(); i++)
                            traitementValeursManquantes(Data.unsortedAttributes.get(i), Statistics.calculMode(Data.unsortedAttributes.get(i)).get(0));
                        updateStructures(unsortedAttributes);
                        break;
                    case "Remplacement par la médiane":
                        for (int i = 0; i < Data.unsortedAttributes.size(); i++)
                            traitementValeursManquantes(Data.unsortedAttributes.get(i), Statistics.calculMedianne(Data.unsortedAttributes.get(i)));
                        updateStructures(unsortedAttributes);
                        break;
                }
                populateTable(unsortedAttributes);

            }
            else {
                int indexAtt = -1;
                for(int i = 0; i < Data.attributnames.size(); i++)
                    if(Data.attributnames.get(i).equals(selected_attribute))
                        indexAtt = i;

                switch (missingValuesCombo.getSelectionModel().getSelectedItem()) {
                    case "Suppression":
                        for (int i = 0; i < dataSet.size(); i++) {
                            if (dataSet.get(i).get(indexAtt).equals(" ")) {
                                dataSet.remove(i);
                                break;
                            }
                        }
                        updateStructures(transposer(dataSet));
                        break;
                    case "Remplacement par la moyenne":
                        traitementValeursManquantes(Data.unsortedAttributes.get(indexAtt), Statistics.calculMoyenne(Data.unsortedAttributes.get(indexAtt)));
                        updateStructures(unsortedAttributes);
                        break;
                    case "Remplacement par le mode":
                        traitementValeursManquantes(Data.unsortedAttributes.get(indexAtt), Statistics.calculMode(Data.unsortedAttributes.get(indexAtt)).get(0));
                        updateStructures(unsortedAttributes);
                        break;
                    case "Remplacement par la médiane":
                        traitementValeursManquantes(Data.unsortedAttributes.get(indexAtt), Statistics.calculMedianne(Data.unsortedAttributes.get(indexAtt)));
                        updateStructures(unsortedAttributes);
                        break;
                }
                populateTable(unsortedAttributes);
            }
        }
    }

    @FXML
    void treatOutliers(ActionEvent event) {
        String selected = outliersCombo.getSelectionModel().getSelectedItem();
        String selected_attribute = attributWithOutliersCombo.getSelectionModel().getSelectedItem();
        //int indexAtt = attributWithOutliersCombo.getSelectionModel().getSelectedIndex();

        if (selected != null && !selected.isEmpty() && selected_attribute != null && !selected_attribute.isEmpty()) {

            if (selected_attribute.equals("All")) {

                switch (outliersCombo.getSelectionModel().getSelectedItem()) {
                    case "Suppression":
                        System.out.println("can't delete all items");
                        break;
                    case "Remplacement par la moyenne":
                        for (int i = 0; i < Data.unsortedAttributes.size(); i++)
                            if (Data.attribute_type.get(i) == 0)  //Si l'attribut est numérique
                                traitementValeursAberrantes(Data.unsortedAttributes.get(i), i, Statistics.calculMoyenne(Data.unsortedAttributes.get(i)));
                        updateStructures(unsortedAttributes);
                        break;
                    case "Remplacement par le mode":
                        for (int i = 0; i < Data.unsortedAttributes.size(); i++)
                            if (Data.attribute_type.get(i) == 0)  //Si l'attribut est numérique
                                traitementValeursAberrantes(Data.unsortedAttributes.get(i), i, Statistics.calculMode(Data.unsortedAttributes.get(i)).get(0));
                        updateStructures(unsortedAttributes);
                        break;
                    case "Remplacement par la médiane":
                        for (int i = 0; i < Data.unsortedAttributes.size(); i++)
                            if (Data.attribute_type.get(i) == 0)  //Si l'attribut est numérique
                                traitementValeursAberrantes(Data.unsortedAttributes.get(i), i, Statistics.calculMedianne(Data.unsortedAttributes.get(i)));
                        updateStructures(unsortedAttributes);
                        break;
                }
                populateTable(unsortedAttributes);
            }
            else {

                int indexAtt = -1;
                for(int i = 0; i < Data.attributnames.size(); i++)
                    if(Data.attributnames.get(i).equals(selected_attribute))
                        indexAtt = i;

                switch (outliersCombo.getSelectionModel().getSelectedItem()) {
                    case "Suppression":
                        System.out.println(dataSet.size());
                        if (Data.attribute_type.get(indexAtt) == 0) { //Si l'attribut est numérique
                            List<Integer> listRowsHavingOutliers = traitementValeursAberrantes(Data.unsortedAttributes.get(indexAtt), indexAtt, " ");

                            if (!listRowsHavingOutliers.isEmpty()) {
                                int n = listRowsHavingOutliers.size() - 1;
                                if(n < dataSet.size()/2){
                                    for (int j = n; j >= 0; j--) {
                                        int ind = listRowsHavingOutliers.get(j);
                                        dataSet.remove(ind);
                                    }
                                    updateStructures(transposer(dataSet));
                                }
                                else{
                                    //todo : show an Alert
                                    System.out.println("Cette action supprimera une très grande partie de la dataset");
                                }

                            }
                        }

                        break;
                    case "Remplacement par la moyenne":
                        if (Data.attribute_type.get(indexAtt) == 0) { //Si l'attribut est numérique
                            traitementValeursAberrantes(Data.unsortedAttributes.get(indexAtt), indexAtt, Statistics.calculMoyenne(Data.unsortedAttributes.get(indexAtt)));
                            updateStructures(unsortedAttributes);
                        }
                        break;
                    case "Remplacement par le mode":
                        if (Data.attribute_type.get(indexAtt) == 0) { //Si l'attribut est numérique
                            traitementValeursAberrantes(Data.unsortedAttributes.get(indexAtt), indexAtt, Statistics.calculMode(Data.unsortedAttributes.get(indexAtt)).get(0));
                            updateStructures(unsortedAttributes);
                        }
                        break;
                    case "Remplacement par la médiane":
                        if (Data.attribute_type.get(indexAtt) == 0) { //Si l'attribut est numérique
                            traitementValeursAberrantes(Data.unsortedAttributes.get(indexAtt), indexAtt, Statistics.calculMedianne(Data.unsortedAttributes.get(indexAtt)));
                            updateStructures(unsortedAttributes);
                        }
                        break;
                }
                populateTable(unsortedAttributes);
            }
            }
        }


    @FXML
    private Button deleteAttributeButton;
    @FXML
    private ComboBox<String> attributeToDeleteCombo;
    @FXML
    public void deleteColumn(ActionEvent actionEvent) {
        String selected_attribute = attributeToDeleteCombo.getSelectionModel().getSelectedItem();
        //int indexAtt = attributeToDeleteCombo.getSelectionModel().getSelectedIndex();

        if (selected_attribute != null && !selected_attribute.isEmpty()) {
            //attribute index
            int indexAtt = -1;
            for(int i = 0; i < Data.attributnames.size(); i++)
                if(Data.attributnames.get(i).equals(selected_attribute))
                    indexAtt = i;

            suppressionAttribut(indexAtt);
            populateTable(unsortedAttributes);

            attributeToDeleteCombo.getItems().remove(indexAtt);

        }
    }

    @FXML
    private Button dropCatButton;
    @FXML
    public void eliminationAttributsCategorique(ActionEvent event) {
        Data.dropCategoricalAttributes();
        populateTable(unsortedAttributes);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// PART 3
    @FXML
    private TextArea frequentItemsArea;
    @FXML
    private TextArea rulesArea;
    @FXML
    private TextField minConfidence;
    @FXML
    private TextField minSupport;

    @FXML
    void mine(ActionEvent event) {

        if( !minConfidence.getText().isEmpty() && !minSupport.getText().isEmpty()) {
            
            float s = Float.parseFloat(minSupport.getText());
            float c = Float.parseFloat(minConfidence.getText());

            Helpers Helpers = new Helpers();

            Helpers.setSupportMin(s);
            Helpers.setConfianceMin(c);

            //call Apriori
            List<String> listAssoc = new ArrayList<>();
            listAssoc = Helpers.Apriori(s, c, Data.unsortedAttributes, 0);


            StringBuilder sb = new StringBuilder();

            for(String a : listAssoc){
                sb.append(a).append("\n"); //.toString()
            }

            //frequentItemsArea.setText(sb.toString());
            rulesArea.setText(sb.toString());

            String freq = "freq";
            frequentItemsArea.setText(freq);


        }


        }

}



