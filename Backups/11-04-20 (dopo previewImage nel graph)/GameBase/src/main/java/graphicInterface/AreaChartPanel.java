package graphicInterface;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class AreaChartPanel extends ChartPanel {
	
	public AreaChartPanel() {
		super(null);
	}
	
	public AreaChartPanel( String title, String categoryAxisLabel, String valueAxisLabel, HashMap<String,Double> data, String orientation, 
			boolean isLegendPresent,  boolean canGenerateTooltips, boolean canGenerateURLs ) {
		
		super(buildAreaChart(title,categoryAxisLabel,valueAxisLabel,data,orientation,isLegendPresent,canGenerateTooltips,canGenerateURLs));
	}
	
	private static JFreeChart buildAreaChart(String title, String categoryAxisLabel, String valueAxisLabel, HashMap<String,Double> data, String orientation, 
			boolean isLegendPresent,  boolean canGenerateTooltips, boolean canGenerateURLs) {
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (Map.Entry<String, Double> entry : data.entrySet()) {
		    String key = entry.getKey();
		    double value = entry.getValue();
		  
		    dataset.addValue(value, key, key);
		}
		
		PlotOrientation chartOrientation = null;
		
		if( orientation == "H" ) {
			
			chartOrientation = PlotOrientation.HORIZONTAL;
		} else if( orientation == "V") {
			
			chartOrientation = PlotOrientation.VERTICAL;
		}
		
		JFreeChart chart = ChartFactory.createAreaChart(title, categoryAxisLabel, valueAxisLabel, dataset, chartOrientation, 
				isLegendPresent, canGenerateTooltips, canGenerateURLs);
		
		return chart;
	}
}
