package com.chart.ui;


import android.util.Log;

import com.chart.filesystem.dao.Dao;
import com.chart.filesystem.dao.DataFileUtil;
import com.chart.filesystem.dao.DataPoint;
import com.chart.filesystem.dao.DataPointCollection;
import com.chart.filesystem.dao.FileBasedDao;
import com.chart.filesystem.io.FileIO;
import com.chart.filesystem.util.FileUtil;
import com.github.mikephil.charting.data.Entry;

import java.io.File;
import java.util.List;

import static com.chart.filesystem.dao.DataDtoConversion.convertToChartData;

public class ChartModel {
    private final File directory;
    private final Dao dao;
    private DataPointCollection dataPointCollection;

    public ChartModel( File filesDirectory ) {
        directory = filesDirectory;

        File file = FileUtil.getDataFile( directory );

        dao = new FileBasedDao( new FileIO( file ) );

        dataPointCollection = DataFileUtil.readAllDataSortedByDate( directory );
    }

    public void addDataPoint( DataPoint newDataPoint ) {

        Log.e( "ChartModel", "ChartModel dataPointCollection.size() " + dataPointCollection.size() );

        dataPointCollection.addDataPoint( newDataPoint );
    }

    public List<Entry> chartData( ) {
        return convertToChartData( dataPointCollection );
    }

    public void saveData( ) {
        dao.write( dataPointCollection.shrinkDataSize() );
    }

}
