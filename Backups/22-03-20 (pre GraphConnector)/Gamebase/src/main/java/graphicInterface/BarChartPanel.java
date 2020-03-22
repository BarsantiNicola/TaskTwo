package graphicInterface;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import java.util.HashMap;
import java.util.Map;

public class BarChartPanel extends ChartPanel {
	
	public BarChartPanel() {
		super(null);
	}
	
	public BarChartPanel( String title, String categoryAxisLabel, String valueAxisLabel, HashMap<String,Double> data, String orientation, 
			boolean isLegendPresent,  boolean canGenerateTooltips, boolean canGenerateURLs ) {
		
		super(buildBarChart(title,categoryAxisLabel,valueAxisLabel,data,orientation,isLegendPresent,canGenerateTooltips,canGenerateURLs));
	}
	
	private static JFreeChart buildBarChart(String title, String categoryAxisLabel, String valueAxisLabel, HashMap<String,Double> data, String orientation, 
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
		
		JFreeChart chart = ChartFactory.createBarChart(title, categoryAxisLabel, valueAxisLabel, dataset, chartOrientation, 
				isLegendPresent, canGenerateTooltips, canGenerateURLs);
		
		return chart;
	}

}
