package services;

import com.avaje.ebean.Expr;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Model.Finder;
import models.Media;
import models.News;
import models.user.User;
import server.error.RequestError;
import server.exception.BadRequestException;
import util.NewsManager;

import java.sql.Timestamp;
import java.util.*;

public class NewsService extends Service<News> {

    private static NewsService instance;

    private NewsService(Finder<Long, News> finder) {
        super(finder);
    }

    public static NewsService getInstance() {
        if (instance == null) instance = new NewsService(new Finder<>(News.class));
        return instance;
    }

    public static void create(News n) throws BadRequestException {
        // Como la media ya esta guardada en la base de datos, no puedo guardar porque hay error de primary key.
        // Entonces saco la imagen, guardo la noticia, le pongo la imagen de nuevo, y hago el update debido.
        Media aux = n.getImage();
        n.setImage(null);
        format(n);
        n.save();
        n.setImage(aux);
        n.update();
        NewsManager.getInstance().notifyReaders(n, n.getAuthor());
    }

    public List<News> getUserNews(Long id) {
        return getFinder().where()
                .eq("author_id", id)
                .eq("recipe_id", null)
                .orderBy().desc("created")
                .findList();
    }

    public List<News> getNewsPublishedByUser(Long id) {
        return getFinder().where()
                .eq("author_id", id)
                .findList();
    }

    public News getNewsForRecipe(Long id) {
        return getFinder().where()
                .eq("recipe_id", id)
                .findUnique();
    }

    public Set<News> getTopNewsFeed(Collection<Long> authorsId, Collection<Long> recipesId, Date date) {
        return getFinder().where()
                .or(Expr.in("author_id", authorsId), Expr.in("recipe_id", recipesId))
                .lt("created", date)
                .orderBy().desc("created")
                .setMaxRows(5)
                .findSet();
    }

    private static void format(News n) throws BadRequestException {
        try {
            n.setCreated(new Date());
            n.setTitle(StringFormatter.capitalizeFirstCharacter(n.getTitle()).trim());
            if (n.getTitle().isEmpty()) throw new BadRequestException(RequestError.BAD_FORMAT);
            n.setDescription(StringFormatter.capitalizeFirstCharacter(n.getDescription()).trim());
            if (n.getDescription().isEmpty()) throw new BadRequestException(RequestError.BAD_FORMAT);
            if (n.getVideoUrl() != null && !n.getVideoUrl().matches("^(https?://(www\\.)?)?youtube\\.com/watch\\?v=[a-zA-Z0-9_-]+$")) n.setVideoUrl(null);
            if (n.getImage() != null) {
                if (n.getImage().getId() == null) n.setImage(null);
                else if (!MediaService.getInstance().get(n.getImage().getId()).isPresent())
                    throw new BadRequestException(RequestError.BAD_FORMAT);
            }
        } catch (NullPointerException e) {
            throw new BadRequestException(RequestError.BAD_FORMAT);
        }
    }
}
