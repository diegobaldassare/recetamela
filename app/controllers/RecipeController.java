package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.Controller;
import play.mvc.Result;

public class RecipeController extends Controller {

    public Result createRecipe() {
        final JsonNode req = request().body().asJson();

        return ok();
    }
}
