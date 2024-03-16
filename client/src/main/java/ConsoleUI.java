import chess.ChessPiece;
import ui.EscapeSequences;

import java.util.Scanner;

public class ConsoleUI {
    private ChessClient client;

    public ConsoleUI(String serverURL) {
        this.client = new ChessClient(serverURL);
    }

    public void run() {
        Scanner userInput = new Scanner(System.in);
        String result = "";
        System.out.println("Welcome to your Chessgame. Select from the options below:");
        System.out.println(client.help());

        while(!result.equals("Quit")) {
            printPrompt();
            String line = userInput.nextLine();

            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (Throwable ex) {

            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_BLACK + client.getUserState() + " >>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }
}
