package timetogether.config.kakaoapi;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import timetogether.GroupWhere.GroupWhere;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class KakaoAPI {
  @Value("${kakao.rest-api}")
  private String apiKey = "KakaoAK 967533de3fc3839252ee41386fe0bd95";
  private static final String KEYWORD_URL = "https://dapi.kakao.com/v2/local/search/keyword";
  private static final String CATEGORY_URL = "https://dapi.kakao.com/v2/local/search/category";
  private double x;
  private double y;
  private int radius;
  public void setRadius(int radius) {
    this.radius = radius;
  }
  public int getRadius() {
    return radius;
  }
  // 키워드 검색 메서드
  public JSONObject keywordSearch(String keyword) throws UnsupportedEncodingException {
    String url = KEYWORD_URL + "?query=" + URLEncoder.encode(keyword, "UTF-8"); // 이게 있어야 url keyword에서 띄어쓰기까지 인식 가능
    JSONObject json = getJson(url); // json = {documents : [{객체A},{객체B}....] , meta : [배열2]}
    log.info("json :{}" ,json);
    JSONArray documents = json.getJSONArray("documents"); // documents = [{객체A},{객체B}....] // documents 키값에 대응하는 value.
    JSONObject mylocation = documents.getJSONObject(0); //base = {객체A}
    this.x = Double.parseDouble(String.valueOf(mylocation.get("x")));
    this.y = Double.parseDouble(String.valueOf(mylocation.get("y")));
    return mylocation; // 키워드 검색 결과 중 가장 앞에 있는 객체 선택
  }
  public List<GroupWhere> categorySearch() throws JSONException {
    String url = CATEGORY_URL + "?x=" + x + "&y=" + y + "&category_group_code="
            + "CE7" + "&radius=" + radius;
    JSONObject json = getJson(url);
    JSONArray documents = json.getJSONArray("documents");
    List<GroupWhere> groupWhereList = new ArrayList<>();

    // 최대 5개까지만 처리
    int count = Math.min(documents.length(), 5);

    for (int i = 0; i < count; i++) {
      JSONObject location = documents.getJSONObject(i);

      GroupWhere groupWhere = GroupWhere.builder()
              .groupWhereName(location.getString("place_name"))
              .groupWhereUrl(location.getString("place_url"))
              .build();

      groupWhereList.add(groupWhere);
      //printResult(documents);
    }
    return groupWhereList;
  }
  // REST API 이용 메서드
  private JSONObject getJson(String apiUrl) { //requestURL 설정 후 Kakao-API의 response 값 인 json 을 받아오는 메서드
    String json = "";
    try {
      log.info("에이피아이키 apikey : {} ", apiKey);
      HttpClient client = HttpClientBuilder.create().build();
      HttpGet getRequest = new HttpGet(apiUrl); //Get 메소드 URL 생성
      getRequest.addHeader("Authorization",apiKey); //apikey 입력
      HttpResponse getResponse = client.execute(getRequest); // 위에 보낸 request에 대한 response 내용(json)

      BufferedReader br = new BufferedReader(new InputStreamReader(getResponse.getEntity().getContent(), "UTF-8"));
      json = br.readLine();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return new JSONObject(json);
  }
  private void printResult(JSONArray arr) {
    try {
      // 출력할 개수 설정 (배열 길이와 원하는 개수 중 작은 값 선택)
      int count = Math.min(arr.length(), 5);

      for (int i = 0; i < count; i++) {
        JSONObject obj = arr.getJSONObject(i);
        System.out.println("검색결과 #" + (i + 1));
        System.out.println("- 장소 URL(지도 위치): " + obj.get("place_url"));
        System.out.println("- 상호명: " + obj.get("place_name"));
        System.out.println("- 주소: " + obj.get("road_address_name"));
        System.out.println("- 전화번호: " + obj.get("phone"));
        System.out.println(
                "- 거리(km): " + Double.parseDouble((String) obj.get("distance")) / 1000 + "km");
        System.out.println("----------------------------------------------------");
      }

      System.out.println();
      System.out.println("프로그램 종료");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}