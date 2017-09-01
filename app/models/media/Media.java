package models.media;

import models.BaseModel;

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
}
