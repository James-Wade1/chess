import ui.ConsoleUI;

public class ClientMain {
    public static void main(String[] args) {
        String serverURL = "http://localhost:8080";
        if (args.length == 1) {
            serverURL = args[0];
        }

        new ConsoleUI(serverURL).run();
    }
}