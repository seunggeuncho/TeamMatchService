package com.example.fighteam.payment.controller;

import com.example.fighteam.payment.domain.History;
import com.example.fighteam.payment.repository.HistoryRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryRepository historyRepository;
    @GetMapping("/test")
    public String test() {
        return "hwang/member/payment/test/test";
    }

    @GetMapping("/history")
    public String getHistory() {


        return "hwang/history/historyList";
    }

    @PostMapping("/historySubmit")
    public String getHistoryList(@RequestParam("type") String type, Model model, HttpSession session) {

        Long id = (Long) session.getAttribute("loginId");
//        System.out.println("id = " + id);
//        System.out.println("type = " + type);
        List<History> findHistory = historyRepository.findByMemberId(id, type);
//        for (History history : findHistory) {
//            System.out.println("history.getDate() = " + history.getDate());
//            System.out.println("history.getCost() = " + history.getCost());
//            System.out.println("history.getBalance() = " + history.getBalance());
//        }
        model.addAttribute("historyList", findHistory);
        return "hwang/history/historyList";
    }
}
