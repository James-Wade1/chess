package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage {

    String errorMessage;

    public ErrorMessage(ServerMessageType serverMessageType, String message) {
        super(serverMessageType);
        this.errorMessage = message;
    }

    public String getMessage() {
        return this.errorMessage;
    }
}
