package src.app;

import java.util.*;

import static src.app.Statistics.*;

public class DataProcessor {
    /*public static List<String> discretisation1(int q, List<String> attributeList){
        List<String> distinctValues = new ArrayList<>(new HashSet<>(attributeList));
        List<String> discrettedAttributeList = new ArrayList<>();
        int n = distinctValues.size();
        int intervalSize = (int) Math.ceil(n/q);
        //todo utiliser les pourcentages pour les quantiles, ieme = i/q,val du ieme Ni/q, comparer avec ni/q
        for (String s : attributeList) {
            float fval = Float.parseFloat(s);
            boolean replaced = false;
            int j = 0;
            while (!replaced & (j < q - 1)) {
                float upperborder = Float.parseFloat(distinctValues.get((j + 1) * intervalSize / q));
                float lowerborder = Float.parseFloat(distinctValues.get(j * intervalSize / q));
                if ((lowerborder <= fval) & (fval < upperborder)) {
                    discrettedAttributeList.add(fval + "");
                }
                j += 1;
            }
        }
        return discrettedAttributeList ;
    }*/

    public static List<String> discretisation_effectifs(int q, List<String> attributeList){
        List<String> discrettedAttributeList = new ArrayList<>();
        int n = attributeList.size();
        int size = (int) Math.ceil(n/q);

        List<String> bin;
        float moyenne=0;
        for(int i=0; i< q; i++){
            bin = new ArrayList<>();
            moyenne=0;
            for(int j= i*size; j< (i+1)*size; j++){
                if(j >= n)
                    break;
                bin.add(attributeList.get(j));
                moyenne += Float.parseFloat(attributeList.get(j));
            }

            //Todo : Verify if Numerical attribute is Int or Float
            moyenne = moyenne / bin.size();
            //Solution temporaire : si la dataset a été normalisé return float Else return Int
            if(normalized)
                for(int b=0; b<bin.size();b++)
                    discrettedAttributeList.add(moyenne+"");
            else
                for(int b=0; b<bin.size();b++)
                    discrettedAttributeList.add(Math.ceil(moyenne)+"");
        }

        //Remplissage des valeurs restantes
        int i = discrettedAttributeList.size();
        if(normalized){
            while(i<attributeList.size()){
                discrettedAttributeList.add(moyenne+"");
                i++;
            }

        }
        else {
            while(i<attributeList.size()){
                discrettedAttributeList.add(Math.ceil(moyenne)+"");
                i++;
            }
        }

        return discrettedAttributeList ;
    }

    public static List<String> discretisation_amplitude(int k, List<String> attributeList){
        List<String> discretizedAttributeList = new ArrayList<>();

        float min= Float.parseFloat(attributeList.get(0)) ;
        float max=  Float.parseFloat(attributeList.get(0)) ;
        for (String s : attributeList) {
            if(! s.equals(" ")){
                float val = Float.parseFloat(s);
                if (val > max) {
                    max = val;
                }
                if (val < min) {
                    min = val;
                }
            }
        }
        int largeurInterval = (int) Math.ceil((max - min)/k);

        List<Integer> list_mins = new ArrayList<>();
        for(int i=0; i< k+1; i++){
            list_mins.add( (int) Math.ceil(min) + largeurInterval * i);
        }

        List<Integer> bin;
        float moyenne;
        for(int i=0; i< k; i++){
            bin = new ArrayList<>();
             moyenne = 0;
            for(String val: attributeList){
                if(! val.equals(" ") && ! val.equals("NaN")){
                    int intVal = (int) Math.ceil(Float.parseFloat(val));
                    if( intVal >= list_mins.get(i) && intVal < list_mins.get(i+1).intValue()) {
                        bin.add(intVal);
                        moyenne += Float.parseFloat(val);
                    }
                }

            }

            //Todo : Verify if Numerical attribute is Int or Float
            moyenne = moyenne/bin.size();
            //Solution temporaire : si la dataset a été normalisé return float Else return Int
            if(normalized)
                for(int b=0; b<bin.size();b++)
                    discretizedAttributeList.add(moyenne+"");
            else
                for(int b=0; b<bin.size();b++)
                    discretizedAttributeList.add(Math.ceil(moyenne)+"");
        }
        return discretizedAttributeList ;
    }

    public static void traitementValeursManquantes(List<String> attributeList, String valDeRemplacement){

        for (int i = 0; i < attributeList.size(); i++) {

            if (attributeList.get(i).equals(" ") || attributeList.get(i).equals("NaN")) {
                attributeList.set(i, valDeRemplacement);
        }
        }
    }

