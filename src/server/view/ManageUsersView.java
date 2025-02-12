package server.view;

import server.controller.handlers.PassengerHandler;
import server.model.users.Passenger;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("all")
public class ManageUsersView extends JFrame {
	 public ManageUsersView () {
		  SwingUtilities.invokeLater(this::display);
		  loadPassengerData();
	 }

	 protected static List<Passenger> passengerList;
	 protected JTable passengerTable;
	 protected int selectedPassengerRow;
	 protected Passenger selectedPassenger;
	 protected PassengerHandler passenger_updater;
	 protected String passenger_file_path;

	 protected Color primaryColor = Color.decode("#007BFF");
	 protected Color bgColor = Color.decode("#FFF9F5");
	 protected Border primaryBorder = BorderFactory.createLineBorder(primaryColor);
	 protected ImageIcon icon = new ImageIcon("src/public/icons/bus_ease_icon.png");
	 protected JTextField passengerUsernameField;
	 protected JTextField passengerNameField;
	 protected JPasswordField passengerPasswordField;
	 protected JLabel passengerTypeLabel = new JLabel();

	 protected JButton returnButton = new JButton("Return to Dashboard");
	 protected JButton cancelPassengerButton = new JButton("Cancel");
	 protected JButton editPassengerButton = new JButton("Edit Passenger");
	 protected JButton banPassengerButton = new JButton("Ban Passenger");
	 protected JButton deletePassengerButton = new JButton("Delete Passenger");
	 protected JButton addPassengerButton = new JButton("Add Passenger");
	 protected JTabbedPane tabbedPane = new JTabbedPane();

	 private void display() {
		  // Frame Configuration
		  setTitle("Bus Ease | Manage Users");
		  setSize(900, 500);
		  setDefaultCloseOperation(EXIT_ON_CLOSE);
		  setIconImage(icon.getImage());
		  setResizable(false);
		  setLocationRelativeTo(null);
		  setVisible(true);

		  // Tabbed Pane
		  tabbedPane.addTab("Manage Passengers", managePassengers());

		  // Upper Panel
		  JPanel upperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		  upperPanel.add(returnButton);
		  returnButton.setPreferredSize(new Dimension(165,25));

		  add(tabbedPane, BorderLayout.CENTER);
		  add(upperPanel, BorderLayout.NORTH);

		  updatePassengerTable();
	 }

	 // Manage Users Actions
	 public void addReturnButtonListener(ActionListener listener) {
		  returnButton.addActionListener(listener);
		  new ServerMainView();
	 }

	 private void editPassenger () {
		  // Implement passenger edit action
		  if (selectedPassenger != null) {
				// Get the new values from the text fields
				String newUsername = passengerUsernameField.getText().trim();
				String newName = passengerNameField.getText().trim();
				String newPassword = passengerPasswordField.getText().trim();

				if (!newName.equals(selectedPassenger.getName()) || !newUsername.equals(selectedPassenger.getUsername()) || !newPassword.equals(selectedPassenger.getPassword())) {
					 try {
						  editPassengerDetails(newUsername, newUsername, newPassword, selectedPassenger.getId());

						  // Update the passenger in the table model
						  selectedPassenger.setUsername(newUsername);
						  selectedPassenger.setName(newName);
						  selectedPassenger.setPassword(newPassword);

						  // Update the table row
						  ((DefaultTableModel) passengerTable.getModel()).fireTableRowsUpdated(selectedPassengerRow, selectedPassengerRow);

						  // Display a success message
						  JOptionPane.showMessageDialog(
									 null,
									 "Passenger details updated successfully.",
									 "Update Success",
									 JOptionPane.INFORMATION_MESSAGE);
					 } catch (Exception e) {
						  JOptionPane.showMessageDialog(
									 null,
									 "Failed to update passenger details.",
									 "Update Error",
									 JOptionPane.ERROR_MESSAGE);
					 }
				} else {
					 // If no changes were made, display a message indicating so
					 JOptionPane.showMessageDialog(
								null,
								"No changes were made to the passenger details.",
								"Update Error",
								JOptionPane.INFORMATION_MESSAGE);
				}
		  } else {
				// If no passenger is selected, display an error message
				JOptionPane.showMessageDialog(
						  null,
						  "No passenger selected.",
						  "Update Error",
						  JOptionPane.ERROR_MESSAGE);
		  }
	 } // end of editPassenger method

	 private void banPassenger () {
		  if (selectedPassenger != null) {
				passenger_updater.changeBanStatus(selectedPassenger.getId(), selectedPassenger.isBanned());
				((DefaultTableModel) passengerTable.getModel()).fireTableRowsUpdated(selectedPassengerRow, selectedPassengerRow);
		  }
	 } // end of banPassenger method

	 private void deletePassenger () {
		  if (selectedPassenger != null) {
				if (passenger_updater.deletePassenger(selectedPassenger.getId())) {
					 passengerList = Passenger.parseCredentials(passenger_file_path);
					 updatePassengerTable();
				} else {
					 JOptionPane.showMessageDialog(
								null,
								"Failed to delete the passenger.",
								"Delete Error",
								JOptionPane.ERROR_MESSAGE);
				}
		  }
	 } // end of deletePassenger method

