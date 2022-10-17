package src.app;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import src.app.Statistics;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class Data {
    //Contient les données brut
    public List<List<String>> dataSet = new ArrayList<>();
    //Contient les attributs dans l'ordre initial
    public List<List<String>> unsortedAttributes = new ArrayList<>();
    
    //Contient les attributs triée , par colones
    public  List<List<String>> attributlist = new ArrayList<>();

    //Contient les noms des attributs
    public  List<String> attributnames = new ArrayList<>();

    Statistics stats = new Statistics();
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

}
