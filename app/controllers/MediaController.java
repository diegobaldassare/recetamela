package controllers;

import models.Media;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.MultipartFormData;
import play.mvc.Result;
import play.mvc.Results;
import server.error.RequestError;
import services.MediaService;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Media controller persists media files and retrieves their metadata.
 */
public class MediaController extends Controller {

    /**
     * Persists an instance of the Media model in the database and the file in disk.
     * The request body must have a 'multipart/form-data' content type and the file
     * must appear as the value of the key 'file'.
     * @return MediaJson that represents the Media model persisted in the database.
     * @throws IOException If the file cannot be written to disk. A response
     * with 500 error is returned to the client.
     */
    // @Authenticate({FreeUser.class, PremiumUser.class})
    public Result create() throws IOException {
        final MultipartFormData<File> body = request().body().asMultipartFormData();
        if (body == null)
            return badRequest(RequestError.BAD_FORMAT.toString()).as(Http.MimeTypes.JSON);
        final MultipartFormData.FilePart<File> file = body.getFile("file");
        if (file == null)
            return badRequest(RequestError.BAD_FORMAT.toString()).as(Http.MimeTypes.JSON);
        final Media media = MediaService.getInstance().save(file);
        return ok(Json.toJson(media));
    }

    /**
     * @param id ID of the requested Media instance.
     * @return MediaJson that represents a Media instance with provided id
     * persisted in the database.
     */
    // @Authenticate({FreeUser.class, PremiumUser.class})
    public Result get(long id) {
        final Optional<Media> media = MediaService.getInstance().get(id);
        return media.map(m -> ok(Json.toJson(m))).orElseGet(Results::notFound);
    }

    // @Authenticate({FreeUser.class, PremiumUser.class})
    public Result getFile(String name) {
        final File file = MediaService.getInstance().getFile(name);
        if (file.exists()) return ok(file);
        else return notFound();
    }
}
