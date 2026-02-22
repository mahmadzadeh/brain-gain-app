package com.chart.ui;

import com.github.mikephil.charting.data.Entry;

import java.util.List;

interface IChartPresenter {

    void onCreate( );

    void onPause( );

    void onResume( );

    void onDestroy( );

    void saveData( );

    List<Entry> convertToChartData( );
}
