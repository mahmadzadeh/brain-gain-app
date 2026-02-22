package com.chart.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.chart.filesystem.dao.DataDtoConversion;
import com.chart.filesystem.dao.DataPoint;
import com.chart.filesystem.dao.DataPointCollection;
import com.github.mikephil.charting.data.Entry;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DataPointCollectionConversionTest {

    @Test
    public void givenEmptyDataDtoThenConvertToChartDataReturnsEmptyChartData( ) {

        DataPointCollection dataPointCollection = new DataPointCollection( new ArrayList<>() );

        List<Entry> chartData = DataDtoConversion.convertToChartData( dataPointCollection );

        assertEquals( 0, chartData.size() );
        assertEquals( 0, chartData.size() );
    }

    @Test
    public void givenDataDtoWithOneDataPointThenConvertToChartDataReturnsChartDataWithOneXAndYValue( ) {

        ArrayList<DataPoint> dataPoints = new ArrayList<>();
        dataPoints.add( new DataPoint( new Date( 1461478244180l ), 14 ) );

        DataPointCollection dataPointCollection = new DataPointCollection( dataPoints );

        List<Entry> chartData  = DataDtoConversion.convertToChartData( dataPointCollection );

        Entry entry = chartData.get( 0 );

        assertTrue( new Entry( 1f, 14f ).equalTo( entry ) );
    }

}