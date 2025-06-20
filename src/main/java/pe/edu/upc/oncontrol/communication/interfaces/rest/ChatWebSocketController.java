package pe.edu.upc.oncontrol.communication.interfaces.rest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import pe.edu.upc.oncontrol.communication.domain.model.commands.SendChatMessageCommand;
import pe.edu.upc.oncontrol.communication.domain.services.ChatMessageCommandService;
import pe.edu.upc.oncontrol.communication.interfaces.rest.resources.ChatMessageRequest;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class ChatWebSocketController {
    private final ChatMessageCommandService chatMessageCommandService;
    private final Validator validator;


    public ChatWebSocketController(ChatMessageCommandService chatMessageCommandService, Validator validator) {
        this.chatMessageCommandService = chatMessageCommandService;
        this.validator = validator;
    }

    @MessageMapping("/chat/{doctorUuid}/{patientUuid}/send")
    public void receiveMessage(@DestinationVariable UUID doctorUuid,
                               @DestinationVariable UUID patientUuid,
                               ChatMessageRequest request,
                               SimpMessageHeaderAccessor headerAccessor) {

        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
        String role = (String) headerAccessor.getSessionAttributes().get("role");

        // 👇 Debug temporal
        System.out.println("userId: " + userId);
        System.out.println("role: " + role);

        SendChatMessageCommand command = new SendChatMessageCommand(
                doctorUuid,
                patientUuid,
                role,
                request.getContent(),
                request.getType(),
                request.getFileUrl()
        );

        chatMessageCommandService.handle(command);
    }



}
