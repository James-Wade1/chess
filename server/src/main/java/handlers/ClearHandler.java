package handlers;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import service.ClearService;

public class ClearHandler {

    ClearService myClearService = new ClearService();

    public ClearHandler() {};

    public Object clearData(Request req, Response res) {
        myClearService.clearData();
        res.status(200);
        return "{}";
    }
}
