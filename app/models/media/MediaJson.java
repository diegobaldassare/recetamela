package models.media;

public class MediaJson {
    private Media media;

    public MediaJson(Media media) {
        this.media = media;
    }

    public long getId() {
        return media.id;
    }
}
