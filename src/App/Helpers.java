package src.app;

import org.apache.tools.ant.util.StringUtils;

import java.util.*;

public class Helpers {
    Map<String, List<String>> Transactions ;
    List<String> transactionsList;
    List<String> itemsList;
    Map<List<String>, Float> C1;
    List<String> L1;
    Map<String,List<String>> L;
    float SupportMin ;
    float ConfianceMin;
    float Lift;

    public void setSupportMin(float supportMin){
        this.SupportMin = supportMin;
    }
    public static List<String> getDistinctValues(List<String> paramlist){
        return new ArrayList<>(new HashSet<>(paramlist));
    }

    public List<String> getItems(List<List<String>> itemsList){
        List<String> Items = new ArrayList<>();
        for (int i = 0; i < itemsList.get(0).size(); i++) {
            StringBuilder item = new StringBuilder();
            for (List<String> strings : itemsList) {
                item.append(strings.get(i)).append(" ");
            }
            String s = String.valueOf(item);
            Items.add(s.substring(0, s.length()-1));
        }
        return Items;
    }

    public void toTransaction(List<List<String>> dataSet, int transactionsIndex){
        //initialisation
        List<String> transactionsAsListed = dataSet.get(transactionsIndex);
        dataSet.remove(transactionsIndex);
        List<String> itemsAsListed = getItems(dataSet);

        List<String> distinctTransactions = getDistinctValues(transactionsAsListed);
        Collections.sort(distinctTransactions);
        List<String> distinctItems = getDistinctValues(itemsAsListed);
        Collections.sort(distinctItems);
        this.transactionsList = distinctTransactions;
        this.itemsList = distinctItems;
        this.Transactions = new HashMap<>();

        for (String distinctTransaction : distinctTransactions) {
            Transactions.put(distinctTransaction, new ArrayList<>());
        }
        //population
        for (int i = 0; i < itemsAsListed.size(); i++) {
            String key = transactionsAsListed.get(i);

            int hasitem = Transactions.get(key).indexOf(itemsAsListed.get(i));
            if (hasitem == -1){
                Transactions.get(key).add(itemsAsListed.get(i));
            }
        }
        generateC1();
        generateL1();
    }

    public void toTransactionRenameItems(List<List<String>> dataSet, int transactionsIndex){
        //initialisation
        List<String> transactions = dataSet.get(transactionsIndex);
        dataSet.remove(transactionsIndex);
        List<String> items = getItems(dataSet);

        List<String> distinctTransactions = getDistinctValues(transactions);
        Collections.sort(distinctTransactions);
        List<String> distinctItems = getDistinctValues(items);

        this.transactionsList = distinctTransactions;
        this.itemsList = distinctItems;
        this.Transactions = new HashMap<>();

        for (String distinctTransaction : distinctTransactions) {
            Transactions.put(distinctTransaction, new ArrayList<>());
        }
        //population
        for (int i = 0; i < items.size(); i++) {
            String key = transactions.get(i);
            int itemNumber = distinctItems.indexOf(items.get(i));
            int hasitem = Transactions.get(key).indexOf("i" + itemNumber);
            if (hasitem == -1){
                Transactions.get(key).add("i" + itemNumber);
            }
        }
    }

    public float kItemSupport(List<String> itemList){
        float nbtransactions = 0;

        for( Map.Entry<String, List<String>> entry: this.Transactions.entrySet()){
            int i = 0;
            boolean isin = true;
            while(i<itemList.size() & isin){
                isin = (entry.getValue().contains(itemList.get(i)));
                i++;
            }
            if(isin){
                nbtransactions +=1;
            }
        }
        double support = (float) Math.ceil(100 * nbtransactions / Transactions.size());
        support = Math.floor(support*10000)/10000;
        return (float) support;
    }

    public void generateC1(){
        this.C1 = new HashMap<>();
        for (String item : this.itemsList) {
            this.C1.put(List.of(item), kItemSupport(List.of(item)));
        }
    }

    public void generateL1(){
        this.L = new HashMap<>();
        List<String> L1 = new ArrayList<>();
        for (Map.Entry<List<String>, Float> entry: this.C1.entrySet()
             ) {
            if(entry.getValue()>= this.SupportMin){
                L1.add(entry.getKey().get(0));
            }
        }
        this.L.put("L1",L1);
    }

    public String getItemCode(String item, List<String> distinctItems){
        return "i" + distinctItems.indexOf(item);
    }
    public String getItemValue(String itemCode, List<String> distinctItems ){
        return distinctItems.get(Integer.parseInt(itemCode.replace("i", "")));
    }

