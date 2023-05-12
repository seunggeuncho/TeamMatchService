package com.example.fighteam.payment.controller;

import com.example.fighteam.post.domain.Post;
import com.example.fighteam.payment.repository.PostRepositoryJpa;

import com.example.fighteam.payment.service.ChargeService;
import com.example.fighteam.payment.service.PaymentService;
import com.example.fighteam.user.domain.repository.User;
import com.example.fighteam.user.service.UserService;
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

    private final PostRepositoryJpa postRepository;
    private final PaymentService paymentService;
    private final UserService userService;
    private final ChargeService chargeService;

    @PostMapping("/payment")
    public String payment(@RequestParam("post_id") Long postId, HttpSession session, Model model) {
        //나중에 postId도 파라미터로 받아와야함

        Long id = (Long) session.getAttribute("loginId");
        //  Apply findApply = applyRepository.findByMember(id); -> 쿼리가 3방 나감
        // apply1번 + getPost()1번 getMember() 1번 -> 페치 조인으로 한방에 불러오도록 수정

        // apply가 아니라 post를 받아와야함
//        Apply findApply = applyRepository.findByMemberWithPost(id);

//        Post findPost = postRepository.findById(postId).orElse(null);

        Post findPost = postRepository.findById(postId).orElse(null);
//        System.out.println("findPost.getId() = " + findPost.getId());
        model.addAttribute("post", findPost);
        model.addAttribute("member", userService.findUser(id));


        return "payment/member/payment/paymentForm2";
    }

    @PostMapping("payment/submit")
    public String paymentSubmit(@RequestParam("post.id") Long postId, Model model, HttpSession session) {

        Long loginId = (Long) session.getAttribute("loginId");

        //post.id, apply에들어갈 userdeposit
//        System.out.println("postId = " + postId);
        //apply 저장을 어떻게 할지 -> 내가하는게 편함 돈 관리여서
        int payment = paymentService.payment(postId, loginId);
        model.addAttribute("payment", payment);
        return "redirect:/payment/success";
    }



    @GetMapping("payment/success")
    public String paymentResult(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("loginId");
        User user = userService.findUser(userId);
        model.addAttribute("payment", user.getDeposit());

        return "payment/member/payment/paymentResult";

    }
}
