package services.recipe;

import models.Media;
import models.recipe.Ingredient;
import models.recipe.Recipe;
import models.recipe.RecipeCategory;
import models.recipe.RecipeStep;
import services.MediaService;

import java.util.ListIterator;
import java.util.Optional;

public class RecipeFormatter {

    public static void format(Recipe r) {
        formatCategories(r);
        formatDescription(r);
        formatImages(r);
        formatIngredients(r);
        formatName(r);
        formatSteps(r);
        formatVideoUrl(r);
    }

    private static void formatCategories(Recipe r) {
        if (r.getCategories() == null) return;
        final ListIterator<RecipeCategory> it = r.getCategories().listIterator();
        while (it.hasNext()) {
            final RecipeCategory c = it.next();
            if (c == null || c.getId() == null) {
                it.remove();
                continue;
            }
            final Optional<RecipeCategory> category = RecipeCategoryService.getInstance().get(c.getId());
            if (category.isPresent()) it.set(category.get());
            else it.remove();
        }
    }

    private static void formatDescription(Recipe r) {
        if (r.getDescription() == null) return;
        r.setDescription(capitalizeFirstCharacter(r.getDescription().trim()));
    }

    private static void formatImages(Recipe r) {
        if (r.getImages() == null) return;
        final ListIterator<Media> it = r.getImages().listIterator();
        while (it.hasNext()) {
            final Media i = it.next();
            if (i == null || i.getId() == null) {
                it.remove();
                continue;
            }
            final Optional<Media> image = MediaService.getInstance().get(i.getId());
            if (image.isPresent()) it.set(image.get());
            else it.remove();
        }
    }

    private static void formatIngredients(Recipe r) {
        if (r.getIngredients() == null) return;
        final ListIterator<Ingredient> it = r.getIngredients().listIterator();
        while (it.hasNext()) {
            Ingredient i = it.next();
            if (i == null || i.getName() == null) {
                it.remove();
                continue;
            }
            i.setName(i.getName().trim().toLowerCase());
            final Optional<Ingredient> ingredient = IngredientService.getInstance().getByName(i.getName());
            if (ingredient.isPresent()) i = ingredient.get();
            else {
                i.setId(null);
                i.save();
            }
            it.set(i);
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
            if (s.getImage() != null && (s.getImage().getId() == null || !MediaService.getInstance().get(s.getImage().getId()).isPresent()))
                s.setImage(null);
            s.setId(null);
            s.setDescription(capitalizeFirstCharacter(s.getDescription().trim()));
            it.set(s);
        }
    }

    private static void formatVideoUrl(Recipe r) {
        if (r.getVideoUrl() != null && r.getVideoUrl().length() == 0) r.setVideoUrl(null);
    }

    private static String capitalizeFirstCharacter(String text) {
        if (text.length() < 2) return "" + Character.toUpperCase(text.charAt(0));
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }
}
