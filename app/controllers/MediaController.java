package controllers;

import models.media.Media;
import models.media.MediaJson;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Result;
import services.MediaService;

import java.io.IOException;

public class MediaController extends Controller {

    public Result createMedia() throws IOException {
        final MultipartFormData<java.io.File> body = request().body().asMultipartFormData();
        if (body == null) return badRequest();
        final MultipartFormData.FilePart<java.io.File> file = body.getFile("file");
        if (file == null) return badRequest();
        final Media media = MediaService.getInstance().save(file);
        final MediaJson mediaJson = new MediaJson(media);
        return ok(Json.toJson(mediaJson));
    }

    public Result getMedia(long id) {
        final Media media = MediaService.getInstance().getById(id);
        if (media == null) return notFound();
        final MediaJson mediaJson = new MediaJson(media);
        return ok(Json.toJson(mediaJson));
    }
}
