package passenger.view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class RegisterView extends JPanel {
	 protected JLabel nameLabel, usernameLabel, passwordLabel, confirmPasswordLabel;
	 protected JTextField nameField, usernameField;
	 protected JPasswordField passwordField, confirmPasswordField;
	 protected JButton backButton, registerButton;

	 protected JPanel regPanel;
	 protected CardLayout regCardLayout;

	 public RegisterView(JPanel regPanel, CardLayout regCardLayout) {
		  this.regPanel = regPanel;
		  this.regCardLayout = regCardLayout;
		  display();
	 }

	 private void display() {
		  // Color and Border
		  Color primaryColor = Color.decode("#007BFF");
		  Color bgColor = Color.decode("#FFF9F5");
		  Border border = BorderFactory.createLineBorder(primaryColor);

		  /* Header */
		  // Bus GIF
		  ImageIcon busGif = new ImageIcon("src/public/images/bus.gif");
		  JLabel busGifLabel = new JLabel(busGif);
		  busGifLabel.setBounds(0,0,510,512);
		   add(busGifLabel);
		  // Bus Logo
		  ImageIcon busLogo = new ImageIcon("src/public/images/bus_ease_logo.png");
		  Image busLogo_original = busLogo.getImage();
		  Image busLogo_resized = busLogo_original.getScaledInstance(200, 130, Image.SCALE_SMOOTH);
		  ImageIcon resizedBusLogo = new ImageIcon(busLogo_resized);
		  JLabel busLogoLabel = new JLabel(resizedBusLogo);
		  busLogoLabel.setBounds(530,-105,400,400);
		  add(busLogoLabel);
		  // Register Header Label
		  JLabel regHeaderLabel = new JLabel("Create an Account");
		  regHeaderLabel.setBounds(605,150,300,35);
		  regHeaderLabel.setFont(new Font("Comic Sans", Font.BOLD, 30));
		  regHeaderLabel.setForeground(primaryColor);
		  add(regHeaderLabel);

		  // Panel Configuration
		  setPreferredSize(new Dimension(960, 540));
		  setLayout(null);
		  setBackground(bgColor);
		  setBorder(border);

		  // Component Initialization
		  // Name
		  nameLabel = new JLabel("Name");
		  nameLabel.setBounds(555, 190, 100, 15);
		  add(nameLabel);
		  nameField = new JTextField(20);
		  nameField.setBounds(555, 210, 350, 30);
		  nameField.setBackground(bgColor);
		  add(nameField);
		  // Username
		  usernameLabel = new JLabel("Username");
		  usernameLabel.setBounds(555,250, 100, 15);
		  add(usernameLabel);
		  usernameField = new JTextField(20);
		  usernameField.setBounds(555, 270, 350, 30);
		  usernameField.setBackground(bgColor);
		  add(usernameField);
		  // Password
		  passwordLabel = new JLabel("Password");
		  passwordLabel.setBounds(555, 310, 100, 15);
		  add(passwordLabel);
		  passwordField = new JPasswordField(20);
		  passwordField.setBounds(555, 330, 350, 30);
		  passwordField.setBackground(bgColor);
		  add(passwordField);
		  // Confirm Password
		  confirmPasswordLabel = new JLabel("Confirm Password");
		  confirmPasswordLabel.setBounds(555, 370, 150, 15);
		  add(confirmPasswordLabel);
		  confirmPasswordField = new JPasswordField(20);
		  confirmPasswordField.setBounds(555, 390, 350, 30);
		  confirmPasswordField.setBackground(bgColor);
		  add(confirmPasswordField);

		  // Register Button
		  registerButton = new JButton("Register");
		  registerButton.setBounds(555, 430, 165, 40);
		  registerButton.setForeground(bgColor);
		  registerButton.setBackground(primaryColor);
		  registerButton.setFont(new Font("Roboto", Font.BOLD, 15));
		  add(registerButton);
		  // Back Button
		  backButton = new JButton("Back to Login");
		  backButton.setBounds(740, 430, 165, 40);
		  backButton.setForeground(bgColor);
		  backButton.setBackground(primaryColor);
		  backButton.setFont(new Font("Roboto", Font.BOLD, 15));
		  add(backButton);

		  backButton.addActionListener(_ -> regCardLayout.show(regPanel, "Login"));

	 } // end of display method

	 public boolean validateInput() {
		  // Empty Username
		  if (usernameField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(
						  this,
						  "Username cannot be empty",
						  "Validation Error",
						  JOptionPane.ERROR_MESSAGE);
				return false;
		  }
		  // Empty Password
		  if (new String(passwordField.getPassword()).isEmpty()) {
				JOptionPane.showMessageDialog(
						  this,
						  "Password cannot be empty",
						  "Validation Error",
						  JOptionPane.ERROR_MESSAGE);
				return false;
		  }
		  // Password does not match Confirm Password
		  if (!new String(passwordField.getPassword()).equals(new String(confirmPasswordField.getPassword()))) {
				JOptionPane.showMessageDialog(
						  this,
						  "Passwords do not match",
						  "Validation Error",
						  JOptionPane.ERROR_MESSAGE);
				return false;
		  }
		  return true;
	 }

	 // Register Actions
	 public JButton getRegisterButton() {
		  return registerButton;
	 }
	 public String getUsernameInput() {
		  return usernameField.getText();
	 }
	 public String getNameInput() {
		  return nameField.getText();
	 }
	 public char[] getPasswordInput() {
		  return passwordField.getPassword();
	 }
	 public char[] getConfirmPasswordInput() {
		  return confirmPasswordField.getPassword();
	 }
	 public void showInvalidRegisterDialog(String msg) {
		  JOptionPane.showMessageDialog(
					 this,
					 msg,
					 "Error",
					 JOptionPane.ERROR_MESSAGE);
	 } // end of showInvalidRegisterDialog method
	 public void showValidRegisterDialog(String msg) {
		  JOptionPane.showMessageDialog(
					 this,
					 "Register Successful!",
					 "Success",
					 JOptionPane.INFORMATION_MESSAGE);
	 } // end of showValidRegisterDialog method
} // end of RegisterView class
