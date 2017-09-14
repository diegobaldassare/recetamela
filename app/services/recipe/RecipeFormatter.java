package services.recipe;

import models.recipe.Ingredient;
import models.recipe.Recipe;
import models.recipe.RecipeStep;

import java.util.ListIterator;

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
        r.getCategories().removeIf(c -> c == null || c.getId() == null);
    }

    private static void formatDescription(Recipe r) {
        if (r.getDescription() == null) return;
        r.setDescription(capitalizeFirstCharacter(r.getDescription().trim()));
    }

    private static void formatImages(Recipe r) {
        if (r.getImages() == null) return;
        r.getImages().removeIf(i -> i == null || i.getId() == null);
    }

    private static void formatIngredients(Recipe r) {
        if (r.getIngredients() == null) return;
        final ListIterator<Ingredient> it = r.getIngredients().listIterator();
        while (it.hasNext()) {
            final Ingredient i = it.next();
            if (i == null || i.getName() == null) it.remove();
            else {
                i.setName(i.getName().trim().toLowerCase());
                i.setId(null);
                it.set(i);
            }
        }
    }

    private static void formatName(Recipe r) {
        if (r.getName() == null) return;
        r.setName(capitalizeFirstCharacter(r.getName()).trim());
    }

    private static void formatSteps(Recipe r) {
        if (r.getSteps() == null) return;
        final ListIterator<RecipeStep> it = r.getSteps().listIterator();
        while (it.hasNext()) {
            final RecipeStep s = it.next();
            if (s == null || s.getDescription() == null) {
                it.remove();
                continue;
            }
            if (s.getImage() != null && s.getImage().getId() == null) s.setImage(null);
            s.setDescription(capitalizeFirstCharacter(s.getDescription().trim()));
            s.setId(null);
            it.set(s);
        }
    }

    private static String capitalizeFirstCharacter(String text) {
        if (text.length() < 2) return "" + Character.toUpperCase(text.charAt(0));
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }
}
