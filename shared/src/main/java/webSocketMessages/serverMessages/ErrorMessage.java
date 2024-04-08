package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage {

    String message;

    public ErrorMessage(ServerMessageType serverMessageType, String message) {
        super(serverMessageType);
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
