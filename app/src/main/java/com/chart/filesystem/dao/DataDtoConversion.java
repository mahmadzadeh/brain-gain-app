package com.chart.filesystem.dao;


import com.github.mikephil.charting.data.Entry;

import java.util.List;
import java.util.stream.Collectors;


public class DataDtoConversion {
    public static List<Entry> convertToChartData( DataPointCollection dataPointCollection ) {

        return dataPointCollection
                .userDataPoints()
                .stream()
                .map( dataPoint -> new Entry( dataPoint.date().getTime(), dataPoint.score() ) )
                .map( entry -> entry.getX() < 0 ? new Entry( 0, entry.getY() ) : entry ) // weed out negative scores
                .collect( Collectors.toList() );
    }
}
