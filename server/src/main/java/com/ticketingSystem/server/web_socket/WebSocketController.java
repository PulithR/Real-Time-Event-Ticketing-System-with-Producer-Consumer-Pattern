package com.ticketingSystem.server.web_socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void broadcastSimulationUpdate(String message) {
        messagingTemplate.convertAndSend("/topic/simulation", message);
    }
}