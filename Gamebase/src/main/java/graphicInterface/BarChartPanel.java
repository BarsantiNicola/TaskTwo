package graphicInterface;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BarChartPanel extends ChartPanel {
	
	public BarChartPanel() {
		super(null);
	}
	
	public BarChartPanel( String title, String categoryAxisLabel, String valueAxisLabel, HashMap<String,Double> data, String orientation, 
			boolean isLegendPresent,  boolean canGenerateTooltips, boolean canGenerateURLs, String orderBy) {
		
		super(buildBarChart(title,categoryAxisLabel,valueAxisLabel,data,orientation,isLegendPresent,canGenerateTooltips,canGenerateURLs,orderBy));
	}
	
	private static JFreeChart buildBarChart(String title, String categoryAxisLabel, String valueAxisLabel, HashMap<String,Double> data, String orientation, 
			boolean isLegendPresent,  boolean canGenerateTooltips, boolean canGenerateURLs, String orderBy) {
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		LinkedHashMap<String,Double> sortedMap = new LinkedHashMap<>();
		
		if( orderBy == "valueDesc" ) {
			
			data.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
		}
		
		if( orderBy == "valueAsc" ) {
			
			data.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
		}
		
		if( orderBy == "keyDesc" ) {
			
			data.entrySet().stream().sorted(Map.Entry.comparingByKey(Comparator.reverseOrder())).forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
		}
		
		if( orderBy == "keyAsc" ) {
			
			data.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
		}
		
		for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {
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
