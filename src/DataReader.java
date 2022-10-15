import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class DataReader {
    static List<List<String>> dataset = new ArrayList<>();
    public static List<List<String>> attributlist = new ArrayList<>();
    public static List<String> attributnames = new ArrayList<>();
    static Analyser a = new Analyser();
    public List<List<String>> readdata(String filepath){
        try {
            FileInputStream file = new FileInputStream(
                    new File(filepath)
            );

            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()){
                List<String> dataline = new ArrayList<>();
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()){
                    Cell cell = cellIterator.next();

                    switch (cell.getCellType()){
                        case NUMERIC:
                        case STRING:
                            dataline.add(cell.toString());
                            break;
                        default:
                            dataline.add(" ");

                    }
                }
                dataset.add(dataline);

            }
            attributnames = dataset.get(0);
            dataset.remove(0);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return dataset;
    }
    public static void main(String[] args) {
        DataReader reader = new DataReader();
        reader.readdata("C:\\Users\\boule\\Downloads\\SII\\Data Mining\\Projet\\partie1\\Dataset\\Dataset1_ HR-EmployeeAttrition.xlsx");
        a.triedata(dataset, attributlist);
        System.out.println("----Valeurs Tries par ordre croissant----");
        //a.calculTendanceCentraleAll(attributlist);

        //a.calculMesuresDispersion(attributlist.get(9));
        a.calculMesuresDispersionAll(attributlist);

        WhiskersPlot whiskerplot = new WhiskersPlot();
        whiskerplot.generatewhiskerplot(attributlist.get(18), attributnames.get(18));

        HistogrammsPlot histogramplot = new HistogrammsPlot();
        histogramplot.generatehistogramplot(attributlist.get(0), attributnames.get(2));

        ScatterPlot scatterPlot = new ScatterPlot();
        scatterPlot.generatehistogramplot( attributlist.get(32), attributlist.get(33),  attributnames.get(32), attributnames.get(33));

        new Ihm();



    }
}
