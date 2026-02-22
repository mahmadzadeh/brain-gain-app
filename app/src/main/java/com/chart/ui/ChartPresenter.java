package com.chart.ui;

import com.chart.filesystem.dao.GameKey;
import com.github.mikephil.charting.data.Entry;

import java.util.List;

public class ChartPresenter implements IChartPresenter {
    private final ChartModel model;
    private final ChartView view;

    public ChartPresenter( ChartView chartView, GameKey gameKey ) {
        this.view = chartView;
        this.model = new ChartModel( view.getFilesDirectory(), gameKey );
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
