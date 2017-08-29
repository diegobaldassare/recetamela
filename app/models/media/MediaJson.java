package models.media;

public class MediaJson {
    private Media media;

    public MediaJson(Media media) {
        this.media = media;
    }

    public long getId() {
        return media.id;
    }

    public String getUrl() {
        return "localhost:9000/static/" + media.getName();
    }
}
