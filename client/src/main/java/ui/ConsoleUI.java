package ui;

import responseException.ResponseException;
import ui.EscapeSequences;
import userStates.ChessClient;

import java.util.Scanner;

public class ConsoleUI implements NotificationHandler {
    private ChessClient client;

    public ConsoleUI(String serverURL) {
        try {
            this.client = new ChessClient(serverURL, this);
        } catch (ResponseException ex) {
            System.out.println("Expected not error but got " + ex.getMessage());
        }
    }

    public void run() {
        Scanner userInput = new Scanner(System.in);
        String result = "";
        System.out.println(EscapeSequences.BLACK_KNIGHT + "Welcome to your Chessgame. Select from the options below:");
        System.out.println(client.help());
        printPrompt();

        while(!result.equals("Quit")) {
            String line = userInput.nextLine();

            try {
                result = client.eval(line);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_YELLOW + result);
            } catch (Throwable ex) {
                System.out.println(ex.getMessage());
            }
            printPrompt();
        }
        System.out.println();
        System.out.println(EscapeSequences.SET_TEXT_COLOR_YELLOW + "Thanks for playing!");
    }

    private void printPrompt() {
        System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY + client.getUserState() + " >>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }
    @Override
    public void notify(String message) {
        System.out.println();
        System.out.println(message);
        printPrompt();
    }
}
