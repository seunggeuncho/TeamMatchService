package com.example.fighteam.payment.controller;

import com.example.fighteam.payment.domain.Apply;
import com.example.fighteam.payment.domain.Member;
import com.example.fighteam.payment.repository.ApplyRepository;
import com.example.fighteam.payment.repository.MemberRepository;
import com.example.fighteam.payment.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final ApplyRepository applyRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @GetMapping("/payment")
    public String payment(HttpSession session, Model model) {
        //나중에 postId도 파라미터로 받아와야함

        Long id = (Long) session.getAttribute("loginId");
        //  Apply findApply = applyRepository.findByMember(id); -> 쿼리가 3방 나감
        // apply1번 + getPost()1번 getMember() 1번 -> 페치 조인으로 한방에 불러오도록 수정
        Apply findApply = applyRepository.findByMemberWithPost(id);
        model.addAttribute("post", findApply.getPost());
        model.addAttribute("apply", findApply);
        model.addAttribute("member", findApply.getMember());


        return "hwang/member/payment/paymentForm";
    }

    @PostMapping("payment/submit")
    public String paymentResult(@RequestParam("applyId") Long applyId, Model model) {

        System.out.println("applyId = " + applyId);
        int payment = memberService.payment(applyId);
        model.addAttribute("payment", payment);
        return "redirect:/payment/success";
    }



    @GetMapping("payment/success")
    public String paymentResult(HttpSession session, Model model) {
        Long memberId = (Long) session.getAttribute("loginId");
        Member member = memberRepository.findMember(memberId);
        model.addAttribute("payment", member.getDeposit());

        return "hwang/member/payment/paymentResult";

    }
}
