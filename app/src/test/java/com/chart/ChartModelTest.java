package com.chart;

import com.chart.filesystem.dao.DataPoint;
import com.chart.ui.ChartModel;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;


public class ChartModelTest {

    private static final String TEST_RESOURCES_DIR = "src/test/resources";
    private ChartModel chartModel;

    @Before
    public void setUp( ) {
        chartModel = new ChartModel( new File( TEST_RESOURCES_DIR ) );
    }

    @Test
    public void getDataPoints( ) {
        assertThat( chartModel.chartData().size() ).isEqualTo( 0 );
    }

    @Test
    public void addingSingleDataPoint( ) {
        DataPoint dataPoint = new DataPoint( new Date(), 30 );

        assertThat( chartModel.chartData().size() ).isEqualTo( 0 );

        chartModel.addDataPoint( dataPoint );

        assertThat( chartModel.chartData().size() ).isEqualTo( 1 );
    }

    @Test
    public void chartData( ) {
    }

    @Test
    public void saveData( ) throws Exception {
    }

}