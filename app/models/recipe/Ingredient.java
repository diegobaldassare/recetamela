package models.recipe;

import models.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
public class Ingredient extends BaseModel {

    @Column(nullable = false)
    private String name;

    public Ingredient() {}

    public Ingredient(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
