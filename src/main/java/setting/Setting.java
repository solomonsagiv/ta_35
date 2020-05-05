package setting;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import api.ApiObject;
import arik.Arik;
import counter.WindowTA35;
import dataBase.HB;
import dataBase.HBsession;
import options.Option;
import options.OptionsDataUpdater;
import tables.ExpTable;
import tables.SettingTable;

public class Setting {

	private JFrame frame;
	private JPanel panel;
	private JDesktopPane desktopPane;
	private JPanel setting_panel;
	private JLabel lblNewLabel_1;
	private JLabel lblIndex;
	private JTextField setting_f_up;
	private JTextField setting_f_down;
	private JTextField setting_index_up;
	private JTextField setting_index_down;
	private JPanel panel_1;
	private JButton set;
	private JLabel lblExpPrice;
	private JPanel panel_2;
	private JTextField exp_price_field;

	ApiObject apiObject = ApiObject.getInstance();
	private JTextField futureExp;
	private JTextField indexExp;
	private JLabel lblOptimiTimer;
	private JLabel lblPesimiTimer;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JLabel lblBasketUp;
	private JLabel lblBasketDown;
	private JTextField textField_3;
	private JLabel lblExpTimer;
	private JLabel lblExpBaskets;
	private JTextField textField_4;
	private JTextField expBasketsField;
	private JTextField textField_6;
	private JTextField optimiMoveField;
	private JTextField pesimiMoveField;
	private JTextField futureCounterField;
	private JTextField indexCounterField;
	private JButton btnLoadExpData;

	public static JButton btnDoExp;
	private JTextField expWeekStartField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Setting window = new Setting();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Setting() {
		initialize();
		load_on_startup();
	}

