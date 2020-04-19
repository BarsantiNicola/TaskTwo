package graphicInterface;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
			boolean isLegendPresent,  boolean canGenerateTooltips, boolean canGenerateURLs, String orderBy ) {
		
		super(buildAreaChart(title,categoryAxisLabel,valueAxisLabel,data,orientation,isLegendPresent,canGenerateTooltips,canGenerateURLs,orderBy));
	}
	
	private static JFreeChart buildAreaChart(String title, String categoryAxisLabel, String valueAxisLabel, HashMap<String,Double> data, String orientation, 
			boolean isLegendPresent,  boolean canGenerateTooltips, boolean canGenerateURLs, String orderBy) {
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		LinkedHashMap<String,Double> sortedMap = new LinkedHashMap<>();
		
		if( orderBy.equals("valueDesc") ) {
			
			data.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
		}
		
		if( orderBy.equals("valueAsc") ) {
			
			data.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
		}
		
		if( orderBy.equals("keyDesc") ) {
			
			data.entrySet().stream().sorted(Map.Entry.comparingByKey(Comparator.reverseOrder())).forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
		}
		
		if( orderBy.equals("keyAsc") ) {
			
			data.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
		}
		
		for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {
		    String key = entry.getKey();
		    double value = entry.getValue();
		  
		    dataset.addValue(value, key, key);
		}
		
		PlotOrientation chartOrientation = null;
		
		if( orientation.equals("H") ) {
			
			chartOrientation = PlotOrientation.HORIZONTAL;
		} else if( orientation.equals("V")) {
			
			chartOrientation = PlotOrientation.VERTICAL;
		}
		
		JFreeChart chart = ChartFactory.createAreaChart(title, categoryAxisLabel, valueAxisLabel, dataset, chartOrientation, 
				isLegendPresent, canGenerateTooltips, canGenerateURLs);
		
		return chart;
	}
}
