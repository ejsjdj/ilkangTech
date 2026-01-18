package com.itwillbs.ilkwangtech.schedule.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class HolidayService {
	
	// 공공데이터포털 인증키 (Decoding 키 또는 Encoding 키 확인 필요)
    // 주의: 키값에 %가 포함되어 있다면 'Encoding 키'이므로 그대로 사용하면 되고,
    //      %가 없다면(Base64 등) 인코딩 이슈가 있을 수 있으니 포털 가이드를 참고하세요.
    private final String SERVICE_KEY = "3fc91242a1f21a97e4bc652b5b4a1ddc8058cc16640214352fc64cca48678bc5";

    public List<Map<String, Object>> getHolidays(String year) {
        List<Map<String, Object>> holidayList = new ArrayList<>();
        
        try {
            // 1. API 요청 URL 만들기
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo");
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + SERVICE_KEY);
            urlBuilder.append("&" + URLEncoder.encode("solYear", "UTF-8") + "=" + year);
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=100");
            urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=json");

            // 2. 연결 및 데이터 가져오기
            URL url = URI.create(urlBuilder.toString()).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json"); // 헤더 추가 권장
            
            // 응답 코드 확인 (200 아니면 에러)
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
                
                // 데이터 파싱
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(sb.toString());
                
                // response -> body -> items -> item 경로 찾아가기
                JsonNode itemsNode = root.path("response").path("body").path("items").path("item");

                if (itemsNode.isArray()) {
                    // 결과가 여러 개인 경우 (배열)
                    for (JsonNode node : itemsNode) {
                        Map<String, Object> holiday = new HashMap<>();
                        holiday.put("dateName", node.path("dateName").asText()); // 예: 설날
                        holiday.put("locdate", node.path("locdate").asInt());    // 예: 20260217
                        holiday.put("isHoliday", node.path("isHoliday").asText());
                        holidayList.add(holiday);
                    }
                } else if (!itemsNode.isMissingNode()) {
                    // 결과가 1개인 경우 (객체로 옴)
                    Map<String, Object> holiday = new HashMap<>();
                    holiday.put("dateName", itemsNode.path("dateName").asText());
                    holiday.put("locdate", itemsNode.path("locdate").asInt());
                    holiday.put("isHoliday", itemsNode.path("isHoliday").asText());
                    holidayList.add(holiday);
                }
                
                // (디버깅용) 콘솔에 데이터 찍어보기
                System.out.println(year + "년 공휴일 개수: " + holidayList.size());
                // -------------------------------------------------------------
            } else {
                System.out.println("API 요청 실패: " + conn.getResponseCode());
            }
            conn.disconnect();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return holidayList;
    }
}