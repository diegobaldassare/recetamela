package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class PreFlightController extends Controller {

    public Result handle(String all) {
        return ok()
                .withHeader("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, authentication, cache-control")
                .withHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
    }
}