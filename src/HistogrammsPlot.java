package src;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.Value;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.List;

public class HistogrammsPlot {
    public HistogramDataset createdataset(List<String> attribut, String attributname){
        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.FREQUENCY);
        double[] values = new double[attribut.size()];
        for (int i = 0; i < attribut.size(); i++) {
            values[i] = Double.parseDouble(attribut.get(i));
        }

        dataset.addSeries(attributname, values,
                //new HashSet<>(attribut).size()
                10);
        return dataset;
    }
    public JFreeChart createhistogram(HistogramDataset dataset, String attributename){

        return ChartFactory.createHistogram("Histogram Chart" + attributename , "values","frequencey(nb of ppl)",
                dataset, PlotOrientation.VERTICAL, true,true,true);
    }

    public void createframe(JFreeChart histogram){
        ChartPanel panel = new ChartPanel(histogram);
        panel.setPreferredSize(new Dimension(640, 480));
        JFrame frame = new JFrame();
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public void renderplot (JFreeChart histogram){
        XYPlot plot = (XYPlot) histogram.getPlot();
        ValueAxis axisx = plot.getDomainAxis();
        ValueAxis axisy = plot.getRangeAxis();
        XYBarRenderer renderer = new XYBarRenderer();
        plot.setRenderer(renderer);
    }

    public void generatehistogramplot(List<String> attributlist, String attributname ){

        HistogramDataset histogramdata = createdataset(attributlist, attributname);
        JFreeChart histogramchart = createhistogram(histogramdata, attributname);
        createframe(histogramchart);
        renderplot(histogramchart);
    }

}
