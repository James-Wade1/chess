import chess.ChessBoard;
import ui.EscapeSequences;

public class GameplayClient {

    ServerFacade server;

    public GameplayClient(ServerFacade server) {
        this.server = server;
    }

    public String printBoard() {
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        String boardStr = board.toString();
        var rows = boardStr.split("\n");

        StringBuilder output = new StringBuilder();
        output.append(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
        int i = 0;
        for (var row : rows) {
            for (var c : row.toCharArray()) {
                switch(c) {
                    case '|' -> output.append(EscapeSequences.BORDER);
                    case ' ' -> output.append(EscapeSequences.EMPTY);
                    default -> {
                        output.append(EscapeSequences.SET_TEXT_COLOR_WHITE);
                        output.append(parseChessCharacter(c));
                        output.append(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
                    }
                }
            }
            if (i != 7) {
                output.append("\n");
            }
            i++;
        }

        output.append(EscapeSequences.SET_TEXT_COLOR_WHITE);
        return output.toString();
    }

    private String parseChessCharacter(char c) {
        if (Character.isLowerCase(c)) {
            return switch(c) {
                case 'p' -> EscapeSequences.WHITE_PAWN;
                case 'r' -> EscapeSequences.WHITE_ROOK;
                case 'b' -> EscapeSequences.WHITE_BISHOP;
                case 'n' -> EscapeSequences.WHITE_KNIGHT;
                case 'k' -> EscapeSequences.WHITE_KING;
                case 'q' -> EscapeSequences.WHITE_QUEEN;
                default -> " ";
            };
        }
        else {
            return switch(c) {
                case 'P' -> EscapeSequences.BLACK_PAWN;
                case 'R' -> EscapeSequences.BLACK_ROOK;
                case 'B' -> EscapeSequences.BLACK_BISHOP;
                case 'N' -> EscapeSequences.BLACK_KNIGHT;
                case 'K' -> EscapeSequences.BLACK_KING;
                case 'Q' -> EscapeSequences.BLACK_QUEEN;
                default -> " ";
            };
        }
    }
}
