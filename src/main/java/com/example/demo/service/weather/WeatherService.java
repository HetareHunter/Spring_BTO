package com.example.demo.service.weather;

import com.example.demo.model.WeatherEntity;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {
  /**
   * 天気情報を取得する
   * @param weatherEntity
   * @return
   */
  public WeatherEntity setWeatherInfo(WeatherEntity weatherEntity) {
    String weatherStr = "";
    try {
      // OpenWeatherから天気情報を取得する
      String requestUrl =
          "https://api.openweathermap.org/data/2.5/weather?q=Tokyo,jp&APPID=5668b66baa1c8dd707dc6b35a935f6b6&units=metric&rain.3h";

      URL url = new URL(requestUrl);
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      connection.setRequestMethod("GET");
      connection.connect();

      // 接続状態を格納する
      int responseCode = connection.getResponseCode();

      // OpenWeatherに正常に接続できた場合にJSON形式で天気情報を取得する
      if (responseCode == HttpURLConnection.HTTP_OK) {
        Scanner scanner = new Scanner(url.openStream());

        weatherStr = scanner.nextLine();
        System.out.println(weatherStr);
        scanner.close();
      } else {
        throw new RuntimeException("HttpResponseCode: " + responseCode);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    // 天気の名前を正規表現で切り分ける
    Pattern pattern = Pattern.compile(
        "\"weather\":\\[\\{\"id\":\\d+,\"main\":\"([^\"]+)\",\"description\":\"([^\"]+)\"");
    Matcher matcher = pattern.matcher(weatherStr);
    if (matcher.find()) {
      weatherEntity.setWeatherType(matcher.group(1));
      weatherEntity.setWeatherdescription(matcher.group(2));
    }

    // 湿度を正規表現で切り分ける
    pattern = Pattern.compile("\"humidity\":(\\d+)");
    matcher = pattern.matcher(weatherStr);
    if (matcher.find()) {
      weatherEntity.setHumidity((matcher.group(1)));
    }

    // 温度を正規表現で切り分ける
    pattern = Pattern.compile("\"temp\":(\\d+\\.\\d+)");
    matcher = pattern.matcher(weatherStr);
    if (matcher.find()) {
      weatherEntity.setTemperature(matcher.group(1));
    }

    // 最高気温を正規表現で切り分ける
    pattern = Pattern.compile("\"temp_max\":(\\d+\\.\\d+)");
    matcher = pattern.matcher(weatherStr);
    if (matcher.find()) {
      weatherEntity.setMaxTemperature(matcher.group(1));
    }

    // 最低気温を正規表現で切り分ける
    pattern = Pattern.compile("\"temp_min\":(\\d+\\.\\d+)");
    matcher = pattern.matcher(weatherStr);
    if (matcher.find()) {
      weatherEntity.setMinTemperature(matcher.group(1));
    }

    // 現在の日時を取得
    var nowTime = LocalDateTime.now();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM月dd日(E)");
    weatherEntity.setDate(nowTime);
    weatherEntity.setDateStr(dtf.format(nowTime));
    weatherEntity.setId(1);

    System.out.println("日付：" + weatherEntity.getDateStr());
    System.out.println("天気：" + weatherEntity.getWeatherType());
    System.out.println("天気詳細：" + weatherEntity.getWeatherdescription());
    System.out.println("湿度：" + weatherEntity.getHumidity());
    System.out.println("現在の気温：" + weatherEntity.getTemperature());
    System.out.println("最高気温：" + weatherEntity.getMaxTemperature());
    System.out.println("最低気温：" + weatherEntity.getMinTemperature());

    return weatherEntity;
  }
}
