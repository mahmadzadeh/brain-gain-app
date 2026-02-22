package com.chart;

import com.chart.filesystem.dao.GameKey;
import com.chart.ui.ChartModel;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;


public class ChartModelTest {

    private static final String TEST_RESOURCES_DIR = "src/test/resources";
    private ChartModel chartModel;

    @Before
    public void setUp( ) {
        chartModel = new ChartModel( new File( TEST_RESOURCES_DIR ), GameKey.DUAL_N_BACK );
    }

    @Test
    public void getDataPoints( ) {
        assertThat( chartModel.chartData().size() ).isEqualTo( 0 );
    }

    @Test
    public void chartData( ) {
    }

    @Test
    public void saveData( ) throws Exception {
    }

}