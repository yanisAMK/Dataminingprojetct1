package src.app;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
public class Statistics {
    public HashMap<String, String> mesures = new HashMap<>();
    public boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    public boolean checktype(List<String> paramlist){
        boolean isnumeric = false;
        for (int i = 0; i < Math.ceil(paramlist.size()/4); i++) {
            if(isNumeric(paramlist.get(i))){
                return true;
            }
        }
        return isnumeric;
    }

    public List<String> getDistinctValues(List<String> paramlist){
        List<String> distinctparams = new ArrayList<>(new HashSet<>(paramlist));
        return distinctparams;
    }

    public List<Integer> getValuesFreq(List<String> paramlist,List<String> distinctparams ){
        List<Integer> paramsfreq = new ArrayList<>();
        for (String dval: distinctparams
             ) {
            paramsfreq.add(Collections.frequency(paramlist, dval));
        }
        return paramsfreq;
    }

    // calcul des tendances centrales

    public String calculMoyenne(List<String> paramlist){

        float moyenne = 0;
        for (String val: paramlist) {
            if (isNumeric(val)) {
                moyenne += Float.parseFloat(val);
            }else {
                return "None";
            }
        }
        return "" + moyenne/paramlist.size();
    }

    public String calculMedianne(List<String> paramlist){
        int length = paramlist.size();
        if (length%2 != 0){
            if(isNumeric(paramlist.get((length+1)/2))){
                return paramlist.get((length+1)/2);
            }
            return "None";
        }
        if (checktype(paramlist)) {
            Float v1 = Float.parseFloat(paramlist.get(length/2));
            Float v2 = Float.parseFloat(paramlist.get((length +1)/2));
            return "" +(v2+v1)/2;
        }
        return "None";

    }
    public List<String> calculMode(List<String> paramlist){
        List<String> distinctparams = getDistinctValues(paramlist);
        List<Integer> paramsfreq = getValuesFreq(paramlist, distinctparams);
        List<String> modes = new ArrayList<>();
        Integer max = Collections.max(paramsfreq, null);

        //suppression des valeures nulls des parms distinct et freqparam
        if(Objects.equals(distinctparams.get(paramsfreq.indexOf(max)), " ")){
            distinctparams.remove(paramsfreq.indexOf(max));
            paramsfreq.remove(max);
            max = Collections.max(paramsfreq, null);
        }

        List<Integer> maxindex = new ArrayList<>() ;
        for (int i = 0; i < paramsfreq.size(); i++) {
            if(paramsfreq.get(i).equals(max)){
                maxindex.add(i);
            }
        }
        maxindex.forEach(i ->{
            modes.add(distinctparams.get(i));
        });

        if(modes.size() == paramlist.size()){
            List<String>mode = new ArrayList<>();
            mode.add("All values are unique");
            return mode;
        }
        return modes;
    }


    public void calculTendanceCentrale(List<String> paramlist){
        mesures.put("moyenne" ,calculMoyenne(paramlist));
        mesures.put("mediane" ,calculMedianne(paramlist));
        mesures.put("mode" , ""+calculMode(paramlist));

    }
    public void calculTendanceCentraleAll (List<List<String>> paramlists){
        AtomicInteger i = new AtomicInteger();
        paramlists.forEach(paramlist->{
            System.out.println("------param " + i);
            System.out.println("moyenne " + calculMoyenne(paramlist));
            System.out.println("medianne " + calculMedianne(paramlist));
            System.out.println("mode " + calculMode(paramlist));
            i.addAndGet(1);
        });


    }

    //Calcul des mesures de dispersion
    public List<String> quartile(List<String> paramlist){
        List<String> quartiles = new ArrayList<>();
        quartiles.add(paramlist.get((int) Math.ceil(paramlist.size()/4)));
        quartiles.add(calculMedianne(paramlist));
        quartiles.add(paramlist.get((int) Math.ceil(paramlist.size()*3/4)));

        return quartiles;
    }
    public static double getmax(List<String> paramlist){
        final double[] max = new double[1];
        max[0] = Double.parseDouble(paramlist.get(0));
        paramlist.forEach(s -> {
            if(max[0] < Double.parseDouble(s)){
                max[0] = Double.parseDouble(s);
            }
        });
        return max[0];
    }
    public static double getmin(List<String> paramlist){
        final double[] min = new double[1];
        min[0] = Double.parseDouble(paramlist.get(0));
        paramlist.forEach(s -> {
            if(min[0] > Double.parseDouble(s)){
                min[0] = Double.parseDouble(s);
            }
        });
        return min[0];
    }

    public void calculMesuresDispersion(List<String> paramlist){

        List<String> quartiles = quartile(paramlist);

        double max = getmax(paramlist);
        double min = getmin(paramlist);
        double IQR = (Double.parseDouble(quartiles.get(2)) - Double.parseDouble(quartiles.get(0)));
        double linesup = Double.parseDouble(quartiles.get(2)) + IQR*1.5;
        double lineinf = Double.parseDouble(quartiles.get(0)) - IQR*1.5;

        List<Double> outliers = new ArrayList<>();
        paramlist.forEach(s ->{
            if(Double.parseDouble(s) < lineinf){
                outliers.add(Double.parseDouble(s));
            }
            if(Double.parseDouble(s) > linesup){
                outliers.add(Double.parseDouble(s));
            }
        });

        mesures.put("max",max+"");
        mesures.put("min",min+"");
        mesures.put("q1",quartiles.get(0));
        mesures.put("q2",quartiles.get(1));
        mesures.put("q3",quartiles.get(2));
        mesures.put("iqr",IQR + "");
        mesures.put("nbOutliers", outliers.size()+"");
        mesures.put("variance", calculVariance(paramlist)+"");
        mesures.put("ecartType", calculEcartType(paramlist)+ "");

        //mesures.add(linesup +" < outliers || outliers < "+lineinf);
        /*outliers.forEach(o ->{
            mesures.add(o+"");
        });*/

    }

    public void calculMesuresDispersionAll(List<List<String>> paramlist){
        paramlist.forEach(attribute ->{
            if(checktype(attribute)) {
                System.out.println("\n--------attribut :" + paramlist.indexOf(attribute));
                calculMesuresDispersion(attribute);
            }
        });
    }

    public Double calculVariance(List<String> attributes){
        return Math.pow(calculEcartType(attributes),2);
    }

    public Double calculEcartType(List<String> attributes){
        double sum = 0;
        double moy = Double.parseDouble(calculMoyenne(attributes));
        double std = 0;
        for (String attribute : attributes) {
            std = std + Math.pow((Double.parseDouble(attribute) - moy), 2);
        }
        std /= attributes.size();
        std = Math.sqrt(std);
        return std;
    }

    public void calculMesures(List<String> paramlist){
        calculTendanceCentrale(paramlist);
        calculMesuresDispersion(paramlist);

    }

}
