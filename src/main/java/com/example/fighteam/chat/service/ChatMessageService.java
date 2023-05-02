package com.example.fighteam.chat.service;

import com.example.fighteam.chat.domain.dto.ChatMessageRequestDto;
import com.example.fighteam.chat.domain.repository.ChatMessage;
import com.example.fighteam.chat.domain.repository.ChatMessageRepository;
import com.example.fighteam.chat.domain.repository.ChatRoom;
import com.example.fighteam.chat.domain.repository.ChatRoomRepository;
import com.example.fighteam.user.domain.repository.User;
import com.example.fighteam.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : 김효준
 * @fileName : ChatMessageService
 * @since : 2023/05/02
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ChatMessageService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public void messageSave(ChatMessageRequestDto messageDto){
        ChatRoom chatRoom = chatRoomRepository.findById(Long.parseLong(messageDto.getRoomId())).orElse(null);
        User sendUser = userRepository.findById(Long.parseLong(messageDto.getSender())).orElse(null);
        if (chatRoom != null && sendUser != null) {
            ChatMessage message = new ChatMessage(messageDto.getMessage(), sendUser, chatRoom);
            chatMessageRepository.save(message);
        }
    }


}