	// Load on startup
	private void load_on_startup() {
		SessionFactory factory = HBsession.getSessionInstance().getSessionFactory();
		Session session = factory.getCurrentSession();

		SettingTable setting = (SettingTable) HB.get(session, SettingTable.class.getName(), 1);
		apiObject.setExport_dir(setting.getItem_data());
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 675, 346);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Image image = (new ImageIcon("C:\\Users\\user\\Desktop\\Work\\Development\\icons\\Misc-Database-3-icon.png")
				.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
		frame.getContentPane().setLayout(null);

		panel = new JPanel();
		panel.setBounds(0, 0, 671, 546);
		frame.getContentPane().add(panel);
		panel.setBackground(new Color(0, 0, 51));
		panel.setLayout(null);

		desktopPane = new JDesktopPane();
		desktopPane.setBounds(0, 0, 671, 546);
		panel.add(desktopPane);
		desktopPane.setLayout(null);

		setting_panel = new JPanel();
		setting_panel.setBounds(0, 0, 671, 546);
		desktopPane.add(setting_panel);
		setting_panel.setBackground(SystemColor.control);

		panel_1 = new JPanel();
		panel_1.setBounds(0, 0, 155, 147);
		panel_1.setBackground(SystemColor.control);

		panel_2 = new JPanel();
		panel_2.setBounds(161, 155, 406, 153);

		lblOptimiTimer = new JLabel("Optimi timer ");
		lblOptimiTimer.setBounds(166, 11, 71, 21);
		lblOptimiTimer.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 12));

		lblPesimiTimer = new JLabel("Pesimi timer ");
		lblPesimiTimer.setBounds(255, 11, 71, 21);
		lblPesimiTimer.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 12));

		textField = new JTextField();
		textField.setBounds(166, 38, 71, 22);
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					apiObject.setOptimiTimer(Integer.parseInt(arg0.getActionCommand()));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setForeground(new Color(0, 51, 153));
		textField.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		textField.setColumns(10);
		textField.setBorder(new LineBorder(Color.DARK_GRAY));
		textField.setBackground(SystemColor.menu);

		textField_1 = new JTextField();
		textField_1.setBounds(255, 38, 71, 22);
		textField_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					apiObject.setPesimiTimer(Integer.parseInt(e.getActionCommand()));
				} catch (Exception e1) {
					// TODO: handle exception
				}
			}
		});
		textField_1.setHorizontalAlignment(SwingConstants.CENTER);
		textField_1.setForeground(new Color(0, 51, 153));
		textField_1.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		textField_1.setColumns(10);
		textField_1.setBorder(new LineBorder(Color.DARK_GRAY));
		textField_1.setBackground(SystemColor.menu);

		textField_2 = new JTextField();
		textField_2.setBounds(166, 92, 71, 22);
		textField_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					apiObject.setBasketUp(Integer.parseInt(e.getActionCommand()));
				} catch (Exception e1) {
					// TODO: handle exception
				}
			}
		});
		textField_2.setHorizontalAlignment(SwingConstants.CENTER);
		textField_2.setForeground(new Color(0, 51, 153));
		textField_2.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		textField_2.setColumns(10);
		textField_2.setBorder(new LineBorder(Color.DARK_GRAY));
		textField_2.setBackground(SystemColor.menu);

		lblBasketUp = new JLabel("Basket up");
		lblBasketUp.setBounds(166, 66, 71, 21);
		lblBasketUp.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 12));

		lblBasketDown = new JLabel("Basket down");
		lblBasketDown.setBounds(255, 66, 71, 21);
		lblBasketDown.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 12));

		textField_3 = new JTextField();
		textField_3.setBounds(255, 92, 71, 22);
		textField_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					apiObject.setBasketDown(Integer.parseInt(e.getActionCommand()));
				} catch (Exception e1) {
					// TODO: handle exception
				}
			}
		});
		textField_3.setHorizontalAlignment(SwingConstants.CENTER);
		textField_3.setForeground(new Color(0, 51, 153));
		textField_3.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		textField_3.setColumns(10);
		textField_3.setBorder(new LineBorder(Color.DARK_GRAY));
		textField_3.setBackground(SystemColor.menu);
		panel_2.setLayout(null);

		lblExpPrice = new JLabel("Exp month");
		lblExpPrice.setBounds(10, 46, 74, 21);
		panel_2.add(lblExpPrice);
		lblExpPrice.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));

		exp_price_field = new JTextField();
		exp_price_field.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					double start_exp = Double.parseDouble(exp_price_field.getText());
					apiObject.setStartExp(start_exp);
				} catch (NumberFormatException e) {
					exp_price_field.setText(null);
					WindowTA35.popup("Number format exception ", e);
					e.printStackTrace();
				}
			}
		});
		exp_price_field.setHorizontalAlignment(SwingConstants.CENTER);
		exp_price_field.setForeground(new Color(0, 51, 153));
		exp_price_field.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		exp_price_field.setColumns(10);
		exp_price_field.setBorder(new LineBorder(Color.DARK_GRAY));
		exp_price_field.setBackground(SystemColor.menu);
		exp_price_field.setBounds(10, 73, 67, 22);
		panel_2.add(exp_price_field);

		futureExp = new JTextField();
		futureExp.setHorizontalAlignment(SwingConstants.CENTER);
		futureExp.setForeground(new Color(0, 51, 153));
		futureExp.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		futureExp.setColumns(10);
		futureExp.setBorder(new LineBorder(Color.DARK_GRAY));
		futureExp.setBackground(SystemColor.menu);
		futureExp.setBounds(107, 73, 57, 22);
		panel_2.add(futureExp);

		JLabel lblFuture = new JLabel("Future");
		lblFuture.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		lblFuture.setBounds(119, 46, 45, 21);
		panel_2.add(lblFuture);

		indexExp = new JTextField();
		indexExp.setHorizontalAlignment(SwingConstants.CENTER);
		indexExp.setForeground(new Color(0, 51, 153));
		indexExp.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		indexExp.setColumns(10);
		indexExp.setBorder(new LineBorder(Color.DARK_GRAY));
		indexExp.setBackground(SystemColor.menu);
		indexExp.setBounds(184, 73, 57, 22);
		panel_2.add(indexExp);

		JLabel lblIndex_1 = new JLabel("Index");
		lblIndex_1.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		lblIndex_1.setBounds(196, 46, 45, 21);
		panel_2.add(lblIndex_1);

		JButton button = new JButton("Set");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int future = 0;
				int index = 0;

				try {
					future = Integer.parseInt(futureExp.getText());
					index = Integer.parseInt(indexExp.getText());
				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					apiObject.setFutureExpRaces(future);
					apiObject.setIndexExpRaces(index);
				}
			}
		});
		button.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		button.setBounds(107, 106, 134, 30);
		panel_2.add(button);
		panel_1.setLayout(null);

		setting_index_down = new JTextField();
		setting_index_down.setBackground(SystemColor.control);
		setting_index_down.setBounds(83, 71, 45, 22);
		panel_1.add(setting_index_down);
		setting_index_down.setForeground(new Color(204, 0, 51));
		setting_index_down.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		setting_index_down.setHorizontalAlignment(SwingConstants.CENTER);
		setting_index_down.setBorder(new LineBorder(Color.DARK_GRAY));
		setting_index_down.setColumns(10);

		setting_f_down = new JTextField();
		setting_f_down.setBackground(SystemColor.control);
		setting_f_down.setBounds(24, 71, 45, 22);
		panel_1.add(setting_f_down);
		setting_f_down.setForeground(new Color(204, 0, 51));
		setting_f_down.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		setting_f_down.setHorizontalAlignment(SwingConstants.CENTER);
		setting_f_down.setBorder(new LineBorder(Color.DARK_GRAY));
		setting_f_down.setColumns(10);

		setting_f_up = new JTextField();
		setting_f_up.setBackground(SystemColor.control);
		setting_f_up.setBounds(24, 38, 45, 22);
		panel_1.add(setting_f_up);
		setting_f_up.setForeground(new Color(0, 51, 153));
		setting_f_up.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		setting_f_up.setHorizontalAlignment(SwingConstants.CENTER);
		setting_f_up.setBorder(new LineBorder(Color.DARK_GRAY));
		setting_f_up.setColumns(10);

		setting_index_up = new JTextField();
		setting_index_up.setBackground(SystemColor.control);
		setting_index_up.setBounds(83, 38, 45, 22);
		panel_1.add(setting_index_up);
		setting_index_up.setForeground(new Color(0, 51, 153));
		setting_index_up.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		setting_index_up.setHorizontalAlignment(SwingConstants.CENTER);
		setting_index_up.setBorder(new LineBorder(Color.DARK_GRAY));
		setting_index_up.setColumns(10);

		lblIndex = new JLabel("Index");
		lblIndex.setBounds(87, 11, 45, 21);
		panel_1.add(lblIndex);
		lblIndex.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));

		lblNewLabel_1 = new JLabel("Future");
		lblNewLabel_1.setBounds(25, 11, 45, 21);
		panel_1.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		lblNewLabel_1.setLabelFor(setting_f_up);

		set = new JButton("Set");
		set.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					apiObject.setFuture_up(Integer.parseInt(setting_f_up.getText()));
					apiObject.setFuture_down(Integer.parseInt(setting_f_down.getText()));
					apiObject.setIndex_up(Integer.parseInt(setting_index_up.getText()));
					apiObject.setIndex_down(Integer.parseInt(setting_index_down.getText()));

				} catch (NumberFormatException e) {
					e.printStackTrace();
					popup("Only numbers are eccepted ", e);
				}
			}

		});
		setting_panel.setLayout(null);
		set.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		set.setBounds(24, 104, 104, 30);
		panel_1.add(set);
		setting_panel.add(panel_1);
		setting_panel.add(panel_2);

		JLabel lblExp = new JLabel("Exp");
		lblExp.setHorizontalAlignment(SwingConstants.LEFT);
		lblExp.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		lblExp.setBounds(10, 11, 67, 21);
		panel_2.add(lblExp);

		textField_4 = new JTextField();
		textField_4.setBounds(254, 73, 56, 22);
		panel_2.add(textField_4);
		textField_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		textField_4.setHorizontalAlignment(SwingConstants.CENTER);
		textField_4.setForeground(new Color(0, 51, 153));
		textField_4.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		textField_4.setColumns(10);
		textField_4.setBorder(new LineBorder(Color.DARK_GRAY));
		textField_4.setBackground(SystemColor.menu);

		lblExpTimer = new JLabel("Exp timer ");
		lblExpTimer.setBounds(254, 46, 71, 21);
		panel_2.add(lblExpTimer);
		lblExpTimer.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 12));

		lblExpBaskets = new JLabel("Exp baskets");
		lblExpBaskets.setBounds(335, 46, 71, 21);
		panel_2.add(lblExpBaskets);
		lblExpBaskets.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 12));

		expBasketsField = new JTextField();
		expBasketsField.setBounds(335, 73, 56, 22);
		panel_2.add(expBasketsField);
		expBasketsField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				apiObject.setExpBasketUp(Integer.parseInt(e.getActionCommand()));
				apiObject.setExpBasketDown(0);
			}
		});
		expBasketsField.setHorizontalAlignment(SwingConstants.CENTER);
		expBasketsField.setForeground(new Color(0, 51, 153));
		expBasketsField.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		expBasketsField.setColumns(10);
		expBasketsField.setBorder(new LineBorder(Color.DARK_GRAY));
		expBasketsField.setBackground(SystemColor.menu);

		JLabel lblExpWeek = new JLabel("Exp week");
		lblExpWeek.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		lblExpWeek.setBounds(10, 93, 67, 21);
		panel_2.add(lblExpWeek);

		expWeekStartField = new JTextField();
		expWeekStartField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {

					double price = Double.parseDouble(expWeekStartField.getText());
					apiObject.setStartWeekExp(price);

				} catch (Exception e) {
					popup("Set exp week price faild", e);
				}
			}
		});
		expWeekStartField.setHorizontalAlignment(SwingConstants.CENTER);
		expWeekStartField.setForeground(new Color(0, 51, 153));
		expWeekStartField.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		expWeekStartField.setColumns(10);
		expWeekStartField.setBorder(new LineBorder(Color.DARK_GRAY));
		expWeekStartField.setBackground(SystemColor.menu);
		expWeekStartField.setBounds(10, 120, 67, 22);
		panel_2.add(expWeekStartField);
		setting_panel.add(lblOptimiTimer);
		setting_panel.add(textField);
		setting_panel.add(lblPesimiTimer);
		setting_panel.add(textField_1);
		setting_panel.add(textField_2);
		setting_panel.add(lblBasketUp);
		setting_panel.add(lblBasketDown);
		setting_panel.add(textField_3);

		JLabel lblTableMiddleStrike = new JLabel("Table middle strike");
		lblTableMiddleStrike.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		lblTableMiddleStrike.setBounds(10, 173, 129, 21);
		setting_panel.add(lblTableMiddleStrike);

		textField_6 = new JTextField();
		textField_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int strike;
				try {
					strike = Integer.parseInt(arg0.getActionCommand());
					OptionsDataUpdater.updateStrikes(strike);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		textField_6.setHorizontalAlignment(SwingConstants.CENTER);
		textField_6.setForeground(new Color(0, 51, 153));
		textField_6.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		textField_6.setColumns(10);
		textField_6.setBorder(new LineBorder(Color.DARK_GRAY));
		textField_6.setBackground(SystemColor.menu);
		textField_6.setBounds(10, 200, 129, 22);
		setting_panel.add(textField_6);

		optimiMoveField = new JTextField();
		optimiMoveField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					apiObject.setOptimiMoveFromOutSide(Double.parseDouble(event.getActionCommand()));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		optimiMoveField.setHorizontalAlignment(SwingConstants.CENTER);
		optimiMoveField.setForeground(new Color(0, 51, 153));
		optimiMoveField.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		optimiMoveField.setColumns(10);
		optimiMoveField.setBorder(new LineBorder(Color.DARK_GRAY));
		optimiMoveField.setBackground(SystemColor.menu);
		optimiMoveField.setBounds(359, 38, 56, 22);
		setting_panel.add(optimiMoveField);

		JLabel lblOptimiMive = new JLabel("Optimi move");
		lblOptimiMive.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 12));
		lblOptimiMive.setBounds(359, 11, 71, 21);
		setting_panel.add(lblOptimiMive);

		JLabel lblPesimiMove = new JLabel("Pesimi move");
		lblPesimiMove.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 12));
		lblPesimiMove.setBounds(440, 11, 71, 21);
		setting_panel.add(lblPesimiMove);

		pesimiMoveField = new JTextField();
		pesimiMoveField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					apiObject.setPesimiMoveFromOutSide(Double.parseDouble(pesimiMoveField.getText()));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		pesimiMoveField.setHorizontalAlignment(SwingConstants.CENTER);
		pesimiMoveField.setForeground(new Color(0, 51, 153));
		pesimiMoveField.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		pesimiMoveField.setColumns(10);
		pesimiMoveField.setBorder(new LineBorder(Color.DARK_GRAY));
		pesimiMoveField.setBackground(SystemColor.menu);
		pesimiMoveField.setBounds(440, 38, 56, 22);
		setting_panel.add(pesimiMoveField);

		JLabel lblFutureCounter = new JLabel("Fut counter");
		lblFutureCounter.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 12));
		lblFutureCounter.setBounds(359, 65, 71, 21);
		setting_panel.add(lblFutureCounter);

		futureCounterField = new JTextField();
		futureCounterField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					apiObject.setFutureCounter(Integer.parseInt(arg0.getActionCommand()));
				} catch (Exception e) {
					popup(e.getMessage().toString(), e);
				}
			}
		});
		futureCounterField.setHorizontalAlignment(SwingConstants.CENTER);
		futureCounterField.setForeground(new Color(0, 51, 153));
		futureCounterField.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		futureCounterField.setColumns(10);
		futureCounterField.setBorder(new LineBorder(Color.DARK_GRAY));
		futureCounterField.setBackground(SystemColor.menu);
		futureCounterField.setBounds(359, 92, 56, 22);
		setting_panel.add(futureCounterField);

		JLabel lblIndCounter = new JLabel("Ind counter");
		lblIndCounter.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 12));
		lblIndCounter.setBounds(440, 65, 71, 21);
		setting_panel.add(lblIndCounter);

		indexCounterField = new JTextField();
		indexCounterField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					apiObject.setIndexCounter(Integer.parseInt(e.getActionCommand()));
				} catch (Exception e2) {
					popup(e2.getMessage().toString(), e2);
				}
			}
		});
		indexCounterField.setHorizontalAlignment(SwingConstants.CENTER);
		indexCounterField.setForeground(new Color(0, 51, 153));
		indexCounterField.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		indexCounterField.setColumns(10);
		indexCounterField.setBorder(new LineBorder(Color.DARK_GRAY));
		indexCounterField.setBackground(SystemColor.menu);
		indexCounterField.setBounds(440, 92, 56, 22);
		setting_panel.add(indexCounterField);

		JButton resetOptionsButton = new JButton("Reset options");
		resetOptionsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				for (Option option : apiObject.getOptionsMonth().getOptionsList()) {
					option.setBidAskCounter(0);
					option.setBidAskCalcCounter(0);
					option.getBidAskCalcCounterList().clear();
					option.getBidAskCounterList().clear();

				}

				Thread res = new Thread(() -> {
					try {
						String preText = resetOptionsButton.getText();

						resetOptionsButton.setText("Cleared");

						Thread.sleep(2000);

						resetOptionsButton.setText(preText);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});

				res.start();

			}
		});
		resetOptionsButton.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		resetOptionsButton.setBounds(527, 11, 134, 30);
		setting_panel.add(resetOptionsButton);

		btnLoadExpData = new JButton("Load exp data");
		btnLoadExpData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ExpTable exp = (ExpTable) HB.get_line_by_id(ExpTable.class, 1);
				apiObject.setFutureExpRaces(exp.getFuture_races());
				apiObject.setIndexExpRaces(exp.getIndex_races());
				apiObject.setStartExp(exp.getStart_exp());
				apiObject.setOptimiTimer(exp.getOptimi_timer());
				apiObject.setExpPesimiTimer(exp.getPesimi_timer());
				apiObject.setExpBasketUp(exp.getBasket_up());
				apiObject.setExpBasketDown(exp.getBasket_down());
			}
		});
		btnLoadExpData.setBounds(527, 52, 134, 30);
		setting_panel.add(btnLoadExpData);
		btnLoadExpData.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));

		btnDoExp = new JButton("Do exp");
		btnDoExp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				apiObject.setStartExp(apiObject.getOpen());
				apiObject.setFutureExpRaces(0);
				apiObject.setIndexExpRaces(0);
				apiObject.setExpBasketUp(0);
				apiObject.setExpBasketDown(0);

				// Set to db
				ExpTable exp = (ExpTable) HB.get_line_by_id(ExpTable.class, 1);
				exp.setStart_exp(apiObject.getOpen());
				exp.setFuture_races(0);
				exp.setIndex_races(0);
				exp.setBasket_up(0);
				exp.setBasket_down(0);

				HB.save(exp);
			}
		});
		btnDoExp.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		btnDoExp.setBounds(527, 91, 134, 30);
		setting_panel.add(btnDoExp);

		JButton btnResetAll = new JButton("Reset all");
		btnResetAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				// Options
				resetOptionsButton.doClick();

				// Races
				apiObject.setFuture_up(0);
				apiObject.setFuture_down(0);
				apiObject.setIndex_up(0);
				apiObject.setIndex_down(0);

				// Baskets
				apiObject.setBasketUp(0);
				apiObject.setBasketDown(0);

				// Timers op
				apiObject.setOptimiTimer(0);
				apiObject.setPesimiTimer(0);

				// Op move
				apiObject.setOptimiLiveMove(0);
				apiObject.setPesimiLiveMove(0);

				apiObject.getOptimiMoveList().clear();
				apiObject.getPesimiMoveList().clear();

				// Op avg
				apiObject.getOpList().clear();

				// Index and future bid ask counters
				apiObject.setIndexCounter(0);
				apiObject.getIndexBidAskCounterList().clear();
				apiObject.setFutureCounter(0);
				apiObject.getFutureBidAskCounterList().clear();
				apiObject.getIndexList().clear();

			}
		});
		btnResetAll.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		btnResetAll.setBounds(5, 278, 134, 30);
		setting_panel.add(btnResetAll);

		JButton btnResetDb = new JButton("Reset db");
		btnResetDb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				apiObject.getHbHandler().getStatusHandler().resetDataToHB();
				apiObject.getHbHandler().getArraysHandler().resetDataToHB();

				Arik.getInstance().sendMessage(Arik.sagivID, "Ta35 reset success", null);
			}
		});
		btnResetDb.setFont(new Font("Dubai Medium", Font.PLAIN, 11));
		btnResetDb.setBounds(577, 155, 84, 30);
		setting_panel.add(btnResetDb);
	}

	// Creating popup window alert
	private void popup(String message, Exception e) {
		JOptionPane.showMessageDialog(frame, message + "\n" + e.getMessage());
	}

	// Set the window visible
	public void setVisible() {
		frame.setVisible(true);
	}
}
