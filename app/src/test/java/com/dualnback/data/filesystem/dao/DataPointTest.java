package com.dualnback.data.filesystem.dao;

import com.dualnback.game.NBackVersion;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class DataPointTest {

    @Test
    public void canCreateInstance() {
        // When/Then: Can create DataPoint with valid parameters
        Date date = new Date();
        DataPoint dataPoint = new DataPoint(date, 85, NBackVersion.TwoBack);

        assertThat(dataPoint).isNotNull();
        assertThat(dataPoint.date()).isEqualTo(date);
        assertThat(dataPoint.score()).isEqualTo(85);
        assertThat(dataPoint.version()).isEqualTo(NBackVersion.TwoBack);
    }

    @Test
    public void date_returnsCorrectDate() {
        // Given: A DataPoint with specific date
        Date expectedDate = new Date(1234567890000L);
        DataPoint dataPoint = new DataPoint(expectedDate, 100, NBackVersion.ThreeBack);

        // When: Getting the date
        Date actualDate = dataPoint.date();

        // Then: Returns the correct date
        assertThat(actualDate).isEqualTo(expectedDate);
    }

    @Test
    public void score_returnsCorrectScore() {
        // Given: A DataPoint with specific score
        DataPoint dataPoint = new DataPoint(new Date(), 92, NBackVersion.FourBack);

        // When: Getting the score
        int actualScore = dataPoint.score();

        // Then: Returns the correct score
        assertThat(actualScore).isEqualTo(92);
    }

    @Test
    public void version_returnsCorrectVersion() {
        // Given: A DataPoint with specific version
        DataPoint dataPoint = new DataPoint(new Date(), 75, NBackVersion.FiveBack);

        // When: Getting the version
        NBackVersion actualVersion = dataPoint.version();

        // Then: Returns the correct version
        assertThat(actualVersion).isEqualTo(NBackVersion.FiveBack);
    }

    @Test
    public void compareTo_returnsNegativeWhenThisIsEarlier() {
        // Given: Two DataPoints with different dates
        Date earlier = new Date(1000000000000L);
        Date later = new Date(2000000000000L);
        DataPoint earlierDataPoint = new DataPoint(earlier, 80, NBackVersion.TwoBack);
        DataPoint laterDataPoint = new DataPoint(later, 90, NBackVersion.ThreeBack);

        // When: Comparing
        int result = earlierDataPoint.compareTo(laterDataPoint);

        // Then: Returns negative (earlier < later)
        assertThat(result).isLessThan(0);
    }

    @Test
    public void compareTo_returnsPositiveWhenThisIsLater() {
        // Given: Two DataPoints with different dates
        Date earlier = new Date(1000000000000L);
        Date later = new Date(2000000000000L);
        DataPoint earlierDataPoint = new DataPoint(earlier, 80, NBackVersion.TwoBack);
        DataPoint laterDataPoint = new DataPoint(later, 90, NBackVersion.ThreeBack);

        // When: Comparing
        int result = laterDataPoint.compareTo(earlierDataPoint);

        // Then: Returns positive (later > earlier)
        assertThat(result).isGreaterThan(0);
    }

    @Test
    public void compareTo_returnsZeroWhenDatesAreEqual() {
        // Given: Two DataPoints with same date
        Date sameDate = new Date(1500000000000L);
        DataPoint dataPoint1 = new DataPoint(sameDate, 80, NBackVersion.TwoBack);
        DataPoint dataPoint2 = new DataPoint(sameDate, 90, NBackVersion.ThreeBack);

        // When: Comparing
        int result = dataPoint1.compareTo(dataPoint2);

        // Then: Returns zero (equal)
        assertThat(result).isEqualTo(0);
    }

    @Test
    public void toString_containsAllFields() {
        // Given: A DataPoint
        Date date = new Date(1234567890000L);
        DataPoint dataPoint = new DataPoint(date, 88, NBackVersion.ThreeBack);

        // When: Converting to string
        String result = dataPoint.toString();

        // Then: String contains all field information
        assertThat(result).contains("DataPoint");
        assertThat(result).contains("date=");
        assertThat(result).contains("score=88");
        assertThat(result).contains("version=");
    }

    @Test
    public void toJSON_returnsValidJSON() throws JSONException {
        // Given: A DataPoint
        Date date = new Date();
        DataPoint dataPoint = new DataPoint(date, 95, NBackVersion.FourBack);

        // When: Converting to JSON
        String jsonString = dataPoint.toJSON();

        // Then: Returns valid JSON that can be parsed
        assertThat(jsonString).isNotEmpty();
        JSONObject jsonObject = new JSONObject(jsonString);
        assertThat(jsonObject.has("datapoint")).isTrue();
    }

    @Test
    public void toJSON_containsDateScoreAndVersion() throws JSONException {
        // Given: A DataPoint with specific values
        Date date = new Date();
        DataPoint dataPoint = new DataPoint(date, 77, NBackVersion.TwoBack);

        // When: Converting to JSON
        String jsonString = dataPoint.toJSON();

        // Then: JSON contains all fields
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject innerObject = jsonObject.getJSONObject("datapoint");

        assertThat(innerObject.has("date")).isTrue();
        assertThat(innerObject.has("score")).isTrue();
        assertThat(innerObject.has("version")).isTrue();
        assertThat(innerObject.getInt("score")).isEqualTo(77);
        assertThat(innerObject.getString("version")).isEqualTo("Dual 2-Back");
    }

    @Test
    public void toJSON_handlesAllVersionTypes() throws JSONException {
        // Given: DataPoints with different versions
        NBackVersion[] versions = {
            NBackVersion.OneBack,
            NBackVersion.TwoBack,
            NBackVersion.ThreeBack,
            NBackVersion.FourBack,
            NBackVersion.FiveBack,
            NBackVersion.SixBack,
            NBackVersion.SevenBack,
            NBackVersion.EightBack,
            NBackVersion.NineBack
        };

        for (NBackVersion version : versions) {
            // When: Converting each version to JSON
            DataPoint dataPoint = new DataPoint(new Date(), 50, version);
            String jsonString = dataPoint.toJSON();

            // Then: JSON is valid and contains version
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject innerObject = jsonObject.getJSONObject("datapoint");
            assertThat(innerObject.getString("version")).isEqualTo(version.getTextRepresentation());
        }
    }

    @Test
    public void toJSON_handlesZeroScore() throws JSONException {
        // Given: A DataPoint with zero score
        DataPoint dataPoint = new DataPoint(new Date(), 0, NBackVersion.TwoBack);

        // When: Converting to JSON
        String jsonString = dataPoint.toJSON();

        // Then: JSON correctly represents zero score
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject innerObject = jsonObject.getJSONObject("datapoint");
        assertThat(innerObject.getInt("score")).isEqualTo(0);
    }

    @Test
    public void toJSON_handlesHighScore() throws JSONException {
        // Given: A DataPoint with high score
        DataPoint dataPoint = new DataPoint(new Date(), 100, NBackVersion.NineBack);

        // When: Converting to JSON
        String jsonString = dataPoint.toJSON();

        // Then: JSON correctly represents high score
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject innerObject = jsonObject.getJSONObject("datapoint");
        assertThat(innerObject.getInt("score")).isEqualTo(100);
    }
}
