import java.util.Scanner;

public class ConsoleUI {

    private String serverURL;
    private ChessClient client;

    public ConsoleUI(String serverURL) {
        this.serverURL = serverURL;
        this.client = new ChessClient(serverURL);
    }

    public void run() {
        Scanner userInput = new Scanner(System.in);
        var result = "";
        System.out.println("Welcome to your Chessgame. Select from the options below:");
        System.out.println();
        while(!result.equals("Quit")) {

        }
    }
}
