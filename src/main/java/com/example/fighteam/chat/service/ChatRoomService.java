package com.example.fighteam.chat.service;

import com.example.fighteam.chat.domain.dto.ChatMessageRequestDto;
import com.example.fighteam.chat.domain.repository.ChatRoom;
import com.example.fighteam.chat.domain.repository.ChatRoomRepository;
import com.example.fighteam.user.domain.repository.User;
import com.example.fighteam.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    public Long createChat(Long[] users) {
        List<User> chatMember = userRepository.findAllById(Arrays.asList(users));
        ChatRoom createdChatRoom = new ChatRoom(chatMember.get(0), chatMember.get(1));
        chatRoomRepository.save(createdChatRoom);
        return createdChatRoom.getId();
    }

    public void sendMessage(ChatMessageRequestDto chatMessageRequestDto) {

    }

}
