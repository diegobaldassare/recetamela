package models.recipe;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import models.Media;

import javax.persistence.*;

@Entity
public class RecipeStep extends Model {

    @Id
    private Long id;

    @Column(nullable = false, length = 4096)
    private String description;

    @OneToOne(cascade = CascadeType.REMOVE)
    private Media image;

    public RecipeStep() {}

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
