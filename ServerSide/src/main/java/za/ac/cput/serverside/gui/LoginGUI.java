package za.ac.cput.serverside.gui;

import za.ac.cput.serverside.gui.AdminGUI;
import java.awt.BorderLayout;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LoginGUI extends JFrame implements ActionListener {

    private JLabel usernameLabel, passwordLabel, titleLabel;
    private JTextField usernametxt;
    private JTextField passwordtxt;
    private JButton loginButton;
    private JComboBox<String> titleCombo;
    private JPanel centerPnl, bottomPnl;

    public LoginGUI() {
        setTitle("USER LOGIN");
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        centerPnl = new JPanel(new GridLayout(3, 2,10,10));
        centerPnl.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); 
       
        
        usernameLabel = new JLabel("Username: ");
        usernametxt = new JTextField(15);

        passwordLabel = new JLabel("Password: ");
        passwordtxt = new JPasswordField(15);

        //combobox to store 2 options
        titleLabel = new JLabel("Title: ");
        String[] userTitle = {" ", "Student", "Staff"};
        titleCombo = new JComboBox<>(userTitle);

        bottomPnl = new JPanel();
        bottomPnl.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30)); 
        loginButton = new JButton("Login");

        centerPnl.add(usernameLabel);
        centerPnl.add(usernametxt);
        centerPnl.add(passwordLabel);
        centerPnl.add(passwordtxt);
        centerPnl.add(titleLabel);
        centerPnl.add(titleCombo);
        bottomPnl.add(loginButton);

        loginButton.addActionListener(this);

        //add panels to the frame
        add(centerPnl, BorderLayout.CENTER);
        add(bottomPnl, BorderLayout.SOUTH);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernametxt.getText();
            String password = passwordtxt.getText();
            String userTitle = (String) titleCombo.getSelectedItem(); //return selected item
         
            //credentials
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both username and password");
            }
            if (userTitle.equals("Staff") && username.equals("admin") && password.equals("password")) {
                JOptionPane.showMessageDialog(this, "Welcome!!");
                new AdminGUI(); //navigate to adminGui

            } else if (userTitle.equals("Student") && username.equals("student") && password.equals("password")) {
                JOptionPane.showMessageDialog(this, "Welcome!!");
                new StudentGUI(); //navigate to StudentGui

            } 
        }
    }

    public static void main(String[] args) {
        new LoginGUI();
    }
}
