package com.example.fighteam.payment.controller;

import com.example.fighteam.payment.domain.dto.PaymentDto;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final PostRepositoryJpa postRepository;
    private final PaymentService paymentService;
    private final UserService userService;
    private final ChargeService chargeService;

//    @PostMapping("/payment")
//    public String payment(@RequestParam("post_id") Long postId, HttpSession session, Model model) {
//
//        Long id = (Long) session.getAttribute("loginId");
//
//        Post findPost = postRepository.findById(postId).orElse(null);
//        model.addAttribute("post", findPost);
//        String paymentError = "";
//        model.addAttribute("member", userService.findUser(id));
//        model.addAttribute("paymentError", paymentError);
//
//
//        return "payment/member/payment/paymentForm2";
//    }
//
//    @PostMapping("payment/submit")
//    public String paymentSubmit(@RequestParam("post.id") Long postId,  Model model, HttpSession session) {
//
//
//        Long loginId = (Long) session.getAttribute("loginId");
//
//
//        int payment = paymentService.payment(postId, loginId);
//        model.addAttribute("payment", payment);
//        return "redirect:/payment/success";
//    }



    @PostMapping("/payment")
    public String payment(@RequestParam("post_id") Long postId, HttpSession session, Model model) {

        Long id = (Long) session.getAttribute("loginId");

        Post findPost = postRepository.findById(postId).orElse(null);
        User findUser = userService.findUser(id);
        PaymentDto paymentDto = PaymentDto.builder()
                        .postId(findPost.getId())
                        .postDeposit(findPost.getDeposit())
                        .postTitle(findPost.getTitle())
                        .userId(findUser.getId())
                        .userDeposit(findUser.getDeposit())
                        .build();
        model.addAttribute("paymentDto", paymentDto);
        model.addAttribute("errorMessage", null);

        return "payment/member/payment/paymentForm2";
    }

    @PostMapping("payment/submit")
    public String paymentSubmit(@ModelAttribute("paymentDto") PaymentDto paymentDto, BindingResult result,  Model model, HttpSession session) {
        if(paymentDto.getUserDeposit() - paymentDto.getPostDeposit() < 0){
            model.addAttribute("errorMessage", "잔액이 부족합니다. 보증금을 충전해주세요.");
            return "payment/member/payment/paymentForm2";
        }

        Long loginId = (Long) session.getAttribute("loginId");
        System.out.println("paymentDto = " + paymentDto);

        int payment = paymentService.payment(paymentDto.getPostId(), loginId);
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
    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalStateException(IllegalStateException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "payment/member/payment/paymentForm2";
    }
}
