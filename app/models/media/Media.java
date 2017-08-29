package models.media;

import models.BaseModel;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class Media extends BaseModel {

    @NotNull
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
