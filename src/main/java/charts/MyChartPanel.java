package charts;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

public class MyChartPanel extends ChartPanel {

	JLabel highLbl;
	JLabel lowLbl;
	JLabel lastLbl;

	public MyChartPanel(JFreeChart chart, boolean includeTicker) {
		super(chart);
		setLayout(null);
		
		if (includeTicker) {
			
			add(createLbl("Last", Color.BLACK, 20, 0));
			lastLbl = createLbl("Last", Color.BLACK, 20, 20);
			add(lastLbl);
			add(createLbl("High", Color.BLACK, 70, 0));
			highLbl = createLbl("High", Color.BLACK, 70, 20);
			add(highLbl);
			add(createLbl("Low", Color.BLACK, 120, 0));
			lowLbl = createLbl("Low", Color.BLACK, 120, 20);
			add(lowLbl);
		}
	}

	// Create basic lbl
	public JLabel createLbl(String name, Color color, int x, int y) {
		final JLabel lbl = new JLabel(name);
		lbl.setBounds(x, y, 50, 50);
		lbl.setFont(new Font("Arial", Font.BOLD, 14));
		return lbl;
	}

}
