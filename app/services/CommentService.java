package services;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.avaje.ebean.SqlUpdate;
import models.Comment;

public class CommentService extends Service<Comment> {

    private static CommentService instance;

    private CommentService(Model.Finder<Long, Comment> finder) {
        super(finder);
    }

    public static CommentService getInstance() {
        if (instance == null) instance = new CommentService(new Model.Finder<>(Comment.class));
        return instance;
    }

    public void deleteRecipeComments(Long recipeID) {
        SqlUpdate delete = Ebean.createSqlUpdate(
                "delete from comment " +
                        "where recipe_id = :id");
        delete.setParameter("id", recipeID);
        Ebean.execute(delete);
    }

    public void deleteUserComments(Long userID) {
        SqlUpdate delete = Ebean.createSqlUpdate(
                "delete from comment " +
                        "where author_id = :id");
        delete.setParameter("id", userID);
        Ebean.execute(delete);
    }
}
