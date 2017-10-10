package services;

import com.avaje.ebean.Model.Finder;
import models.News;
import server.error.RequestError;
import server.exception.BadRequestException;

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
        format(n);
        n.save();
    }

    private static void format(News n) throws BadRequestException {
        try {
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
