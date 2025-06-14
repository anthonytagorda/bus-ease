package server.view;

import server.model.ScheduleModel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerMainView extends JFrame {
	 protected Color primaryColor = Color.decode("#007BFF");
	 protected Color secondaryColor = Color.decode("#FFFFF0");
	 protected Color bgColor = Color.decode("#FFF9F5");
	 protected Border primaryBorder = BorderFactory.createLineBorder(primaryColor);
	 protected Border secondaryBorder = BorderFactory.createLineBorder(secondaryColor);
	 protected JLabel dateLabel = new JLabel();
	 protected JLabel timeLabel = new JLabel();
	 protected JButton addRouteButton = new JButton("Add Route");
	 protected JButton manageUsersButton = new JButton("Manage Users");
	 protected JButton manageBusesButton = new JButton("Manage Buses");
	 protected JButton bookingListButton = new JButton("Booking Lists");
	 protected ImageIcon icon = new ImageIcon("src/public/icons/bus_ease_icon.png");
	 protected ImageIcon logo = new ImageIcon("src/public/images/bus_ease_db_logo.png");
	 protected ImageIcon startIcon = new ImageIcon("src/public/icons/start_server.png");
	 protected Image startIcon_original = startIcon.getImage();
	 protected Image startIcon_resized = startIcon_original.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
	 protected ImageIcon resizedStartIcon = new ImageIcon(startIcon_resized);
	 protected JButton startServerButton = new JButton("Start Server", resizedStartIcon);
	 protected ImageIcon stopIcon = new ImageIcon("src/public/icons/stop_server.png");
	 protected Image stopIcon_original = stopIcon.getImage();
	 protected Image stopIcon_resized = stopIcon_original.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
	 protected ImageIcon resizedStopIcon = new ImageIcon(stopIcon_resized);
	 protected JButton stopServerButton = new JButton("Stop Server", resizedStopIcon);
	 protected ImageIcon logoutIcon = new ImageIcon("src/public/icons/logout.png");
	 protected Image logoutIcon_original = logoutIcon.getImage();
	 protected Image logoutIcon_resized = logoutIcon_original.getScaledInstance(15, 15, Image.SCALE_SMOOTH);
	 protected ImageIcon resizedLogoutIcon = new ImageIcon(logoutIcon_resized);
	 protected JButton logoutButton = new JButton("Logout", resizedLogoutIcon);
	 protected JPanel busPanel = new JPanel();
	 protected JLabel availableBusesLabel = new JLabel("Available Buses ");
	 protected JLabel busCountLabel = new JLabel("0");
	 protected JPanel ticketSoldPanel = new JPanel();
	 protected JLabel ticketSoldLabel = new JLabel("Tickets Sold ");
	 protected JLabel ticketsSoldCountLabel = new JLabel("0");
	 protected JPanel refundPanel = new JPanel();
	 protected JLabel refundLabel = new JLabel("Pending Refunds ");
	 protected JLabel refundCountLabel = new JLabel("0");
	 protected JPanel schedulesPanel = new JPanel();
	 protected JLabel schedulesLabel = new JLabel("Schedules ");
	 protected JTable schedulesTable = new JTable();
	 protected JLabel logLabel = new JLabel("Server Audit Log");
	 protected JTextArea logText = new JTextArea(20, 20);
	 protected JScrollPane logsScrollPane = new JScrollPane(logText);
	 protected JButton clearButton = new JButton("Clear");

	 public ServerMainView () {
		  SwingUtilities.invokeLater(this::display);
	 }

	 public void display() {
		  // Frame Configuration
		  setTitle("Bus Ease | Admin Dashboard");
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
		  // Server Main Buttons
		  leftPanel.add(addRouteButton);
		  addRouteButton.setBounds(25, 180, 150, 25);
		  addRouteButton.setBackground(Color.decode("#E7EBF7"));
		  leftPanel.add(manageUsersButton);
		  manageUsersButton.setBounds(25, 210, 150, 25);
		  manageUsersButton.setBackground(Color.decode("#E7EBF7"));
		  leftPanel.add(manageBusesButton);
		  manageBusesButton.setBounds(25, 240, 150, 25);
		  manageBusesButton.setBackground(Color.decode("#E7EBF7"));
		  leftPanel.add(bookingListButton);
		  bookingListButton.setBounds(25, 270, 150, 25);
		  bookingListButton.setBackground(Color.decode("#E7EBF7"));
		  leftPanel.add(startServerButton);
		  startServerButton.setBounds(25, 360, 150, 25);
		  startServerButton.setBackground(Color.decode("#3BB371"));
		  leftPanel.add(stopServerButton);
		  stopServerButton.setBounds(25, 390, 150, 25);
		  stopServerButton.setBackground(Color.decode("#DC0C40"));
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
		  // Available Buses Panel
		  busPanel.setLayout(null);
		  busPanel.setBounds(210, 10, 165, 85);
		  busPanel.setBackground(primaryColor);
		  availableBusesLabel.setForeground(bgColor);
		  availableBusesLabel.setBounds(5,5,150,25);
		  availableBusesLabel.setFont(new Font("Roboto", Font.BOLD, 14));
		  busPanel.add(availableBusesLabel);
		  busCountLabel.setForeground(Color.decode("#3BB371"));
		  busCountLabel.setBounds(10,40,150,25);
		  busCountLabel.setFont(new Font("Roboto", Font.BOLD, 20));
		  busPanel.add(busCountLabel);
		  mainPanel.add(busPanel);
		  // Total Tickets Sold
		  ticketSoldPanel.setLayout(null);
		  ticketSoldPanel.setBounds(380,10,170,85);
		  ticketSoldPanel.setBackground(primaryColor);
		  ticketSoldLabel.setForeground(bgColor);
		  ticketSoldLabel.setBounds(5,5,150,25);
		  ticketSoldLabel.setFont(new Font("Roboto", Font.BOLD, 14));
		  ticketSoldPanel.add(ticketSoldLabel);
		  ticketsSoldCountLabel.setForeground(Color.decode("#3BB371"));
		  ticketsSoldCountLabel.setBounds(10,40,150,25);
		  ticketsSoldCountLabel.setFont(new Font("Roboto", Font.BOLD, 20));
		  ticketSoldPanel.add(ticketsSoldCountLabel);
		  mainPanel.add(ticketSoldPanel);
		  // Pending Refunds
		  refundPanel.setLayout(null);
		  refundPanel.setBounds(555,10,170,85);
		  refundPanel.setBackground(primaryColor);
		  refundLabel.setForeground(bgColor);
		  refundLabel.setBounds(5,5,150,25);
		  refundLabel.setFont(new Font("Roboto", Font.BOLD, 14));
		  refundPanel.add(refundLabel);
		  refundCountLabel.setForeground(Color.decode("#3BB371"));
		  refundCountLabel.setBounds(10,40,150,25);
		  refundCountLabel.setFont(new Font("Roboto", Font.BOLD, 20));
		  refundPanel.add(refundCountLabel);
		  mainPanel.add(refundPanel);
		  // Departures Log
		  schedulesPanel.setLayout(null);
		  schedulesPanel.setBounds(210,100,515,210);
		  schedulesPanel.setBackground(primaryColor);
		  schedulesLabel.setForeground(bgColor);
		  schedulesLabel.setBounds(5,5,150,25);
		  schedulesLabel.setFont(new Font("Roboto", Font.BOLD, 16));
		  schedulesPanel.add(schedulesLabel);
		  // Departure Table
		  String[] columnNames = {"Bus #", "Type", "Date", "Time", "Origin", "Destination", "Distance" , "Slots"};
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
		  departureScrollPane.setBounds(5, 30, 505, 175);
		  departureScrollPane.setBorder(BorderFactory.createEmptyBorder());
		  schedulesPanel.add(departureScrollPane);
		  mainPanel.add(schedulesPanel);

		  // Server Audit Log
		  logLabel.setForeground(Color.BLACK);
		  logLabel.setBounds(210,270,515,100);
		  logText.setEditable(false);
		  logText.setLineWrap(true);
		  logText.setBackground(bgColor);
		  logText.setForeground(Color.decode("#232323"));
		  logText.setBorder(primaryBorder);
		  logText.setFont(new Font("Consolas", Font.PLAIN, 12));
		  mainPanel.add(logLabel);
		  logsScrollPane.setBounds(210, 330, 515, 80);
		  logsScrollPane.setBorder(secondaryBorder);
		  logsScrollPane.setViewportView(logText);
		  logsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		  mainPanel.add(logsScrollPane);
		  clearButton.setBounds(625, 420, 100, 25);
		  clearButton.setBackground(Color.decode("#E7EBF7"));
		  mainPanel.add(clearButton);
		  redirectSystemStreams();

		  add(mainPanel);
	 } // end of display method

	 // Server Main Actions
	 public void addRouteButtonListener(ActionListener listener) {
		  addRouteButton.addActionListener(listener);
	 }

	 public void addManageUsersButtonListener(ActionListener listener) {
		  manageUsersButton.addActionListener(listener);
	 }

	 public void addClearButtonListener(ActionListener listener) {
		  clearButton.addActionListener(listener);
	 }

	 public void addStartServerButtonListener(ActionListener listener) {
		  startServerButton.addActionListener(listener);
	 }

	 public void addStopServerButtonListener(ActionListener listener) {
		  stopServerButton.addActionListener(listener);
	 }

	 public void addLogoutButtonListener(ActionListener listener) {
		  logoutButton.addActionListener(listener);
	 }

	 // TODO -> add Manage Buses
	 // TODO -> add Booking Lists

	 public void clearLogText() {
		  logText.setText("");
	 }

	 // Other Server Main helper methods
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

	 private void updateTextArea(final String text) {
		  SwingUtilities.invokeLater(() -> {
				logText.append(text);
				logText.setCaretPosition(logText.getDocument().getLength());
		  });
	 }

	 private void redirectSystemStreams() {
		  OutputStream out = new OutputStream() {
				@Override
				public void write(int b) {
					 updateTextArea(String.valueOf((char) b));
				}

				@Override
				public void write(byte[] b, int off, int len) {
					 updateTextArea(new String(b, off, len));
				}

				@Override
				public void write(byte[] b) {
					 write(b, 0, b.length);
				}
		  };
		  // Redirect standard output to the custom OutputStream
		  System.setOut(new PrintStream(out, true));
		  System.setErr(new PrintStream(out, true));
	 }
} // end of ServerMainView class
