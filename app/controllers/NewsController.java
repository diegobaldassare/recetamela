package controllers;

import controllers.authentication.Authenticate;
import models.News;
import models.user.ChefUser;
import models.user.FreeUser;
import models.user.PremiumUser;
import models.user.User;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import server.exception.BadRequestException;
import services.MediaService;
import services.NewsService;
import services.user.UserService;

import java.util.Optional;

public class NewsController extends BaseController {

    @Authenticate({ChefUser.class})
    public Result create() {
        final News n = getBody(News.class);
        try {
            NewsService.create(n);
        } catch (BadRequestException e) {
            return badRequest(e.getMessage()).as(Http.MimeTypes.JSON);
        }
        return ok(Json.toJson(n));
    }

    @Authenticate({ChefUser.class})
    public Result delete(long id) {
        return NewsService.getInstance().get(id).map(n -> {
            if (!getRequester().getId().equals(n.getAuthor().getId()))
                return unauthorized();
            if (n.getImage() != null) MediaService.getInstance().deleteFile(n.getImage());
            NewsService.getInstance().getFinder().deleteById(id);
            return ok();
        }).orElse(notFound());
    }

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class})
    public Result get(long id) {
        return NewsService.getInstance().get(id).map(n -> ok(Json.toJson(n))).orElse(notFound());
    }
}
