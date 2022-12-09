package com.chart.filesystem.json;

import com.chart.filesystem.dao.DataPoint;
import com.util.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DefaultJsonPolicy implements JsonPolicy {

    public static final String DATA_ELEMENT = "data";
    public static final String DATAPOINT_ELEMENT = "datapoint";
    public static final String DATAPOINTS_ELEMENT = "datapoints";
    public static final String DATE_ELEMENT = "date";
    public static final String SCORE_ELEMENT = "score";
    public static final String VERSION_ELEMENT = "version";
    private static final String GAME_ELEMENT = "game";

    @Override
    public List<DataPoint> parseDataPoints( JSONObject oneRow ) throws JSONException {
        JSONArray array = oneRow.getJSONArray( DATAPOINTS_ELEMENT );

        List<DataPoint> dataPoints = new ArrayList<>();

        for ( int i = 0; i < array.length(); i++ ) {

            JSONObject oneDataRow = ( JSONObject ) array.get( i );

            Date date = DateUtil.parse( oneDataRow.getString( DATE_ELEMENT ) );
            int score = oneDataRow.getInt( SCORE_ELEMENT );

            dataPoints.add( new DataPoint( date, score ) );
        }

        return dataPoints;
    }

    @Override
    public boolean isDataPointsForGame( SupportedGame game, JSONObject oneRow ) throws JSONException {
        return oneRow != null && oneRow.getString( GAME_ELEMENT ).equalsIgnoreCase( game.toString() );
    }
}
