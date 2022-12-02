package com.chart.ui;

import com.chart.filesystem.dao.DataPoint;
import com.github.mikephil.charting.data.Entry;

import java.util.List;

interface IChartPresenter {

    void onCreate( );

    void onPause( );

    void onResume( );

    void onDestroy( );

    void saveData( );

    void addDataPoint( DataPoint lastDataPoint );

    List<Entry> convertToChartData( );
}
