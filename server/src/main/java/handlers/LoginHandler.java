package handlers;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.UserData;
import responseException.ResponseException;
import service.LoginService;
import spark.Request;
import spark.Response;

public class LoginHandler extends Handler {

    LoginService myLoginService;
    public LoginHandler(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        super(authDAO, gameDAO, userDAO);
        myLoginService = new LoginService(myAuthDAO, myGameDAO, myUserDAO);
    };

    public Object loginUser(Request req, Response res) throws ResponseException {
        UserData returningUser = new Gson().fromJson(req.body(), UserData.class);

        res.status(200);
        return new Gson().toJson(myLoginService.loginUser(returningUser));
    }
}
