package src.app;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import src.app.Statistics;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

import static org.apache.poi.ss.usermodel.CellType.BLANK;

public class Data {
    //Contient les données brut
    public List<List<String>> dataSet = new ArrayList<>();
    //Contient les attributs dans l'ordre initial
    public List<List<String>> unsortedAttributes = new ArrayList<>();
    
    //Contient les attributs triée , par colones
    public  List<List<String>> attributlist = new ArrayList<>();

    //Contient les noms des attributs
    public static List<String> attributnames = new ArrayList<>();

    public int noOfRows;
    public int noOfCols;

    Statistics stats = new Statistics();

    //Contient les types des attributs
    public static List<Integer> attribute_type; //0 numérique , 1 string , 2 bool

    public List<Integer> noMissing_attribute;
    public int noMissing_total;

    public void triedata(List<List<String>> dataset){


        for (int i = 0; i < dataset.get(0).size(); i++) {
            unsortedAttributes.add(new ArrayList<>());
        }

        dataset.forEach((line) -> {
            for (int i = 0; i < line.size(); i++) {
                unsortedAttributes.get(i).add(line.get(i));
            }
        });

        for (List<String> strings : unsortedAttributes) {
            if (stats.checktype(strings)) {
                List<Double> dline = new ArrayList<>();
                List<String> sline = new ArrayList<>();

                strings.forEach(s -> {
                    if(!Objects.equals(s, " ")){
                        dline.add(Double.parseDouble(s));
                    }
                    /*else {
                        dline.add((double) -1);
                    }*/
                });
                Collections.sort(dline);
                dline.forEach(val -> {
                    if (val != -1) {
                        sline.add(val + "");
                    }
                    /*else {
                        sline.add(" ");
                    }*/
                });

                attributlist.add(sline);
            } else {
                Collections.sort(strings);
                List<String> clean = new ArrayList<>();
                strings.forEach(s ->{
                    if(!Objects.equals(s, " ")){
                        clean.add(s);
                    }
                });
                attributlist.add(clean);
            }
        }
    }

    public void reaData(String filepath){
        try {
            FileInputStream file = new FileInputStream(
                    new File(filepath)
            );

            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            noOfRows = sheet.getLastRowNum();
            noOfCols = sheet.getRow(0).getLastCellNum();
            attribute_type = new ArrayList<>(Collections.nCopies(noOfCols, null));
            noMissing_attribute = new ArrayList<>(Collections.nCopies(noOfCols, 0));
            noMissing_total =0;

            int i;
            while (rowIterator.hasNext()){
                List<String> dataline = new ArrayList<>();
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                i=0;
                while (cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    if (cell.toString().equals("") || cell.getCellType() == BLANK || cell.toString().equals(" ")) // Cellule vide
                    {
                        noMissing_attribute.set(i,(noMissing_attribute.get(i))+1);
                        noMissing_total++;
                        dataline.add(" ");

                    } else {

                        switch (cell.getCellType()) {
                            case NUMERIC:
                                attribute_type.set(i, 0); //attribut numérique
                                dataline.add(cell.toString());
                                break;
                            case STRING:
                                String strSimple = cell.toString().trim().toLowerCase();
                                if (strSimple.equals("yes") || strSimple.equals("y") || strSimple.equals("no") || strSimple.equals("n"))
                                    attribute_type.set(i, 2); //attribut booléen
                                else
                                    attribute_type.set(i, 1); //attribut String

                                dataline.add(cell.toString());
                                break;
                            case BOOLEAN:
                                attribute_type.set(i, 2);
                                dataline.add(cell.toString());
                                break;
                            default:
                                dataline.add(" ");
                        }
                    }
                    i++;
                }
                dataSet.add(dataline);

            }
            attributnames = dataSet.get(0);
            dataSet.remove(0);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        triedata(dataSet);

    }

    public String getTypeAtrribute(int att) {
        String type = switch (att) {
            case 0 -> "Numerique";
            case 1 -> "String";
            case 2 -> "Booleen";
            default -> "???";
        };
        return type;
    }

    public String getPlageValeur(int i){
        if(attribute_type.get(i) == 0)
            return "[ " + Statistics.getmin(attributlist.get(i)) + " .. " + Statistics.getmax(attributlist.get(i)) + " ]";
        return " / ";
    }
}
