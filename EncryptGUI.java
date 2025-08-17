import java.awt.*;
import java.awt.datatransfer.*;
import javax.swing.*;

public class EncryptGUI extends JFrame {
    private JComboBox<String> modeDropdown;
    private JTextArea inputArea;
    private JTextArea outputArea;
    private JButton processButton;
    private JButton copyButton;

    public EncryptGUI() {
        setTitle("Encrypt / Decrypt Tool");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        modeDropdown = new JComboBox<>(new String[]{"Encrypt", "Decrypt"});
        inputArea = new JTextArea(5, 40);
        outputArea = new JTextArea(5, 40);
        outputArea.setEditable(false);

        processButton = new JButton("Process");
        copyButton = new JButton("Copy Output");

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Mode:"));
        topPanel.add(modeDropdown);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        centerPanel.add(new JScrollPane(inputArea));
        centerPanel.add(new JScrollPane(outputArea));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(processButton);
        buttonPanel.add(copyButton);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // ===== Actions =====
        processButton.addActionListener(e -> {
            String text = inputArea.getText().trim();
            if (text.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter some text.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String mode = (String) modeDropdown.getSelectedItem();
            String result;

            try {
                if (mode.equals("Encrypt")) {
                    if (looksEncrypted(text)) {
                        JOptionPane.showMessageDialog(this, "This text already looks encrypted!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Encode encoder = new Encode(text);
                    result = encoder.encode();
                } else {
                    if (!isValidEncryptedFormat(text)) {
                        JOptionPane.showMessageDialog(this, "This text doesn't match the expected encrypted format!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Decrypt decoder = new Decrypt(text);
                    result = decoder.decode();
                }
                outputArea.setText(result);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        copyButton.addActionListener(e -> {
            StringSelection selection = new StringSelection(outputArea.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, null);
            JOptionPane.showMessageDialog(this, "Output copied to clipboard!");
        });
    }

    // Encrypt mode check
    private boolean looksEncrypted(String text) {
        return text.matches(".*@.*\\d.*") && text.contains("@"); // has @ and numbers
    }

    // Decrypt mode check
    private boolean isValidEncryptedFormat(String text) {
        return text.matches("[a-f0-9@]+"); // allows a-f, digits, and @
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EncryptGUI gui = new EncryptGUI();
            gui.setVisible(true);
        });
    }
}
