package passenger.view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginView extends JPanel {
	 // Login Components
	 protected JLabel usernameLabel, passwordLabel, forgotPasswordLabel, registerLabel;
	 protected JTextField usernameField;
	 protected JPasswordField passwordField;
	 protected JButton loginButton;

	 private final JPanel loginCardPanel;
	 private final CardLayout loginCardLayout;

	 public LoginView (JPanel loginCardPanel, CardLayout loginCardLayout) {
		  this.loginCardPanel = loginCardPanel;
		  this.loginCardLayout = loginCardLayout;
		  display();
	 }

	 private void display ( ) {
		  // Color and Border
		  Color primaryColor = Color.decode("#007BFF");
		  Color bgColor = Color.decode("#FFF9F5");
		  Border border = BorderFactory.createLineBorder(primaryColor);

		  /* Header */
		  // Bus GIF
		  ImageIcon busGif = new ImageIcon("src/public/images/bus.gif");
		  JLabel busGifLabel = new JLabel(busGif);
		  busGifLabel.setBounds(0, 0, 510, 512);
		  add(busGifLabel);
		  // Bus Logo
		  ImageIcon busLogo = new ImageIcon("src/public/images/bus_ease_logo.png");
		  Image busLogo_original = busLogo.getImage();
		  Image busLogo_resized = busLogo_original.getScaledInstance(200, 130, Image.SCALE_SMOOTH);
		  ImageIcon resizedBusLogo = new ImageIcon(busLogo_resized);
		  JLabel busLogoLabel = new JLabel(resizedBusLogo);
		  busLogoLabel.setBounds(530, - 105, 400, 400);
		  add(busLogoLabel);
		  // Login Header Label
		  JLabel logHeaderLabel = new JLabel("Login");
		  logHeaderLabel.setBounds(690, 160, 250, 35);
		  logHeaderLabel.setFont(new Font("Comic Sans", Font.BOLD, 30));
		  logHeaderLabel.setForeground(primaryColor);
		  add(logHeaderLabel);

		  // Panel Configuration
		  setPreferredSize(new Dimension(960, 540));
		  setLayout(null);
		  setBackground(bgColor);
		  setBorder(border);

		  // Component Initialization
		  // Username
		  usernameLabel = new JLabel("Username");
		  usernameLabel.setBounds(555, 215, 100, 15);
		  add(usernameLabel);
		  usernameField = new JTextField(20);
		  usernameField.setBounds(555, 235, 350, 30);
		  usernameField.setBackground(bgColor);
		  add(usernameField);
		  // Password
		  passwordLabel = new JLabel("Password");
		  passwordLabel.setBounds(555, 275, 100, 15);
		  add(passwordLabel);
		  passwordField = new JPasswordField(20);
		  passwordField.setBounds(555, 295, 350, 30);
		  passwordField.setBackground(Color.decode("#FAFFFA"));
		  add(passwordField);
		  // Forgot Password
		  forgotPasswordLabel = new JLabel("<html><u>Forgot Password?</u></html>");
		  forgotPasswordLabel.setForeground(primaryColor);
		  forgotPasswordLabel.setBounds(555, 340, 150, 15);
		  add(forgotPasswordLabel);
		  // TODO -> Forgot Password

		  // Login Button
		  loginButton = new JButton("Login");
		  loginButton.setBounds(555, 380, 350, 40);
		  loginButton.setForeground(bgColor);
		  loginButton.setBackground(primaryColor);
		  loginButton.setFont(new Font("Roboto", Font.BOLD, 15));
		  add(loginButton);
		  // Register Label
		  registerLabel = new JLabel("<html><u>No Account? Create an Account</u></html>");
		  registerLabel.setBounds(620, 440, 250, 40);
		  registerLabel.setBackground(primaryColor);
		  registerLabel.setFont(new Font("Roboto", Font.BOLD, 15));
		  add(registerLabel);

		  registerLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked (MouseEvent e) {
					 // Switch to the Register view
					 loginCardLayout.show(loginCardPanel, "Register");
				}

				@Override
				public void mouseEntered (MouseEvent e) {
					 // Optionally change the cursor to a hand cursor when hovering
					 registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}

				@Override
				public void mouseExited (MouseEvent e) {
					 // Optionally reset the cursor when not hovering
					 registerLabel.setCursor(Cursor.getDefaultCursor());
				}
		  });
	 } // end of display method

	 // Login Actions
	 public JButton getLoginButton ( ) {
		  return loginButton;
	 }

	 public String getUsernameInput ( ) {
		  return usernameField.getText();
	 }

	 public char[] getPasswordInput ( ) {
		  return passwordField.getPassword();
	 }

	 public void showInvalidLoginDialog (String msg) {
		  JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
	 } // end of showInvalidLoginDialog

	 public void showValidLoginDialog ( ) {
		  JOptionPane.showMessageDialog(null, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

		  Window window = SwingUtilities.getWindowAncestor(loginButton);
		  if ( window != null ) {
				window.dispose();
		  }
	 } // end of showValidLoginDialog method

} // end of LoginView panel
