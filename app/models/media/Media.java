package models.media;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.BaseModel;
import server.Constant;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Media model that represents media metadata stored in the database.
 */
@Entity
public class Media extends BaseModel {

    /**
     * Name with extension of the media file.
     */
    @Column(nullable = false)
    @JsonIgnore
    private String name;

    public Media() {}

    public Media(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Direct and public url of the media file.
     */
    public String getUrl() {
        return Constant.BASE_URL + "static/" + getName();
    }
}
