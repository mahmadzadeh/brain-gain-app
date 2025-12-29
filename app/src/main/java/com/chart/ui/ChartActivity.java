package com.chart.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

        // Set icon and title dynamically
        ImageView statsIcon = findViewById( R.id.statsIcon );
        int iconResId = getIntent().getIntExtra( com.mainscreen.ui.continuescreen.ContinueActivity.EXTRA_ICON_RES_ID, R.drawable.monkey_ladder_icon );
        statsIcon.setImageResource( iconResId );

        TextView statsTitle = findViewById( R.id.statsTitle );
        statsTitle.setText( "Performance Stats" );

        LineChart lineChart = findViewById( R.id.line_chart );

        DataPoint lastDataPoint = IntentUtility.extractDatePointFromExtras( getIntent().getExtras() );

        presenter.addDataPoint( lastDataPoint );

        setData( lineChart );

        Button continueButton = findViewById( R.id.chart_continue );
        Button playAgainButton = findViewById( R.id.statsPlayAgain );

        continueButton.setOnClickListener( v -> {
            presenter.saveData();
            finish();
            startActivity( new android.content.Intent( this, com.mainscreen.ui.GameSelectionActivity.class ) );
        } );

        String replayActivity = getIntent().getStringExtra( com.mainscreen.ui.continuescreen.ContinueActivity.EXTRA_REPLAY_ACTIVITY );
        playAgainButton.setOnClickListener( v -> {
            presenter.saveData();
            finish();
            try {
                Class<?> activityClass = Class.forName( replayActivity );
                startActivity( new android.content.Intent( this, activityClass ) );
            } catch ( ClassNotFoundException e ) {
                e.printStackTrace();
            }
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
