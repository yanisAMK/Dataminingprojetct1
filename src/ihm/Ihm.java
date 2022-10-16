package src.ihm;

import src.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class Ihm extends JFrame implements ActionListener {

    JTabbedPane tabbedPane1;
    JPanel panel1;
    JComboBox attributcombobox;
    private JComboBox<String> comboBox3;
    private JButton analyseButton;
    private JLabel moyenneField;
    private JLabel medianeField;
    private JLabel modeField;
    private JLabel maxField;
    private JLabel minField;
    private JLabel q1Field;
    private JLabel q2Field;
    private JLabel q3Field;
    private JLabel outliersField;
    private JButton loadDatasetButton;
    private JPanel mesureJpanel;
    private JPanel whiskersField;
    private JPanel histogrammeField;
    private JButton whiskersBoxButton;
    private JButton histogrammPlotButton;
    private JComboBox<String> comboBox4;
    private JComboBox<String> comboBox5;
    private JButton scatterPlotButton;
    private JTable table1;
    int indexTendances;
    public Ihm(){
        Data data = new Data();
        Statistics stats = new Statistics();


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.attributcombobox = new JComboBox<>(new String[]{"dags", "cats", "s"});

        this.setContentPane(panel1);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        loadDatasetButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                data.reaData(getFilePath());
                data.attributnames.forEach(s->{
                    comboBox3.addItem(s);
                    comboBox4.addItem(s);
                    comboBox5.addItem(s);
                });
            }

        });


        analyseButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                indexTendances = comboBox3.getSelectedIndex();
                data.triedata(data.dataSet);
                data.attributnames.forEach(System.out::print);
                moyenneField.setText(stats.calculMoyenne(data.attributlist.get(indexTendances)));
                medianeField.setText(stats.calculMedianne((data.attributlist.get(indexTendances))));
                modeField.setText(stats.calculMode(data.attributlist.get(indexTendances)).toString());

                List<String> mesures = stats.calculMesuresDispersion(data.attributlist.get(indexTendances));
                maxField.setText(mesures.get(0));
                minField.setText(mesures.get(1));
                q1Field.setText(mesures.get(2));
                q2Field.setText(mesures.get(3));
                q3Field.setText(mesures.get(4));
                outliersField.setText(mesures.get(5));
                


            }
        });
        whiskersBoxButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                WhiskersPlot whiskers = new WhiskersPlot();
                whiskers.generatewhiskerplot(data.attributlist.get(indexTendances),data.attributnames.get(indexTendances));
            }
        });
        histogrammPlotButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                HistogrammsPlot histogramm = new HistogrammsPlot();
                histogramm.generatehistogramplot(data.attributlist.get(indexTendances), data.attributnames.get(indexTendances));
            }
        });
        scatterPlotButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index1 = comboBox4.getSelectedIndex();
                int index2 = comboBox5.getSelectedIndex();
                ScatterPlot scater = new ScatterPlot();
                scater.generateScatterPlot(data.unsortedAttributes.get(index1),data.unsortedAttributes.get(index2),
                        data.attributnames.get(index1), data.attributnames.get(index2));
            }
        });
        whiskersBoxButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                WhiskersPlot whiskers = new WhiskersPlot();
                whiskers.generatewhiskerplot(data.attributlist.get(indexTendances),data.attributnames.get(indexTendances));
            }
        });
        histogrammPlotButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                HistogrammsPlot histogramm = new HistogrammsPlot();
                histogramm.generatehistogramplot(data.attributlist.get(indexTendances), data.attributnames.get(indexTendances));
            }
        });
    }

    /**
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

    }
    public String getFilePath() {
        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter fnef = new FileNameExtensionFilter("Files",
                "xls", "xlsx", "xlsm");
        fc.setFileFilter(fnef);
        fc.setDialogTitle("");
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int res = fc.showDialog(this, "Select");

        String path = null;
        if (res == JFileChooser.APPROVE_OPTION) {
            File df = fc.getSelectedFile();
            path = df.getAbsolutePath();
        }
        return path;
    }
}
