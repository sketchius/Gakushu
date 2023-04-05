package huhtala.bryce.jpgpt.service;

import huhtala.bryce.jpgpt.model.BotRequest;
import huhtala.bryce.jpgpt.model.ChatGptRequest;
import huhtala.bryce.jpgpt.model.ChatGptResponse;
import huhtala.bryce.jpgpt.resources.ApiKeyConfig;
import huhtala.bryce.jpgpt.resources.ChatGptConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ChatGptService {

    @Value("${openAIKey}")
    private String API_KEY = ApiKeyConfig.CHATGPT_API_KEY;

    private static final RestTemplate restTemplate = new RestTemplate();

    private ChatGptResponse getResponse(HttpEntity<ChatGptRequest> chatGptRequestHttpEntity) {
        ResponseEntity<ChatGptResponse> responseEntity = restTemplate.postForEntity(ChatGptConfig.URL,
                chatGptRequestHttpEntity,
                ChatGptResponse.class);
        return responseEntity.getBody();
    }

    public ChatGptResponse askQuestion(BotRequest botRequest, ConversationService conversationService, int maxTokens, String stop) {
        ConversationService newConversationService = new ConversationService();
        if (maxTokens == -1) maxTokens = ChatGptConfig.MAX_TOKEN;
        newConversationService.addMessage("user",botRequest.getMessage());
        return getResponse(
                createRequestEntity(
                        new ChatGptRequest(ChatGptConfig.MODEL,
                                newConversationService.getMessageArray(),
                                maxTokens,
                                ChatGptConfig.TEMPERATURE,
                                ChatGptConfig.TOP_P,
                                stop)
                )
        );
    }


    private HttpEntity<ChatGptRequest> createRequestEntity(ChatGptRequest chatGptRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(ChatGptConfig.MEDIA_TYPE));
        headers.add(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + API_KEY);
        return new HttpEntity<>(chatGptRequest, headers);
    }

}
