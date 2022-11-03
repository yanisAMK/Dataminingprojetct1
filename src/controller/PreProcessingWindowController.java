package src.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import src.app.Data;
import src.app.DataProcessor;
import src.app.Statistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static src.app.Data.*;
import static src.app.Data.unsortedAttributes;
import static src.app.DataProcessor.*;
import static src.app.DataProcessor.traitementValeursAberrantes;

public class PreProcessingWindowController {

    private Scene scene;
    private Stage stage;
    private Parent root;
    Statistics stats = new Statistics();

    @FXML
    private ComboBox<String> normalizationCombo;

    @FXML
    private ComboBox<String> discretizationCombo;

    @FXML
    private Button reductionButton;

    public ComboBox<String> attributBox = new ComboBox<>();
    @FXML
    private ComboBox<String> attributCombo = new ComboBox<>();
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
    private ComboBox<String> attributWithOutliersCombo = new ComboBox<>();

    @FXML
    void initPretraitement() {
        ObservableList<String> options = FXCollections.observableArrayList("Min Max", "z-score");
        normalizationCombo.setItems(options);

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
        int k=1;
        if(! noOfBins.getText().isEmpty())
            k = Integer.parseInt(noOfBins.getText());
        else
            k = (int) Math.ceil(1 + (10/3) * Math.log10(Data.unsortedAttributes.get(0).size()));

        if (selected != null && !selected.isEmpty()) { // && ! noOfBins.getText().isEmpty()

            List<List<String>> AttributeList = new ArrayList<>();
            List<String> discretizedAttribute = new ArrayList<>();

            if (selected.equals("En classes d'effectifs égaux ")){

                List<List<String>> sortedDataset = new ArrayList<>();

                for (int i=0; i< Data.dataSet.get(i).size();i++)
                {
                    sortedDataset = linkedListData_to_List(Data.sortDataSetByValuesOfAttribut(i));
                    discretizedAttribute = DataProcessor.discretisation_effectifs(k, transposer(sortedDataset).get(i));
                    AttributeList.add(discretizedAttribute);
                }
            }
            else{
                for (int i=0; i< Data.unsortedAttributes.size();i++)
                {
                    if(Data.attribute_type.get(i) == 0) {
                        discretizedAttribute = DataProcessor.discretisation_amplitude(k, Data.unsortedAttributes.get(i));
                        AttributeList.add(discretizedAttribute);
                    }
                    else AttributeList.add(unsortedAttributes.get(i));
                }

            }
            //AttributeList = DataProcessor.discretisation_amplitude(0, Data.attributlist.get(0));

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



    public void switchToMeasureWindow(ActionEvent event) throws Exception{
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/src/views/MeasureWindow.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToMainInterface(ActionEvent event) throws Exception{
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/src/views/Maininterface.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



}
