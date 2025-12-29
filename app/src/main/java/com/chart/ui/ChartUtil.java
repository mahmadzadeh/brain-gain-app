package com.chart.ui;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.util.DateUtil;

import java.util.Date;
import java.util.List;

public class ChartUtil {
    public static final int CHART_COLOR = Color.rgb( 18, 48, 90 ); // colorPrimaryDark #12305A

    public static LineDataSet dataSetForYAxis( List<Entry> entries ) {
        LineDataSet dataSet = new LineDataSet( entries, "Data Sets" );

        dataSet.enableDashedLine( 10f, 5f, 0f );
        dataSet.enableDashedHighlightLine( 10f, 5f, 0f );
        dataSet.setColor( CHART_COLOR );
        dataSet.setCircleColor( CHART_COLOR );
        dataSet.setLineWidth( 1f );
        dataSet.setCircleRadius( 3f );
        dataSet.setDrawCircleHole( false );
        dataSet.setValueTextSize( 9f );
        dataSet.setDrawFilled( true );
        dataSet.setFillColor( CHART_COLOR );
        dataSet.setValueTextColor( Color.WHITE );

        return dataSet;
    }

    public static final String SCORE_DATA_DESC = "Score Data";

    public static void setUpChart( LineChart chart, LineData data ) {
        Description desc = new Description();
        desc.setEnabled( false );
        chart.setDescription( desc );

        chart.getAxisLeft().setTextColor( Color.WHITE );
        chart.getAxisRight().setTextColor( Color.WHITE );
        chart.getXAxis().setTextColor( Color.WHITE );
        chart.setData( data );

        XAxis yAxis = chart.getXAxis();
        yAxis.setValueFormatter( new ValueFormatter() {
            @Override
            public String getFormattedValue( float value ) {
                return DateUtil.formatForChartUI( new Date( ( long ) value ) );
            }
        } );
    }


}
