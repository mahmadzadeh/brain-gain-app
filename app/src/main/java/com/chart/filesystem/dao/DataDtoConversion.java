package com.chart.filesystem.dao;


import com.github.mikephil.charting.data.Entry;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class DataDtoConversion {
    public static List<Entry> convertToChartData( DataPointCollection dataPointCollection ) {
        AtomicInteger index = new AtomicInteger( 1 );

        return dataPointCollection
                .sortedDataPoints()
                .userDataPoints()
                .stream()
                .map( dataPoint -> new Entry( index.getAndIncrement(), dataPoint.score() ) )
                .collect( Collectors.toList() );
    }
}
