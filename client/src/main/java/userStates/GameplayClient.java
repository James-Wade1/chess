package userStates;

import chess.*;
import responseException.ResponseException;
import serverFacade.ServerFacade;
import ui.EscapeSequences;
import ui.NotificationHandler;
import websocket.GameHandler;
import websocket.WebSocketFacade;

import java.util.Arrays;

public class GameplayClient implements GameHandler {

    ServerFacade server;
    NotificationHandler notificationHandler;

    private WebSocketFacade wsFacade;

    private ChessGame game;

    ChessGame.TeamColor playerColor = null;

    private static final String[] backgroundColors = {EscapeSequences.SET_BG_COLOR_TAN, EscapeSequences.SET_BG_COLOR_LIGHT_GREEN};

    public GameplayClient(ServerFacade server, String url, NotificationHandler notificationHandler) throws ResponseException {
        this.server = server;
        this.notificationHandler = notificationHandler;
        this.wsFacade = new WebSocketFacade(url, this);
        this.game = null;
    }

    public String help() {
        return """
                - Help
                - Redraw
                - Leave
                - MakeMove
                - Resign
                - Highlight <letter> <number>
                """;
    }

    @Override
    public void updateGame(ChessGame game) {
        this.game = game;
        notificationHandler.notify(printBoard());
    }

    @Override
    public void printMessage(String message) {
        notificationHandler.notify(message);
    }

