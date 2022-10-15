import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ihm extends JFrame implements ActionListener {
    JTabbedPane tabbedPane1;
    JPanel panel1;
    JTabbedPane tabbedPane2;
    JComboBox attributcombobox;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JButton scatterplotbutton;
    private JComboBox comboBox3;
    private JButton analyseButton;
    private JLabel moyenneField;
    private JLabel médianeField;
    private JLabel modeField;
    private JLabel maxField;
    private JLabel minField;
    private JLabel q1Field;
    private JLabel q2Field;
    private JLabel q3Field;
    private JLabel outliersField;
    private JButton loadDatasetButton;

    Ihm(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.attributcombobox = new JComboBox<>(new String[]{"dags", "cats", "s"});
        this.setContentPane(panel1);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    /**
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
