package controllers.recipe;

import com.google.inject.Inject;
import models.PremiumUser;
import models.recipe.RecipeBook;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;
import services.recipe.RecipeBookService;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

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
        RecipeBook newRB = Json.fromJson(request().body().asJson(), RecipeBook.class);
        Optional<RecipeBook> optRB = RecipeBookService.getInstance().get(newRB.getId());
        RecipeBook oldRB;
        if(optRB.isPresent()) oldRB = optRB.get();
        else return Results.notFound();
        if (newRB.getName() != null) oldRB.setName(newRB.getName());
        if (newRB.getDescription() != null) oldRB.setDescription(newRB.getDescription());
        if (newRB.getRecipes() != null) oldRB.setRecipes(newRB.getRecipes());
        oldRB.update();
        return ok();
    }

    public Result getRecipeBooks(){
        List<RecipeBook> recipeBooks = RecipeBookService.getInstance().getFinder().all();
        return ok(Json.toJson(recipeBooks));
    }

    public Result deleteRecipeBook(Long id){
        final Optional<RecipeBook> recipe = RecipeBookService.getInstance().get(id);
        return recipe.map(r -> {
            r.delete();
            return ok();
        }).orElseGet(Results::notFound);
    }

    public Result getRecipeBook(Long id) {
        final Optional<RecipeBook> recipe = RecipeBookService.getInstance().get(id);
        return recipe.map(r -> ok(Json.toJson(r))).orElseGet(Results::notFound);
    }
}
