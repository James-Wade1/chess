package handlers;

import com.google.gson.Gson;
import responseException.ErrorMessage;
import responseException.ResponseException;
import spark.Request;
import spark.Response;

public class ExceptionHandler {

    public ExceptionHandler() {}
    public void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.StatusCode());
        res.body(new Gson().toJson(new ErrorMessage(ex.getMessage())));
    }
}
