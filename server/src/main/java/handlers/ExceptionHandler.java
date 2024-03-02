package handlers;

import com.google.gson.Gson;
import responseException.ErrorMessage;
import responseException.ResponseException;
import spark.Request;
import spark.Response;

public class ExceptionHandler {

    public ExceptionHandler() {}
    public void responseExceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.statusCode());
        res.body(new Gson().toJson(new ErrorMessage(ex.getMessage())));
    }

    public void generalExceptionHandler(Exception ex, Request req, Response res) {
        res.status(500);
        res.body(new Gson().toJson(new ErrorMessage(ex.getMessage())));
    }
}
