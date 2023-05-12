package com.example.fighteam.payment.service;

import com.example.fighteam.payment.domain.*;
import com.example.fighteam.payment.repository.ApplyRepository;
import com.example.fighteam.payment.repository.HistoryRepository;
import com.example.fighteam.payment.repository.MemberRepository;
import com.example.fighteam.payment.repository.PostRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * packageName    : com.example.fighteam.payment.service
 * fileName       : PaymentService
 * author         : jeonghwan
 * date           : 2023/05/11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/05/11        jeonghwan       최초 생성
 */
@RequiredArgsConstructor
@Service
public class PaymentService {
    private final MemberRepository memberRepository;
    private final HistoryRepository historyRepository;
    private final ApplyRepository applyRepository;
    private final PostRepositoryJpa postRepositoryJpa;

    @Transactional
    public int payment(Long postId, Long memberId) { // 보증금 결제


        Post post = postRepositoryJpa.findById(postId).orElse(null);
        Member member = memberRepository.findMember(memberId);

        //Apply 생성
        Apply apply = new Apply(member, post, post.getPostDeposit(), false);
        int cost = post.getPostDeposit();
        //0원 보다 작으면 에러 발생
        member.minusDeposit(cost);
        apply.plusUserDeposit(cost);

        int userDeposit = member.getDeposit();
        // 위의 로직이 끝나고 에러가 없으면 apply를 DB에 저장
        applyRepository.save(apply);

        History history = new History(member, apply, HistoryType.PAYMENT, cost, userDeposit);
        historyRepository.saveHistory(history);

        return userDeposit;
    }

}
