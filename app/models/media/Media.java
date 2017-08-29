package models.media;

import models.BaseModel;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class Media extends BaseModel {

    private String url;

    public Media() {}

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
