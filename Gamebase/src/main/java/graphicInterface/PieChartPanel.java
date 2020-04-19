package graphicInterface;

import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class PieChartPanel extends ChartPanel {
	
	public PieChartPanel() {
		super(null);
	}
	
	public PieChartPanel( String title, String categoryAxisLabel, String valueAxisLabel, HashMap<String,Double> data, String orientation, 
			boolean isLegendPresent,  boolean canGenerateTooltips, boolean canGenerateURLs ) {
		
		super(buildPieChart(title,categoryAxisLabel,valueAxisLabel,data,orientation,isLegendPresent,canGenerateTooltips,canGenerateURLs));
	}
	
	private static JFreeChart buildPieChart(String title, String categoryAxisLabel, String valueAxisLabel, HashMap<String,Double> data, String orientation, 
			boolean isLegendPresent,  boolean canGenerateTooltips, boolean canGenerateURLs) {
		
		DefaultKeyedValues values = new DefaultKeyedValues();
		
		for (Map.Entry<String, Double> entry : data.entrySet()) {
		    String key = entry.getKey();
		    double value = entry.getValue();
		  
		    values.addValue(key, value);
		}
		
		DefaultPieDataset dataset = new DefaultPieDataset(values);
		
		PlotOrientation chartOrientation = null;
		
		if( orientation.equals("H") ) {
			
			chartOrientation = PlotOrientation.HORIZONTAL;
		} else if( orientation.equals("V")) {
			
			chartOrientation = PlotOrientation.VERTICAL;
		}
		
		//can also use 3d
		JFreeChart chart = ChartFactory.createPieChart(title, dataset, 
				isLegendPresent, canGenerateTooltips, canGenerateURLs);

		return chart;
	}
}
