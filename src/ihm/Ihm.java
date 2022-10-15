package src.ihm;

import src.Data;
import src.Statistics;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Ihm extends JFrame implements ActionListener {

    JTabbedPane tabbedPane1;
    JPanel panel1;
    JTabbedPane tabbedPane2;
    JComboBox attributcombobox;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JButton scatterplotbutton;
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
                });
            }

        });


        analyseButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int indeex = comboBox3.getSelectedIndex();
                data.triedata(data.dataSet);
                //System.out.println(indeex);
                moyenneField.setText(stats.calculMoyenne(data.attributlist.get(indeex)));
                medianeField.setText(stats.calculMedianne((data.attributlist.get(indeex))));
                modeField.setText(stats.calculMode(data.attributlist.get(indeex)).toString());
                List<String> mesures = stats.calculMesuresDispersion(data.attributlist.get(indeex));
                maxField.setText(mesures.get(0));
                minField.setText(mesures.get(1));
                q1Field.setText(mesures.get(2));
                q2Field.setText(mesures.get(3));
                q3Field.setText(mesures.get(4));

                outliersField.setText(mesures.get(5));

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
