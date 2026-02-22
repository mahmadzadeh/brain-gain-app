package com.chart.ui;


import static com.chart.filesystem.dao.DataDtoConversion.convertToChartData;

import com.chart.filesystem.dao.DataPointCollection;
import com.chart.filesystem.dao.GameKey;
import com.chart.filesystem.dao.GameStatsRepository;
import com.github.mikephil.charting.data.Entry;

import java.io.File;
import java.util.List;

public class ChartModel {
    private final GameStatsRepository repository;
    private final GameKey gameKey;
    private DataPointCollection dataPointCollection;

    public ChartModel( File filesDirectory, GameKey gameKey ) {
        this.repository = GameStatsRepository.create( filesDirectory );
        this.gameKey = gameKey;
        this.dataPointCollection = repository.read( gameKey );
    }

    public List<Entry> chartData( ) {
        return convertToChartData( dataPointCollection );
    }

    public void saveData( ) {
        repository.write( gameKey, dataPointCollection.shrinkDataSize() );
    }

}
