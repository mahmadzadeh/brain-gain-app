package com.chart.ui;

import com.chart.filesystem.dao.DataPoint;
import com.github.mikephil.charting.data.Entry;

import java.util.List;

public class ChartPresenter implements IChartPresenter {
    private final ChartModel model;
    private final ChartView view;

    public ChartPresenter( ChartView chartView ) {
        this.view = chartView;
        this.model = new ChartModel( view.getFilesDirectory() );
    }

    @Override
    public void addDataPoint( DataPoint dataPoint ) {
        this.model.addDataPoint( dataPoint );
    }

    @Override
    public List<Entry> convertToChartData( ) {
        return model.chartData();
    }

    @Override
    public void onCreate( ) {

    }

    @Override
    public void onPause( ) {

    }

    @Override
    public void onResume( ) {

    }

    @Override
    public void onDestroy( ) {

    }

    @Override
    public void saveData( ) {
        model.saveData();
    }
}
