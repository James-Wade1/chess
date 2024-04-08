package serverFacade;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import responseException.ResponseException;
import model.GameIDResponse;
import model.GameResponseClass;
import model.PlayerJoinRequest;
import websocket.WebSocketFacade;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {

    String serverURL = "";
    private AuthData authToken = null;

    public ServerFacade(String serverURL) throws ResponseException {
        this.serverURL = serverURL;
    }

    public void registerUser(UserData newUser) throws ResponseException {
        String path = "/user";
        this.makeRequest("POST", path, newUser, null, false);
    }

    public void loginUser(UserData returningUser) throws ResponseException {
        String path = "/session";
        authToken = this.makeRequest("POST", path, returningUser, AuthData.class, false);
    }

    public void logoutUser() throws ResponseException {
        String path = "/session";
        this.makeRequest("DELETE", path, null, null, true);
    }

    public void createGame(GameData newGame) throws ResponseException {
        String path = "/game";
        this.makeRequest("POST", path, newGame, GameIDResponse.class, true);
    }

    public GameResponseClass listGames() throws ResponseException {
        String path = "/game";
        return this.makeRequest("GET", path, null, GameResponseClass.class, true);
    }

    public void joinGame(PlayerJoinRequest joinRequest) throws ResponseException {
        String path = "/game";
        this.makeRequest("PUT", path, joinRequest, null, true);
    }

    public void delete() throws ResponseException {
        String path = "/db";
        this.makeRequest("DELETE", path, null, null, false);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, boolean authTokenNeeded) throws ResponseException {
        try {
            URL url = (new URI(serverURL + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            if (authTokenNeeded) {
                http.addRequestProperty("authorization", authToken.authToken());
            }
            if (request != null) {
                http.setDoOutput(true);
                writeBody(request, http);
            }

            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        String requestData = new Gson().toJson(request);

        try (OutputStream requestBody = http.getOutputStream()) {
            requestBody.write(requestData.getBytes());
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        int status = http.getResponseCode();
        if (status != 200) {
            throw new ResponseException(status, "Failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream responseBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(responseBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }

        return response;
    }
}
