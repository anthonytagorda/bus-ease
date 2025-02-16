package server.view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;

@SuppressWarnings("all")
public class AdminLoginView extends JFrame {
	 protected JLabel usernameLabel = new JLabel("Admin Username:");
	 protected JLabel passwordLabel = new JLabel("Admin Password:");
	 protected JTextField usernameField = new JTextField();
	 protected JPasswordField passwordField = new JPasswordField();
	 protected JButton loginButton = new JButton("Login");
	 protected JPanel loginPanel = new JPanel();

	 public AdminLoginView () {
		  SwingUtilities.invokeLater(this::display);
	 }

	 public void display() {
		  // Color and Border
		  Color primaryColor = Color.decode("#007BFF");
		  Color bgColor = Color.decode("#FFF9F5");
		  Border border = BorderFactory.createLineBorder(primaryColor);

		  // Icon and Images
		  ImageIcon icon = new ImageIcon("src/public/icons/bus_ease_icon.png");
		  ImageIcon image = new ImageIcon("src/public/images/bus_ease_logo.png");
		  Image image_original = image.getImage();
		  Image image_resized = image_original.getScaledInstance(350, 200, Image.SCALE_SMOOTH);
		  ImageIcon resizedImage = new ImageIcon(image_resized);
		  JLabel imageLabel = new JLabel(resizedImage);

		  // Frame Configuration
		  setTitle("Bus Ease | Admin Login");
		  setVisible(true);
		  setSize(420, 440);
		  setDefaultCloseOperation(EXIT_ON_CLOSE);
		  setIconImage(icon.getImage());
		  setResizable(false);
		  setLocationRelativeTo(null);

		  // Panel Configurations
		  add(loginPanel);
		  		loginPanel.setLayout(null);
		  		loginPanel.setPreferredSize(new Dimension(340, 25));
		  		loginPanel.setBorder(border);
		  		loginPanel.setBackground(bgColor);

		  // Add components
		  loginPanel.add(usernameLabel);
		  	usernameLabel.setBounds(70, 230, 120, 25);
		  loginPanel.add(passwordLabel);
		  	passwordLabel.setBounds(70, 260, 120, 25);
		  loginPanel.add(usernameField);
		  	usernameField.setBounds(190, 230, 120, 25);
		  loginPanel.add(passwordField);
		  	passwordField.setBounds(190, 260, 120, 25);
		  loginPanel.add(loginButton);
		  	loginButton.setBounds(140, 320, 120, 45);
		  loginPanel.add(imageLabel);
		  	imageLabel.setBounds(-250, -20, 900, 300);
	 } // end of display method

	 // Admin Login Actions
	 public void addLoginButtonListener(ActionListener listener) {
		  loginButton.addActionListener(listener);
	 }

	 public String getUsername() {
		  return usernameField.getText();
	 }

	 public char[] getPassword() {
		  return passwordField.getPassword();
	 }

	 public void showError(String message) {
		  JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	 }

	 public void clearPassword() {
		  passwordField.setText("");
	 }

}
