package src.app;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DataProcessor {
    public List<String> discretisation1(int q, List<String> attributeList){
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

    }
    public List<String> discretisation2(int k, List<String> attributeList){
        List<String> discrettedAttributeList = new ArrayList<>();
        float min= Float.parseFloat(attributeList.get(0)) ;
        float max=  Float.parseFloat(attributeList.get(0)) ;
        for (String s : attributeList) {
            float val = Float.parseFloat(s);
            if (val > max) {
                max = val;
            }
            if (val < min) {
                min = val;
            }
        }
        int largeurInterval = (int) Math.ceil((max - min)/k);
        for (int i = 0; i < attributeList.size(); i++) {
            boolean replaced = false;
            int j = 0;
            while(j<k & !replaced){
                float val = Float.parseFloat(attributeList.get(i));
                if((val < (j+1)*largeurInterval) & (val>j*largeurInterval)){
                    discrettedAttributeList.add(j+ "");
                    replaced = true;
                }
            }
        }
        return discrettedAttributeList ;

    }


}
