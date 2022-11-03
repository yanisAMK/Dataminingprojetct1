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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import src.app.Data;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainWindowController {

    private Scene scene;
    private Parent root;
    private Stage stage;
    Data data = new Data();



    public static String filePath = "";
    public static String fileExtension = "txt";




    @FXML
    public TextArea descriptionAttributs;

    @FXML
    public TextArea descriptionGlobal;

    @FXML
    private TableView<ObservableList<String>> tableView_2;


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
            //attributBox.setItems(x);
            //attributBox1.setItems(x);

            init();
            printDescription();
            //initPretraitement();
            //populateTable(Data.unsortedAttributes);


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



    public void switchToMeasureWindow(ActionEvent event) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/src/views/MeasureWindow.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void switchToPreProcessingWindow(ActionEvent event) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/src/views/preProcessingWindow.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
