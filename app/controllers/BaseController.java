package controllers;

import models.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;

abstract class BaseController extends Controller {

    static User getRequester() {
        return (User) Http.Context.current().args.get("user");
    }

    static <T> T getBody(Class<T> c) {
        return Json.fromJson(request().body().asJson(), c);
    }
}
