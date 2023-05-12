package com.example.fighteam.payment.controller;

import com.example.fighteam.payment.domain.Member;
import com.example.fighteam.payment.repository.MemberRepository;
import com.example.fighteam.payment.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "hwang/member/login";
    }
    @PostMapping("/login")
    public String Login(LoginDto loginDto, HttpSession session) {
        Long loginId = memberService.login(loginDto.email, loginDto.password);
        session.setAttribute("loginId", loginId);

        return "hwang/member/main";
    }

    @GetMapping("/main")
    public String main() {
        return "hwang/member/main";
    }

    public String chargeForm(Model model, HttpSession session) {

        Long loginId = (Long) session.getAttribute("loginId");
        Member findmember = memberRepository.findMember(loginId);
//        System.out.println("findmember.getDeposit() = " + findmember.getDeposit());
        model.addAttribute("balance", findmember.getDeposit());
        return "hwang/member/charge/chargeForm2";
    }

    @RequestMapping("/charge")
    public String chargeFormJson(Model model, HttpSession session) {

        Long loginId = (Long) session.getAttribute("loginId");
        Member findmember = memberRepository.findMember(loginId);
//        System.out.println("findmember.getDeposit() = " + findmember.getDeposit());
        model.addAttribute("balance", findmember.getDeposit());
        return "hwang/member/charge/chargeForm2";
    }

    @PostMapping("/charge/submit")
    public String chargeResult(@RequestParam("charge-amount") int cost, HttpSession session, Model model) {
        Long loginId = (Long) session.getAttribute("loginId");
//        System.out.println("loginId = " + loginId);
        int deposit = memberService.chargeDeposit(loginId, cost);

        return "redirect:/chargeSuccess";
    }

    @GetMapping("/chargeSuccess")
    public String chargeSuceess(HttpSession session, Model model) {
        Long loginId = (Long) session.getAttribute("loginId");
        Member findMember = memberRepository.findMember(loginId);
        model.addAttribute("balance", findMember.getDeposit());

        return "hwang/member/charge/chargeResult";
    }
    @Data
    static class LoginDto {

        private String email;
        private String password;

    }
}