    public static List<Integer> traitementValeursAberrantes(List<String> attributeList, int indexAtt,String valDeRemplacement){
        List<Integer> listRowsHavingOutliers = new ArrayList<>();
        Statistics stats = new Statistics();
        //int index = Data.unsortedAttributes.indexOf(attributeList);
        stats.calculMesuresDispersion(Data.attributlist.get(indexAtt));
        System.out.println(stats.mesures.get("nbOutliers"));
        for (int i = 0; i < attributeList.size(); i++) {
            if( ! attributeList.get(i).equals(" ") && ! attributeList.get(i).equals("NaN") && (( Float.parseFloat(attributeList.get(i)) < Float.parseFloat(stats.mesures.get("lineinf")))||
                    (Float.parseFloat(stats.mesures.get("linesup")) < Float.parseFloat(attributeList.get(i))))){

                attributeList.set(i, valDeRemplacement);
                listRowsHavingOutliers.add(i);

            }
        }
        return listRowsHavingOutliers;
    }

    //identify attributes that have duplicate values
    //@param {attributelist} each line represents an attribute
    public static List<Integer> getVerticalDuplicates(List<List<String>> attributelist){

        List<Integer> verticalDuplicates = new ArrayList<>();

        for(int i=0;i<attributelist.size();i++){
            List<String> attribute = attributelist.get(i);
            boolean allEqual = true;

            for (String s : attribute) {
                if (!s.equals(attribute.get(0)))
                    allEqual =  false;
            }
            if(allEqual)
                verticalDuplicates.add(i);
        }
        return verticalDuplicates;
    }

    public static List<List<String>> removeHorizontalDuplicates(List<List<String>> dataset){
        List<List<String>> list = new ArrayList<>();
        Set<List<String>> listWithoutDuplicates = new LinkedHashSet<List<String>>(dataset);
        list.addAll(listWithoutDuplicates);
        return list;
    }

    public static String eliminateRedundancy(List<List<String>> dataset){
        String info = "";

        List<List<String>> newDataSet = removeHorizontalDuplicates(dataset);
        List<List<String>> attributeList = Data.transposer(newDataSet);

        int nbDeletedRows = dataset.size() - newDataSet.size() ;
        info = info.concat(nbDeletedRows+" rows have been deleted\n");

        List<Integer> verticalDuplicates = getVerticalDuplicates(attributeList);

        info = info.concat(verticalDuplicates.size()+" columns have been deleted\n");
        for (int i : verticalDuplicates)
            info = info.concat(Data.attributnames.get(i)+"\n");

        //System.out.println(info);

        //suppression des redondances verticals
        int n =verticalDuplicates.size()-1;
        for(int i = n; i>=0 ; i--){
            int ind = verticalDuplicates.get(i);
            attributeList.remove(ind);
        }

        //newDataSet = Data.generateAttributeList(attributeList);

        Data.updateStructures(attributeList, verticalDuplicates);
        return info;
    }


    public static boolean normalized = false;
    public static List<List<String>> minMaxNormalization(List<List<String>> attributeList){
        final float MIN = 0, MAX = 1;
        List<List<String>> normalizedAttributeList = new ArrayList<>();
        float minV, maxV, normalizedV = 0;

        for(int i=0;i<attributeList.size();i++){
            if (Data.attribute_type.get(i) == 0){ //if attribute type = 0 (integer)
                List<String> attribute = attributeList.get(i);
                List<String> normalizedAttribute = new ArrayList<>();
                minV = getmin(attribute);
                maxV = getmax(attribute);
                if(minV<maxV){
                    for (String s : attribute) {
                        if (! s.equals(" ")){ //if value isn't missing
                            normalizedV = ((Float.parseFloat(s) - minV) / (maxV - minV) ) * (MAX - MIN) - MIN;
                            normalizedAttribute.add(Float.toString(normalizedV));
                        }
                        else{
                            System.out.println("Veuillez traiter les valeurs manquantes.");
                            normalizedAttribute.add(" ");
                        }
                    }
                    normalizedAttributeList.add(normalizedAttribute);
                }
                else
                    normalizedAttributeList.add(attributeList.get(i)); //can't normalize a constant column
            }
            else{
                normalizedAttributeList.add(attributeList.get(i)); //can't normalize a categorical attribute
            }

            normalized = true;
        }
        return normalizedAttributeList;
    }

    public static List<List<String>> zscoresNormalization(List<List<String>> attributeList){

        List<List<String>> normalizedAttributeList = new ArrayList<>();
        float moyenne, ecartType, normalizedV = 0;
        for(int i=0;i<attributeList.size();i++){
            if (Data.attribute_type.get(i) == 0){ //if attribute type = 0 (integer)
                List<String> attribute = attributeList.get(i);
                List<String> normalizedAttribute = new ArrayList<>();
                ecartType = calculEcartType(attribute);
                moyenne = Float.parseFloat(calculMoyenne(attribute));
                for (String s : attribute) {
                    if (! s.equals(" ")){ //if value isn't missing
                    normalizedV = (Float.parseFloat(s) - moyenne) /ecartType;
                    normalizedAttribute.add(Float.toString(normalizedV));
                    }
                    else {
                        System.out.println("Missing values not treated, Normalization done by ignoring it");
                        normalizedAttribute.add(" ");
                    }
                }
                normalizedAttributeList.add(normalizedAttribute);
            }
            else{
                normalizedAttributeList.add(attributeList.get(i));
            }
        }
        return normalizedAttributeList;
    }

}
