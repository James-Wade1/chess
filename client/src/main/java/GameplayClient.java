import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import ui.EscapeSequences;

public class GameplayClient {

    ServerFacade server;

    public GameplayClient(ServerFacade server) {
        this.server = server;
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

    private String parseBoard(String board) {
        String whiteBottom = printWhiteBottom(board);
        return printWhiteBottom(board) + "\n\n" + printBlackBottom(board);
    }

    private String printWhiteBottom(String board) {
        var rows = board.split("\n");

        StringBuilder output = new StringBuilder();
        output.append(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
        int numRow = 8;
        output.append("   \u2009\u200A");
        for (char col = 'a'; col < 'i'; col++) {
            output.append("\u2000");
            output.append(col);
            output.append("\u2000");
        }
        output.append("\n");

        for (var row : rows) {
            output.append(String.format(" %d ", numRow));
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
            output.append(String.format(" %d ", numRow));
            if (numRow != 1) {
                output.append("\n");
            }
            numRow--;
        }
        output.append("\n   \u2009\u200A");
        for (char col = 'a'; col < 'i'; col++) {
            output.append("\u2000");
            output.append(col);
            output.append("\u2000");
        }
        return output.toString();
    }

    private String printBlackBottom(String board) {
        board = new StringBuilder(board).reverse().toString();
        var rows = board.split("\n");

        StringBuilder output = new StringBuilder();
        output.append(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
        int numRow = 1;
        output.append("   \u2009\u200A");
        for (char col = 'h'; col >= 'a'; col--) {
            output.append("\u2000");
            output.append(col);
            output.append("\u2000");
        }
        output.append("\n");

        for (var row : rows) {
            output.append(String.format(" %d ", numRow));
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
            output.append(String.format(" %d ", numRow));
            if (numRow != 8) {
                output.append("\n");
            }
            numRow++;
        }
        output.append("\n   \u2009\u200A");
        for (char col = 'h'; col >= 'a'; col--) {
            output.append("\u2000");
            output.append(col);
            output.append("\u2000");
        }

        return output.toString();
    }
}
