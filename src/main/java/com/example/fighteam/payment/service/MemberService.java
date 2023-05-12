package com.example.fighteam.payment.service;

import com.example.fighteam.payment.domain.History;
import com.example.fighteam.payment.domain.HistoryType;
import com.example.fighteam.payment.domain.Member;
import com.example.fighteam.payment.repository.ApplyRepository;
import com.example.fighteam.payment.repository.HistoryRepository;
import com.example.fighteam.payment.repository.MemberRepository;
import com.example.fighteam.payment.repository.PostRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final HistoryRepository historyRepository;
    private final ApplyRepository applyRepository;
    private final PostRepositoryJpa postRepository;
    public Long join(Member member){
        memberRepository.save(member);
        return member.getId();
    }
    @Transactional
    public int chargeDeposit(Long id, int cost){ //보증금 충전
        Member findMember = memberRepository.findMember(id);
        int deposit = findMember.getDeposit();
        deposit += cost;
        findMember.setDeposit(deposit);

        History history = new History(findMember, HistoryType.CHARGE, cost, deposit);
        historyRepository.saveHistory(history);
        //DTO에 member id, type, cost, 입금날짜, 잔액 담아서 controller로 반환

        return deposit;

    }

    public Long login(String email, String password) {
        Member findMember = memberRepository.findByEmail(email);
        if (findMember.getPasswd().equals(password)) {
            return findMember.getId();
        }
        return 0L;

    }



}
