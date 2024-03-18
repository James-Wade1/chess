import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import responseException.ResponseException;

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

    public ServerFacade(String serverURL) {
        this.serverURL = serverURL;
    }

    public void register(UserData newUser) throws ResponseException {
        String path = "/user";
        this.makeRequest("POST", path, newUser, null, false);
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
            throw new ResponseException(200, "Failure: " + status);
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
