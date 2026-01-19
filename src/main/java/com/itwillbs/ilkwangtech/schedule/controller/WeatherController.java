package com.itwillbs.ilkwangtech.schedule.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class WeatherController {

    private final String SERVICE_KEY = "3fc91242a1f21a97e4bc652b5b4a1ddc8058cc16640214352fc64cca48678bc5";

    @GetMapping("/api/weather/current")
    public Map<String, Object> getCurrentWeather() {
        Map<String, Object> resultMap = new HashMap<>();
        
        try {
            // 1. 현재 날씨 
            LocalDateTime now = LocalDateTime.now();
            
            // 매시 45분에 데이터 생성 -> 45분 이전이면 1시간 전 데이터 사용
            if (now.getMinute() < 45) { 
                now = now.minusHours(1); 
            }
            
            String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String baseTime = now.format(DateTimeFormatter.ofPattern("HH")) + "00"; 
            
            String nx = "98"; 
            String ny = "76";

            // 초단기예보 호출
            String responseRealtime = callApi("getUltraSrtFcst", baseDate, baseTime, nx, ny, "60");
            
            if(responseRealtime == null || !responseRealtime.trim().startsWith("{")) {
                log.info("🚨 초단기예보 API 에러 (JSON 아님): " + responseRealtime);
                resultMap.put("success", false);
                return resultMap; // 여기서 끝냄
            }
            
            parseRealtime(responseRealtime, resultMap);

            // 최저/최고 기온
            String baseTimeForMinMax = "0200"; 
            
            // 단기예보 호출
            String responseMinMax = callApi("getVilageFcst", baseDate, baseTimeForMinMax, nx, ny, "300");
            
            if(responseMinMax != null && responseMinMax.trim().startsWith("{")) {
                parseMinMax(responseMinMax, resultMap);
            } else {
            	log.info("API 호출 에러 (XML 응답): " + responseMinMax);
            }

            resultMap.put("success", true);

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success", false);
        }
        return resultMap;
    }

    private String callApi(String apiType, String baseDate, String baseTime, String nx, String ny, String numOfRows) throws Exception {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/" + apiType);
        
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + SERVICE_KEY);
        
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8"));

        URL url = new URI(urlBuilder.toString()).toURL();
        
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        
        return sb.toString();
    }

    private void parseRealtime(String jsonStr, Map<String, Object> resultMap) {
        JSONObject json = new JSONObject(jsonStr);
        JSONArray items = json.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");

        resultMap.put("temp", "-");
        resultMap.put("sky", "1");
        resultMap.put("pty", "0");
        resultMap.put("rain", "0");
        resultMap.put("humidity", "0");
        resultMap.put("wind", "0");

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String category = item.getString("category");
            String fcstValue = item.getString("fcstValue");

            if (category.equals("T1H") && resultMap.get("temp").equals("-")) resultMap.put("temp", fcstValue);
            else if (category.equals("SKY") && resultMap.get("sky").equals("1")) resultMap.put("sky", fcstValue);
            else if (category.equals("PTY") && resultMap.get("pty").equals("0")) resultMap.put("pty", fcstValue);
            else if (category.equals("RN1") && resultMap.get("rain").equals("0")) resultMap.put("rain", fcstValue);
            else if (category.equals("REH") && resultMap.get("humidity").equals("0")) resultMap.put("humidity", fcstValue);
            else if (category.equals("WSD") && resultMap.get("wind").equals("0")) resultMap.put("wind", fcstValue);
        }
    }

    private void parseMinMax(String jsonStr, Map<String, Object> resultMap) {
        JSONObject json = new JSONObject(jsonStr);
        JSONArray items = json.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");

        String tmn = "-";
        String tmx = "-";

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String category = item.getString("category");
            String fcstValue = item.getString("fcstValue");

            if (category.equals("TMN")) tmn = fcstValue;
            else if (category.equals("TMX")) tmx = fcstValue;
        }
        
        if(!tmn.equals("-")) tmn = String.valueOf((int)Double.parseDouble(tmn));
        if(!tmx.equals("-")) tmx = String.valueOf((int)Double.parseDouble(tmx));

        resultMap.put("tmn", tmn);
        resultMap.put("tmx", tmx);
    }
}