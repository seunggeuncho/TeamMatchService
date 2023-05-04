package com.example.fighteam.chat.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : 김효준
 * @fileName : ChatRoomRepository
 * @since : 2023/04/26
 */
@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
}
