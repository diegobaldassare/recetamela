package controllers;

import controllers.authentication.Authenticate;
import models.Comment;
import models.user.AdminUser;
import models.user.ChefUser;
import models.user.FreeUser;
import models.user.PremiumUser;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Result;
import services.CommentService;
import services.recipe.RecipeService;

import javax.inject.Inject;
import java.util.Date;

public class CommentController extends BaseController {

    private static Form<Comment> commentForm;

    @Inject
    public CommentController(FormFactory formFactory) { commentForm = formFactory.form(Comment.class); }

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result create(Long recipeId) {
        Comment comment = commentForm.bindFromRequest().get();
        RecipeService recipeService = RecipeService.getInstance();
        return recipeService.get(recipeId).map(recipe -> {
            comment.setAuthor(getRequester());
            comment.setDate(new Date());
            recipeService.addComment(recipe, comment);
            return ok(Json.toJson(comment));
        }).orElse(notFound());
    }

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result update(Long id) {
        Comment newComment = commentForm.bindFromRequest().get();
        return CommentService.getInstance().get(id).map(comment -> {
            comment.setDate(new Date());
            comment.setComment(newComment.getComment());
            comment.update();
            return ok(Json.toJson(comment));
        }).orElse(notFound());
    }

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result get(Long id) {
        return CommentService.getInstance().get(id).map(comment -> ok(Json.toJson(comment))).orElse(notFound());
    }

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result delete(Long id) {
        return CommentService.getInstance().get(id).map(comment -> {
            comment.delete();
            return ok();
        }).orElse(notFound());
    }
}
