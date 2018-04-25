package org.charts.dataviewer.javafx.example.factory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.charts.dataviewer.api.config.DataViewerConfiguration;
import org.charts.dataviewer.api.data.PlotData;
import org.charts.dataviewer.api.trace.LineTrace;
import org.charts.dataviewer.javafx.JavaFxDataViewer;
import org.charts.dataviewer.utils.AxisType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaFxDataViewerFactory {

	private final static Logger log = LoggerFactory.getLogger(JavaFxDataViewerFactory.class);

	private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);

	private static int index = 0;

	public static JavaFxDataViewer createDataViewerExample1() {

		JavaFxDataViewer dataviewer = new JavaFxDataViewer();

		DataViewerConfiguration config = new DataViewerConfiguration();
		config.setPlotTitle("Line Trace Example");
		config.setxAxisTitle("X Example 1");
		config.setyAxisTitle("Y Example 1");
		config.showLegend(true);
		config.setLegendInsidePlot(false);
		dataviewer.updateConfiguration(config);

		PlotData plotData = new PlotData();

		plotData.addTrace(TraceFactory.createLineTrace());
		plotData.addTrace(TraceFactory.createLineTraceWithConfig());

		dataviewer.updatePlot(plotData);

		return dataviewer;
	}

	public static JavaFxDataViewer createDataViewerExample2() {

		JavaFxDataViewer dataviewer = new JavaFxDataViewer();

		DataViewerConfiguration config = new DataViewerConfiguration();
		config.setPlotTitle("Multiple Trace Example");
		config.setxAxisTitle("X Example 2");
		config.setyAxisTitle("Y Example 2");
		config.showLegend(false);
		dataviewer.updateConfiguration(config);

		PlotData plotData = new PlotData();

		plotData.addTrace(TraceFactory.createBarTrace());
		plotData.addTrace(TraceFactory.createLineTrace());
		plotData.addTrace(TraceFactory.createScatterTraceWithConfig());

		dataviewer.updatePlot(plotData);
		return dataviewer;
	}

	public static JavaFxDataViewer createDataViewerExample3() {
		JavaFxDataViewer dataviewer = new JavaFxDataViewer();

		DataViewerConfiguration config = new DataViewerConfiguration();
		config.setPlotTitle("Bar Trace Example");
		config.setxAxisTitle("X Example 3");
		config.setyAxisTitle("Y Example 3");
		config.showLegend(false);
		dataviewer.updateConfiguration(config);

		PlotData plotData = new PlotData();

		plotData.addTrace(TraceFactory.createBarTrace());
		plotData.addTrace(TraceFactory.createBarTrace());
		plotData.addTrace(TraceFactory.createBarTrace());

		dataviewer.updatePlot(plotData);
		return dataviewer;
	}

	public static JavaFxDataViewer createDataViewerExample4() {
		JavaFxDataViewer dataviewer = new JavaFxDataViewer();

		DataViewerConfiguration config = new DataViewerConfiguration();
		config.setPlotTitle("Scatter Trace Example");
		config.setxAxisTitle("X Example 4");
		config.setyAxisTitle("Y Example 4");
		config.showLegend(false);
		dataviewer.updateConfiguration(config);

		PlotData plotData = new PlotData();

		plotData.addTrace(TraceFactory.createScatterTraceWithConfig());
		plotData.addTrace(TraceFactory.createScatterTrace());

		dataviewer.updatePlot(plotData);
		return dataviewer;
	}

	public static synchronized JavaFxDataViewer createDataViewerTuneExample(int caseIdx) {

		String csvFile = "";
		String plotTitle = "";
		switch (caseIdx) {
		case 1:
			csvFile = JavaFxDataViewerFactory.class.getResource("/files/test_file1.csv").getPath();
			plotTitle = "Test 1a data";
			break;
		case 2:
			csvFile = JavaFxDataViewerFactory.class.getResource("/files/test_file2.csv").getPath();
			plotTitle = "Test 1b data";
			break;
		case 3:
			csvFile = JavaFxDataViewerFactory.class.getResource("/files/test_file3.csv").getPath();
			plotTitle = "Test 2a data";
			break;
		case 4:
			csvFile = JavaFxDataViewerFactory.class.getResource("/files/test_file4.csv").getPath();
			plotTitle = "Test 2b data";
			break;
		default:
			log.error("Invalid input for createDataViewerTuneExample(int caseIdx)");
			return null;
		}

		List<Double[]> parsedTuneDataDouble = new ArrayList<>();

		BufferedReader bufferReader = null;
		String line = "";
		try {
			bufferReader = new BufferedReader(new FileReader(csvFile));
			while ((line = bufferReader.readLine()) != null) {
				String[] field = line.split(",");
				Double[] tuneDataRow = new Double[512];
				if (field.length == 513) {
					for (int i = 1; i < field.length; i++) {
						tuneDataRow[i - 1] = Double.parseDouble(field[i]);
					}
					parsedTuneDataDouble.add(tuneDataRow);
				}
			}
		} catch (FileNotFoundException ex) {
			log.error("FileNotFoundException", ex);
		} catch (IOException ex) {
			log.error("IOException", ex);
		} finally {
			if (bufferReader != null) {
				try {
					bufferReader.close();
				} catch (IOException ex) {
					log.error("IOException", ex);
				}
			}
		}

		Double[] exampleFrequency = new Double[512];
		for (int i = 0; i < 512; i++) {
			exampleFrequency[i] = (double) 11000 / (exampleFrequency.length * 2) * i;
		}

		JavaFxDataViewer dataviewer = new JavaFxDataViewer();

		log.info("dataviewer id is : {}", dataviewer.getId());

		DataViewerConfiguration config = new DataViewerConfiguration();
		config.setPlotTitle(plotTitle);
		config.setxAxisTitle("Frequency(Hz)");
		config.setyAxisTitle("Data");
		config.showLegend(true);
		config.setyRange(-160.0, -50.0);
		dataviewer.updateConfiguration(config);

		PlotData plotData = new PlotData();
		LineTrace<Double> bigTrace = new LineTrace<>();
		bigTrace.setTraceName("example trace");
		bigTrace.setxArray(exampleFrequency);

		plotData.addTrace(bigTrace);

		dataviewer.updatePlot(plotData);

		executor.scheduleAtFixedRate(
				() -> JavaFxDataViewerFactory.updateTune(dataviewer, bigTrace, parsedTuneDataDouble, plotData), 5000,
				150, TimeUnit.MILLISECONDS);

		return dataviewer;

	}

	private static void updateTune(JavaFxDataViewer dataviewer, LineTrace<Double> tuneTrace, List<Double[]> tuneData,
			PlotData plotData) {
		if (index == tuneData.size())
			index = 0;
		tuneTrace.setyArray(tuneData.get(index++));
		PlotData plot = new PlotData();
		plot.addTrace(tuneTrace);
		dataviewer.updatePlot(plot);
	}

	public static JavaFxDataViewer createDataViewerExample5() {

		JavaFxDataViewer dataviewer = new JavaFxDataViewer();

		DataViewerConfiguration config = new DataViewerConfiguration();
		config.setPlotTitle("Hybrid Log Trace Example");
		config.setxAxisTitle("X Example 5");
		config.setyAxisTitle("Y Example 5");
		config.showLegend(true);
		config.setLegendInsidePlot(true);
		config.setyAxisType(AxisType.LOG);
		config.setxAxisType(AxisType.LOG);

		dataviewer.updateConfiguration(config);

		PlotData plotData = new PlotData();
		plotData.addTrace(TraceFactory.createLineTrace());
		plotData.addTrace(TraceFactory.createScatterTraceWithConfig());

		dataviewer.updatePlot(plotData);

		return dataviewer;
	}

	public static JavaFxDataViewer createDataViewerExample6() {

		JavaFxDataViewer dataviewer = new JavaFxDataViewer();

		DataViewerConfiguration config = new DataViewerConfiguration();
		config.setPlotTitle("TimeSeries Trace Example");
		config.setxAxisTitle("X Example 6");
		config.setyAxisTitle("Y Example 6");
		config.showLegend(true);
		config.setLegendInsidePlot(true);
		dataviewer.updateConfiguration(config);

		PlotData plotData = new PlotData();
		plotData.addTrace(TraceFactory.createTimeSeriesTrace());
		dataviewer.updatePlot(plotData);

		return dataviewer;
	}

	public static JavaFxDataViewer createDataViewerExample7() {

		JavaFxDataViewer dataviewer = new JavaFxDataViewer();

		DataViewerConfiguration config = new DataViewerConfiguration();
		config.setPlotTitle("Contour Example");
		config.setxAxisTitle("X Example 7");
		config.setyAxisTitle("Y Example 7");
		dataviewer.updateConfiguration(config);

		PlotData plotData = new PlotData();
		plotData.addTrace(TraceFactory.createContourTrace());
		dataviewer.updatePlot(plotData);

		return dataviewer;
	}

	public static JavaFxDataViewer createDataViewerExample8() {

		JavaFxDataViewer dataviewer = new JavaFxDataViewer();

		DataViewerConfiguration config = new DataViewerConfiguration();
		config.setPlotTitle("Histogram Example");
		config.setxAxisTitle("X Example 8");
		config.setyAxisTitle("Y Example 8");
		dataviewer.updateConfiguration(config);

		PlotData plotData = new PlotData();
		plotData.addTrace(TraceFactory.createHistogramTrace());
		dataviewer.updatePlot(plotData);

		return dataviewer;
	}

	public static JavaFxDataViewer createDataViewerExample9() {

		JavaFxDataViewer dataviewer = new JavaFxDataViewer();

		DataViewerConfiguration config = new DataViewerConfiguration();
		config.setPlotTitle("Density Example");
		config.setxAxisTitle("X Example 9");
		config.setyAxisTitle("Y Example 9");
		config.setxRange2(0.85, 1);
		config.setyRange2(0.85, 1);
		config.showLegend(false);
		dataviewer.updateConfiguration(config);

		PlotData plotData = new PlotData();
		plotData.addAll(TraceFactory.createDensityTrace());
		dataviewer.updatePlot(plotData);

		return dataviewer;
	}

}
