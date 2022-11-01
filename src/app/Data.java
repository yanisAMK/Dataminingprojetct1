package src.app;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

import static org.apache.poi.ss.usermodel.CellType.BLANK;

public class Data {
    //Contient les données brut
    public static List<List<String>> dataSet = new ArrayList<>();

    public static List<List<Double>> normalizedDataset = new ArrayList<>();

    //Contient les attributs dans l'ordre initial
    public static List<List<String>> unsortedAttributes = new ArrayList<>();
    
    //Contient les attributs triée , par colones
    public static List<List<String>> attributlist = new ArrayList<>();

    //Contient les noms des attributs
    public static List<String> attributnames = new ArrayList<>();

    public static int noOfRows;
    public static int noOfCols;

    static Statistics stats = new Statistics();

    //Contient les types des attributs
    public static List<Integer> attribute_type; //0 numérique , 1 string , 2 bool

    public static List<Integer> noMissing_attribute;
    public static int noMissing_total;

    public static List<List<String>> transposer(List<List<String>> dataset){

        List<List<String>> AttributeList = new ArrayList<>();

        for (int i = 0; i < dataset.get(0).size(); i++) {
            AttributeList.add(new ArrayList<>());
        }

        dataset.forEach((line) -> {
            for (int i = 0; i < line.size(); i++) {
                AttributeList.get(i).add(line.get(i));
            }
        });

        return AttributeList;
    }
    public static void triedata(List<List<String>> unsortedAttributes){

        List<List<String>> unsortedList = new ArrayList<>();

        //create one sorted list out of the original (without coping the reference!)
        for(List<String> list : unsortedAttributes){
            unsortedList.add(new ArrayList<>(list));
        }

        for (List<String> strings : unsortedList) {
            if (stats.checktype(strings)) {
                List<Float> dline = new ArrayList<>();
                List<String> sline = new ArrayList<>();

                strings.forEach(s -> {
                    if(!Objects.equals(s, " ") && !Objects.equals(s, "NaN")){
                        dline.add(Float.parseFloat(s));
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
                    if(!Objects.equals(s, " ") && !Objects.equals(s, " ")){
                        clean.add(s);
                    }
                });
                attributlist.add(clean);
            }
        }

    }

    public static void reaData(String filepath){
        try {
            FileInputStream file = new FileInputStream(
                    new File(filepath)
            );

            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            dataSet = new ArrayList<>();
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
                    if (cell.toString().equals("") || cell.getCellType() == BLANK || cell.toString().equals(" ") || cell.toString().equals("NaN")) // Cellule vide
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

        unsortedAttributes = transposer(dataSet);

        triedata(new ArrayList<>(unsortedAttributes));
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
        if(Data.attribute_type.get(i) == 0)
            return "[ " + Statistics.getmin(Data.attributlist.get(i)) + " .. " + Statistics.getmax(Data.attributlist.get(i)) + " ]";
        return " / ";
    }

    //Fait la MAJ des Structures Data
    public static void updateStructures(List<List<String>> newDataSet){
        List<Integer> emptyList = new ArrayList<>();
        updateStructures(newDataSet, emptyList);
    }
    public static void updateStructures(List<List<String>> newDataSet, List<Integer> listOfAttributesToRemove){
        dataSet = new ArrayList<>(transposer(newDataSet));
        noOfRows = newDataSet.size();
        noOfCols = newDataSet.get(0).size();

        unsortedAttributes = new ArrayList<>(transposer(dataSet));
        attributlist = new ArrayList<>();

        triedata((new ArrayList<>(unsortedAttributes)));

        if(!listOfAttributesToRemove.isEmpty()){
            int n = listOfAttributesToRemove.size()-1;
            for (int i = n; i>=0 ; i--){
                int ind = listOfAttributesToRemove.get(i);
                attributnames.remove(ind);
                attribute_type.remove(ind);
                noMissing_attribute.remove(ind);
            }
        }


        int i=0;
        noMissing_total = 0;
        noMissing_attribute = new ArrayList<>();
        for(List<String> list : unsortedAttributes){
            noMissing_attribute.add(Collections.frequency(list, " "));
            noMissing_total = noMissing_total + noMissing_attribute.get(i);
            i++;
        }
    }

    public static void reset(){
        dataSet = new ArrayList<>();
        normalizedDataset = new ArrayList<>();
        unsortedAttributes = new ArrayList<>();
        attributlist = new ArrayList<>();
        attributnames = new ArrayList<>();
        noOfRows = 0;
        noOfCols = 0;
        attribute_type = new ArrayList<>();
        noMissing_attribute = new ArrayList<>();
        noMissing_total = 0;
    }

    /*
    * Cette Fonction fait le trie de la dataset selon les valeurs de l'attribut passé en paramètre (ordre ascendant)
    * @param Attribute index
    */
    public static LinkedList<LinkedList<Float>> sortDataSetByValuesOfAttribut(int index){
        //IL FAUT SUPPRIMER les attributs STRING avant l'utilisation de cette fct
        // SUPPRIMER aussi les valeurs null

        LinkedList<LinkedList<Float>> sortedDataset = new LinkedList<>();

        for(List<String> row : Data.dataSet){
            LinkedList<Float> list = new LinkedList<>();
            for(int i=0; i<row.size();i++){
                list.add(Float.parseFloat(row.get(i)));
            }
            sortedDataset.add(list);
        }

/*        System.out.println("Before sorting :");
        for (LinkedList<Float> list : sortedDataset) {
            for (Float d : list) {
                System.out.print(d + " ");
            }
            System.out.println();
        }*/

        Collections.sort(sortedDataset, new Comparator<LinkedList<Float>>() {
            @Override
            public int compare(LinkedList<Float> o1, LinkedList<Float> o2) {
                try {
                    return o1.get(index).compareTo(o2.get(index));
                } catch (NullPointerException e) {
                    return 0;
                }
            }
        });

/*        System.out.println("After sorting :");
        for (LinkedList<Float> list : sortedDataset) {
            for (Float d : list) {
                System.out.print(d + " ");
            }
            System.out.println();
        }*/

        return sortedDataset;
    }

    //Fait la conversion de la dataset de linkedList vers List
    public static List<List<String>> linkedListData_to_List(LinkedList<LinkedList<Float>> LinkedListDataSet){
        List<List<String>> ListDataSet = new ArrayList<>();

        for(LinkedList<Float> row : LinkedListDataSet){
            List<String> list = new ArrayList<>();
            for(int i=0; i<row.size();i++){
                list.add(row.get(i) +"");
            }
            ListDataSet.add(list);
        }
        return ListDataSet;
    }

    public static void dropCategoricalAttributes() {
        List<List<String>> attributesList = new ArrayList<>(Data.unsortedAttributes);

        if(attribute_type.contains(1) || attribute_type.contains(2)) {
            List<Integer> listOfRemovedAttributes = new ArrayList<>();
            int n = attributesList.size() - 1; //last index

            //suppression des attributs categoriques

            for (int i = n; i >= 0; i--) {
                if ( Data.attribute_type.get(i) != 0) {
                    attributesList.remove(i);
                    listOfRemovedAttributes.add(i);
                }
            }

            Collections.reverse(listOfRemovedAttributes);

            updateStructures(attributesList, listOfRemovedAttributes);
        }
    }

    public static void suppressionAttribut(int index) {
        if(index < Data.unsortedAttributes.size()){
            List<List<String>> attributesList = new ArrayList<>(Data.unsortedAttributes);
            attributesList.remove(index);

            List<Integer> removedAttribute = new ArrayList<>();
            removedAttribute.add(index);

            updateStructures(attributesList, removedAttribute);
        }
    }
}
