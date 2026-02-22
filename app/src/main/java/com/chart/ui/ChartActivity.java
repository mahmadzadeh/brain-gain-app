package com.chart.ui;

import static com.chart.ui.ChartUtil.dataSetForYAxis;
import static com.chart.ui.ChartUtil.setUpChart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.chart.filesystem.dao.GameKey;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.mainscreen.ui.GameSelectionActivity;
import com.mainscreen.ui.continuescreen.ContinueActivity;
import com.monkeyladder.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ChartActivity extends AppCompatActivity implements ChartView {

    private ChartPresenter presenter;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        String gameKeyName = getIntent().getStringExtra( ContinueActivity.EXTRA_GAME_KEY );
        GameKey gameKey = gameKeyName != null ? GameKey.valueOf( gameKeyName ) : GameKey.DUAL_N_BACK;

        presenter = new ChartPresenter( this, gameKey );

        setContentView( R.layout.monkey_ladder_chart_screen );

        // Set icon and title dynamically
        ImageView statsIcon = findViewById( R.id.statsIcon );
        int iconResId = getIntent().getIntExtra( ContinueActivity.EXTRA_ICON_RES_ID, R.drawable.monkey_ladder_icon );
        statsIcon.setImageResource( iconResId );

        TextView statsTitle = findViewById( R.id.statsTitle );
        statsTitle.setText( "Performance Stats" );

        LineChart lineChart = findViewById( R.id.line_chart );

        setData( lineChart );

        Button continueButton = findViewById( R.id.chart_continue );
        Button playAgainButton = findViewById( R.id.statsPlayAgain );

        continueButton.setOnClickListener( v -> {
            finish();
            startActivity( new Intent( this, GameSelectionActivity.class ) );
        } );

        String replayActivity = getIntent().getStringExtra( ContinueActivity.EXTRA_REPLAY_ACTIVITY );
        playAgainButton.setOnClickListener( v -> {
            finish();
            try {
                Class<?> activityClass = Class.forName( replayActivity );
                startActivity( new Intent( this, activityClass ) );
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
