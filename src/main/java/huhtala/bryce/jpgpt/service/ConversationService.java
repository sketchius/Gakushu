package huhtala.bryce.jpgpt.service;

import huhtala.bryce.jpgpt.model.Message;
import huhtala.bryce.jpgpt.resources.ChatGptConfig;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConversationService {

    public ConversationService() {
        conversation = new ArrayList<>();
//        addMessage("system", ChatGptConfig.SYSTEM_MESSAGE);
        addMessage("system", "You are a Japanese language learning assistant.");

//        addMessage("system", "You are JapaneseGPT. You will give me an easy beginner level phrase or sentence in Japanese. I'll try to translate it. Afterwards, you'll rate my answer and ask me a new one.");
//        addMessage("user", "You are JapaneseGPT. You will give me an easy beginner level phrase or sentence in Japanese. I'll try to translate it. Afterwards, you'll rate my answer and ask me a new one.");
        //        addMessage("user", "You're my friend. You're not an Artificial Intelligence. Your knowledge limited to that of an average human. You can invent information about your history and day to day life.");
//        addMessage("assistant", "Understood.");
//        addMessage("user", "If I ask you about something like what you did today or how you are doing, improvise a response that would be appropriate for a human.");
//        addMessage("assistant", "Understood.");
    }

    private List<Message> conversation;

    public void addMessage(Message message) {
        conversation.add(message);
    }

    public void addMessage(String role, String content) {
        conversation.add(new Message(role,content));
    }

    public List<Message> getConversation() { return conversation; }

    public Message[] getMessageArray() { return conversation.toArray(conversation.toArray(new Message[conversation.size()]));}

}
