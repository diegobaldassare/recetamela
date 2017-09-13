package controllers;

import models.User;
import play.mvc.Controller;
import play.mvc.Http;

abstract class BaseController extends Controller {

    static User getRequester() {
        return (User) Http.Context.current().args.get("user");
    }
}
