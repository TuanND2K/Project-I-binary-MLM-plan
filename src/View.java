import javax.swing.*;

public class View {
    private JPanel panel;
    private JButton createButton;
    private JButton addButton;
    private JButton deleteButton;
    private JComboBox saleBox;
    private JButton calcButton;
    private JTextArea companyInfor;

    public View() {
        prepareGUI();
    }

    public void prepareGUI() {
        JFrame mainFrame = new JFrame("Demo");
        mainFrame.setContentPane(panel);
        mainFrame.setSize(800, 700);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);


    }
    public static void main(String[] args) {
        View view = new View();
    }
}
