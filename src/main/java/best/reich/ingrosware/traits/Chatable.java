package best.reich.ingrosware.traits;

import best.reich.ingrosware.util.other.chat.ChatBuilder;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/14/2020
 **/
public interface Chatable {

    default ChatBuilder chatBuilderFactory() {
        return new ChatBuilder();
    }

    default ChatBuilder clientChatMessageFactory() {
        return new ChatBuilder().appendPrefix();
    }

    default ChatBuilder cb() {
        return chatBuilderFactory();
    }

    default ChatBuilder clientChatMsg() {
        return clientChatMessageFactory();
    }

}
