
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainFrame extends JFrame {

    public MainFrame(String title) {
        super(title);
        init();
    }

    private void init() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        Container pane = getContentPane();
        // top
        JPanel topPanel = new JPanel();
        pane.add(topPanel, BorderLayout.NORTH);
        addressField = new JTextField(10);
        topPanel.add(new JLabel("Server address"));
        topPanel.add(addressField);
        startButton = new JButton("Connect");
        startButton.addActionListener(a -> connectToServer());
        intButton = new JRadioButton("Integer");
        doubleButton = new JRadioButton("Double");
        stringButton = new JRadioButton("String", true);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(intButton);
        buttonGroup.add(doubleButton);
        buttonGroup.add(stringButton);
        topPanel.add(new JLabel("Tree element type"));
        topPanel.add(intButton);
        topPanel.add(doubleButton);
        topPanel.add(stringButton);
        topPanel.add(startButton);

        // center
        JScrollPane scrollPane = new JScrollPane();
        textArea = new JTextArea(10, 20);
        scrollPane.setViewportView(textArea);
        pane.add(scrollPane);
        textArea.setFont(new Font("Courier New", Font.PLAIN, 14));
        textArea.setEditable(false);

        // bottom
        JPanel bottomPanel = new JPanel();
        pane.add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.add(new JLabel("Element"));
        elementField = new JTextField(10);
        bottomPanel.add(elementField);
        addButton = new JButton("Add element");
        addButton.addActionListener(a -> processAdd());
        deleteButton = new JButton("Delete element");
        deleteButton.addActionListener(a -> processDelete());
        findButton = new JButton("Find element");
        bottomPanel.add(addButton);
        bottomPanel.add(findButton);
        bottomPanel.add(deleteButton);
        addButton.setEnabled(false);
        findButton.setEnabled(false);
        deleteButton.setEnabled(false);
        elementField.setEnabled(false);
        pack();
    }

    private void processDelete() {
        String text = elementField.getText();
        if (validator.isValidType(text)) {
            deleteElement(text);
            elementField.setText("");
            drawTree();
        } else {
            JOptionPane.showMessageDialog(this, "Wrong type of Element");
        }
    }

    private void processAdd() {
        String text = elementField.getText();
        if (validator.isValidType(text)) {
            addElement(text);
            elementField.setText("");
            drawTree();
        } else {
            JOptionPane.showMessageDialog(this, "Wrong type of Element");
        }
    }

    private void drawTree() {
        try {
            out.println("draw");
            String response = in.readLine().replaceAll("\\|", "\n");
            SwingUtilities.invokeLater(()->textArea.setText(response));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addElement(String text) {
        try {
            out.println("insert " + text);
            String response = in.readLine();
            JOptionPane.showMessageDialog(this, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteElement(String text) {
        try {
            out.println("delete " + text);
            String response = in.readLine();
            JOptionPane.showMessageDialog(this, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connectToServer() {
        String host = addressField.getText();
        try {
            socket = new Socket(host, 1234);
            JOptionPane.showMessageDialog(this, "Connected!");
            toggleControls();
            processConnection();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error while connecting to server");
        }
    }

    private void toggleControls() {
        addressField.setEnabled(false);
        startButton.setEnabled(false);
        intButton.setEnabled(false);
        doubleButton.setEnabled(false);
        stringButton.setEnabled(false);
        addButton.setEnabled(true);
        findButton.setEnabled(true);
        deleteButton.setEnabled(true);
        elementField.setEnabled(true);
    }

    private void processConnection() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            in.readLine();
            if (intButton.isSelected()) {
                out.println("1");
                validator = TypeValidator.IntegerValidator;
            }
            else if (doubleButton.isSelected()) {
                out.println("2");
                validator = TypeValidator.DoubleValidator;
            }
            else {
                out.println("3");
                validator = TypeValidator.StringValidator;
            }
            textArea.setText(in.readLine());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame("Tree Client").setVisible(true));
    }

    PrintWriter out;
    BufferedReader in;
    Socket socket;
    private JTextField addressField;
    private JTextArea textArea;
    private JButton startButton;
    JRadioButton intButton;
    JRadioButton doubleButton;
    JRadioButton stringButton;
    JTextField elementField;
    JButton addButton;
    JButton deleteButton;
    JButton findButton;

    TypeValidator validator;
}