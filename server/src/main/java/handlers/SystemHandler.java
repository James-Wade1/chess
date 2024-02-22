package handlers;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import spark.Request;
import spark.Response;

import service.SystemService;

public class SystemHandler extends Handler {

    SystemService mySystemService;

    public SystemHandler(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        super(authDAO, gameDAO, userDAO);
        this.mySystemService = new SystemService(myAuthDAO, myGameDAO, myUserDAO);
    };

    public Object clearData(Request req, Response res) {
        mySystemService.clearData();
        res.status(200);
        return "{}";
    }
}