    public String eval(String userInput) throws ResponseException, InvalidMoveException {
        var tokens = userInput.split(" ");
        String cmd = (tokens.length > 0) ? tokens[0] : "Help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "Help" -> help();
            case "Redraw" -> redraw();
            case "Leave" -> "";
            case "MakeMove" -> "";
            case "Resign" -> "";
            case "Highlight" -> highlightMoves(Integer.parseInt(params[1]), params[0].charAt(0));
            default -> "Unknown command. Please try again\n" + help();
        };
    }

    private String redraw() {
        return printBoard();
    }

    private String highlightMoves(int row, char col) throws ResponseException, InvalidMoveException {
        if (row < 1 || row > 8 || col < 'a' || col > 'h') {
            throw new ResponseException(500, "Invalid boundaries");
        }
        String highlightedBoard = this.game.getValidMovesString(row, ((int) col) - 96);
        return printHighlightedBoard(highlightedBoard);
    }

    public void joinGame(String userInput) throws Exception {
        var tokens = userInput.split(" ");
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        if (params.length == 1) {
            wsFacade.joinObserver(userInput, server.getAuthToken());
            this.playerColor = null;
        }
        else {
            wsFacade.joinPlayer(userInput, server.getAuthToken());
            if (params[1].equalsIgnoreCase("WHITE")) {
                this.playerColor = ChessGame.TeamColor.WHITE;
            }
            else {
                this.playerColor = ChessGame.TeamColor.BLACK;
            }
        }
    }

    private String printBoard() {
        if (this.playerColor == null || this.playerColor == ChessGame.TeamColor.WHITE) {
            return printWhiteBottom(this.game.getBoard().toString());
        }
        else {
            return printBlackBottom(this.game.getBoard().toString());
        }
    }

    private String printHighlightedBoard(String board) {
        if (this.playerColor == null || this.playerColor == ChessGame.TeamColor.WHITE) {
            return printWhiteBottom(board);
        }
        else {
            return printBlackBottom(board);
        }
    }

    private String parseChessCharacter(char c) {
        if (Character.isLowerCase(c)) {
            return switch(c) {
                case 'p' -> EscapeSequences.BLACK_PAWN;
                case 'r' -> EscapeSequences.BLACK_ROOK;
                case 'b' -> EscapeSequences.BLACK_BISHOP;
                case 'n' -> EscapeSequences.BLACK_KNIGHT;
                case 'k' -> EscapeSequences.BLACK_KING;
                case 'q' -> EscapeSequences.BLACK_QUEEN;
                default -> " ";
            };
        }
        else {
            return switch(c) {
                case 'P' -> EscapeSequences.WHITE_PAWN;
                case 'R' -> EscapeSequences.WHITE_ROOK;
                case 'B' -> EscapeSequences.WHITE_BISHOP;
                case 'N' -> EscapeSequences.WHITE_KNIGHT;
                case 'K' -> EscapeSequences.WHITE_KING;
                case 'Q' -> EscapeSequences.WHITE_QUEEN;
                default -> " ";
            };
        }
    }

    private String printWhiteBottom(String board) {
        var rows = board.split("\n");
        int i = 0;
        boolean toggle = false;

        StringBuilder output = new StringBuilder();
        int numRow = 8;

        output.append(printHeader('a', 'i', 1));
        output.append("\n");
        output.append(printBody(numRow, -1, toggle, rows));
        output.append(printHeader('a', 'i', 1));

        return output.toString();
    }

    private String printBlackBottom(String board) {
        board = new StringBuilder(board).reverse().toString();
        var rows = board.split("\n");

        int i = 0;
        boolean toggle = false;

        StringBuilder output = new StringBuilder();
        int numRow = 1;

        output.append(printHeader('h', 'a', -1));
        output.append("\n");
        output.append(printBody(numRow, 1, toggle, rows));
        output.append(printHeader('h', 'a', -1));

        return output.toString();
    }

    private String printHeader(char startChar, char endChar, int increment) {
        StringBuilder output = new StringBuilder();
        output.append(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
        output.append(EscapeSequences.SET_BG_COLOR_TAN);
        output.append("   \u2009\u200A");
        for (char col = startChar; (increment == 1 && col < endChar) || (increment == -1 && col >= endChar); col+=increment) {
            output.append("\u2000");
            output.append(col);
            output.append("\u2000");
        }
        output.append("\u2009\u200A   ");
        output.append(EscapeSequences.RESET_BG_COLOR);

        return output.toString();
    }

    private String printBody(int startRow, int increment, boolean toggle, String[] rows) {
        StringBuilder output = new StringBuilder();
        int numRow = startRow;
        int i = 0;
        boolean specialSpotFirstEntry = false;
        for (var row : rows) {
            i = toggle ? 0 : 1;
            toggle = !toggle;
            output.append(EscapeSequences.SET_BG_COLOR_TAN);
            output.append(String.format(" %d ", numRow));
            output.append(EscapeSequences.RESET_BG_COLOR);
            for (var c : row.toCharArray()) {
                switch(c) {
                    case '|' -> output.append(EscapeSequences.BORDER);
                    case 's' -> {
                        if (specialSpotFirstEntry == false) {
                            output.append(EscapeSequences.SET_BG_COLOR_GREEN);
                            specialSpotFirstEntry = true;
                        }
                        else {
                            specialSpotFirstEntry = false;
                        }
                    }
                    case 'v' -> {
                        if (specialSpotFirstEntry == false) {
                            output.append(EscapeSequences.SET_BG_COLOR_YELLOW);
                            specialSpotFirstEntry = true;
                        }
                        else {
                            specialSpotFirstEntry = false;
                        }
                    }
                    case ' ' -> {
                        i = toggle ? 0 : 1;
                        toggle = !toggle;
                        if (specialSpotFirstEntry == false) {
                            output.append(backgroundColors[i]);
                        }
                        output.append(EscapeSequences.EMPTY);
                        output.append(EscapeSequences.RESET_BG_COLOR);
                    }
                    default -> {
                        i = toggle ? 0 : 1;
                        toggle = !toggle;
                        if (specialSpotFirstEntry == false) {
                            output.append(backgroundColors[i]);
                        }
                        output.append(EscapeSequences.SET_TEXT_COLOR_BLACK);
                        output.append(parseChessCharacter(c));
                        output.append(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
                        output.append(EscapeSequences.RESET_BG_COLOR);
                    }
                }
            }
            output.append(EscapeSequences.SET_BG_COLOR_TAN);
            output.append(String.format(" %d ", numRow));
            output.append(EscapeSequences.RESET_BG_COLOR);
            output.append("\n");
            numRow+=increment;
        }

        return output.toString();
    }
}