    public Map<String, Float> generateCondidatsListk(int k){
        List<String> previousL = this.L.get("L"+(k-1));
        Map<String,Float> Ck = new HashMap<>();
        for (int i = 0; i < previousL.size()-1; i++) {
            for (int j = i+1; j < previousL.size(); j++) {
                String candidate = previousL.get(i) + ";" + previousL.get(j);
                List<String> temp = getDistinctValues(List.of(candidate.split(";")));
                Collections.sort(temp);
                candidate = StringUtils.join(temp, ";");
                float support = kItemSupport(List.of(candidate.split(";")));
                Ck.put(candidate,support);
            }
        }
        return Ck;
    }
    public void generateLK(Map<String, Float>CK, int k){

        List<String> Lk = new ArrayList<>();
        for (Map.Entry<String, Float> entry: CK.entrySet()
        ) {
            if(entry.getValue()>= this.SupportMin){
                Lk.add(entry.getKey());
            }
        }
        this.L.put("L"+k, Lk);
    }
    public void showCk(Map<String , Float> CK){
        System.out.println("Candidats list's Size : " + CK.size());
        CK.forEach((item, support)->{
            System.out.println("item : " + item + "  |||| Support : " + support);
        });
        return;
    }
    public List<String> generateItemSet(List<String> itemlist){
        List<String> itemset = new ArrayList<>();
        for (int i = 0; i < itemlist.size()-1; i++) {
            for (int j = i+1; j < itemlist.size(); j++) {
                String candidate = itemlist.get(i) + ";" + itemlist.get(j);
                List<String> temp = getDistinctValues(List.of(candidate.split(";")));
                Collections.sort(temp);
                candidate = StringUtils.join(temp, ";");
                itemset.add(candidate);
            }
        }
        itemset = getDistinctValues(itemset);
        return itemset;
    }

    public Map<List<String>, List<String>> generateAssociationRules(int k, int i, int r) {
        List<String> lk = List.of(L.get("L" + k).get(i).split(";"));
        //todo: generaliser pour r = x
        Map<List<String>, List<String>> associations = new HashMap<>();
        List<String> itemSets = generateitemsets(lk, r);

        for (String stringItem:
             itemSets) {

            List<String> temp= new ArrayList<>(List.of(stringItem.split(";")));
            Collections.sort(temp);
            for (int j = 0; j < temp.size(); j++) {
                List<String> temp2 = new ArrayList<>(temp);
                String tempA = temp2.get(j);
                temp2.remove(j);
                String tempB = StringUtils.join(temp2, ";");
                //todo: condition if > Confiance fait
                if( confiance(tempA, tempB)>= ConfianceMin){
                    if(!associations.containsKey(List.of(tempA))){
                        associations.put(List.of(tempA), new ArrayList<>(Collections.singleton(tempB)));
                    }
                    else {
                        if(!associations.get(List.of(tempA)).contains(tempB)){
                            associations.get(List.of(tempA)).add(tempB);
                        }
                    }
                }
                //todo: condition if > confiance fait
                if(confiance(tempB, tempA)>= ConfianceMin){
                    if(!associations.containsKey(List.of(tempB))){
                        associations.put(List.of(tempB), new ArrayList<>(Collections.singleton(tempA)));
                    }
                    else {
                        if(!associations.get(List.of(tempB)).contains(tempA)){
                            associations.get(List.of(tempB)).add(tempA);
                        }
                    }
                }
            }
        }
        return associations;
    }

    public Map<List<String>, List<String>> generateCorrelationRules(int k, int i, int r) {
        List<String> lk = List.of(L.get("L" + k).get(i).split(";"));
        //todo: generaliser pour r = x
        Map<List<String>, List<String>> associations = new HashMap<>();
        List<String> itemSets = generateitemsets(lk, r);

        for (String stringItem:
                itemSets) {

            List<String> temp= new ArrayList<>(List.of(stringItem.split(";")));
            Collections.sort(temp);
            for (int j = 0; j < temp.size(); j++) {
                List<String> temp2 = new ArrayList<>(temp);
                String tempA = temp2.get(j);
                temp2.remove(j);
                String tempB = StringUtils.join(temp2, ";");
                //todo: condition if > lift
                if( (confiance(tempA, tempB)>= ConfianceMin) && ((float)lift(tempA,tempB)>= Lift)){
                    if(!associations.containsKey(List.of(tempA))){
                        associations.put(List.of(tempA), new ArrayList<>(Collections.singleton(tempB)));
                    }
                    else {
                        if(!associations.get(List.of(tempA)).contains(tempB)){
                            associations.get(List.of(tempA)).add(tempB);
                        }
                    }
                }
                //todo: condition if > lift
                if( (confiance(tempB, tempA)>= ConfianceMin) && ((float)lift(tempB,tempA)>= Lift)){
                    if(!associations.containsKey(List.of(tempB))){
                        associations.put(List.of(tempB), new ArrayList<>(Collections.singleton(tempA)));
                    }
                    else {
                        if(!associations.get(List.of(tempB)).contains(tempA)){
                            associations.get(List.of(tempB)).add(tempA);
                        }
                    }
                }
            }
        }
        return associations;
    }

    public void setConfianceMin(float confianceMin){
        this.ConfianceMin = confianceMin;
    }

    public double confiance(String A, String B){
        List<String> itemsA = List.of(A.split(";"));
        List<String> itemsB = new ArrayList<>(List.of(B.split(";")));
        itemsB.addAll(itemsA);
        itemsB = getDistinctValues(itemsB);// usless ??
        double conf = kItemSupport(itemsB) / kItemSupport(itemsA);
        conf = Math.floor(conf*10000)/10000;

        return conf;
    }

