package passenger.view;

import server.model.ScheduleModel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardView extends JFrame {
	 protected Color primaryColor = Color.decode("#007BFF");
	 protected Color secondaryColor = Color.decode("#FFFFF0");
	 protected Color bgColor = Color.decode("#FFF9F5");
	 protected Border primaryBorder = BorderFactory.createLineBorder(primaryColor);
	 protected Border secondaryBorder = BorderFactory.createLineBorder(secondaryColor);
	 protected JLabel dateLabel = new JLabel();
	 protected JLabel timeLabel = new JLabel();
	 protected JButton bookTripButton = new JButton("Book Trip");
	 protected JButton editProfileButton = new JButton("Edit Profile");
	 protected JButton logoutButton = new JButton("Logout");
	 protected ImageIcon icon = new ImageIcon("src/public/icons/bus_ease_icon.png");
	 protected ImageIcon logo = new ImageIcon("src/public/images/bus_ease_db_logo.png");
	 protected JPanel schedulesPanel = new JPanel();
	 protected JLabel schedulesLabel = new JLabel("Schedules ");
	 protected JTable schedulesTable = new JTable();

	 public DashboardView ( ) {
		  SwingUtilities.invokeLater(this::display);
	 }

	 public void display ( ) {
		  // Frame Configuration
		  setTitle("Bus Retro | Passenger Dashboard");
		  setSize(750, 500);
		  setDefaultCloseOperation(EXIT_ON_CLOSE);
		  setIconImage(icon.getImage());
		  setResizable(false);
		  setLocationRelativeTo(null);
		  setVisible(true);

		  // Left Panel
		  JPanel leftPanel = new JPanel();
		  leftPanel.setLayout(null);
		  leftPanel.setBorder(primaryBorder);
		  leftPanel.setBackground(primaryColor);
		  leftPanel.setBounds(0,0,200,461);

		  // Dashboard Logo
		  Image dashboard_original = logo.getImage();
		  Image dashboard_resized = dashboard_original.getScaledInstance(140, 80, Image.SCALE_SMOOTH);
		  ImageIcon resizedLogo = new ImageIcon(dashboard_resized);
		  JLabel logoLabel = new JLabel(resizedLogo);
		  leftPanel.add(logoLabel);
		  logoLabel.setBounds(20, -10, 160, 200);
		  // Date and Time
		  dateLabel.setBounds(30,130,200,25);
		  dateLabel.setForeground(bgColor);
		  updateDate();
		  leftPanel.add(dateLabel);
		  timeLabel.setBounds(45,150, 150, 25);
		  timeLabel.setForeground(bgColor);
		  startTimer();
		  leftPanel.add(timeLabel);
		  // Dashboard Left Panel Buttons
		  leftPanel.add(bookTripButton);
		  bookTripButton.setBounds(25, 180, 150, 25);
		  bookTripButton.setBackground(Color.decode("#E7EBF7"));
		  leftPanel.add(editProfileButton);
		  editProfileButton.setBounds(25, 210, 150, 25);
		  editProfileButton.setBackground(Color.decode("#E7EBF7"));
		  leftPanel.add(logoutButton);
		  logoutButton.setBounds(25,420,150,25);
		  logoutButton.setBackground(Color.decode("#E7EBF7"));
		  add(leftPanel);
		  // Main Panel
		  JPanel mainPanel = new JPanel();
		  mainPanel.setLayout(null);
		  mainPanel.setBorder(primaryBorder);
		  mainPanel.setBounds(200,0,550,500);
		  mainPanel.setBackground(secondaryColor);
		  // Departures Log
		  schedulesPanel.setLayout(null);
		  schedulesPanel.setBounds(210,10,515,400);
		  schedulesPanel.setBackground(primaryColor);
		  schedulesLabel.setForeground(bgColor);
		  schedulesLabel.setBounds(5,5,150,25);
		  schedulesLabel.setFont(new Font("Roboto", Font.BOLD, 16));
		  schedulesPanel.add(schedulesLabel);
		  // Departure Table
		  String[] columnNames = {"Bus #", "Type", "Date", "Time", "Origin", "Destination", "Distance" , "Slots"};
		  // TODO: make departureTable read from the stream
		  Object[][] data = ScheduleModel.loadSchedules("src/server/model/bookings/schedule.xml");
		  schedulesTable = new JTable(data, columnNames) {
				@Override
				public boolean isCellEditable(int row, int column) {
					 return false;
				}
		  };
		  DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		  centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		  for (int i = 0; i < columnNames.length; i++) {
				schedulesTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		  }
		  schedulesTable.getColumnModel().getColumn(0).setPreferredWidth(90); // Bus No
		  schedulesTable.getColumnModel().getColumn(1).setPreferredWidth(120); // Bus Type
		  schedulesTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Date
		  schedulesTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Time
		  schedulesTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Origin
		  schedulesTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Destination
		  schedulesTable.getColumnModel().getColumn(6).setPreferredWidth(80);  // Distance
		  schedulesTable.getColumnModel().getColumn(7).setPreferredWidth(50);  // Slots
		  schedulesTable.setFillsViewportHeight(true);
		  JScrollPane departureScrollPane = new JScrollPane(schedulesTable);
		  departureScrollPane.setBounds(5, 30, 505, 360);
		  departureScrollPane.setBorder(BorderFactory.createEmptyBorder());
		  schedulesPanel.add(departureScrollPane);
		  mainPanel.add(schedulesPanel);
		  add(mainPanel);
	 } // end of display method

	 // Dashboard Actions
	 public void addBookTripButtonListener (ActionListener listener) {
		  bookTripButton.addActionListener(listener);
	 }

	 public void addEditProfileButtonListener (ActionListener listener) {
		  editProfileButton.addActionListener(listener);
	 }

	 public void addLogoutButtonListener (ActionListener listener) {
		  logoutButton.addActionListener(listener);
	 }

	 // Other dashboard helper methods
	 private void startTimer() {
		  Timer timer = new Timer(1000, e -> updateTime());
		  timer.start();
	 }

	 private void updateTime() {
		  SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss a");
		  String currentTime = sdf.format(new Date());
		  timeLabel.setText("Time:    %s".formatted(currentTime));
	 }

	 private void updateDate() {
		  SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy");
		  String currentDate = dateFormat.format(new Date());
		  dateLabel.setText(currentDate);
	 }
} // end of DashboardView class