package graphicInterface;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;

public class XYChartPanel extends ChartPanel{
		
	public XYChartPanel() {
		
		super(null);
	}
	
	public XYChartPanel( String title, String xAxisLabel, String yAxisLabel, double[][] data, String orientation /*H or V*/, 
			boolean isLegendPresent, boolean canGenerateTooltips, boolean canGenerateURLs  ) {
		
		super(buildXYChart(title,xAxisLabel,yAxisLabel,data,orientation,isLegendPresent,canGenerateTooltips,canGenerateURLs));
		
	}
	
	private static JFreeChart buildXYChart(String title, String xAxisLabel, String yAxisLabel, double[][] data, String orientation /*H or V*/, 
			boolean isLegendPresent, boolean canGenerateTooltips, boolean canGenerateURLs) {
		DefaultXYDataset dataset = new DefaultXYDataset();
		dataset.addSeries("Series1", data);
		
		PlotOrientation chartOrientation = null;
		
		if( orientation.equals("H") ) {
			
			chartOrientation = PlotOrientation.HORIZONTAL;
		} else if( orientation.equals("V")) {
			
			chartOrientation = PlotOrientation.VERTICAL;
		}
		
		JFreeChart XYChart = ChartFactory.createXYLineChart(title, xAxisLabel, yAxisLabel, dataset, chartOrientation, isLegendPresent, canGenerateTooltips, canGenerateURLs);
		
		return XYChart;
	}

}
