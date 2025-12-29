package com.dualnback.data.filesystem.util;

import com.dualnback.data.filesystem.dao.DataPoint;
import com.dualnback.game.NBackVersion;

import org.json.JSONException;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JSONUtilTest {

    private static final String EMPTY_JSON = "{ \"data\" :[] }";

    private static final String SINGLE_DATAPOINT_JSON =
        "{\"data\":[{\"datapoint\":{\"date\":\"2024-01-15T12:30:00\",\"score\":85,\"version\":\"Dual 2-Back\"}}]}";

    private static final String MULTIPLE_DATAPOINTS_JSON =
        "{\"data\":[" +
        "{\"datapoint\":{\"date\":\"2024-01-15T12:00:00\",\"score\":75,\"version\":\"Dual 2-Back\"}}," +
        "{\"datapoint\":{\"date\":\"2024-01-15T13:00:00\",\"score\":82,\"version\":\"Dual 3-Back\"}}," +
        "{\"datapoint\":{\"date\":\"2024-01-15T14:00:00\",\"score\":91,\"version\":\"Dual 4-Back\"}}" +
        "]}";

    @Test
    public void parse_emptyDataReturnsEmptyList() throws JSONException {
        // Given: JSON with no data points
        String json = EMPTY_JSON;

        // When: Parsing
        List<DataPoint> result = JSONUtil.parse(json);

        // Then: Returns empty list
        assertThat(result).isEmpty();
    }

    @Test
    public void parse_singleDataPoint() throws JSONException {
        // Given: JSON with one data point
        String json = SINGLE_DATAPOINT_JSON;

        // When: Parsing
        List<DataPoint> result = JSONUtil.parse(json);

        // Then: Returns list with one data point
        assertThat(result).hasSize(1);
        DataPoint dataPoint = result.get(0);
        assertThat(dataPoint.score()).isEqualTo(85);
        assertThat(dataPoint.version()).isEqualTo(NBackVersion.TwoBack);
    }

    @Test
    public void parse_multipleDataPoints() throws JSONException {
        // Given: JSON with multiple data points
        String json = MULTIPLE_DATAPOINTS_JSON;

        // When: Parsing
        List<DataPoint> result = JSONUtil.parse(json);

        // Then: Returns list with all data points
        assertThat(result).hasSize(3);
        assertThat(result.get(0).score()).isEqualTo(75);
        assertThat(result.get(0).version()).isEqualTo(NBackVersion.TwoBack);
        assertThat(result.get(1).score()).isEqualTo(82);
        assertThat(result.get(1).version()).isEqualTo(NBackVersion.ThreeBack);
        assertThat(result.get(2).score()).isEqualTo(91);
        assertThat(result.get(2).version()).isEqualTo(NBackVersion.FourBack);
    }

    @Test
    public void parse_allVersionTypes() throws JSONException {
        // Given: JSON with all version types
        String json = "{\"data\":[" +
                "{\"datapoint\":{\"date\":\"2024-01-01T10:00:00\",\"score\":50,\"version\":\"Dual 1-Back\"}}," +
                "{\"datapoint\":{\"date\":\"2024-01-02T10:00:00\",\"score\":60,\"version\":\"Dual 2-Back\"}}," +
                "{\"datapoint\":{\"date\":\"2024-01-03T10:00:00\",\"score\":70,\"version\":\"Dual 3-Back\"}}," +
                "{\"datapoint\":{\"date\":\"2024-01-04T10:00:00\",\"score\":80,\"version\":\"Dual 4-Back\"}}," +
                "{\"datapoint\":{\"date\":\"2024-01-05T10:00:00\",\"score\":85,\"version\":\"Dual 5-Back\"}}," +
                "{\"datapoint\":{\"date\":\"2024-01-06T10:00:00\",\"score\":90,\"version\":\"Dual 6-Back\"}}," +
                "{\"datapoint\":{\"date\":\"2024-01-07T10:00:00\",\"score\":92,\"version\":\"Dual 7-Back\"}}," +
                "{\"datapoint\":{\"date\":\"2024-01-08T10:00:00\",\"score\":95,\"version\":\"Dual 8-Back\"}}," +
                "{\"datapoint\":{\"date\":\"2024-01-09T10:00:00\",\"score\":98,\"version\":\"Dual 9-Back\"}}" +
                "]}";

        // When: Parsing
        List<DataPoint> result = JSONUtil.parse(json);

        // Then: All versions are parsed correctly
        assertThat(result).hasSize(9);
        assertThat(result.get(0).version()).isEqualTo(NBackVersion.OneBack);
        assertThat(result.get(1).version()).isEqualTo(NBackVersion.TwoBack);
        assertThat(result.get(2).version()).isEqualTo(NBackVersion.ThreeBack);
        assertThat(result.get(3).version()).isEqualTo(NBackVersion.FourBack);
        assertThat(result.get(4).version()).isEqualTo(NBackVersion.FiveBack);
        assertThat(result.get(5).version()).isEqualTo(NBackVersion.SixBack);
        assertThat(result.get(6).version()).isEqualTo(NBackVersion.SevenBack);
        assertThat(result.get(7).version()).isEqualTo(NBackVersion.EightBack);
        assertThat(result.get(8).version()).isEqualTo(NBackVersion.NineBack);
    }

    @Test
    public void parse_invalidVersion_usesDefault() throws JSONException {
        // Given: JSON with invalid version string
        String json = "{\"data\":[{\"datapoint\":{\"date\":\"2024-01-15T12:00:00\",\"score\":88,\"version\":\"Invalid Version\"}}]}";

        // When: Parsing
        List<DataPoint> result = JSONUtil.parse(json);

        // Then: Uses default version (TwoBack)
        assertThat(result).hasSize(1);
        assertThat(result.get(0).version()).isEqualTo(NBackVersion.TwoBack);
        assertThat(result.get(0).score()).isEqualTo(88);
    }

    @Test
    public void parse_zeroScore() throws JSONException {
        // Given: JSON with zero score
        String json = "{\"data\":[{\"datapoint\":{\"date\":\"2024-01-15T12:00:00\",\"score\":0,\"version\":\"Dual 2-Back\"}}]}";

        // When: Parsing
        List<DataPoint> result = JSONUtil.parse(json);

        // Then: Parses zero score correctly
        assertThat(result).hasSize(1);
        assertThat(result.get(0).score()).isEqualTo(0);
    }

    @Test
    public void parse_highScore() throws JSONException {
        // Given: JSON with high score
        String json = "{\"data\":[{\"datapoint\":{\"date\":\"2024-01-15T12:00:00\",\"score\":100,\"version\":\"Dual 9-Back\"}}]}";

        // When: Parsing
        List<DataPoint> result = JSONUtil.parse(json);

        // Then: Parses high score correctly
        assertThat(result).hasSize(1);
        assertThat(result.get(0).score()).isEqualTo(100);
        assertThat(result.get(0).version()).isEqualTo(NBackVersion.NineBack);
    }

    @Test
    public void parse_preservesDateInformation() throws JSONException {
        // Given: JSON with specific date
        String json = SINGLE_DATAPOINT_JSON;

        // When: Parsing
        List<DataPoint> result = JSONUtil.parse(json);

        // Then: Date is parsed (not null)
        assertThat(result).hasSize(1);
        assertThat(result.get(0).date()).isNotNull();
    }

    @Test(expected = JSONException.class)
    public void parse_invalidJSON_throwsException() throws JSONException {
        // Given: Invalid JSON string
        String json = "{this is not valid JSON}";

        // When/Then: Throws JSONException
        JSONUtil.parse(json);
    }

    @Test(expected = JSONException.class)
    public void parse_missingDatapointObject_throwsException() throws JSONException {
        // Given: JSON with data array but no datapoint object
        String json = "{\"data\":[{\"wrongKey\":{\"date\":\"2024-01-15T12:00:00\",\"score\":80,\"version\":\"Dual 2-Back\"}}]}";

        // When/Then: Throws JSONException
        JSONUtil.parse(json);
    }

    @Test(expected = JSONException.class)
    public void parse_missingDate_throwsException() throws JSONException {
        // Given: JSON without date field
        String json = "{\"data\":[{\"datapoint\":{\"score\":80,\"version\":\"Dual 2-Back\"}}]}";

        // When/Then: Throws JSONException
        JSONUtil.parse(json);
    }

    @Test(expected = JSONException.class)
    public void parse_missingScore_throwsException() throws JSONException {
        // Given: JSON without score field
        String json = "{\"data\":[{\"datapoint\":{\"date\":\"2024-01-15T12:00:00\",\"version\":\"Dual 2-Back\"}}]}";

        // When/Then: Throws JSONException
        JSONUtil.parse(json);
    }

    @Test
    public void parse_whitespacesInJSON_handlesCorrectly() throws JSONException {
        // Given: JSON with extra whitespaces and newlines
        String json = "{\n  \"data\": [\n    {\n      \"datapoint\": {\n        \"date\": \"2024-01-15T12:00:00\",\n        \"score\": 85,\n        \"version\": \"Dual 3-Back\"\n      }\n    }\n  ]\n}";

        // When: Parsing
        List<DataPoint> result = JSONUtil.parse(json);

        // Then: Parses correctly despite formatting
        assertThat(result).hasSize(1);
        assertThat(result.get(0).score()).isEqualTo(85);
        assertThat(result.get(0).version()).isEqualTo(NBackVersion.ThreeBack);
    }

    @Test
    public void constants_haveCorrectValues() {
        // Verify the constant values are as expected
        assertThat(JSONUtil.DATA_ELEMENT).isEqualTo("data");
        assertThat(JSONUtil.DATAPOINT_ELEMENT).isEqualTo("datapoint");
        assertThat(JSONUtil.DATE_ELEMENT).isEqualTo("date");
        assertThat(JSONUtil.SCORE_ELEMENT).isEqualTo("score");
        assertThat(JSONUtil.VERSION_ELEMENT).isEqualTo("version");
        assertThat(JSONUtil.DEFAULT_N_BACK_VERSION).isEqualTo(NBackVersion.TwoBack);
    }
}
