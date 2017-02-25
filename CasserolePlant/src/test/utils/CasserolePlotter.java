package test.utils;

import java.util.ArrayList;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class CasserolePlotter {

	double[] time;
	ArrayList<XYPlot> subplots = new ArrayList<XYPlot>(3);
	
	public CasserolePlotter(double[] time_in){
		time = time_in;
	}
	
	public void addGraph(String units, CasseroleSimSignal... signals){
		final XYSeriesCollection collection = new XYSeriesCollection();
		
		for(CasseroleSimSignal signal : signals){
			XYSeries newSeries = new XYSeries(signal.name + "(" + signal.units + ")");
			for(int idx = 0; idx < time.length; idx++){
				newSeries.add(signal.time[idx], signal.data[idx]);
			}
			collection.addSeries(newSeries);
		}
		
		final XYDataset data1 = collection;
		final XYItemRenderer renderer1 = new StandardXYItemRenderer();
		final NumberAxis rangeAxis1 = new NumberAxis(units);
		final XYPlot subplot = new XYPlot(data1, null, rangeAxis1, renderer1);
		subplots.add(subplot);
	}
	
	public void displayPlot(){
		
        // parent plot...
        final CombinedDomainXYPlot plot = new CombinedDomainXYPlot(new NumberAxis("Time (s)"));
        plot.setGap(10.0);
        
        // add the subplots...
        for(XYPlot sp : subplots){
        	plot.add(sp);
        }
        plot.setOrientation(PlotOrientation.VERTICAL);

        // return a new chart containing the overlaid plot...
        JFreeChart combinedChart =  new JFreeChart("SimResults",
                                          JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        final ChartPanel panel = new ChartPanel(combinedChart, true, true, true, false, true);
        panel.setPreferredSize(new java.awt.Dimension(800, 600));
	    ApplicationFrame viewer = new ApplicationFrame("Sim Results");
	    viewer.setContentPane(panel);
	    viewer.setSize(800, 600);
	    viewer.setVisible(true);
		 
	}
}
