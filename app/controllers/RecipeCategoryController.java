package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.recipe.RecipeCategory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import server.error.RequestError;
import server.exception.BadRequestException;
import services.recipe.RecipeCategoryService;

public class RecipeCategoryController extends Controller {
    
    public Result create(){
        final JsonNode body = request().body().asJson();
        final String input = Json.fromJson(body, String.class);
        if (badCreateRequest(input))
            return badRequest(RequestError.BAD_FORMAT.toString()).as(Http.MimeTypes.JSON);
        try {
            final RecipeCategory recipe = RecipeCategoryService.getInstance().save(input);
            return ok(Json.toJson(recipe));
        } catch (BadRequestException e) {
            return badRequest(e.getMessage()).as(Http.MimeTypes.JSON);
        }
    }

    private boolean badCreateRequest(String input) {
        return input == null || input.length() == 0;
    }

//    public Result update(){
//        TODO receive newName and id of the category for update in RecipeService.
//    }

//    public Result delete(){
//        TODO receive id of the category for delete in RecipeService.
//    }

    public Result get(long id){
        final RecipeCategory recipeCategory = RecipeCategoryService.getInstance().getById(id);
        if(recipeCategory == null) return notFound();
        return ok(Json.toJson(recipeCategory));
    }
}
