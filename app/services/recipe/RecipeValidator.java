package services.recipe;

import models.Media;
import models.recipe.Ingredient;
import models.recipe.Recipe;
import models.recipe.RecipeCategory;
import models.recipe.RecipeStep;
import org.apache.commons.lang3.StringUtils;
import server.error.RequestError;
import server.exception.BadRequestException;
import services.MediaService;

import java.util.Optional;

public class RecipeValidator {

    public static void validateAllFields(Recipe r) throws BadRequestException {
        validateName(r);
        validateDescription(r);
        validateCategories(r);
        validateIngredients(r);
        validateImages(r);
        validateDifficulty(r);
        validateSteps(r);
        validateVideoUrl(r);
    }

    public static void validateNotNullFields(Recipe r) throws BadRequestException {
        if (r.getName() != null) validateName(r);
        if (r.getDescription() != null) validateDescription(r);
        if (r.getCategories() != null) validateCategories(r);
        if (r.getIngredients() != null) validateIngredients(r);
        if (r.getImages() != null) validateImages(r);
        if (r.getDifficulty() != 0) validateDifficulty(r);
        if (r.getSteps() != null) validateSteps(r);
        if (r.getVideoUrl() != null) validateVideoUrl(r);
    }

    private static void validateName(Recipe r) throws BadRequestException {
        if (r.getName() == null || !isAlphaNumericSpaceNotEmpty(r.getName()))
            throw new BadRequestException(RequestError.BAD_FORMAT);
    }

    private static void validateDescription(Recipe r) throws BadRequestException {
        if (r.getDescription() == null || r.getDescription().length() == 0)
            throw new BadRequestException(RequestError.BAD_FORMAT);
    }

    private static void validateCategories(Recipe r) throws BadRequestException {
        if (r.getCategories() == null) throw new BadRequestException(RequestError.BAD_FORMAT);
        for (final RecipeCategory c : r.getCategories()) {
            if (RecipeCategoryService.getInstance().get(c.getId()).isPresent()) continue;
            r.getCategories().remove(c);
        }
        if (r.getCategories().isEmpty()) throw new BadRequestException(RequestError.BAD_FORMAT);
    }

    private static void validateIngredients(Recipe r) throws BadRequestException {
        if (r.getIngredients() == null) throw new BadRequestException(RequestError.BAD_FORMAT);
        for (final Ingredient i : r.getIngredients()) {
            final Optional<Ingredient> ingredient = IngredientService.getInstance().getByName(i.getName());
            if (ingredient.isPresent()) i.setId(ingredient.get().getId());
            else i.save();
        }
        if (r.getIngredients().isEmpty()) throw new BadRequestException(RequestError.BAD_FORMAT);
    }

    private static void validateImages(Recipe r) throws BadRequestException {
        if (r.getImages() == null) throw new BadRequestException(RequestError.BAD_FORMAT);
        for (final Media i : r.getImages()) {
            if (MediaService.getInstance().get(i.getId()).isPresent()) continue;
            r.getImages().remove(i);
        }
        if (r.getImages().isEmpty()) throw new BadRequestException(RequestError.BAD_FORMAT);
    }

    private static void validateDifficulty(Recipe r) throws BadRequestException {
        if (r.getDifficulty() < 1 || r.getDifficulty() > 5)
            throw new BadRequestException(RequestError.BAD_FORMAT);
    }

    private static void validateSteps(Recipe r) throws BadRequestException {
        if (r.getSteps() == null || r.getSteps().isEmpty())
            throw new BadRequestException(RequestError.BAD_FORMAT);
        for (final RecipeStep s : r.getSteps()) {
            if (s.getImage() != null && !MediaService.getInstance().get(s.getImage().getId()).isPresent())
                s.setImage(null);
            s.save();
        }
    }

    private static void validateVideoUrl(Recipe r) throws BadRequestException {
        if (r.getVideoUrl() == null) return;
        if (r.getVideoUrl().length() == 0) {
            r.setVideoUrl(null);
            return;
        }
        if (r.getVideoUrl().matches("^(https?://(www\\.)?)?youtube\\.com/watch\\?v=[a-zA-Z0-9_]+$")) return;
        throw new BadRequestException(RequestError.BAD_FORMAT);
    }

    private static boolean isAlphaNumericSpaceNotEmpty(String s) {
        return s.length() != 0 && StringUtils.isAlphanumericSpace(s);
    }
}
