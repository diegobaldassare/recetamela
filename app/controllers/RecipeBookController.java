package controllers;

import com.google.inject.Inject;
import models.recipe.RecipeBook;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import services.recipe.RecipeBookService;

import java.util.List;
import java.util.Optional;

public class RecipeBookController extends Controller {

    private static Form<RecipeBook> recipeBookForm;

    @Inject
    public RecipeBookController(FormFactory formFactory) {
        recipeBookForm =  formFactory.form(RecipeBook.class);
    }

    public Result createRecipeBook() {
        RecipeBook recipeBook = recipeBookForm.bindFromRequest().get();
        recipeBook.save();
        return ok(Json.toJson(recipeBook));
    }

    public Result updateRecipeBook() {
        //..Falta
        return ok();
    }

    public Result getRecipeBooks(){
        List<RecipeBook> recipeBooks = RecipeBookService.getInstance().getFinder().all();
        return ok(Json.toJson(recipeBooks));
    }

    public Result deleteRecipeBook(Long id){
        Optional<RecipeBook> recipeBook = RecipeBookService.getInstance().get(id);
        if (recipeBook.isPresent()){
            recipeBook.get().delete();
            return ok();
        }
        return notFound();
    }

    public Result getRecipeBook(Long id) {
        final Optional<RecipeBook> recipeBook = RecipeBookService.getInstance().get(id);
        return recipeBook.map(u -> ok(Json.toJson(recipeBook))).orElseGet(Results::notFound);
    }
}
