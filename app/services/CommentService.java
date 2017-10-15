package services;

import com.avaje.ebean.Model;
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
}
