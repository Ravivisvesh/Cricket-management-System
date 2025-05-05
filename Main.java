import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class CricketManager extends JFrame implements ActionListener {
    JTextField nameField, countryField, roleField, runsField;
    JButton insertBtn, displayBtn, updateBtn, deleteBtn, clearBtn;

    public CricketManager() {
        setTitle("🏏 Cricket Manager");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(230, 240, 250));

        // Title
        JLabel title = new JLabel("Cricket Manager", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(25, 25, 112));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(250, 250, 250));
        inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "Player Info"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);

        String[] labels = {"Player Name:", "Country:", "Role:", "Total Runs:"};
        JTextField[] fields = {
                nameField = new JTextField(20),
                countryField = new JTextField(20),
                roleField = new JTextField(20),
                runsField = new JTextField(20)
        };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.anchor = GridBagConstraints.LINE_END;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(labelFont);
            inputPanel.add(lbl, gbc);

            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.LINE_START;
            fields[i].setFont(fieldFont);
            inputPanel.add(fields[i], gbc);
        }

        add(inputPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(230, 240, 250));

        insertBtn = createStyledButton("Insert", new Color(34, 139, 34));
        displayBtn = createStyledButton("Display", new Color(70, 130, 180));
        updateBtn = createStyledButton("Update", new Color(255, 140, 0));
        deleteBtn = createStyledButton("Delete", new Color(178, 34, 34));
        clearBtn = createStyledButton("Clear", new Color(105, 105, 105));

        buttonPanel.add(insertBtn);
        buttonPanel.add(displayBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(90, 35));
        btn.addActionListener(this);
        return btn;
    }

    private Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/cricketdb", "root", "ravi");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "DB connection failed");
            return null;
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == insertBtn) insert();
        else if (e.getSource() == displayBtn) display();
        else if (e.getSource() == updateBtn) update();
        else if (e.getSource() == deleteBtn) delete();
        else clear();
    }

    private void insert() {
        if (!validateInput()) return;
        try (Connection c = connect()) {
            PreparedStatement ps = c.prepareStatement("INSERT INTO players (name, country, role, runs) VALUES (?, ?, ?, ?)");
            setParams(ps);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Player inserted.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Insert error");
        }
    }

    private void display() {
        try (Connection c = connect()) {
            ResultSet rs = c.createStatement().executeQuery("SELECT * FROM players");
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("Name: ").append(rs.getString("name"))
                        .append(", Country: ").append(rs.getString("country"))
                        .append(", Role: ").append(rs.getString("role"))
                        .append(", Runs: ").append(rs.getInt("runs"))
                        .append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.length() == 0 ? "No players found." : sb.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Display error");
        }
    }

    private void update() {
        if (!validateInput()) return;
        try (Connection c = connect()) {
            PreparedStatement ps = c.prepareStatement("UPDATE players SET country=?, role=?, runs=? WHERE name=?");
            ps.setString(1, countryField.getText());
            ps.setString(2, roleField.getText());
            ps.setInt(3, Integer.parseInt(runsField.getText()));
            ps.setString(4, nameField.getText());
            int rows = ps.executeUpdate();
            JOptionPane.showMessageDialog(this, rows > 0 ? "✅ Player updated." : "Player not found.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Update error");
        }
    }

    private void delete() {
        String name = nameField.getText();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter player name to delete.");
            return;
        }
        try (Connection c = connect()) {
            PreparedStatement ps = c.prepareStatement("DELETE FROM players WHERE name=?");
            ps.setString(1, name);
            int rows = ps.executeUpdate();
            JOptionPane.showMessageDialog(this, rows > 0 ? "🗑️ Player deleted." : "Player not found.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Delete error");
        }
    }

    private void clear() {
        nameField.setText("");
        countryField.setText("");
        roleField.setText("");
        runsField.setText("");
    }

    private boolean validateInput() {
        if (nameField.getText().isEmpty() || countryField.getText().isEmpty()
                || roleField.getText().isEmpty() || runsField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return false;
        }
        try {
            Integer.parseInt(runsField.getText());
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Runs must be a valid number.");
            return false;
        }
    }

    private void setParams(PreparedStatement ps) throws SQLException {
        ps.setString(1, nameField.getText());
        ps.setString(2, countryField.getText());
        ps.setString(3, roleField.getText());
        ps.setInt(4, Integer.parseInt(runsField.getText()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CricketManager::new);
    }
}
