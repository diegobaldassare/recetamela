package models.recipe;

import models.BaseModel;
import models.Media;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class RecipeStep extends BaseModel {

    @Column(nullable = false)
    private String description;

    @OneToOne(cascade = CascadeType.REMOVE)
    private Media image;
}
