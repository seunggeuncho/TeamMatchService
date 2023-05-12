package com.example.fighteam.payment.controller;

import com.example.fighteam.payment.domain.Member;
import com.example.fighteam.payment.repository.HistoryRepository;
import com.example.fighteam.payment.repository.MemberRepository;
import com.example.fighteam.payment.service.MemberService;
import com.example.fighteam.payment.service.kakao.KakaoPayService;
import com.example.fighteam.payment.service.kakao.dto.ApproveResponse;
import com.example.fighteam.payment.service.kakao.dto.RequestPay;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@SessionAttributes("tid")
public class KakaoPayController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final HistoryRepository historyRepository;
    private final KakaoPayService kakaoPayService;
    @Value("${AdminKey}")
    private String adminKey;

    @PostMapping("/charge/chargeJson")

    public String postPayment(@RequestBody PaymentDto paymentDto) {
//        System.out.println("paymentDto.getCharge() = " + paymentDto.getCharge());
//        System.out.println("adminKey = " + adminKey);
        return "main";
    }

    @Data
    static class PaymentDto {
        private int charge;
    }

    // 카카오페이결제 요청, 카카오페이 서버에 결제 요청을 REST API 전송
    @PostMapping("/charge/pay")
    @ResponseBody
    public RequestPay payReady(@RequestBody PaymentDto paymentDto, Model model, HttpSession session) {
        Long loginId = (Long) session.getAttribute("loginId");
        Member member = memberRepository.findMember(loginId);

        // 카카오 결제 준비하기	- 결제요청 service 실행.
        RequestPay requestPay = kakaoPayService.payReady(paymentDto.getCharge(), member);
        // 요청처리후 받아온 결재고유 번호(tid)를 모델에 저장
        System.out.println("readyResponse = " + requestPay);
        model.addAttribute("tid", requestPay.getTid());

        return requestPay; // 클라이언트에(앞단으로) 보냄.(tid,next_redirect_pc_url이 담겨있음.)
    }

    // 결제승인요청
    @GetMapping("/charge/pay/completed")
    public String payCompleted(@RequestParam("pg_token") String pgToken, @ModelAttribute("tid") String tid, Model model, HttpSession session) {
//        System.out.println("tid = " + tid);
        String tid1 = (String) model.getAttribute("tid");
//        System.out.println("tid1 = " + tid1);
//        System.out.println("pgToken = " + pgToken);
        //세션에 로그인된 회원 정보를 결제 요청으로 넘김
        Long loginId = (Long) session.getAttribute("loginId");
        Member member = memberRepository.findMember(loginId);

        // 카카오 결제 승인 요청하기
        ApproveResponse approveResponse = kakaoPayService.payApprove(tid, pgToken, member);
        int cost = approveResponse.getAmount().getTotal();
//        System.out.println("approveResponse. = " + cost);

        // history에 결제내역 저장
        memberService.chargeDeposit(member.getId(), cost);


        return "redirect:/chargeSuccess";
    }
    // 결제 취소시 실행 url
    @GetMapping("/charge/pay/cancel")
    public String payCancel() {
        return "redirect:/charge";
    }

    // 결제 실패시 실행 url
    @GetMapping("/charge/pay/fail")
    public String payFail() {
        return "redirect:/charge";
    }


}
