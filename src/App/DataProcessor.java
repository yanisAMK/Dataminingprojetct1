package src.app;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DataProcessor {
    public void discretisation(int q, List<String> attributeList){
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
}
