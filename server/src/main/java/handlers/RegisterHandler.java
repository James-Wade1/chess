package handlers;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;
import responseException.ResponseException;
import service.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler extends Handler {

    RegisterService myRegisterService;

    public RegisterHandler(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        super(authDAO, gameDAO, userDAO);
        myRegisterService = new RegisterService(myAuthDAO, myGameDAO, myUserDAO);
    };

    public Object registerUser(Request req, Response res) throws ResponseException {
        UserData newUser = new Gson().fromJson(req.body(), UserData.class);

        AuthData newAuthData = myRegisterService.registerUser(newUser);

        res.status(200);
        return new Gson().toJson(newAuthData);
    }
}
