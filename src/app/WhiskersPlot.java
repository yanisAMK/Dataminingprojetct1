package src.app;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBoxAndWhiskerRenderer;
import org.jfree.data.Range;
import org.jfree.data.statistics.BoxAndWhiskerCalculator;
import org.jfree.data.statistics.BoxAndWhiskerItem;
import org.jfree.data.statistics.BoxAndWhiskerXYDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerXYDataset;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WhiskersPlot {
    Range range ;
    public BoxAndWhiskerXYDataset towhiskerdata(List<String> argument, String argname ){
        DefaultBoxAndWhiskerXYDataset whiskerdata = new DefaultBoxAndWhiskerXYDataset(argname);
        List<Double> values = new ArrayList<Double>();
        argument.forEach(s ->{
            values.add(Double.parseDouble(s));
        });
        BoxAndWhiskerItem x = BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(values);
        this.range = new Range((Double) x.getMinOutlier(),(Double)x.getMaxOutlier()*1.2);
        whiskerdata.add(new Date() , x);
        return whiskerdata;
    }

    public JFreeChart createchart(BoxAndWhiskerXYDataset whiskerdata){
        JFreeChart chart = ChartFactory.createBoxAndWhiskerChart("Box and Whisker Chart", "Date", "Valeurs", whiskerdata, true);
        return chart;
    }

    public ChartPanel creatpanel(JFreeChart chart){
        ChartPanel chartpanel = new ChartPanel(chart);
        chartpanel.setPreferredSize(new Dimension(300, 300));
        JFrame frame = new JFrame();
        frame.add(chartpanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return chartpanel;
    }

    public void ploltbox(JFreeChart boxplot){
        XYPlot plot = (XYPlot) boxplot.getPlot();
        XYBoxAndWhiskerRenderer renderer = new XYBoxAndWhiskerRenderer();
        Range r = new Range(0,2);
        ValueAxis axisy = plot.getRangeAxis();
        axisy.setAutoRange(false);
        axisy.setRange(this.range);
        plot.setRangeAxis(axisy);
        plot.setRenderer(renderer);

    }

    public void generatewhiskerplot(List<String> attribute, String attributename){
        BoxAndWhiskerXYDataset dataset = towhiskerdata(attribute, attributename);
        JFreeChart chart = createchart(dataset);
        creatpanel(chart);
        ploltbox(chart);
    }


}
