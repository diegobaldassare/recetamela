package controllers;

import models.user.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;

public abstract class BaseController extends Controller {

    protected static User getRequester() {
        return (User) Http.Context.current().args.get("user");
    }

    protected static <T> T getBody(Class<T> c) {
        return Json.fromJson(request().body().asJson(), c);
    }
}
