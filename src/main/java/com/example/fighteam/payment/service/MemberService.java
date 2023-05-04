package com.example.fighteam.payment.service;

import com.example.fighteam.payment.domain.Apply;
import com.example.fighteam.payment.domain.History;
import com.example.fighteam.payment.domain.HistoryType;
import com.example.fighteam.payment.domain.Member;
import com.example.fighteam.payment.repository.ApplyRepository;
import com.example.fighteam.payment.repository.HistoryRepository;
import com.example.fighteam.payment.repository.MemberRepository;
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
    @Transactional
    public int payment(Long applyId) { // 보증금 결제
        // apply와 member를 조인해서 쿼리해와서 결제후 결제 내역을 history에 저장
        // member deposit은 cost만큼빼고 apply의 deposit은 cost만큼 더해야함
//        Member findMember = memberRepository.findMember(member.getId());
//        int deposit = findMember.getDeposit();

        Apply findApply = applyRepository.findOne(applyId);
        Member findMember = findApply.getMember();
        int cost = findApply.getPost().getPostDeposit();
        //0원 보다 작으면 에러 발생
        findMember.minusDeposit(cost);
        findApply.plusUserDeposit(cost);

        int userDeposit = findMember.getDeposit();

        History history = new History(findMember, findApply, HistoryType.PAYMENT, cost, userDeposit);
        historyRepository.saveHistory(history);

        return userDeposit;
    }

    public Long login(String email, String password) {
        Member findMember = memberRepository.findByEmail(email);
        if (findMember.getPasswd().equals(password)) {
            return findMember.getId();
        }
        return 0L;

    }



}
