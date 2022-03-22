package ru.fishtext.tests;

import io.restassured.path.json.JsonPath;
import jdk.jfr.Description;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.fishtext.api.FishTextApi;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static ru.fishtext.helpers.CountHelper.countNumbersOfEntries;
import static ru.fishtext.helpers.CountHelper.countSentences;

public class FishTextTests {

    private static HashMap<String, Object> createQueries(String format, Object number, String type) {
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("format", format);
        queryParams.put("number", number);
        queryParams.put("type", type);

        return queryParams;
    }

    @Tag("JSON")
    @Tag("Positive")
    @Description("Проверка того, что при запросе приходит ответ JSON с указанным количеством абзацев/заголовков ")
    @ParameterizedTest
    @CsvSource(value = {"json:3:paragraph", "json:500:title"}, delimiter = ':')
    void countParagraphsAndTitlesWithJSONTest(String format, int number, String type) {
        HashMap<String, Object> queryParams = createQueries(format,number,type);

        var response = FishTextApi.get(queryParams);
        String body = response.getBody().asString();

        int countOfEntries = countNumbersOfEntries(body, "\\\\n\\\\n");
        System.out.println(countOfEntries);

        assertThat(countOfEntries, equalTo(number));
        assertThat(body, containsString("success"));
    }

    @Tag("JSON")
    @Tag("Positive")
    @Description("Проверка того, что при запросе приходит ответ JSON с указанным количеством предложений ")
    @Test
    void countSentencesWithJSONTest() {
        HashMap<String, Object> queryParams = createQueries("JSON", 8, "sentence");

        var response = FishTextApi.get(queryParams);
        String body = response.getBody().asString();

        int countOfSentences = countSentences(body);
        System.out.println(countOfSentences);

        assertThat(countOfSentences, equalTo(8));
        assertThat(body, containsString("success"));
    }

    @Tag("JSON")
    @Tag("Negative")
    @Description("Проверка того, что при запросе c количеством приходит ответ JSON с указанным количеством предложений ")
    @Test
    void countSentencesWithJSONNegativeTest() {
        HashMap<String, Object> queryParams = createQueries("JSON", 501,"sentence");

        var response = FishTextApi.get(queryParams);

        JsonPath body = new JsonPath(response.asString());
        String status = body.get("status").toString();
        String errorCode = body.get("errorCode").toString();

        assertThat(status, equalTo("error"));
        assertThat(errorCode, equalTo("11"));
    }

    @Tag("HTML")
    @Tag("Positive")
    @Description("Проверка того, что при запросе приходит ответ HTML с указанным количеством абзацев/заголовков ")
    @ParameterizedTest
    @CsvSource(value = {"html:5:paragraph:<p>", "html:8:title:<h1>"}, delimiter = ':')
    void countParagraphsAndTitlesWithHTMLTest(String format, int number, String type, String entryType) {
        HashMap<String, Object> queryParams = createQueries(format,number,type);

        var response = FishTextApi.get(queryParams);
        String body = response.getBody().asString();

        int countOfEntries = countNumbersOfEntries(body, entryType);
        System.out.println(countOfEntries);

        assertThat(countOfEntries, equalTo(number));
    }

    @Tag("HTML")
    @Tag("Positive")
    @Description("Проверка того, что при запросе приходит ответ HTML с указанным количеством предложений ")
    @Test
    void countSentencesWithHTMLTest() {
        HashMap<String, Object> queryParams = createQueries("html", 15, "sentences");

        var response = FishTextApi.get(queryParams);
        String body = response.getBody().asString();

        int countOfSentences = countSentences(body);
        System.out.println(countOfSentences);

        assertThat(countOfSentences, equalTo(15));
    }


    @Tag("HTML")
    @Tag("Negative")
    @Description("Проверка того, что при запросе приходит ответ HTML с указанным количеством предложений ")
    @Test
    void countSentencesWithHTMLNegativeTest() {
        HashMap<String, Object> queryParams = createQueries("HTML", 101,"paragraph");

        var response = FishTextApi.get(queryParams);
        String body = response.getBody().asString();

        assertThat(body, containsString("You requested too much content. Be more moderate."));
    }
}
