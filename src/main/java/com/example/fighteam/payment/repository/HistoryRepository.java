package com.example.fighteam.payment.repository;

import com.example.fighteam.payment.domain.History;
import com.example.fighteam.payment.domain.HistoryType;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HistoryRepository {

    @Autowired
    private EntityManager em;


    public List<History> findByMemberId(Long id, String type) {
        if (type.equals("all")) {
            return em.createQuery("select h from History h where h.user.id =: id", History.class)
                    .setParameter("id", id)
                    .getResultList();

        } else {
            return em.createQuery("select h from History  h where h.user.id =: id and h.type =:type", History.class)
                    .setParameter("id", id)
                    .setParameter("type", HistoryType.valueOf(type))
                    .getResultList();
        }
    }

    public Long saveHistory(History history){
        em.persist(history);
        return history.getId();
    }



}
