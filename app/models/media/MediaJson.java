package models.media;

import server.Constant;

public class MediaJson {
    private Media media;

    public MediaJson(Media media) {
        this.media = media;
    }

    public long getId() {
        return media.id;
    }

    public String getUrl() {
        return Constant.BASE_URL + media.getName();
    }
}
