package server;

import com.google.gson.Gson;
import handlers.ExceptionHandler;
import responseException.*;
import spark.*;
import handlers.*;
import responseException.ResponseException;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        ClearHandler myClearHandler = new ClearHandler();
        RegisterHandler myRegisterHandler = new RegisterHandler();

        ExceptionHandler myExceptionHandler = new ExceptionHandler();

        Spark.delete("/db", myClearHandler::clearData);
        Spark.post("/user", myRegisterHandler::registerUser);

        Spark.exception(ResponseException.class, myExceptionHandler::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


}
