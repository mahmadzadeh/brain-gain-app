package com.chart.ui;

import android.os.Bundle;
import android.widget.Button;

import com.chart.filesystem.dao.DataPoint;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.monkeyladder.R;
import com.monkeyladder.ui.mainscreen.StartScreenActivityIntentUtil;
import com.util.IntentUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import static com.chart.ui.ChartUtil.dataSetForYAxis;
import static com.chart.ui.ChartUtil.setUpChart;


public class ChartActivity extends AppCompatActivity implements ChartView {

    private ChartPresenter presenter;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        presenter = new ChartPresenter( this );

        setContentView( R.layout.monkey_ladder_chart_screen );

        LineChart lineChart = findViewById( R.id.line_chart );

        DataPoint lastDataPoint = IntentUtility.extractDatePointFromExtras( getIntent().getExtras() );

        presenter.addDataPoint( lastDataPoint );

        setData( lineChart );

        Button continueButton = findViewById( R.id.chart_continue );

        continueButton.setOnClickListener( v -> {
            presenter.saveData();
            StartScreenActivityIntentUtil.backToStartScreen( v, ChartActivity.this );
        } );
    }

    private void setData( LineChart mChart ) {

        List<Entry> entries = presenter.convertToChartData();

        LineDataSet dataSet = dataSetForYAxis( entries );

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add( dataSet );

        setUpChart( mChart, new LineData( dataSets ) );
    }

    @Override
    public File getFilesDirectory( ) {
        return this.getFilesDir();
    }
}
