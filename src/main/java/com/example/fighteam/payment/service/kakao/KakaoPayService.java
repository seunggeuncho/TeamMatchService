package com.example.fighteam.payment.service.kakao;

import com.example.fighteam.payment.domain.Member;
import com.example.fighteam.payment.service.MemberService;
import com.example.fighteam.payment.service.kakao.dto.ApproveResponse;
import com.example.fighteam.payment.service.kakao.dto.RequestPay;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@RequiredArgsConstructor
@Service
public class KakaoPayService {
    private final MemberService memberService;

    @Value("${AdminKey}")
    private String adminKey;


    public RequestPay payReady(int totalAmount, Member member) {



        String order_id = "충전" + member.getId();
        System.out.println("order_id = " + order_id);

        // 카카오가 요구한 결제요청request값을 담아줍니다.
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
        parameters.add("cid", "TC0ONETIME");
        parameters.add("partner_order_id", order_id);
        parameters.add("partner_user_id", "회원" + member.getId());
        parameters.add("item_name", member.getName());
        parameters.add("quantity", String.valueOf(1));
        parameters.add("total_amount", String.valueOf(totalAmount));
        parameters.add("tax_free_amount", "0");
        parameters.add("approval_url", "http://localhost:8080/charge/pay/completed"); // 결제승인시 넘어갈 url
        parameters.add("cancel_url", "http://localhost:8080/charge/pay/cancel"); // 결제취소시 넘어갈 url
        parameters.add("fail_url", "http://localhost:8080/charge/pay/fail"); // 결제 실패시 넘어갈 url

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, getHeaders());
        for (String s : parameters.keySet()) {
            System.out.println("parameters." + s + " = " + parameters.get(s));
        }
        
        // 외부url요청 통로 열기.
        RestTemplate template = new RestTemplate();
        String url = "https://kapi.kakao.com/v1/payment/ready";
        // template으로 값을 보내고 받아온 ReadyResponse값 readyResponse에 저장.
        RequestPay requestPay = template.postForObject(url, requestEntity, RequestPay.class);
        System.out.println("readyResponse in Service = " + requestPay);
        // 받아온 값 return
        return requestPay;
    }

    // 결제 승인요청 메서드
    public ApproveResponse payApprove(String tid, String pgToken, Member member) {
        System.out.println("tid = " + tid);

        String order_id = "충전" + member.getId();
        // request값 담기.
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
        parameters.add("cid", "TC0ONETIME");
        parameters.add("tid", tid);
        parameters.add("partner_order_id", order_id); // 주문명
        parameters.add("partner_user_id", "회원" + member.getId());
        parameters.add("pg_token", pgToken);

        // 하나의 map안에 header와 parameter값을 담아줌.
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부url 통신
        RestTemplate template = new RestTemplate();
        String url = "https://kapi.kakao.com/v1/payment/approve";
        // 보낼 외부 url, 요청 메시지(header,parameter), 처리후 값을 받아올 클래스.
        ApproveResponse approveResponse = template.postForObject(url, requestEntity, ApproveResponse.class);

        return approveResponse;
    }
    // header() 셋팅
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK "+ adminKey);
        headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        System.out.println("headers.get(\"Authorization\") = " + headers.get("Authorization"));

        return headers;
    }
}