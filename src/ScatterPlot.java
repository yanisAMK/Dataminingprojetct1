package src;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class ScatterPlot {

    public XYSeriesCollection createscatterdataset(List<String> attributelist1,List<String> attributelist2, String attributename1,String attributename2){
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries xyseries = new XYSeries(attributename1 + " vs " +attributename2);
        int size = Integer.min(attributelist1.size(), attributelist2.size());
        for (int i = 0; i < size; i++) {
            xyseries.add(Double.parseDouble(attributelist1.get(i)),Double.parseDouble(attributelist2.get(i)));
        }
        dataset.addSeries(xyseries);
        return dataset;
    }

    public JFreeChart createscatterplot(XYSeriesCollection dataset,String attributename1,String attributename2 ){
        JFreeChart scatterplot = ChartFactory.createScatterPlot("Scatter Plot("+attributename1 + " vs " + attributename2+")",attributename1,attributename2,dataset, PlotOrientation.VERTICAL,true,true,true);
        return scatterplot;
    }

    public void createframe(JFreeChart scatterplot){
        ChartPanel panel = new ChartPanel(scatterplot);
        panel.setPreferredSize(new Dimension(640, 480));
        JFrame frame = new JFrame();
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public void renderplot (JFreeChart scatterplot){
        XYPlot plot = (XYPlot) scatterplot.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        plot.setRenderer(renderer);
    }

    public void generatehistogramplot(List<String> attributelist1,List<String> attributelist2, String attributename1,String attributename2){

        XYSeriesCollection scatterdataset  = createscatterdataset(attributelist1,attributelist2,attributename1,attributename2);
        JFreeChart scatterchart = createscatterplot(scatterdataset,attributename1,attributename2);
        createframe(scatterchart);
        renderplot(scatterchart);
    }

}
