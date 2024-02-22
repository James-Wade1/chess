package handlers;

import spark.Request;
import spark.Response;

import service.ClearService;
import com.google.gson.Gson;

public class clearHandler {

    ClearService myClearService = new ClearService();

    public clearHandler() {};

    public Object clearData(Request req, Response res) {
        myClearService.clearData();
        res.status(200);
        return "";
    }
}