	 // Other Manage Users helper methods
	 private void loadPassengerData () {
		  ClassLoader classLoader = getClass().getClassLoader();
		  passenger_file_path = Objects.requireNonNull(classLoader.getResource("server/model/users/passenger.xml")).getFile();
		  passengerList = Passenger.parseCredentials(passenger_file_path);
		  passenger_updater = new PassengerHandler();
		  // Set initial values for the text fields if selectedPassenger are not null
		  if (selectedPassenger != null) {
				passengerUsernameField.setText(selectedPassenger.getUsername());
				passengerNameField.setText(selectedPassenger.getName());
				passengerPasswordField.setText(selectedPassenger.getPassword());
		  }
	 }

	 private void loadPassengerDataToTable (DefaultTableModel passengerModel) {
		  for (Passenger passenger : passengerList) {
				passengerModel.addRow(new Object[]{
						  passenger.getId(),
						  passenger.getUsername(),
						  passenger.getName(),
						  passenger.getPassword(),
						  passenger.getType()
				});
		  }
	 } // end of loadPassengerDataToTable

	 private Passenger getPassengerById (int targetId) {
		  for (Passenger passenger : passengerList) {
				if (passenger.getId() == targetId) {
					 return passenger;
				}
		  }
		  return null; // Passenger not found
	 } // end of getPassengerById

	 private void updatePassengerTable () {
		  DefaultTableModel passengerModel = (DefaultTableModel) passengerTable.getModel();
		  // Load data into the table
		  loadPassengerDataToTable(passengerModel);
	 } // end of updatePassengerTable

	 private void editPassengerDetails(String username, String name, String password, int selectedPassengerId) {
		  try {
				// Update the passenger details
				passenger_updater.updatePassenger(username, name, password, selectedPassengerId);

				// Update the passenger in the passengerList
				Passenger passenger = getPassengerById(selectedPassengerId);
				if (passenger != null) {
					 passenger.getUsername();
					 passenger.getName();
					 passenger.getPassword();
				} else {
					 throw new NullPointerException("Passenger not found with ID: " +selectedPassengerId);
				}

				// Editable Details
				DefaultTableModel passengerModel = (DefaultTableModel) passengerTable.getModel();
				passengerModel.setValueAt(username, selectedPassengerRow, 1); 	// Update the username column
				passengerModel.setValueAt(name, selectedPassengerRow, 2); 		// Update the name column
				passengerModel.setValueAt(password, selectedPassengerRow, 3); 	// Updated the password column
		  } catch (Exception e) {
				JOptionPane.showMessageDialog(null,
														"Failed to update passenger details",
														"Update Error",
														JOptionPane.ERROR_MESSAGE);
		  }
	 } // end of editPassengerDetails method

	 private void updatePassengerDetailsPanel(JTextField username, JTextField name, JPasswordField password, JLabel type, int selectedPassengerRow) {
		  // Get the passenger ID from the selected row
		  int passengerId = (int) passengerTable.getValueAt(selectedPassengerRow, 0);

		  // Find the passenger by ID
		  Passenger selectedPassenger = passengerList.get(passengerId);
		  if (selectedPassenger != null) {
				username.setText(selectedPassenger.getUsername());
				name.setText(selectedPassenger.getName());
				password.setText(selectedPassenger.getPassword());
				type.setText(selectedPassenger.getType());
		  }
	 } // end of updatePassengerDetailsPanel

	 private JPanel managePassengers () {
		  JPanel passengerPanel = new JPanel(new BorderLayout());
		  JLabel passengerManagementLabel = new JLabel("Passenger Management Panel");
		  			passengerManagementLabel.setForeground(Color.WHITE);
		  			passengerPanel.add(passengerManagementLabel, BorderLayout.NORTH);
		  			passengerPanel.setBorder(primaryBorder);
		  			passengerPanel.setBackground(primaryColor);

		  JPanel passengerTablePanel = new JPanel(new BorderLayout());
		  			passengerTablePanel.setBorder(primaryBorder);
		  			passengerTablePanel.setPreferredSize(new Dimension(450,500));
		  DefaultTableModel passengerModel = new DefaultTableModel(0,5) {
				public boolean isCellEditable(int row, int column) {
					 return false;
				}
		  };

		  passengerTable = new JTable(passengerModel);
		  passengerModel.setColumnIdentifiers(new Object[]{"ID", "Username", "Name", "Password", "Type", "Status", "Ban"});
		  passengerTable.setDefaultRenderer(Object.class, new ManageUsersView.HighlightRowRenderer());
		  passengerTable.getColumnModel().getColumn(0).setMaxWidth(20);   // ID
		  passengerTable.getColumnModel().getColumn(1).setMaxWidth(100);	// Username
		  passengerTable.getColumnModel().getColumn(2).setMaxWidth(220);	// Name
		  passengerTable.getColumnModel().getColumn(3).setMaxWidth(100);	// Password
		  passengerTable.getColumnModel().getColumn(4).setMaxWidth(100);	// Type
		  passengerTable.getColumnModel().getColumn(5).setMaxWidth(50);	// Status
		  passengerTable.getColumnModel().getColumn(6).setMaxWidth(70);	// isBanned
		  passengerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		  JScrollPane passengerTableScrollPane = new JScrollPane(passengerTable);
		  JPanel passengerDetailsPanel = passengerDetailsPanel();
		  passengerTablePanel.add(passengerTableScrollPane, BorderLayout.CENTER);
		  passengerPanel.add(passengerDetailsPanel, BorderLayout.EAST);
		  passengerPanel.add(passengerTablePanel, BorderLayout.CENTER);

		  return passengerPanel;
	 } // end of managePassengers method

