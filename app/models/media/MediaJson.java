package models.media;

import server.Constant;

/**
 * Represents a public instance of the Media model persisted in the database in JSON format.
 */
public class MediaJson {
    private Media media;

    public MediaJson(Media media) {
        this.media = media;
    }

    public long getId() {
        return media.id;
    }

    /**
     * @return Direct and public url of the media file.
     */
    public String getUrl() {
        return Constant.BASE_URL + media.getName();
    }
}
