package services.recipe;

import models.Media;
import models.recipe.Ingredient;
import models.recipe.Recipe;
import models.recipe.RecipeCategory;
import models.recipe.RecipeStep;

public class RecipeFormatter {

    public static void format(Recipe r) {
        formatCategories(r);
        formatDescription(r);
        formatImages(r);
        formatIngredients(r);
        formatName(r);
        formatSteps(r);
    }

    private static void formatCategories(Recipe r) {
        if (r.getCategories() == null) return;
        for (final RecipeCategory c : r.getCategories())
            if (c == null || c.getId() == null) r.getCategories().remove(c);
    }

    private static void formatDescription(Recipe r) {
        if (r.getDescription() == null) return;
        r.setDescription(capitalizeFirstCharacter(r.getDescription().trim()));
    }

    private static void formatImages(Recipe r) {
        if (r.getImages() == null) return;
        for (final Media i : r.getImages())
            if (i == null || i.getId() == null) r.getImages().remove(i);
    }

    private static void formatIngredients(Recipe r) {
        if (r.getIngredients() == null) return;
        for (final Ingredient i : r.getIngredients()) {
            if (i == null || i.getName() == null) {
                r.getIngredients().remove(i);
                continue;
            }
            i.setName(i.getName().trim().toLowerCase());
        }
    }

    private static void formatName(Recipe r) {
        if (r.getName() == null) return;
        r.setName(capitalizeFirstCharacter(r.getName()).trim());
    }

    private static void formatSteps(Recipe r) {
        if (r.getSteps() == null) return;
        for (final RecipeStep s : r.getSteps()) {
            if (s == null || s.getDescription() == null) {
                r.getSteps().remove(s);
                continue;
            }
            if (s.getImage() != null && s.getImage().getId() == null) s.setImage(null);
            s.setDescription(capitalizeFirstCharacter(s.getDescription().trim()));
        }
    }

    private static String capitalizeFirstCharacter(String text) {
        if (text.length() < 2) return "" + Character.toUpperCase(text.charAt(0));
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }
}