	 protected JPanel passengerDetailsPanel () {
		  JPanel passengerDetailsPanel = new JPanel();
		  passengerDetailsPanel.setLayout(new BoxLayout(passengerDetailsPanel, BoxLayout.Y_AXIS));
		  passengerDetailsPanel.setBorder(BorderFactory.createLineBorder(primaryColor));
		  passengerDetailsPanel.setBackground(Color.LIGHT_GRAY);
		  passengerDetailsPanel.setPreferredSize(new Dimension(350,30));

		  JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		  JLabel passengerDetailsLabel = new JLabel("Passenger Details");
		  labelPanel.add(passengerDetailsLabel);
		  passengerDetailsPanel.add(labelPanel);

		  // Username
		  JPanel firstLinePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		  firstLinePanel.add(new JLabel("Username: "));
		  JTextField username = new JTextField("");
		  firstLinePanel.add(username);
		  username.setPreferredSize(new Dimension(200, 25));
		  passengerDetailsPanel.add(firstLinePanel);

		  // Name
		  JPanel secondLinePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		  secondLinePanel.add(new JLabel("Name: "));
		  JTextField name = new JTextField("");
		  secondLinePanel.add(name);
		  name.setPreferredSize(new Dimension(200, 25));
		  passengerDetailsPanel.add(secondLinePanel);

		  // Password
		  JPanel thirdLinePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		  thirdLinePanel.add(new JLabel("Password: "));
		  JPasswordField password = new JPasswordField("");
		  thirdLinePanel.add(password);
		  password.setPreferredSize(new Dimension(200, 25));
		  passengerDetailsPanel.add(thirdLinePanel);

		  // User Type
		  JPanel fourthLinePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		  fourthLinePanel.add(new JLabel("Passenger Type:"));
		  fourthLinePanel.add(passengerTypeLabel);
		  passengerDetailsPanel.add(fourthLinePanel);

		  // Buttons
		  JPanel fifthLinePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		  fifthLinePanel.setBackground(primaryColor);
		  fifthLinePanel.add(cancelPassengerButton);
		  fifthLinePanel.add(editPassengerButton);
		  fifthLinePanel.add(banPassengerButton);
		  fifthLinePanel.add(deletePassengerButton);
		  passengerDetailsPanel.add(fifthLinePanel);

		  // Button Functions
		  editPassengerButton.addActionListener(edit -> editPassenger());
		  banPassengerButton.addActionListener(ban -> banPassenger());
		  deletePassengerButton.addActionListener(delete -> deletePassenger());

		  return passengerDetailsPanel;
	 }

	 private static class HighlightRowRenderer extends DefaultTableCellRenderer {
		  @Override
		  public Component getTableCellRendererComponent(
					 JTable table,
					 Object value,
					 boolean isSelected,
					 boolean hasFocus,
					 int row,
					 int column) {
				Component rendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				// Check if the passenger in the current row is banned based on the passenger ID
				int passengerId = (int) table.getValueAt(row, 0);
				boolean isBanned = isPassengerBanned(passengerId);

				// Set the background color to red if the passenger is banned
				if ( isBanned ) {
					 rendererComponent.setBackground(Color.decode("#FF7F7F"));
				} else {
					 // Reset the background color to default
					 rendererComponent.setBackground(table.getBackground());
				}

				if ( table.getParent() instanceof JScrollPane ) {
					 JScrollPane scrollPane = (JScrollPane) table.getParent();
					 Component parent = scrollPane.getParent();
					 if ( parent instanceof ManageUsersView ) {
						  ManageUsersView frame = (ManageUsersView) parent;
						  if (frame.tabbedPane.getSelectedIndex() == 0 ) {
								frame.updatePassengerDetailsPanel(
										  frame.passengerUsernameField,
										  frame.passengerNameField,
										  frame.passengerPasswordField,
										  frame.passengerTypeLabel,
										  row
								);
						  }
					 }
				}
				return rendererComponent;
		  }

		  private boolean isPassengerBanned(int passengerId) {
				for (Passenger passenger : passengerList) {
					 if ( passenger.getId() == passengerId ) {
						  return passenger.isBanned();
					 }
				}
				return false; // Default to 'not banned' if Passenger is not found
		  } // end of isPassengerBanned method
	 } // end of HighlightRowRenderer
} // end of ManageUsersView class
