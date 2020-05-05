package counter;

import api.ApiObject;
import api.Manifest;
import api.dde.DDE.DDEConnection;
import book.BookWindow;
import charts.BidAskCounterChartWindow;
import charts.ChartWindow;
import charts.INDEX_FUTURECOUNTER_FUTURERATIO_CHART;
import dataBase.DbUpdater;
import dataBase.HB;
import dataBase.HBsession;
import equalMove.EqualMoveCalculator;
import locals.Themes;
import logic.Logic;
import options.Option;
import options.OptionsDataCalculator;
import options.OptionsDataUpdater;
import setting.Setting;
import tables.ExpTable;
import tables.FrameSettingTable;
import tables.OptionsDataTable;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class WindowTA35 {

	boolean isRunning_lastTime = false;

	public JFrame frame;

	Color lightGreen = new Color(12, 135, 0);
	Color lightRed = new Color(229, 19, 0);

	static int h;
	static int m;
	static int s;

	public JTextField op_avg;
	public JTextField index;
	public JTextField future;
	public JButton send;
	public JButton cancelCall;
	public JButton cancelPut;
	public JButton cancelStock;
	public static JTextField rando;
	public JTextArea textArea;
	public JButton start;
	public String res = "All Day";
	public JTextField f_up;
	public JTextField f_down;
	public JTextField index_up;
	public JTextField index_down;
	public JTextField f_sum;
	public JTextField index_sum;
	public JButton export;
	public JButton demo;
	private JPanel panel;
	private JPanel panel_1;
	private JPanel panel_2;
	private JPanel ratioPanel;
	private JPanel bottomPanel;
	public JTextField monthStartExpField;
	public JTable table;
	public JTable optionsCalcTable;
	public static JTextArea log;
	public JButton restBtn;
	public JTextField monthDeltaField;
	public JTextField weekDeltaField;
	public JTextField deltaSumField;

	public int updater_id = 0;

	ApiObject apiObject = ApiObject.getInstance();

	boolean updaterBool = false;
	// Threads
	Updater updater;
	BackGroundRunner backGroundRunner;
	Logic logic;
	HB hb;
	DbUpdater dbUpdater;
	OptionsDataCalculator optionsDataCalculator;
	OptionsDataUpdater optionsDataUpdater;
	EqualMoveCalculator equalMoveCalculator;
	JTextField pesimiBasketField;
	JTextField optimiBasketField;
	public JTextField basketsSumField;
	JTextField optimiMoveField;
	JTextField pesimiMoveField;
	DDEConnection ddeConnection;
	public JTextField futureCounterField;
	private JPanel panel_12;
	private JPanel panel_13;
	private JPanel panel_19;
	private JScrollPane optionsCalcScrollPane;
	public JTextField futureRatioField;
	public JTextField equalMoveField;
	public JTextField weekStartExpField;

	// The main function
	public void startWindow() {
		Thread mainThread = new Thread() {
			public void run() {
				try {
					
					// Open the window
					initialize();
					frame.setVisible(true);
					
					// Show on screen
					showOnScreen(Manifest.screen, frame);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		};
		mainThread.start();
	}

	// Constructor
	public WindowTA35() {
		load_on_startup();
	}

	// Load on startup
	private void load_on_startup() {
		try {

			// DDE connection
			ddeConnection = new DDEConnection();
			ddeConnection.start();

			Thread.sleep(2000);

			// Options from DB
			loadOptionsDataFromDB();

			// Back ground runner
			backGroundRunner = new BackGroundRunner(this);
			backGroundRunner.start();
		} catch (Exception e) {
			JOptionPane.showConfirmDialog(frame, e.getMessage() + "\n" + e.getCause());
		}
	}

	private void loadOptionsDataFromDB() {
		// Get the table
		ArrayList< OptionsDataTable > table = (ArrayList<OptionsDataTable>) HB.getTable(OptionsDataTable.class);

		if (table.size() > 0) {
			// For each option
			for ( Option option : apiObject.getOptionsMonth().getOptionsList()) {
				// For each table line
				for (OptionsDataTable optionTable : table) {
					if (option.getName().equals(optionTable.getOption())) {
						option.setBidAskCounter(optionTable.getBidAskCounter());
					}
				}
			}
		}
	}

	// Show on screen
	public static void showOnScreen(int screen, JFrame frame) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gd = ge.getScreenDevices();
		if (screen > -1 && screen < gd.length) {
			frame.setLocation(gd[screen].getDefaultConfiguration().getBounds().x + frame.getX(), frame.getY());
		} else if (gd.length > 0) {
			frame.setLocation(gd[0].getDefaultConfiguration().getBounds().x + frame.getX(), frame.getY());
		} else {
			throw new RuntimeException("No Screens Found");
		}
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void initialize() throws InterruptedException, SQLException {
		// UI manager
		UIManager.put("InternalFrame.activeTitleBackground", new ColorUIResource(Color.black));
		UIManager.put("InternalFrame.activeTitleForeground", new ColorUIResource(Color.WHITE));
		UIManager.put("InternalFrame.titleFont", new Font("Dialog", Font.PLAIN, 11));

		ImageIcon img = new ImageIcon("C:/Users/ronens/Desktop/���� �����/iconDollar.png");
		frame = new JFrame();
		frame.getContentPane().setForeground(new Color(0, 0, 102));
		frame.getContentPane().setFont(new Font("Arial", Font.PLAIN, 12));
		frame.setTitle("TA35");
		frame.getContentPane().setBackground(new Color(255, 255, 255));
		frame.setBounds(-6, 0, 810, 209);
		frame.getContentPane().setLayout(null);
		frame.setIconImage(Toolkit.getDefaultToolkit()
				.getImage("C:\\Users\\user\\Desktop\\884466c0-505d-464a-bc9e-b2265d91ea38.png"));
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				UIManager.put("OptionPane.background", new ColorUIResource(189, 208, 239));
				UIManager.put("Panel.background", new ColorUIResource(189, 208, 239));

				String[] options = new String[] { "Yes", "No", "Yes with trunticate" };
				int res = JOptionPane.showOptionDialog(null, "Are you sure you want to close the program ?", "Title",
						JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
				if (res == 0) {
					System.exit(0);
				} else if (res == 1) {
					kill_dispose(frame);
				} else if (res == 2) {
					// Save bounds
					save_bounds();
					// Close the program
					System.exit(0);
				}
			}

			// Save the bounds of the windows
			private void save_bounds() {
				FrameSettingTable window_bounds = (FrameSettingTable) HB.get_line_by_id(FrameSettingTable.class, 1);
				window_bounds.setBounds(getBoundsAsString(frame));
				HB.update(window_bounds);
			}

			// Get bounds of frame
			private String getBoundsAsString(JFrame frame) {
				String bounds = String.valueOf(frame.getX()) + "," + String.valueOf(frame.getY()) + ","
						+ String.valueOf(frame.getWidth()) + "," + String.valueOf(frame.getHeight());
				return bounds;
			}
		});

		// timer
		javax.swing.Timer t = new javax.swing.Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Calendar now = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				h = now.get(Calendar.HOUR_OF_DAY);
				m = now.get(Calendar.MINUTE);
				s = now.get(Calendar.SECOND);
			}
		});
		t.start();

		panel = new JPanel();
		panel.setBackground(new Color(176, 196, 222));
		panel.setBounds(0, 26, 106, 102);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		f_up = new JTextField();
		f_up.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				open_setting_window(e);
			}
		});
		f_up.setHorizontalAlignment(SwingConstants.CENTER);
		f_up.setBounds(5, 5, 45, 25);
		panel.add(f_up);
		f_up.setForeground(lightGreen);
		f_up.setFont(new Font("Arial", Font.PLAIN, 15));
		f_up.setColumns(10);

		index_up = new JTextField();
		index_up.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				open_setting_window(e);
			}
		});
		index_up.setHorizontalAlignment(SwingConstants.CENTER);
		index_up.setBounds(55, 5, 45, 25);
		panel.add(index_up);
		index_up.setForeground(lightGreen);
		index_up.setFont(new Font("Arial", Font.PLAIN, 15));
		index_up.setColumns(10);

		index_down = new JTextField();
		index_down.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				open_setting_window(e);
			}
		});
		index_down.setHorizontalAlignment(SwingConstants.CENTER);
		index_down.setBounds(55, 35, 45, 25);
		panel.add(index_down);
		index_down.setForeground(lightRed);
		index_down.setFont(new Font("Arial", Font.PLAIN, 15));
		index_down.setColumns(10);

		f_down = new JTextField();
		f_down.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				open_setting_window(e);
			}
		});
		f_down.setHorizontalAlignment(SwingConstants.CENTER);
		f_down.setBounds(5, 35, 45, 25);
		panel.add(f_down);
		f_down.setForeground(lightRed);
		f_down.setFont(new Font("Arial", Font.PLAIN, 15));
		f_down.setColumns(10);

		f_sum = new JTextField();
		f_sum.setHorizontalAlignment(SwingConstants.CENTER);
		f_sum.setBounds(5, 70, 45, 25);
		panel.add(f_sum);
		f_sum.setForeground(Color.BLACK);
		f_sum.setFont(new Font("Arial", Font.PLAIN, 15));
		f_sum.setColumns(10);

		index_sum = new JTextField();
		index_sum.setHorizontalAlignment(SwingConstants.CENTER);
		index_sum.setBounds(55, 70, 45, 25);
		panel.add(index_sum);
		index_sum.setForeground(Color.BLACK);
		index_sum.setFont(new Font("Arial", Font.PLAIN, 15));
		index_sum.setColumns(10);

		panel_1 = new JPanel();
		panel_1.setBackground(new Color(0, 51, 102));
		panel_1.setBounds(654, 0, 64, 25);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);

		JLabel label_12 = new JLabel("יחס");
		label_12.setBounds(0, 0, 62, 25);
		panel_1.add(label_12);
		label_12.setHorizontalAlignment(SwingConstants.CENTER);
		label_12.setForeground(Color.WHITE);
		label_12.setFont(new Font("Arial", Font.BOLD, 15));

		panel_2 = new JPanel();
		panel_2.setBackground(new Color(0, 51, 102));
		panel_2.setBounds(0, 0, 106, 25);
		frame.getContentPane().add(panel_2);
		panel_2.setLayout(null);

		JLabel label_7 = new JLabel("\u05D7\u05D5\u05D6\u05D4 ");
		label_7.setBounds(10, 5, 45, 15);
		panel_2.add(label_7);
		label_7.setForeground(new Color(255, 255, 255));
		label_7.setFont(new Font("Arial", Font.BOLD, 15));

		JLabel label_8 = new JLabel("\u05DE\u05D3\u05D3");
		label_8.setBounds(57, 5, 42, 15);
		panel_2.add(label_8);
		label_8.setForeground(new Color(255, 255, 255));
		label_8.setFont(new Font("Arial", Font.BOLD, 15));

		ratioPanel = new JPanel();
		ratioPanel.setBackground(new Color(176, 196, 222));
		ratioPanel.setBounds(654, 26, 64, 102);
		frame.getContentPane().add( ratioPanel );
		ratioPanel.setLayout(null);

		JLabel label_9 = new JLabel("פקיעה");
		label_9.setBounds(352, 97, 36, 18);
		ratioPanel.add(label_9);
		label_9.setFont(new Font("Arial", Font.PLAIN, 15));
		label_9.setForeground(new Color(153, 204, 255));

		panel_13 = new JPanel();
		panel_13.setLayout(null);
		panel_13.setBackground(new Color(0, 51, 102));
		panel_13.setBounds(0, 35, 66, 25);
		ratioPanel.add(panel_13);

		JLabel label_2 = new JLabel("חוזה");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setForeground(Color.WHITE);
		label_2.setFont(new Font("Arial", Font.BOLD, 15));
		label_2.setBounds(0, 0, 66, 25);
		panel_13.add(label_2);

		futureCounterField = new JTextField();
		futureCounterField.setBounds(5, 66, 50, 25);
		ratioPanel.add(futureCounterField);
		futureCounterField.setHorizontalAlignment(SwingConstants.CENTER);
		futureCounterField.setForeground(new Color(12, 135, 0));
		futureCounterField.setFont(new Font("Arial", Font.PLAIN, 15));
		futureCounterField.setColumns(10);

		futureRatioField = new JTextField();
		futureRatioField.setBounds(5, 5, 50, 25);
		ratioPanel.add( futureRatioField );
		futureRatioField.setHorizontalAlignment(SwingConstants.CENTER);
		futureRatioField.setForeground(new Color(12, 135, 0));
		futureRatioField.setFont(new Font("Arial", Font.PLAIN, 15));
		futureRatioField.setColumns(10);

		bottomPanel = new JPanel();
		bottomPanel.setBackground(new Color(0, 51, 102));
		bottomPanel.setBounds(0, 129, 810, 38);
		frame.getContentPane().add( bottomPanel );
		bottomPanel.setLayout(null);

		start = new JButton("Start");
		start.setBounds(10, 7, 72, 23);
		bottomPanel.add(start);
		start.setForeground(new Color(0, 0, 51));
		start.setBackground(new Color(211, 211, 211));
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				start();
				start.setEnabled(false);
			}
		});

		start.setFont(new Font("Arial", Font.BOLD, 12));

		export = new JButton("Export");
		export.setBounds(128, 7, 78, 23);
		bottomPanel.add(export);
		export.setForeground(new Color(0, 0, 51));
		export.setBackground(new Color(211, 211, 211));
		export.setFont(new Font("Arial", Font.BOLD, 12));
		export.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Update exp races
				update_exp_races();

				// Append the sum of the day to Mysql
				apiObject.getHbHandler().getSumHandler().insetrLine();

				// Reset data
				apiObject.getHbHandler().getStatusHandler().resetDataToHB();
				apiObject.getHbHandler().getArraysHandler().resetDataToHB();

				export.setEnabled(false);
			}

			// Update exp races
			private void update_exp_races() {
				try {
					// Get the current
					ExpTable exp = (ExpTable) HB.get_line_by_id(ExpTable.class.getName(), 1);

					// Set the new races
					exp.setFuture_races(apiObject.getFutureExpRaces());
					exp.setIndex_races(apiObject.getIndexExpRaces());
					exp.setOptimi_timer(exp.getOptimi_timer() + ((int) (apiObject.getOptimiTimer() / 1000)));
					exp.setPesimi_timer(exp.getPesimi_timer() + ((int) (apiObject.getPesimiTimer() / 1000)));
					exp.setBasket_up(exp.getBasket_up() + apiObject.getBasketUp());
					exp.setBasket_down(exp.getBasket_down() + apiObject.getBasketDown());

					// Save the object
					HB.update(exp);
				} catch (Exception e) {
					popup("Error while updating races ", e);
					e.printStackTrace();
				}
			}

			// Create ta35 daily sum
		});

		JButton options = new JButton("Options");
		options.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean b;
				if (updater_id % 2 == 0) {
					b = true;
				} else {
					b = false;
				}
				BookWindow window = new BookWindow(updater_id, b);
				window.frame.setVisible(true);
				updater_id++;
			}
		});
		options.setForeground(new Color(0, 0, 51));
		options.setFont(new Font("Arial", Font.BOLD, 12));
		options.setBackground(new Color(211, 211, 211));
		options.setBounds(299, 7, 78, 23);
		bottomPanel.add(options);

		JButton chart = new JButton("Chart");
		chart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Create the window
				final ChartWindow chartWindow = new ChartWindow("");
				chartWindow.pack();
				chartWindow.setVisible(true);
			}
		});
		chart.setForeground(new Color(0, 0, 51));
		chart.setFont(new Font("Arial", Font.BOLD, 12));
		chart.setBackground(new Color(211, 211, 211));
		chart.setBounds(215, 7, 78, 23);
		bottomPanel.add(chart);

		restBtn = new JButton("Reset status");
		restBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				apiObject.getHbHandler().getStatusHandler().resetDataToHB();
				apiObject.getHbHandler().getArraysHandler().resetDataToHB();
				
			}
		});
		restBtn.setForeground(new Color(0, 0, 51));
		restBtn.setFont(new Font("Arial", Font.BOLD, 12));
		restBtn.setBackground(new Color(211, 211, 211));
		restBtn.setBounds(387, 7, 123, 23);
		bottomPanel.add(restBtn);

		JPanel panel_5 = new JPanel();
		panel_5.setLayout(null);
		panel_5.setBackground(new Color(176, 196, 222));
		panel_5.setBounds(163, 26, 64, 102);
		frame.getContentPane().add(panel_5);

		optimiMoveField = new JTextField();
		optimiMoveField.setHorizontalAlignment(SwingConstants.CENTER);
		optimiMoveField.setForeground(new Color(12, 135, 0));
		optimiMoveField.setFont(new Font("Arial", Font.PLAIN, 15));
		optimiMoveField.setColumns(10);
		optimiMoveField.setBounds(5, 5, 53, 25);
		panel_5.add(optimiMoveField);

		pesimiMoveField = new JTextField();
		pesimiMoveField.setHorizontalAlignment(SwingConstants.CENTER);
		pesimiMoveField.setForeground(new Color(229, 19, 0));
		pesimiMoveField.setFont(new Font("Arial", Font.PLAIN, 15));
		pesimiMoveField.setColumns(10);
		pesimiMoveField.setBounds(5, 35, 53, 25);
		panel_5.add(pesimiMoveField);

		equalMoveField = new JTextField();
		equalMoveField.setHorizontalAlignment(SwingConstants.CENTER);
		equalMoveField.setForeground(new Color(229, 19, 0));
		equalMoveField.setFont(new Font("Arial", Font.PLAIN, 15));
		equalMoveField.setColumns(10);
		equalMoveField.setBounds(5, 70, 53, 25);
		panel_5.add(equalMoveField);

		JPanel panel_6 = new JPanel();
		panel_6.setLayout(null);
		panel_6.setBackground(new Color(0, 51, 102));
		panel_6.setBounds(163, 0, 64, 25);
		frame.getContentPane().add(panel_6);

		JLabel label = new JLabel("תנועה");
		label.setBounds(0, 0, 64, 26);
		panel_6.add(label);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Arial", Font.BOLD, 15));

		JPanel basketsPanel = new JPanel();
		basketsPanel.setLayout(null);
		basketsPanel.setBackground(new Color(176, 196, 222));
		basketsPanel.setBounds(107, 26, 55, 102);
		frame.getContentPane().add(basketsPanel);

		pesimiBasketField = new JTextField();
		pesimiBasketField.setHorizontalAlignment(SwingConstants.CENTER);
		pesimiBasketField.setForeground(lightRed);
		pesimiBasketField.setFont(new Font("Arial", Font.PLAIN, 15));
		pesimiBasketField.setColumns(10);
		pesimiBasketField.setBounds(5, 35, 45, 25);
		basketsPanel.add(pesimiBasketField);

		optimiBasketField = new JTextField();
		optimiBasketField.setHorizontalAlignment(SwingConstants.CENTER);
		optimiBasketField.setForeground(lightGreen);
		optimiBasketField.setFont(new Font("Arial", Font.PLAIN, 15));
		optimiBasketField.setColumns(10);
		optimiBasketField.setBounds(5, 5, 45, 25);
		basketsPanel.add(optimiBasketField);

		basketsSumField = new JTextField();
		basketsSumField.setHorizontalAlignment(SwingConstants.CENTER);
		basketsSumField.setForeground(new Color(229, 19, 0));
		basketsSumField.setFont(new Font("Arial", Font.PLAIN, 15));
		basketsSumField.setColumns(10);
		basketsSumField.setBounds(5, 70, 45, 25);
		basketsPanel.add(basketsSumField);

		JPanel panel_11 = new JPanel();
		panel_11.setLayout(null);
		panel_11.setBackground(new Color(0, 51, 102));
		panel_11.setBounds(107, 0, 55, 25);
		frame.getContentPane().add(panel_11);

		JLabel label_6 = new JLabel("סלים");
		label_6.setHorizontalAlignment(SwingConstants.CENTER);
		label_6.setForeground(Color.WHITE);
		label_6.setFont(new Font("Arial", Font.BOLD, 15));
		label_6.setBounds(0, 0, 55, 26);
		panel_11.add(label_6);

		JPanel panel_7 = new JPanel();
		panel_7.setLayout(null);
		panel_7.setBackground(new Color(176, 196, 222));
		panel_7.setBounds(488, 0, 165, 128);
		frame.getContentPane().add(panel_7);

		String[] header = { "Call", "Strike", "Put" };
		Object[][] data = new Object[5][3];

		// Table
		table = myTable(data, header);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(5, 5, 155, 115);
		panel_7.add(scrollPane);

		JPanel panel_8 = new JPanel();
		panel_8.setBackground(new Color(176, 196, 222));
		panel_8.setBounds(382, 0, 105, 128);
		frame.getContentPane().add(panel_8);
		panel_8.setLayout(null);

		JPanel panel_9 = new JPanel();
		panel_9.setLayout(null);
		panel_9.setBackground(new Color(0, 51, 102));
		panel_9.setBounds(0, 0, 105, 25);
		panel_8.add(panel_9);

		JLabel lblPehgv = new JLabel("שבועית");
		lblPehgv.setHorizontalAlignment(SwingConstants.CENTER);
		lblPehgv.setForeground(Color.WHITE);
		lblPehgv.setFont(new Font("Arial", Font.BOLD, 15));
		lblPehgv.setBounds(0, 0, 105, 25);
		panel_9.add(lblPehgv);

		monthStartExpField = new JTextField();
		monthStartExpField.setBounds(5, 90, 95, 25);
		panel_8.add(monthStartExpField);
		monthStartExpField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getClickCount() == 2) {
					try {
						double start_exp = Double.parseDouble(JOptionPane.showInputDialog(frame, "Enter start exp"));
						apiObject.setStartExp(start_exp);

						// Get the current
						ExpTable exp = (ExpTable) HB.get_line_by_id(ExpTable.class.getName(), 1);
						exp.setStart_exp(start_exp);
						HB.update(exp);

					} catch (Exception e) {
					}
				}
			}
		});
		monthStartExpField.setHorizontalAlignment(SwingConstants.CENTER);
		monthStartExpField.setForeground(Color.WHITE);
		monthStartExpField.setFont(new Font("Verdana", Font.BOLD, 12));
		monthStartExpField.setColumns(10);
		
		weekStartExpField = new JTextField();
		weekStartExpField.setHorizontalAlignment(SwingConstants.CENTER);
		weekStartExpField.setForeground(Color.WHITE);
		weekStartExpField.setFont(new Font("Verdana", Font.BOLD, 12));
		weekStartExpField.setColumns(10);
		weekStartExpField.setBounds(5, 31, 95, 25);
		panel_8.add(weekStartExpField);
		
		JPanel panel_20 = new JPanel();
		panel_20.setLayout(null);
		panel_20.setBackground(new Color(0, 51, 102));
		panel_20.setBounds(0, 61, 105, 25);
		panel_8.add(panel_20);
		
		JLabel label_1 = new JLabel("חודשית");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setForeground(Color.WHITE);
		label_1.setFont(new Font("Arial", Font.BOLD, 15));
		label_1.setBounds(0, 0, 105, 25);
		panel_20.add(label_1);

		panel_12 = new JPanel();
		panel_12.setBackground(new Color(176, 196, 222));
		panel_12.setBounds(228, 26, 76, 102);
		frame.getContentPane().add(panel_12);
		panel_12.setLayout(null);

		future = new JTextField();
		future.setBounds(5, 66, 65, 25);
		panel_12.add(future);
		future.setForeground(new Color(0, 102, 51));
		future.setHorizontalAlignment(SwingConstants.CENTER);
		future.setFont(new Font("Arial", Font.PLAIN, 15));
		future.setColumns(10);

		JPanel bidAskCounterHeaderPanel = new JPanel();
		bidAskCounterHeaderPanel.setLayout(null);
		bidAskCounterHeaderPanel.setBackground(new Color(0, 51, 102));
		bidAskCounterHeaderPanel.setBounds(0, 35, 76, 25);
		panel_12.add(bidAskCounterHeaderPanel);

		JLabel label_4 = new JLabel("חוזה");
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		label_4.setForeground(Color.WHITE);
		label_4.setFont(new Font("Arial", Font.BOLD, 15));
		label_4.setBounds(0, 0, 76, 25);
		bidAskCounterHeaderPanel.add(label_4);

		JPanel deltaHeaderPanel = new JPanel();
		deltaHeaderPanel.setLayout( null );
		deltaHeaderPanel.setBounds( 719, 0, 80, 25 );
		deltaHeaderPanel.setBackground( Themes.BLUE );

		JLabel deltaLbl = new JLabel("דלתא");
		deltaLbl.setBounds( 0, 0, deltaHeaderPanel.getWidth(), deltaHeaderPanel.getHeight() );
		deltaLbl.setHorizontalAlignment( JLabel.CENTER );
		deltaLbl.setForeground( Color.WHITE );
		deltaHeaderPanel.add( deltaLbl );
		frame.getContentPane().add( deltaHeaderPanel );

		// Delta panel
		JPanel deltaPanel = new JPanel();
		deltaPanel.setBackground( basketsPanel.getBackground() );
		deltaPanel.setLayout( null );
		deltaPanel.setBounds( deltaHeaderPanel.getX(), deltaHeaderPanel.getY() + deltaHeaderPanel.getHeight() + 1, 80, 105 );
		frame.getContentPane().add( deltaPanel );

		// Month delta
		monthDeltaField = new JTextField();
		monthDeltaField.setBounds( 5, 5, 65, 25 );
		monthDeltaField.setHorizontalAlignment( JTextField.CENTER );
		monthDeltaField.setFont(new Font("Arial", Font.BOLD, 15));
		deltaPanel.add( monthDeltaField );

		// Week delta
		weekDeltaField = new JTextField();
		weekDeltaField.setBounds( 5, 35, 65, 25 );
		weekDeltaField.setHorizontalAlignment( JTextField.CENTER );
		weekDeltaField.setFont(new Font("Arial", Font.BOLD, 15));
		deltaPanel.add( weekDeltaField );

		// Delta sum
		deltaSumField = new JTextField();
		deltaSumField.setBounds( 5, 66, 65, 25 );
		deltaSumField.setHorizontalAlignment( JTextField.CENTER );
		deltaSumField.setFont(new Font("Arial", Font.BOLD, 15));
		deltaPanel.add( deltaSumField );

		index = new JTextField();
		index.setBounds(5, 5, 65, 25);
		panel_12.add(index);
		index.setHorizontalAlignment(SwingConstants.CENTER);
		index.setForeground(new Color(128, 0, 0));
		index.setFont(new Font("Arial", Font.PLAIN, 15));
		index.setColumns(10);

		JPanel panel_14 = new JPanel();
		panel_14.setLayout(null);
		panel_14.setBackground(new Color(0, 51, 102));
		panel_14.setBounds(228, 0, 76, 25);
		frame.getContentPane().add(panel_14);

		JLabel label_3 = new JLabel("מחושב");
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		label_3.setForeground(Color.WHITE);
		label_3.setFont(new Font("Arial", Font.BOLD, 15));
		label_3.setBounds(0, 0, 76, 25);
		panel_14.add(label_3);

		JPanel panel_16 = new JPanel();
		panel_16.setBackground(new Color(176, 196, 222));
		panel_16.setBounds(305, 26, 76, 102);
		frame.getContentPane().add(panel_16);
		panel_16.setLayout(null);

		op_avg = new JTextField();
		op_avg.setBounds(5, 5, 65, 25);
		panel_16.add(op_avg);
		op_avg.setHorizontalAlignment(SwingConstants.CENTER);
		op_avg.setForeground(Color.WHITE);
		op_avg.setFont(new Font("Arial", Font.PLAIN, 15));
		op_avg.setColumns(10);

		JPanel panel_18 = new JPanel();
		panel_18.setLayout(null);
		panel_18.setBackground(new Color(0, 51, 102));
		panel_18.setBounds(0, 35, 76, 25);
		panel_16.add(panel_18);

		JLabel label_10 = new JLabel("רנדומלי");
		label_10.setHorizontalAlignment(SwingConstants.CENTER);
		label_10.setForeground(Color.WHITE);
		label_10.setFont(new Font("Arial", Font.BOLD, 15));
		label_10.setBounds(0, 0, 76, 25);
		panel_18.add(label_10);

		rando = new JTextField();
		rando.setBounds(5, 66, 65, 25);
		panel_16.add(rando);
		rando.setHorizontalAlignment(SwingConstants.CENTER);
		rando.setForeground(new Color(255, 255, 255));
		rando.setFont(new Font("Arial", Font.BOLD, 15));
		rando.setColumns(10);

		JPanel panel_17 = new JPanel();
		panel_17.setLayout(null);
		panel_17.setBackground(new Color(0, 51, 102));
		panel_17.setBounds(305, 0, 76, 25);
		frame.getContentPane().add(panel_17);

		JLabel label_5 = new JLabel("ממוצע");
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		label_5.setForeground(Color.WHITE);
		label_5.setFont(new Font("Arial", Font.BOLD, 15));
		label_5.setBounds(0, 0, 76, 25);
		panel_17.add(label_5);

		panel_19 = new JPanel();
		panel_19.setLayout(null);
		panel_19.setBackground(new Color(176, 196, 222));
		panel_19.setBounds(935, 0, 165, 128);
		frame.getContentPane().add(panel_19);

		String[] header2 = { "Call", "Strike", "Put" };
		Object[][] data2 = new Object[5][3];

		optionsCalcTable = myTable(data2, header2);

		optionsCalcScrollPane = new JScrollPane(optionsCalcTable);
		optionsCalcScrollPane.setBounds(5, 5, 155, 115);
		panel_19.add(optionsCalcScrollPane);

		JPanel logPanel = new JPanel();
		logPanel.setBackground(new Color(176, 196, 222));
		logPanel.setBounds(900, 26, 147, 102);
		frame.getContentPane().add(logPanel);
		logPanel.setLayout(null);

		log = new JTextArea();
		log.setBounds(0, 0, 147, 102);
		logPanel.add(log);

	}

	// -------------------- function -------------------- //
	public void kill_dispose(JFrame frame) {
		try {
			// Close db connection
			HBsession.close_connection();
			frame.dispose();
		} catch (Exception e1) {
			popup("", e1);
			e1.printStackTrace();
		}
	}

	// Floor
	public static double floor(double d) {
		return Math.floor(d * 100) / 100;
	};

	// Open setting window if 2 clicks
	private void open_setting_window(MouseEvent e) {
		if (e.getClickCount() == 2) {
			Setting setting = new Setting();
			setting.setVisible();
		}
	}

	public void closeLogic() {
		logic.close();
	}

	// Start
	private void start() {

		// Logic
		logic = new Logic(this);
		logic.start();

		if (dbUpdater == null) {
			dbUpdater = new DbUpdater();
			dbUpdater.start();
		}

		// Start the updater
		startUpdater();

		Thread thread = new Thread(() -> {

			do {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} while (!apiObject.isDbLoaded());

			// 4 Lines chart
			ChartWindow chartWindow = new ChartWindow("TA35");
			chartWindow.pack();
			chartWindow.setVisible(true);

			// x, y width, high
			int[] bounds = { 2697, 2, 465, 837 };

			INDEX_FUTURECOUNTER_FUTURERATIO_CHART chart = new INDEX_FUTURECOUNTER_FUTURERATIO_CHART(bounds);
			chart.createChart();

			// Options window table
			optionsDataCalculator = new OptionsDataCalculator();
			optionsDataCalculator.startRunner();

			optionsDataUpdater = new OptionsDataUpdater(table, optionsCalcTable);
			optionsDataUpdater.start();

			// EqualMoveCalculator
			equalMoveCalculator = new EqualMoveCalculator();
			equalMoveCalculator.getHandler().start();
		});

		thread.start();
	}

	// Stop
	public void stop() {
		start.setEnabled(true);
	}

	// Popup
	public static void popup(String message, Exception e) {
		JOptionPane.showMessageDialog(null, message + "\n" + e.getMessage());
	}

	// Create xls file with the date of today
	public FileOutputStream getFile(String name) {
		try {
			return new FileOutputStream(apiObject.getExport_dir() + name);
		} catch (FileNotFoundException e) {
			WindowTA35.popup("Creating file error ", e);
			e.printStackTrace();
		}
		return null;
	}

	// Start the updater
	public void startUpdater() {
		if (!updaterBool) {
			updater = new Updater(this);
			updater.start();
			updaterBool = true;
		}
	}

	// Start the updater
	public void stopUpdater() {
		updater.close();
	}

	public void stopDbUpdater() {
		dbUpdater.close();
	}

	// Getters and Setters

	public Updater getUpdater() {
		return updater;
	}

	public void setUpdater(Updater updater) {
		this.updater = updater;
	}

	public boolean isUpdaterBool() {
		return updaterBool;
	}

	public void setUpdaterBool(boolean updaterBool) {
		this.updaterBool = updaterBool;
	}

	private JTable myTable(Object[][] rowsData, String[] cols) {
		Color darkBlue = new Color(0, 51, 102);

		// Table
		JTable table = new JTable(rowsData, cols) {

			public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {

				Component c = super.prepareRenderer(renderer, row, col);

				String strike = String.valueOf(getValueAt(row, 1));

				Color originalColor = c.getBackground();

				int cell_val = 0;
				try {
					if (!getValueAt(row, col).equals("")) {
						try {
							cell_val = (int) getValueAt(row, col);
						} catch (ClassCastException e) {
							cell_val = Integer.parseInt((String) getValueAt(row, col));
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

				try {

					// Call
					if (col == 0) {
						// Color forf
						if (cell_val > 0) {
							c.setForeground(lightGreen);
						} else {
							c.setForeground(lightRed);
						}

					} else

					// Put
					if (col == 2) {

						// Color forf
						if (cell_val > 0) {
							c.setForeground(lightGreen);
						} else {
							c.setForeground(lightRed);
						}

					} else

					// Strike
					if (col == 1) {
						c.setFont(c.getFont().deriveFont(Font.BOLD));
						c.setForeground(Color.BLACK);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				return c;
			}
		};
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if (event.getModifiers() == MouseEvent.BUTTON3_MASK) {

					// Main menu
					JPopupMenu menu = new JPopupMenu();

					JMenuItem setting = new JMenuItem("Setting");
					setting.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							Setting setting = new Setting();
							setting.setVisible();
						}
					});

					JMenuItem chart = new JMenuItem("Chart");
					chart.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							try {
								String strike = JOptionPane.showInputDialog("Enter strike");
								BidAskCounterChartWindow bidAskCounterChartWindow = new BidAskCounterChartWindow(
										100000000, strike);
								bidAskCounterChartWindow.pack();
								bidAskCounterChartWindow.setVisible(true);
							} catch (Exception exception) {
								exception.printStackTrace();
							}
						}
					});

					JMenuItem strike = new JMenuItem("Set strike");
					strike.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							try {
								int strike = Integer.parseInt(JOptionPane.showInputDialog("Enter middle strike"));
								OptionsDataUpdater.updateStrikes(strike);
							} catch (Exception exception) {
								exception.printStackTrace();
							}
						}
					});

					menu.add(strike);
					menu.add(chart);
					menu.add(setting);
					// Show the menu
					menu.show(event.getComponent(), event.getX(), event.getY());
				}

				if (event.getClickCount() == 2) {
					table.clearSelection();
				}
			}
		});
		table.setBounds(0, 0, 300, 235);

		// Header
		JTableHeader header = table.getTableHeader();
		header.setVisible(false);
		table.setTableHeader(null);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		table.setDefaultRenderer(Object.class, centerRenderer);
		table.setFillsViewportHeight(true);
		table.setRowHeight(22);
		table.setFont(new Font("Arial", Font.PLAIN, 15));
		table.setShowGrid(true);
		table.setSelectionBackground(Color.YELLOW);
		return table;
	}
}
