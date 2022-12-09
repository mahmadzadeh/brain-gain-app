package com.chart.filesystem.json;

import com.chart.filesystem.dao.DataPoint;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSON {

    public static final String DATA_ELEMENT = "data";

    private final JsonPolicy jsonPolicy;
    private final SupportedGame game;

    public JSON( JsonPolicy jsonPolicy, SupportedGame game ) {
        this.jsonPolicy = jsonPolicy;
        this.game = game;
    }

    public <T extends DataPoint> List<T> parse( String jsonString ) throws JSONException {

        if ( StringUtils.isEmpty( jsonString ) ) {
            throw new IllegalArgumentException( "Invalid JSON string given " + jsonString );
        }

        JSONObject jsonObject = new JSONObject( jsonString );
        JSONArray array = jsonObject.optJSONArray( DATA_ELEMENT );

        List<T> dataPoints = new ArrayList<>();

        for ( int i = 0; i < array.length(); i++ ) {
            JSONObject oneRow = ( JSONObject ) array.get( i );

            if ( jsonPolicy.isDataPointsForGame( game, oneRow ) ) {
                dataPoints = jsonPolicy.parseDataPoints( oneRow );
            }
        }

        return dataPoints;
    }

    public <T extends DataPoint> JSONObject toJson( T dataPoint ) throws JSONException {
        return null;

    }
}
