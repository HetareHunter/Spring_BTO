package com.example.demo.service.weather;

import com.example.demo.model.WeatherEntity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {
  public WeatherEntity setWeatherInfo(WeatherEntity weatherEntity) {
    String weatherStr = "";
    try {
      String requestUrl =
          "https://api.openweathermap.org/data/2.5/weather?q=Tokyo,jp&APPID=5668b66baa1c8dd707dc6b35a935f6b6&units=metric&rain.3h";

      URL url = new URL(requestUrl);
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      connection.setRequestMethod("GET");
      connection.connect();

      int responseCode = connection.getResponseCode();

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

    Pattern pattern = Pattern.compile(
        "\"weather\":\\[\\{\"id\":\\d+,\"main\":\"([^\"]+)\",\"description\":\"([^\"]+)\"");
    Matcher matcher = pattern.matcher(weatherStr);
    if (matcher.find()) {
      weatherEntity.setWeatherType(matcher.group(1) + " (" + matcher.group(2) +
                                   ")");
    }

    pattern = Pattern.compile("\"humidity\":(\\d+)");
    matcher = pattern.matcher(weatherStr);
    if (matcher.find()) {
      weatherEntity.setHumidity((matcher.group(1)));
    }

    pattern = Pattern.compile("\"temp_max\":(\\d+\\.\\d+)");
    matcher = pattern.matcher(weatherStr);
    if (matcher.find()) {
      weatherEntity.setMaxTemperature(matcher.group(1));
    }

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
    System.out.println("日付：" + weatherEntity.getDateStr());
    System.out.println("天気：" + weatherEntity.getWeatherType());
    System.out.println("湿度：" + weatherEntity.getHumidity());
    System.out.println("最高気温：" + weatherEntity.getMaxTemperature());
    System.out.println("最低気温：" + weatherEntity.getMinTemperature());

    return weatherEntity;
  }
}
