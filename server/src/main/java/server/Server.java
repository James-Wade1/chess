package server;

import spark.*;
import handlers.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        clearHandler myClearHandler = new clearHandler();

        Spark.delete("/db", myClearHandler::clearData);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