    public double lift(String A, String B){
        List<String> itemsA = new ArrayList<>(List.of(A.split(";")));
        List<String> itemsB = List.of(B.split(";"));
        float supportA = kItemSupport(itemsA);
        float supportB = kItemSupport(itemsB);
        itemsA.addAll(itemsB);
        itemsA = getDistinctValues(itemsA);
        double lift = kItemSupport(itemsA) / (supportB * supportA);
        lift = Math.floor(lift*10000)/10000;
        return lift;
    }



    public List<String> itemSetRecursiveGen(String[] arr, String[] data, int start, int end, int index, int r) {
        if (index == r){
            return List.of(StringUtils.join(data, ";"));
        }
        List<String> list = new ArrayList<>();
        for (int i=start; i<=end && end-i+1 >= r-index; i++)
        {
            data[index] = arr[i];
            list.addAll(itemSetRecursiveGen(arr, data, i + 1, end, index + 1, r));
        }
        return list;
    }

    //r est la taille de l'itemset
    public List<String> generateitemsets(List<String> items, int r){
        String[] arrS = new String[items.size()];
        for (int i = 0; i < arrS.length; i++) {
            arrS[i] = items.get(i);
        }
        String[] data = new String[r];
        int l = items.size();

        return itemSetRecursiveGen(arrS, data,0,l-1,0,r);
    }
    public Map<List<String>, List<String>> addMapToMap(Map<List<String>, List<String>> A, Map<List<String>, List<String>> B){
        for (Map.Entry<List<String>, List<String>> entry: A.entrySet()
             ) {
            if(!B.containsKey(entry.getKey())){
                B.put(entry.getKey(), entry.getValue());
            }
            else {
                for (String item :
                        A.get(entry.getKey())) {
                    if (!B.get(entry.getKey()).contains(item)){
                        B.get(entry.getKey()).add(item);
                    }
                }
            }
        }
        return B;
    }


    //DataSet each row represents a column in the original dataset
    //transactionIndex is the index in undortedAttributes that holds all the transactions
    public List<String> Apriori(float supportMin, float confianceMin, List<List<String>> DataSet , int transactionIndex){
        setSupportMin(supportMin);
        setConfianceMin(confianceMin);
        toTransaction(DataSet, transactionIndex);
        int k = 2;
        while(L.get("L"+(k-1)).size()>1){
            Map<String, Float> ck = generateCondidatsListk(k);
            generateLK(ck,k);
            k++;
        }

        Map<List<String>, List<String>> associations = new HashMap<>();
        int maxLength = maxLength(L.size()); // pour la taille des combinaison a generer
        int lkSize = L.get("L"+L.size()).size();
        for (int i = 0; i <lkSize ; i++) {
            for (int r = 2; r <= maxLength; r++) {
                associations = addMapToMap(generateAssociationRules(3,i, r), associations) ;
            }
        }
        System.out.println("Apriori Associations");
        System.out.println(associations);

        List<String> aprioriResult = new ArrayList<>();
        for (Map.Entry<List<String>, List<String>> entry :
                associations.entrySet()) {
            String antecedent = entry.getKey().get(0);
            for (int i = 0; i < entry.getValue().size(); i++) {
                String consequent = entry.getValue().get(i);
                aprioriResult.add(antecedent + "====>" + consequent);
            }
        }
        return aprioriResult;
    }


    public List<String> AprioriAvecLift(float lift, float supportMin, float confianceMin, List<List<String>> DataSet , int transactionIndex){
        setSupportMin(supportMin);
        setLift(lift);
        setConfianceMin(confianceMin);
        toTransaction(DataSet, transactionIndex);
        int k = 2;
        while(L.get("L"+(k-1)).size()>1){
            Map<String, Float> ck = generateCondidatsListk(k);
            generateLK(ck,k);
            k++;
        }

        Map<List<String>, List<String>> associations = new HashMap<>();
        int maxLength = maxLength(L.size()); // pour la taille des combinaison a generer
        int lkSize = L.get("L"+L.size()).size();
        for (int i = 0; i <lkSize ; i++) {
            for (int r = 2; r <= maxLength; r++) {
                associations = addMapToMap(generateCorrelationRules(3,i, r), associations) ;
            }
        }
        System.out.println("Apriori Associations");
        System.out.println(associations);

        List<String> aprioriResult = new ArrayList<>();
        for (Map.Entry<List<String>, List<String>> entry :
                associations.entrySet()) {
            String antecedent = entry.getKey().get(0);
            for (int i = 0; i < entry.getValue().size(); i++) {
                String consequent = entry.getValue().get(i);
                aprioriResult.add(antecedent + "====>" + consequent);
            }
        }
        return aprioriResult;
    }

    public int maxLength(int k){
        int max = 0;
        for (int i = 0; i < L.get("L"+k).size(); i++) {
            int sizeitem = List.of(L.get("L"+k).get(i).split(";")).size();
            if(max< sizeitem){
                max = sizeitem;
            }
        }
        return max;
    }


    public void setLift(float lift) {
        Lift = lift;
    }
}