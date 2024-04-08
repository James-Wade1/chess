package webSocketMessages.serverMessages;

public class NotificationMessage extends ServerMessage {

    public String message;

    public NotificationMessage(ServerMessageType serverMessageType, String message) {
        super(serverMessageType);
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
