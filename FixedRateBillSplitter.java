import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

public class FixedRateBillSplitter extends JFrame {
    private DefaultTableModel usageModel;
    private JTextArea resultArea;
    private JTextField nameField, applianceField, usageField;
    private final double RATE_PER_KWH = 7.5;

    private final Color PRIMARY = new Color(30, 136, 229);
    private final Color ACCENT = new Color(0, 172, 193);
    private final Color BG = new Color(244, 246, 248);
    private final Color TEXT = new Color(44, 62, 80);
    private final Color SUCCESS = new Color(67, 160, 71);
    private final Color DANGER = new Color(229, 57, 53);

    public FixedRateBillSplitter() {
        setTitle("Electricity Bill Splitter");
        setSize(950, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 15));
        UIManager.put("Button.font", new Font("Segoe UI Semibold", Font.BOLD, 14));
        UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));

        GradientPaintPanel header = new GradientPaintPanel(PRIMARY, ACCENT);
        header.setLayout(new BorderLayout());
        JLabel title = new JLabel("Electricity Bill Splitter (₹" + RATE_PER_KWH + " per kWh)", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI Semibold", Font.BOLD, 22));
        header.add(title, BorderLayout.CENTER);
        header.setPreferredSize(new Dimension(0, 65));
        add(header, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tabs.setBackground(Color.WHITE);
        tabs.setBorder(new EmptyBorder(10, 10, 10, 10));

        tabs.add("Home", createHomePanel());
        tabs.add("Usage Entry", createUsagePanel());
        tabs.add("Bill Report", createReportPanel());

        add(tabs, BorderLayout.CENTER);
    }

    private JPanel createHomePanel() {
        JPanel home = new JPanel(new GridBagLayout());
        home.setBackground(BG);

        JPanel card = createCardPanel();
        JTextArea info = new JTextArea(
            "Welcome to Electricity Bill Splitter\n\n" +
            "This application helps calculate each roommate’s bill based on their electricity usage.\n\n" +
            "Fixed Rate: ₹" + RATE_PER_KWH + " per kWh\n\n" +
            "Go to 'Usage Entry' to add names, appliances, and usage.\n" +
            "Check 'Bill Report' to view each person’s total share."
        );
        info.setEditable(false);
        info.setBackground(new Color(255, 255, 255, 220));
        info.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        info.setForeground(TEXT);
        info.setMargin(new Insets(25, 25, 25, 25));
        card.add(info, BorderLayout.CENTER);
        home.add(card);
        return home;
    }

    private JPanel createUsagePanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel card = createCardPanel();

        JPanel input = new JPanel(new GridLayout(2, 3, 10, 10));
        input.setBackground(Color.WHITE);

        input.add(new JLabel("Name:"));
        nameField = createTextField();
        input.add(nameField);

        input.add(new JLabel("Appliance:"));
        applianceField = createTextField();
        input.add(applianceField);

        input.add(new JLabel("Usage (kWh):"));
        usageField = createTextField();
        input.add(usageField);

        card.add(input, BorderLayout.NORTH);

        usageModel = new DefaultTableModel(new Object[]{"Name", "Appliance", "Usage (kWh)", "Amount (₹)"}, 0);
        JTable table = new JTable(usageModel);
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 15));
        table.setBackground(Color.WHITE);
        table.setGridColor(new Color(230, 230, 230));
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(new Color(230, 230, 230)));
        card.add(scroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btnPanel.setBackground(Color.WHITE);
        JButton addBtn = makeButton("Add Entry", SUCCESS);
        JButton calcBtn = makeButton("Generate Bill", PRIMARY);
        JButton clearBtn = makeButton("Clear All", DANGER);
        btnPanel.add(addBtn);
        btnPanel.add(calcBtn);
        btnPanel.add(clearBtn);
        card.add(btnPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> addUsage());
        calcBtn.addActionListener(e -> calculateBill());
        clearBtn.addActionListener(e -> clearAll());

        panel.add(card, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createReportPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel card = createCardPanel();

        JLabel label = new JLabel("Final Bill Report", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI Semibold", Font.BOLD, 20));
        label.setForeground(TEXT);
        card.add(label, BorderLayout.NORTH);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 15));
        resultArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        card.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        panel.add(card, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCardPanel() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        return card;
    }

    private JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setBorder(new CompoundBorder(
            new LineBorder(new Color(210, 210, 210), 1, true),
            new EmptyBorder(6, 10, 6, 10)
        ));
        tf.setBackground(new Color(250, 250, 250));
        return tf;
    }

    private JButton makeButton(String text, Color c) {
        JButton btn = new JButton(text);
        btn.setBackground(c);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }

    private void addUsage() {
        String name = nameField.getText().trim();
        String appliance = applianceField.getText().trim();
        String usageText = usageField.getText().trim();

        if (name.isEmpty() || appliance.isEmpty() || usageText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try {
            double usage = Double.parseDouble(usageText);
            double amount = usage * RATE_PER_KWH;
            usageModel.addRow(new Object[]{name, appliance, usage, String.format("%.2f", amount)});
            nameField.setText("");
            applianceField.setText("");
            usageField.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Usage must be numeric.");
        }
    }

    private void calculateBill() {
        if (usageModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No usage data found.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        double total = 0;
        sb.append("-------------------------------------------------------\n");
        sb.append(String.format("%-15s %-15s %-10s %-10s\n", "Name", "Appliance", "Usage", "Amount"));
        sb.append("-------------------------------------------------------\n");

        for (int i = 0; i < usageModel.getRowCount(); i++) {
            String name = usageModel.getValueAt(i, 0).toString();
            String app = usageModel.getValueAt(i, 1).toString();
            double usage = Double.parseDouble(usageModel.getValueAt(i, 2).toString());
            double amount = usage * RATE_PER_KWH;
            total += amount;
            sb.append(String.format("%-15s %-15s %-10.2f ₹%-10.2f\n", name, app, usage, amount));
        }

        sb.append("-------------------------------------------------------\n");
        sb.append(String.format("Total Amount: ₹%.2f\n", total));
        sb.append("-------------------------------------------------------\n");

        resultArea.setText(sb.toString());
    }

    private void clearAll() {
        usageModel.setRowCount(0);
        resultArea.setText("");
        nameField.setText("");
        applianceField.setText("");
        usageField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FixedRateBillSplitter().setVisible(true));
    }

    private static class GradientPaintPanel extends JPanel {
        private final Color c1, c2;
        public GradientPaintPanel(Color c1, Color c2) {
            this.c1 = c1;
            this.c2 = c2;
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
