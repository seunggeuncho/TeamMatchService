package com.example.fighteam.chat.controller;

import com.example.fighteam.chat.domain.dto.ChatMessageResponseDto;
import com.example.fighteam.chat.domain.repository.ChatMessageRepository;
import com.example.fighteam.chat.domain.repository.ChatRoom;
import com.example.fighteam.chat.domain.repository.ChatRoomRepository;
import com.example.fighteam.chat.service.ChatRoomService;
import com.example.fighteam.user.domain.repository.User;
import com.example.fighteam.user.domain.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : 김효준
 * @fileName : ChatController
 * @since : 2023/05/02
 */
@RequiredArgsConstructor
@Slf4j
@Controller
public class ChatController {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository cMessageRepository;
    private final ChatRoomService chatService;

    @PostMapping("/chat/creating")
    public String createChatRoom(@RequestParam("postUser") Long findUser, HttpSession session){
        Long[] chatMember = {findUser, (Long) session.getAttribute("userId")};
        Long chatRoomId = chatService.createChat(chatMember);
        return "redirect:/chat/room/enter/"+chatRoomId;
    }

    @GetMapping("/chat/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable Long roomId){
        return chatService.roomInfoReturn(roomId);
    }

    @GetMapping("/chat/room/user/{userId}")
    @ResponseBody
    public User sendUser(@PathVariable Long userId){
        return userRepository.findById(userId).orElseThrow(null);
    }

    @GetMapping("/chat/room/enter/{roomId}")
    public String enterChatroom(@PathVariable Long roomId, Model model, HttpSession session) {
        try {
            Long loginUserId = (Long) session.getAttribute("userId");
            model.addAttribute("nowSender", loginUserId);
            model.addAttribute("roomInfo", roomId);
            return "chat/chat";
        } catch (Exception e) {
            log.error("NOT FIND CHATROOM : {}",e);
            return "/";
        }
    }

    //message내용 가져오기
    @GetMapping("/room/{roomId}/messages")
    @ResponseBody
    public List<ChatMessageResponseDto> messageInfo(@PathVariable Long roomId){
        return chatService.messageInfoReturn(roomId);
    }
}
