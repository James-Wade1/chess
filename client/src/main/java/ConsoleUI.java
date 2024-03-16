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
        System.out.println(EscapeSequences.BLACK_KNIGHT + "Welcome to your Chessgame. Select from the options below:");
        System.out.println(client.help());

        while(!result.equals("Quit")) {
            printPrompt();
            String line = userInput.nextLine();

            try {
                result = client.eval(line);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_YELLOW + result);
            } catch (Throwable ex) {

            }
        }
        System.out.println();
        System.out.println(EscapeSequences.SET_TEXT_COLOR_YELLOW + "Thanks for playing!");
    }

    private void printPrompt() {
        System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY + client.getUserState() + " >>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }
}
