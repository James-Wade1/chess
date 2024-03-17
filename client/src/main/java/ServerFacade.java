import model.UserData;
import responseException.ResponseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {

    String serverURL = "";
    public ServerFacade(String serverURL) {
        this.serverURL = serverURL;
    }

    public void register(UserData newUser) throws ResponseException {
        String path = "/user";
        this.makeRequest("POST", path, newUser, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverURL + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            if (method.equals("POST")) {
                http.setDoOutput(true);
                http.connect();
            }
            else {
                http.connect();
            }
            return null;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {

    }
}
