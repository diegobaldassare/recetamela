package services;

import com.avaje.ebean.Model.Finder;
import models.Media;
import play.mvc.Http.MultipartFormData.FilePart;
import server.Constant;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * Service methods for the Media model.
 */
public class MediaService extends Service<Media> {

    private static MediaService instance;

    private MediaService(Finder<Long, Media> finder) {
        super(finder);
    }

    public static MediaService getInstance() {
        if (instance == null)
            instance = new MediaService(new Finder<>(Media.class));
        return instance;
    }

    /**
     * Persists the media metadata to the database and the file to disk.
     * @param filePart The file and metadata received from the multipart request.
     * @return Media model instance persisted in the database.
     * @throws IOException If the file cannot be written to disk.
     */
    public Media save(FilePart<File> filePart) throws IOException {
        final String name = filePart.getFilename();
        final String extension = name.substring(name.lastIndexOf("."));
        final String uuid = UUID.randomUUID().toString();
        final File file = filePart.getFile();
        final Media media = new Media(uuid + extension);
        final Path destination = FileSystems
                .getDefault()
                .getPath(Constant.STATIC_PATH + media.getName());
        Files.move(file.toPath(), destination);
        media.save();
        return media;
    }

    public File getFile(String name) {
        return new File(Constant.STATIC_PATH + name);
    }

    public void deleteFile(Media m) {
        getFile(m.getName()).delete();
    }

    public void delete(Media m) {
        deleteFile(m);
        m.delete();
    }
}
