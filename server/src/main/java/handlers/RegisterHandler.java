package handlers;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import responseException.ResponseException;
import service.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler {

    RegisterService myRegisterService = new RegisterService();

    public RegisterHandler() {};

    public Object registerUser(Request req, Response res) throws ResponseException {
        UserData newUser = new Gson().fromJson(req.body(), UserData.class);
        if (newUser.username().isEmpty() || newUser.password().isEmpty() || newUser.email().isEmpty()) {
            res.status(400);
            throw new ResponseException(400, "Error: bad request");
        }

        String authToken = myRegisterService.registerUser(newUser.username(), newUser.password(), newUser.email());

        res.status(200);
        return new Gson().toJson(new AuthData(authToken, newUser.username()));
    }
}
