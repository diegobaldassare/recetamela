package services;

import com.avaje.ebean.Model.Finder;
import models.media.Media;
import play.mvc.Http.MultipartFormData.FilePart;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class MediaService extends Service<Media> {

    private static MediaService instance;
    private static final String savePath = "public/static/";

    private MediaService(Finder<Long, Media> finder) {
        super(finder);
    }

    public static MediaService getInstance() {
        if (instance == null)
            instance = new MediaService(new Finder<>(Media.class));
        return instance;
    }

    public Media save(FilePart<File> filePart) throws IOException {
        final String name = filePart.getFilename();
        final String extension = name.substring(name.lastIndexOf("."));
        final String uuid = UUID.randomUUID().toString();
        final File file = filePart.getFile();
        final Media media = new Media(uuid + extension);
        final Path destination = FileSystems
                .getDefault()
                .getPath(savePath + media.getName());
        Files.move(file.toPath(), destination);
        media.save();
        return media;
    }
}