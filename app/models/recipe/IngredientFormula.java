package models.recipe;

import models.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class IngredientFormula extends BaseModel {

    @OneToOne
    @Column(nullable = false)
    private Ingredient ingredient;

    @Column(nullable = false)
    private float quantity;

    @Column(nullable = false)
    private String unit;

    public IngredientFormula() {}

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
