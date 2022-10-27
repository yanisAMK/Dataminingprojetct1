package src.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DataProcessor {
    public void discretisation1(int q, List<String> attributeList){
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

    }
    public void discretisation2(int k, List<String> attributeList){
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
        float largeurInterval = (max - min)/k;
        for (int i = 0; i < attributeList.size(); i++) {
            
        }

    }


}
