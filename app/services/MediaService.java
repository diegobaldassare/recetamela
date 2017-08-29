package services;

import com.avaje.ebean.Model.Finder;
import models.media.Media;
import play.mvc.Http.MultipartFormData.FilePart;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class MediaService extends Service<Media> {

    private static MediaService instance;
    private static final String mediaPath = "db/media/";

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
        final File file = filePart.getFile();
        final Media media = new Media();
        media.save();
        final Path destination = FileSystems
                .getDefault()
                .getPath(mediaPath + media.id + extension);
        Files.move(file.toPath(), destination);
        media.setUrl(destination.toAbsolutePath().toString());
        media.save();
        return media;
    }
}
