// GPTService.java
package com.ssafy.openai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Service
public class GPTService {
    private static final String OPENAI_API_KEY = "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";
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    // 고정 프롬프트 설정
    private static final String SYSTEM_PROMPT = "당신은 여행 플래너이자 자연 풍경을 전문으로 찍는 포토그래퍼입니다." +
    "그렇기 때문에 당신은 간단한 인사말과 간단한 대화 이 외에 여행에 관한 정보(관광지에 대한 설명, 위치, 주변 식당, 주변 숙박시설)와 관련된 대답만 할 수 있습니다." +
    " +답변은 **마크다운 형식**을 사용하지 말고, **이모티콘**과 **일반 텍스트**로만 작성해주세요." +
    "특히 마크다운 태그(**, ##, # 등)는 사용하지 않도록 주의해주세요." +
    "또한 사용자의 칭찬에 대해 감사를 표할 수 있으며, 사용자의 부정적인 감정에 대해 사과할 수 있습니다." +
    "만약 그 외의 정보에 대해 질문을 받으면, `저는 여행전문가이기 때문에 잘 모르겠습니다.`라고 대답하면 됩니다." +
    "대한민국의 시도나 구(군)가 주어지면 해당 지역에 있는 유명하거나 숨겨진 관광지와 숙박, 음식점을 추천해주고," +
    "여행 계획을 세울 수 있게 관광지 사이의 이동 경로(최단거리와 컨텐츠 타입의 조합)를 코스를 추천해주고," +
    "특정 관광지가 주어지면 그 관광지의 위치, 주변 다른 관광지와 맛집, 숙박시설 등을 추천해주세요."+
    "주제당 2~3즐 이내로 간결하게 작성해줘";


    public static String getResponse(String message) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(OPENAI_API_KEY);

        // 메시지 배열 생성
        String requestBody = "{"
                + "\"model\": \"gpt-4o-mini\","
                + "\"messages\": ["
                + "{\"role\": \"system\", \"content\": \"" + SYSTEM_PROMPT + "\"},"
                + "{\"role\": \"user\", \"content\": \"" + message + "\"}"
                + "]"
                + "}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            String response = restTemplate.postForObject(OPENAI_API_URL, entity, String.class);

            // 응답에서 챗봇의 답변 추출
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            String reply = root.path("choices").get(0).path("message").path("content").asText();

            // 응답 포맷팅
            String formattedReply = formatResponse(reply);
            return formattedReply;
        } catch (Exception e) {
            e.printStackTrace();
            return "죄송합니다. 답변을 가져올 수 없습니다.";
        }
    }

    // 응답 포맷팅 함수
    private static String formatResponse(String response) {
        return response.replace("관광지 추천", "1. 관광지 추천 🏞️")
                .replace("맛집 추천", "2. 인근 맛집 추천 🍽️")
                .replace("숙박시설 추천", "3. 숙박시설 추천 🏨")
                .replace("무각사", "🌟 무각사")
                .replace("광주 국립박물관", "🌟 광주 국립박물관")
                .replace("한양냉면", "🍽️ 한양냉면")
                .replace("신세계수산", "🍽️ 신세계수산")
                .replace("라마다 광주 호텔", "🏨 라마다 광주 호텔")
                .replace("아트 호텔 광주", "🏨 아트 호텔 광주")
                .replace("위치", "📍 위치")
                .replace("설명", "🍜 설명")
                .replace("\n", "\n\n");  // 항목 간 공백 추가
    }

}
