package models.media.json;

import models.media.Media;
import server.Constant;

/**
 * Represents in JSON format a public instance of the Media model persisted in the database.
 */
public class MediaJson {
    private Media media;

    public MediaJson(Media media) {
        this.media = media;
    }

    public long getId() {
        return media.getId();
    }

    /**
     * @return Direct and public url of the media file.
     */
    public String getUrl() {
        return Constant.BASE_URL + media.getName();
    }
}
