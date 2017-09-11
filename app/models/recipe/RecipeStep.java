package models.recipe;

import models.BaseModel;
import models.Media;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class RecipeStep extends BaseModel {

    @Column(nullable = false, length = 1024)
    private String description;

    @OneToOne(cascade = CascadeType.REMOVE)
    private Media image;

    public RecipeStep() { }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Media getImage() {
        return image;
    }

    public void setImage(Media image) {
        this.image = image;
    }
}
