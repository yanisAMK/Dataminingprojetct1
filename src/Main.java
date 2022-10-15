package src;
import src.ihm.Ihm;
public class Main {

    public static void main(String[] args) {
        /*
        Data data = new Data();
        Statistics stats = new Statistics();
        String filePath = "C:\\Users\\boule\\Downloads\\SII\\Data Mining\\Projet\\partie1\\Dataset\\Dataset1_ HR-EmployeeAttrition.xlsx";

        data.reaData(filePath);
        data.triedata(data.dataSet, data.attributlist);
        System.out.println("----Valeurs Tries par ordre croissant----");
        //a.calculTendanceCentraleAll(data.attributlist);


        //a.calculMesuresDispersion(data.attributlist.get(9));
        stats.calculMesuresDispersionAll(data.attributlist);



        WhiskersPlot whiskerplot = new WhiskersPlot();

        whiskerplot.generatewhiskerplot(data.attributlist.get(18), data.attributnames.get(18));

        HistogrammsPlot histogramplot = new HistogrammsPlot();
        histogramplot.generatehistogramplot(data.attributlist.get(0), data.attributnames.get(2));

        ScatterPlot scatterPlot = new ScatterPlot();
        scatterPlot.generatehistogramplot( data.attributlist.get(32), data.attributlist.get(33),  data.attributnames.get(32), data.attributnames.get(33));
        */

        new Ihm();



    }
}
