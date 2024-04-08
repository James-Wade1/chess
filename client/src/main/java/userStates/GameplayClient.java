package userStates;

import responseException.ResponseException;
import serverFacade.ServerFacade;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import ui.EscapeSequences;
import ui.NotificationHandler;
import websocket.GameHandler;
import websocket.WebSocketFacade;

import java.util.Arrays;

public class GameplayClient implements GameHandler {

    ServerFacade server;
    NotificationHandler notificationHandler;

    WebSocketFacade wsFacade;

    private static final String[] backgroundColors = {EscapeSequences.SET_BG_COLOR_TAN, EscapeSequences.SET_BG_COLOR_LIGHT_GREEN};

    public GameplayClient(ServerFacade server, String url, NotificationHandler notificationHandler) throws ResponseException {
        this.server = server;
        this.notificationHandler = notificationHandler;
        this.wsFacade = new WebSocketFacade(url, this);
    }

    public String help() {
        return """
                - Help
                - Redraw
                - Leave
                - MakeMove
                - Resign
                - Highlight
                """;
    }

    @Override
    public void updateGame(ChessGame game) {
        return;
    }

    @Override
    public void printMessage(String message) {
        notificationHandler.printNotification(message);
    }

    public String eval(String userInput) {
        var tokens = userInput.split(" ");
        String cmd = (tokens.length > 0) ? tokens[0] : "Help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "Help" -> help();
            case "Redraw" -> "";
            case "Leave" -> "";
            case "MakeMove" -> "";
            case "Resign" -> "";
            case "Highlight" -> "";
            default -> "Unknown command. Please try again";
        };
    }

    public String printBoard() {
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        board.addPiece(new ChessPosition(3,1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN, true));
        board.removePiece(new ChessPosition(2,1));
        String boardStr = board.toString();

        return parseBoard(boardStr);
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

    private String parseBoard(String board) {
        return printWhiteBottom(board) + "\n\n" + printBlackBottom(board);
    }

    private String printWhiteBottom(String board) {
        var rows = board.split("\n");
        int i = 0;
        boolean toggle = false;

        StringBuilder output = new StringBuilder();
        output.append(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
        output.append(EscapeSequences.SET_BG_COLOR_TAN);
        int numRow = 8;
        output.append("   \u2009\u200A");
        for (char col = 'a'; col < 'i'; col++) {
            output.append("\u2000");
            output.append(col);
            output.append("\u2000");
        }
        output.append("\u2009\u200A   ");
        output.append(EscapeSequences.RESET_BG_COLOR);
        output.append("\n");

        for (var row : rows) {
            i = toggle ? 0 : 1;
            toggle = !toggle;
            output.append(EscapeSequences.SET_BG_COLOR_TAN);
            output.append(String.format(" %d ", numRow));
            output.append(EscapeSequences.RESET_BG_COLOR);
            for (var c : row.toCharArray()) {
                switch(c) {
                    case '|' -> output.append(EscapeSequences.BORDER);
                    case ' ' -> {
                        i = toggle ? 0 : 1;
                        toggle = !toggle;
                        output.append(backgroundColors[i]);
                        output.append(EscapeSequences.EMPTY);
                        output.append(EscapeSequences.RESET_BG_COLOR);
                    }
                    default -> {
                        i = toggle ? 0 : 1;
                        toggle = !toggle;
                        output.append(backgroundColors[i]);
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
            numRow--;
        }
        output.append(EscapeSequences.SET_BG_COLOR_TAN);
        output.append("   \u2009\u200A");
        for (char col = 'a'; col < 'i'; col++) {
            output.append("\u2000");
            output.append(col);
            output.append("\u2000");
        }
        output.append("\u2009\u200A   ");
        output.append(EscapeSequences.RESET_BG_COLOR);

        return output.toString();
    }

    private String printBlackBottom(String board) {
        board = new StringBuilder(board).reverse().toString();
        var rows = board.split("\n");

        int i = 0;
        boolean toggle = false;

        StringBuilder output = new StringBuilder();
        output.append(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
        output.append(EscapeSequences.SET_BG_COLOR_TAN);
        int numRow = 1;
        output.append("   \u2009\u200A");
        for (char col = 'h'; col >= 'a'; col--) {
            output.append("\u2000");
            output.append(col);
            output.append("\u2000");
        }
        output.append("\u2009\u200A   ");
        output.append(EscapeSequences.RESET_BG_COLOR);
        output.append("\n");

        for (var row : rows) {
            i = toggle ? 0 : 1;
            toggle = !toggle;
            output.append(EscapeSequences.SET_BG_COLOR_TAN);
            output.append(String.format(" %d ", numRow));
            output.append(EscapeSequences.RESET_BG_COLOR);
            for (var c : row.toCharArray()) {
                switch(c) {
                    case '|' -> output.append(EscapeSequences.BORDER);
                    case ' ' -> {
                        i = toggle ? 0 : 1;
                        toggle = !toggle;
                        output.append(backgroundColors[i]);
                        output.append(EscapeSequences.EMPTY);
                        output.append(EscapeSequences.RESET_BG_COLOR);
                    }
                    default -> {
                        i = toggle ? 0 : 1;
                        toggle = !toggle;
                        output.append(backgroundColors[i]);
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
            numRow++;
        }
        output.append(EscapeSequences.SET_BG_COLOR_TAN);
        output.append("   \u2009\u200A");
        for (char col = 'h'; col >= 'a'; col--) {
            output.append("\u2000");
            output.append(col);
            output.append("\u2000");
        }

        output.append("\u2009\u200A   ");
        output.append(EscapeSequences.RESET_BG_COLOR);

        return output.toString();
    }


}



