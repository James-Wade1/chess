package server;

import dataAccess.*;
import handlers.ExceptionHandler;
import service.SystemService;
import spark.*;
import handlers.*;
import responseException.ResponseException;

import java.util.HashSet;

public class Server {

    public int run(int desiredPort) {
        /*Setup website*/
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        /*Setup DAOs to pass into handlers*/
        AuthDAO myAuthDAO = null;
        UserDAO myUserDAO = null;
        GameDAO myGameDAO = null;

        HashSet<DAO> myDAOs = SystemService.instantiateDAOs();

        for (DAO myDAO : myDAOs) {
            if (myDAO instanceof AuthDAO) {
                myAuthDAO = (AuthDAO)myDAO;
            }
            else if (myDAO instanceof UserDAO) {
                myUserDAO = (UserDAO)myDAO;
            }
            else if (myDAO instanceof GameDAO) {
                myGameDAO = (GameDAO)myDAO;
            }
            else {
                throw new RuntimeException("Extra DAO not in use");
            }
        }

        if (myAuthDAO == null || myGameDAO == null || myUserDAO == null) {
            throw new RuntimeException("Extra DAO not in use");
        }

        /*Construct Handlers*/
        SystemHandler mySystemHandler = new SystemHandler(myAuthDAO, myGameDAO, myUserDAO);
        RegisterHandler myRegisterHandler = new RegisterHandler(myAuthDAO, myGameDAO, myUserDAO);
        LoginHandler myLoginHandler = new LoginHandler(myAuthDAO, myGameDAO, myUserDAO);
        LogoutHandler myLogoutHandler = new LogoutHandler(myAuthDAO, myGameDAO, myUserDAO);
        ExceptionHandler myExceptionHandler = new ExceptionHandler();

        /*Define HTTP endpoints*/
        Spark.delete("/db", mySystemHandler::clearData);
        Spark.post("/user", myRegisterHandler::registerUser);
        Spark.post("/session", myLoginHandler::loginUser);
        Spark.delete("/session", myLogoutHandler::logoutUser);

        Spark.exception(ResponseException.class, myExceptionHandler::responseExceptionHandler);
        Spark.exception(Exception.class, myExceptionHandler::generalExceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


}
